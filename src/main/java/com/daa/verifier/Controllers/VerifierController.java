package com.daa.verifier.Controllers;

import com.daa.verifier.Models.*;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.daa.verifier.Controllers.HttpConnection;
import com.daa.verifier.Controllers.crypto.BNCurve;
import com.daa.verifier.Models.Issuer;
import org.json.JSONObject;

import static com.daa.verifier.Controllers.utils.bytesToHex;
import static com.daa.verifier.Controllers.utils.hexStringToByteArray;

/**
 * Created by DK on 11/24/16.
 */
@Controller
public class VerifierController {
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    protected Service service = null;

    public com.daa.verifier.Models.joinData getJoinData() {
        return joinData;
    }

    public void setJoinData(com.daa.verifier.Models.joinData joinData) {
        this.joinData = joinData;
    }

    protected joinData joinData = null;
    protected BigInteger serviceSk = null;
    protected Issuer.JoinMessage2 joinMessage2Data = null;
    protected CertData certData = null;
    protected BNCurve curve = BNCurve.createBNCurveFromName(Config.CURVE_NAME);
    protected Issuer.IssuerPublicKey issuerPublicKey = null;

    public Issuer.IssuerPublicKey getIssuerPublicKey() {
        return issuerPublicKey;
    }

    public void setIssuerPublicKey(Issuer.IssuerPublicKey issuerPublicKey) {
        this.issuerPublicKey = issuerPublicKey;
    }

    public BNCurve getCurve() {
        return curve;
    }

    public void setCurve(BNCurve curve) {
        this.curve = curve;
    }

    public CertData getCertData() {
        return certData;
    }

    public void setCertData(CertData certData) {
        this.certData = certData;
    }

    public Issuer.JoinMessage2 getJoinMessage2Data() {
        return joinMessage2Data;
    }

    public void setJoinMessage2Data(Issuer.JoinMessage2 joinMessage2Data) {
        this.joinMessage2Data = joinMessage2Data;
    }

    public BigInteger getServiceSk() {
        return serviceSk;
    }

    public void setServiceSk(BigInteger serviceSk) {
        this.serviceSk = serviceSk;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String Homepage(Map<String, Object> model){
        model.put("time", LocalDateTime.now());
        return "homepage";
    }
    @RequestMapping( method = RequestMethod.POST, value="/login")
    public void Login(HttpServletResponse response,
                      @RequestParam("app_id") Integer app_Id,
                      @RequestParam("m") String m
    ) throws IOException {
        if (app_Id != null && m != null) {
            Service service = new Service(app_Id, m);
            if (checkLogin(service)) {
                this.setService(service);
                response.setStatus(200);
                response.getWriter().println("join Success");
                response.getWriter().println("nonce number: "+this.getJoinData().getNonce());
            }
        }

    }
    @RequestMapping( method = RequestMethod.POST, value="/webLogin")
    public String Login(@RequestParam("app_id") Integer app_Id,
                        @RequestParam("m") String m,
                        ModelMap model
    ) throws IOException {
        if (app_Id != null && m != null) {
            Service service = new Service(app_Id, m);
            if (checkLogin(service)) {
                System.out.println("TEST");
            }
        }
        return "loginResult";
    }

    @RequestMapping( method = RequestMethod.GET, value="/verify")
    public void getVerifyUrl(HttpServletResponse response) throws IOException {
        if (this.getJoinData() != null) {
            String ipkString = this.getJoinData().getIpk();
            BigInteger nonce = this.getJoinData().getNonce();
            // create Elliptic Curve from Issuer curveName and
            BNCurve curve = this.getCurve();
            // create service sk and pk
            SecureRandom random = new SecureRandom();
            BigInteger sk = curve.getRandomModOrder(random);
            this.setServiceSk(sk);
            // get Issuer public key from response;
            Issuer.IssuerPublicKey ipk = new Issuer.IssuerPublicKey(curve, ipkString);
            this.setIssuerPublicKey(ipk);
            try {
                Authenticator authenticator = new Authenticator(curve, ipk, sk);
                Issuer.JoinMessage1 jm1 = authenticator.EcDaaJoin1(nonce);
                String jm1String = jm1.toJson(curve);
                System.out.println("create jm1 Success:"+jm1String);
                joinM1Data jm1Data = new joinM1Data(jm1String, Config.REQUIRE_INFO);
                joinM1(jm1Data, curve);
                if (this.getJoinMessage2Data() != null) {
                    authenticator.EcDaaJoin2(this.getJoinMessage2Data());
                    SigData sigData = createSignature(authenticator, curve);
                    if (sigData != null) {
                        this.getCertificate(sigData);
                        if (this.getCertData() != null) {
                            response.setStatus(200);
                            response.getWriter().println(this.getCertData().getCertificate());
                        } else {
                            response.setStatus(400);
                            response.getWriter().println("Service Provider Certificate Fail");
                        }
                    } else {
                        response.setStatus(400);
                        response.getWriter().println("FAIL to create signature");
                    }
                } else {
                    response.setStatus(400);
                    response.getWriter().println("FAIL to join message 2");
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            response.setStatus(400);
            response.getWriter().println("ERROR: Join Proccess not yet!");
        }
    }
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public void postInfoVerify(HttpServletResponse response,
                               @RequestParam("user_certificate") String certificate,
                               @RequestParam("basename") String basename
    ) throws IOException {
        if (certificate == null || basename == null) {
            response.setStatus(400);
            response.getWriter().println("ERROR: invalid input");
        } else {
            Verifier verifier = new Verifier(this.getCurve());
            byte[] cert = hexStringToByteArray(certificate);
            Authenticator.EcDaaSignature ecDaaSignature = new Authenticator.EcDaaSignature(cert, null, this.getCurve());
            try {
                Boolean verifyResult = verifier.verify(ecDaaSignature, basename, this.getIssuerPublicKey(),null);
                if (verifyResult) {
                    response.setStatus(200);
                    response.getWriter().println("Verify user success!");
                    response.getWriter().println("c2 sig: "+ ecDaaSignature.c2.toString());
                } else {
                    response.setStatus(400);
                    response.getWriter().println("Verify user fail!");
                    response.getWriter().println("c2 sig: "+ ecDaaSignature.c2.toString());
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                response.setStatus(400);
                response.getWriter().println("verify error !!");
                response.getWriter().println("c2 sig: "+ ecDaaSignature.c2.toString());
            }
        }
    }
    // join into issuer get Nonce
    public Boolean checkLogin(Service service) throws IOException {
        joinData joinObject = join(service);
        if (joinObject.getNonce() != null) {
            this.setJoinData(joinObject);
            return true;
        }
        return false;
    }
    // edit IssuerUrl variables in Config if it have difference localhost:8081/issuer
    public joinData join(Service service) throws IOException {
        joinData responseData = new joinData();
        JSONObject response = new JSONObject();
        HttpConnection http = new HttpConnection();
        try {
            response = http.sendJoinMessage(Config.IssuerUrl, service);
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseData.setMsg(response.get(constantVariables.MESSAGE_RESPONSE).toString());
        responseData.setStatus(response.get(constantVariables.STATUS).toString());
        responseData.setCurveName(response.get(constantVariables.CURVE_NAME).toString());
        responseData.setIpk(response.get(constantVariables.ISSUER_PUBLIC_KEY).toString());
        responseData.setNonce(response.getBigInteger(constantVariables.NONCE));
        return responseData;
    }

    public void joinM1(joinM1Data jm1, BNCurve curve) {
        JSONObject response = new JSONObject();
        HttpConnection http = new HttpConnection();
        try {
            response = http.sendJoinMessage1(Config.IssuerUrl, jm1);
            System.out.println("joinM1 response: "+response.get(constantVariables.STATUS));
            Issuer.JoinMessage2 jm2 = new Issuer.JoinMessage2(curve, response.getString("jm2"));
            this.setJoinMessage2Data(jm2);
            System.out.println("joinM2 data: "+jm2.toJson(curve));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SigData createSignature(Authenticator authenticator, BNCurve curve) {
        SigData sigData = null;
        try {
            Authenticator.EcDaaSignature EcDaaSig = authenticator.EcDaaSign(Config.CERT_BASENAME, Config.PERMISSION);
            String stringSig = bytesToHex(EcDaaSig.encode(curve));
            sigData = new SigData(stringSig, this.getJoinData().getNonce(), Config.CERT_BASENAME);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sigData;
    }

    public void getCertificate(SigData sigData) {
        JSONObject response = new JSONObject();
        HttpConnection http = new HttpConnection();
        try {
            response = http.getCertificate(Config.IssuerUrl, sigData);
            CertData certData = new CertData(
                    response.get(constantVariables.MESSAGE_RESPONSE).toString(),
                    response.get(constantVariables.CERT).toString(),
                    response.get(constantVariables.STATUS).toString()
            );
            this.setCertData(certData);
            System.out.println("Cert stored: "+this.getCertData().getCertificate());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
