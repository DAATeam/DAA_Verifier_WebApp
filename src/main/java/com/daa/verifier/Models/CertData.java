package com.daa.verifier.Models;

import javax.validation.constraints.NotNull;

/**
 * Created by DK on 12/6/16.
 */
public class CertData {
    @NotNull
    public String msg;
    @NotNull
    public String certificate;
    @NotNull
    public String status;

    public CertData() {
    }

    public CertData(String msg, String certificate, String status) {
        this.msg = msg;
        this.certificate = certificate;
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
