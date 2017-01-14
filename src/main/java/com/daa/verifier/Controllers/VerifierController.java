package com.daa.verifier.Controllers;

import com.daa.verifier.Models.*;
import com.daa.verifier.Repository.DatabaseOperation;
import com.fasterxml.jackson.databind.node.TextNode;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.net.URLDecoder;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

import com.daa.verifier.Controllers.crypto.BNCurve;
import com.daa.verifier.Models.Issuer;
import org.json.JSONObject;
import java.util.List;

import static com.daa.verifier.Controllers.utils.bytesToHex;
import static com.daa.verifier.Controllers.utils.hexStringToByteArray;

/**
 * Created by DK on 11/24/16.
 */
@Controller
public class VerifierController {
    @Autowired
    private DataSource dataSource;


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
    protected SigData sigData = null;
    protected HashMap<String, String> listSessionId = new HashMap();
    protected AppData appData = new AppData();
    protected HashMap<Integer, ServiceProcess> listServiceProcessing = new HashMap();

    public AppData getAppData() {
        return appData;
    }

    public void setAppData(AppData appData) {
        this.appData = appData;
    }

    public SigData getSigData() {
        return sigData;
    }

    public void setSigData(SigData sigData) {
        this.sigData = sigData;
    }

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
        if (app_Id != null && m != null && (app_Id instanceof Integer)) {
            Service service = new Service(app_Id, m);
            response.setHeader("Access-Control-Allow-Origin", "*");
            if (checkLogin(service)) {
                this.setService(service);
                response.setStatus(200);
                JSONObject json = new JSONObject();
                json.put("status", "login success");
                json.put("link", Config.VerifierUrl+"/loginResult/"+app_Id.toString());
                response.setContentType("application/json");
                json.write(response.getWriter());
            } else {
                response.setStatus(400);
                response.getWriter().println("login Fail");
            }
        } else {
            response.setStatus(400);
            response.getWriter().println("app_Id or m Invalid");
        }

    }
    @RequestMapping( method = RequestMethod.GET, value="/servicelogin")
    public String Login(ModelMap model
    ) throws IOException {
        return "serviceLogin";
    }

    @RequestMapping( method = RequestMethod.GET, value="/loginResult/{appId}")
    public String LoginResult(ModelMap model, @PathVariable Integer appId
    ) throws IOException {
        ServiceProcess ser = this.listServiceProcessing.get(appId);
        if (ser != null) {
            model.put("serviceName", ser.getServiceName());
            String url = Config.VerifierUrl+"/verify/"+appId.toString();
            model.put("url", url);
            return "loginResult";
        }
        return "homepage";
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
            response.setHeader("Access-Control-Allow-Origin", "*");
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
                        this.setSigData(sigData);
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

    @RequestMapping( method = RequestMethod.GET, value="/verify/{appId}")
    public String Login(ModelMap model, @PathVariable Integer appId
    ) throws IOException {
        ServiceProcess serviceProcess = null;
        try  {
            serviceProcess = this.listServiceProcessing.get(appId);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        if (serviceProcess != null) {
            model.put("serviceName", serviceProcess.getServiceName());
            model.put("status", "Available");
            model.put("enable", true);
            model.put("serviceId", appId);
            System.out.println("appId get from path: "+appId);
        } else {
            model.put("serviceName", "");
            model.put("status", "Unavailable!");
            model.put("enable", false);
        }
        return "userauthen";
    }

    @RequestMapping( method = RequestMethod.GET, value="/getCert/{appId}/{appSessionId}")
    public void getCert(HttpServletResponse response, @PathVariable Integer appId, @PathVariable String appSessionId ) throws Exception {
        ServiceProcess serviceProcess = null;
        System.out.println("app sessionId: "+ appSessionId);
        try  {
            serviceProcess = this.listServiceProcessing.get(appId);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        if (serviceProcess != null) {
            String data = getVerifierData(appId);
            String sessionId = utils.generateSessionId();
            this.listSessionId.put(appSessionId, sessionId);
            String cert = null;
            response.setHeader("Access-Control-Allow-Origin", "*");
            try {
                cert = generateCertificate(appSessionId, sessionId, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cert != null) {
                response.setStatus(200);
                response.getWriter().println(cert);
            } else {
                response.setStatus(400);
                response.getWriter().println("Service can't get Certificate. Try again!");
            }
        } else {
            response.setStatus(400);
            response.getWriter().println("Service Unavailable!");
        }
    };

    @RequestMapping(value = "/verify/{appId}/{appSession}", method = RequestMethod.POST)
    public void postInfoVerify(HttpServletResponse response,
                               @PathVariable String appSession,
                               @PathVariable Integer appId,
                               @RequestBody String info
    ) throws IOException {
        if (info == null || appSession == null) {
            response.setStatus(400);
            response.getWriter().println("ERROR: body info or appSession string is Null");
        } else {
            String infoDecode = URLDecoder.decode(info,"UTF-8").replace("=", "");
            System.out.println("info"+infoDecode);
            UserSig userSig = null;
            try {
                userSig = new UserSig(infoDecode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("verify with appSession: "+appSession);
            System.out.println("verify with serviceSession: "+this.listSessionId.get(appSession));
            if (userSig != null) {
                Boolean verifyResult = VerifyUserInfo(this.listSessionId.get(appSession), userSig);
                DatabaseOperation databaseOperation = new DatabaseOperation(dataSource);
                response.setHeader("Access-Control-Allow-Origin", "*");
                if (verifyResult) {
                    response.setStatus(200);
                    databaseOperation.addVerifyLog(appId, userSig.getInformation(), true);
                    response.getWriter().println("Get Info From User success");
                    response.getWriter().println("Your info: "+info);
                } else {
                    response.setStatus(400);
                    databaseOperation.addVerifyLog(appId, userSig.getInformation(), false);
                    response.getWriter().println("Verify User Fail!!!");
                }
            } else {
                response.setStatus(400);
                response.getWriter().println("ERROR: invalid input. not enough params");
            }
        }
        this.listSessionId.remove(appSession);
    }

    // For Test service provider certificate
    @RequestMapping( method = RequestMethod.GET, value="/checkVerifierCert")
    public void checkCert(HttpServletResponse response) throws IOException {
        CertData certData = this.getCertData();
        Verifier verifier = new Verifier(this.getCurve());
        byte[] cert = hexStringToByteArray(certData.getCertificate());
        byte[] sigMessage = this.getSigData().getSig().getBytes();
        byte[] sig = hexStringToByteArray(this.getSigData().getSig());
        byte[] message = Config.PERMISSION.getBytes();
        Authenticator.EcDaaSignature ecDaaSignatureCert = new Authenticator.EcDaaSignature(cert, sigMessage, this.getCurve());
        Authenticator.EcDaaSignature ecDaaSignatureSig = new Authenticator.EcDaaSignature(sig, message, this.getCurve());
        try {
            Boolean verifyCertResult = verifier.verify(ecDaaSignatureCert, Config.CERT_BASENAME, this.getIssuerPublicKey(),null);
            Boolean verifySigResult = verifier.verify(ecDaaSignatureSig, Config.CERT_BASENAME, this.getIssuerPublicKey(),null);
            if (verifyCertResult && verifySigResult) {
                response.setStatus(200);
                response.getWriter().println("SUCCESS! Your message is valid: "+Config.PERMISSION);
            } else {
                response.setStatus(400);
                response.getWriter().println("FAIL! Your message is invalid: "+Config.PERMISSION);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            response.setStatus(200);
            response.getWriter().println("Verify Error");
        }
    }
    // join into issuer get Nonce
    public Boolean checkLogin(Service service) throws IOException {
        return getDataService(service);
    }

    public Boolean getDataService( Service service ) throws IOException {
        DatabaseOperation databaseOperation = new DatabaseOperation(dataSource);
        try {
            if(databaseOperation.checkAppIdExisted(service.getApp_Id().toString())) {
                String data = databaseOperation.getAppData(service.getApp_Id());
                AppData appData = new AppData(data);
                ServiceProcess serviceProcess = new ServiceProcess(getServiceName(data), service.getApp_Id().toString());
                String ipk = appData.getPropertyFromData("ipk");
                Issuer.IssuerPublicKey issuerPublicKey = new Issuer.IssuerPublicKey(this.getCurve(),ipk);
                this.setIssuerPublicKey(issuerPublicKey);
                this.listServiceProcessing.put(service.getApp_Id(),serviceProcess);
                System.out.println("application data get Database: "+data);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        HttpConnection http = new HttpConnection();
        try {
            String data = http.getApplicationData(Config.IssuerUrl, service.getApp_Id());
            AppData appData = null;
            try {
                appData = new AppData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (appData != null) {
                VerifierSignature verifierSignature = new VerifierSignature(service.getApp_Id(), data);
                databaseOperation.addCertificate(verifierSignature);
                ServiceProcess serviceProcess = new ServiceProcess(getServiceName(data), service.getApp_Id().toString());
                String ipk = appData.getPropertyFromData("ipk");
                Issuer.IssuerPublicKey issuerPublicKey = new Issuer.IssuerPublicKey(this.getCurve(),ipk);
                this.setIssuerPublicKey(issuerPublicKey);
                this.listServiceProcessing.put(service.getApp_Id(),serviceProcess);
                System.out.println("application data get from Issuer: "+data);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    };

    public String getVerifierData( Integer appId) {
        DatabaseOperation databaseOperation = new DatabaseOperation(dataSource);
        String data = null;
        try {
            data = databaseOperation.getAppData(appId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
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
            System.out.println("sigData KRD: "+ bytesToHex(EcDaaSig.krd));
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

    public String generateCertificate(String appSessionId, String verifierSessionId, String verifierData) throws Exception {
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject(verifierData);
        json.put("permission", data.getString("permission"));
        json.put("sessionId", verifierSessionId);
        json.put("status", "ok");
        String sig = signMessage(appSessionId, data);
        json.put("sig", sig);
        System.out.println("certificate: "+json.toString());
        return json.toString();
    };
    public String signMessage(String appSession,JSONObject data) throws Exception {
        // curve TPM_ECC_BN_P256
        BNCurve curve = BNCurve.createBNCurveFromName(Config.CURVE_NAME);
        // get gsk permission
        BigInteger gsk = new BigInteger(data.getString("gsk_permission"));
        // ipk
        Issuer.IssuerPublicKey issuerPublicKey = this.getIssuerPublicKey();
        // create authenticator
        Authenticator authenticator = new Authenticator(curve, issuerPublicKey, gsk);
        // create jm2
        String jm2Json = data.getString("credential_permission");
        Issuer.JoinMessage2 jm2 = new Issuer.JoinMessage2(curve, jm2Json);

        // joinM2
        authenticator.EcDaaJoin2Wrt(jm2, data.getString("permission"));

        byte[] info = data.getString("permission").getBytes();
        // sign
        byte[] sigByte = authenticator.EcDaaSignWrt(info, Config.BASENAME_SERVICE, appSession).encode(curve);
        return bytesToHex(sigByte);
    }

    public Boolean VerifyUserInfo(String serviceSessionId, UserSig userSig) {
        Verifier verifier = new Verifier(this.getCurve());
        // byte[] message
        byte[] information = userSig.getInformation().getBytes();
        // byte[] session
        byte[] sessionId = serviceSessionId.getBytes();
        // EcDaaSignature sig
        Authenticator.EcDaaSignature ecDaaSignature = new Authenticator.EcDaaSignature(hexStringToByteArray(userSig.getSig()), sessionId, this.getCurve());
        // String appId
        String baseName = "verification";
        // IssuerPublicKey ipk
        Issuer.IssuerPublicKey ipk = this.getIssuerPublicKey();
        Boolean resultVerify = false;
        try {
            resultVerify = verifier.verifyWrt(information, sessionId, ecDaaSignature, baseName, ipk, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("Result Verify: "+resultVerify);
        return resultVerify;
    }

    public Boolean VerifyServiceInfo(String serviceSessionId, ServiceSig serviceSig) {
        System.out.println("call verify");
        Verifier verifier = new Verifier(this.getCurve());
        // byte[] message
        byte[] information = hexStringToByteArray(serviceSig.getPermission().toString());
        // byte[] session
        byte[] sessionId = hexStringToByteArray(serviceSessionId);
        // EcDaaSignature sig
        Authenticator.EcDaaSignature ecDaaSignature = new Authenticator.EcDaaSignature(hexStringToByteArray(serviceSig.getSig()), sessionId, this.getCurve());
        // String appId
        String baseName = "permission";
        // IssuerPublicKey ipk
        Issuer.IssuerPublicKey ipk = this.getIssuerPublicKey();
        Boolean resultVerify = false;
        try {
            resultVerify = verifier.verifyWrt(information, sessionId, ecDaaSignature, baseName, ipk, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("Result Verify: "+resultVerify);
        return resultVerify;
    }
    public String getServiceName(String appData) {
        JSONObject json = new JSONObject(appData);
        JSONObject info = new JSONObject(json.getString("level_customer"));
        return info.getString("service_name");
    }
    @RequestMapping( method = RequestMethod.GET, value="/testData")
    public void  testRepository(HttpServletResponse response) throws IOException {
        try {
            DatabaseOperation databaseOperation = new DatabaseOperation();
            databaseOperation.setDataSource(dataSource);
            Boolean check = databaseOperation.checkAppIdExisted("1");
            Boolean add = databaseOperation.addCertificate(new VerifierSignature(2, "change test 2"));
            response.setStatus(200);
            response.getWriter().println("OK, result: "+check);
            response.getWriter().println("OK, result add: "+add);
            String sessionId = utils.generateSessionId();
            response.getWriter().println("sessionId: "+sessionId);
            this.listSessionId.put(sessionId, sessionId);
            response.getWriter().println("list sessionId: ");
            response.getWriter().println(this.listSessionId.toString());
            this.listSessionId.remove(sessionId);
        } catch (SQLException e) {
            System.out.println(e);
            response.setStatus(400);
            response.getWriter().println("FAIL");
        }
    }
}
