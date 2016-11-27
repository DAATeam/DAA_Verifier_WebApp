package com.daa.verifier.Models;

import javax.validation.constraints.NotNull;

/**
 * Created by DK on 11/27/16.
 */
public class joinM1Data {
    @NotNull
    public String jm1;
    @NotNull
    public String field;

    public joinM1Data(String jm1, String field) {
        this.jm1 = jm1;
        this.field = field;
    }

    public String getJm1() {
        return jm1;
    }

    public void setJm1(String jm1) {
        this.jm1 = jm1;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
