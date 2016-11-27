package com.daa.verifier.Models;

import javax.validation.constraints.NotNull;

/**
 * Created by DK on 11/26/16.
 */
public class Service {
    @NotNull
    public Integer app_Id;
    @NotNull
    public String m;

    public Service(Integer app_Id, String m) {
        this.app_Id = app_Id;
        this.m = m;
    }

    public Integer getApp_Id() {
        return app_Id;
    }

    public void setApp_Id(Integer app_Id) {
        this.app_Id = app_Id;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
}
