package com.daa.verifier.Models;

import org.json.JSONObject;

/**
 * Created by DK on 12/28/16.
 */
public class UserSig {
    private String status;
    private String sig;
    private String information;

    public UserSig() {
    }

    public UserSig(String status, String sig, String information) {
        this.status = status;
        this.sig = sig;
        this.information = information;
    }

    public UserSig(String stringJson) {
        System.out.println("stringJson: "+stringJson);
        try {
            JSONObject json = new JSONObject(stringJson);
            this.status = json.getString("status");
            this.sig = json.getString("sig");
            this.information = json.getString("information");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
    public String getDetailInfo(String keyName) {
        JSONObject json = new JSONObject(this.getInformation());
        if (json.has(keyName)) {
            return json.getString(keyName);
        }
        return null;
    }
}
