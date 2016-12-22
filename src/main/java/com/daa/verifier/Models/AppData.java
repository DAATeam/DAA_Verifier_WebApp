package com.daa.verifier.Models;

import com.google.gson.JsonParser;
import org.json.JSONObject;

/**
 * Created by DK on 12/22/16.
 */
public class AppData {

    private String data;

    private JSONObject objectData;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        this.objectData = toJson();
    }

    public AppData(String data) {
        this.data = data;
        this.objectData = toJson();
    }

    public AppData() {
        this.data = null;
    }

    public JSONObject toJson() {
        return new JSONObject(this.getData());
    }

    public String getPropertyFromData(String keyName) {
        return this.objectData.getString(keyName);
    }
}
