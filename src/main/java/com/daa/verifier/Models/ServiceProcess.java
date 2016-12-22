package com.daa.verifier.Models;

/**
 * Created by DK on 12/22/16.
 */
public class ServiceProcess {
    private String serviceName;

    private String appId;

    public ServiceProcess(String serviceName, String appId) {
        this.serviceName = serviceName;
        this.appId = appId;
    }

    public ServiceProcess(){}

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
