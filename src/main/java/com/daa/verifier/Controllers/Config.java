package com.daa.verifier.Controllers;

/**
 * Created by DK on 11/27/16.
 */
public class Config {
    public static final String IssuerUrl = "http://localhost:8081/issuer";
    public static final String USER_AGENT = "SP-App";
    public static final String REQUIRE_INFO = "user_name,user_job";
    public static final String CURVE_NAME = "TPM_ECC_BN_P256";
    public static final String CERT_BASENAME = "attestation";
}
