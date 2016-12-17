package com.daa.verifier.Models;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by DK on 12/17/16.
 */
@Entity
@Table(name = "signature")
public class VerifierSignature implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "app_id", nullable = false)
    private int appId;
    @Column(name = "data")
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
}
