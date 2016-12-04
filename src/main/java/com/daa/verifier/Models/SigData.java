package com.daa.verifier.Models;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * Created by DK on 12/3/16.
 */
public class SigData {
    @NotNull
    public String sig;

    @NotNull
    public String nonce;

    @NotNull
    public String basename;

    public SigData() {
    }
    public SigData(String sig, BigInteger nonce, String basename) {
        this.setBasename(basename);
        this.setNonce(nonce.toString());
        this.setSig(sig);
    }

    public String getSig() {
        return sig;
    }
    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }
}
