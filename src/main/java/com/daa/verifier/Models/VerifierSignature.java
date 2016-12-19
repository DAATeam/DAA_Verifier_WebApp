package com.daa.verifier.Models;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by DK on 12/17/16.
 */
public class VerifierSignature implements Serializable {
    private int appId;
    private String data;

    protected VerifierSignature() {}

    public VerifierSignature(int appId, String data) {
        this.appId = appId;
        this.data = data;
    }
    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean checkExistDB() {

        return true;
    }

    public String toString() {
        return "appId: "+this.getAppId()+", data: "+this.getData();
    }
}
