package com.daa.verifier.Controllers;

import org.json.JSONObject;

/**
 * Created by DK on 11/27/16.
 */
public class Config {
    public static final JSONObject PERMISSION_OBJECT = new JSONObject().put("service_permission", "user_name,user_job");
//     public static final String IssuerUrl = "http://localhost:9081";
    public static final String IssuerUrl = "http://localhost:8081/issuer";
    public static final String USER_AGENT = "SP-App";
    public static final String REQUIRE_INFO = "service_permission";
    public static final String CURVE_NAME = "TPM_ECC_BN_P256";
    public static final String CERT_BASENAME = "attestation";
    public static final String PERMISSION="{\"service_permission\":\"\\\"user_name,user_job\\\"\"}";
}
