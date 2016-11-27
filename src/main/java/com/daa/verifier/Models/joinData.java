package com.daa.verifier.Models;

import javax.validation.constraints.NotNull;
import com.daa.verifier.Models.constantVariables;

import java.math.BigInteger;

/**
 * Created by DK on 11/27/16.
 */
public class joinData {
    public String msg;
    public String ipk;
    public String curveName;
    @NotNull
    public BigInteger nonce;
    public String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIpk() {
        return ipk;
    }

    public void setIpk(String ipk) {
        this.ipk = ipk;
    }

    public String getCurveName() {
        return curveName;
    }

    public void setCurveName(String curveName) {
        this.curveName = curveName;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public joinData() {
    }

}
