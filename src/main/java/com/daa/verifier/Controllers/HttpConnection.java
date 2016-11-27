package com.daa.verifier.Controllers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.daa.verifier.Models.Service;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import com.daa.verifier.Models.constantVariables;
import com.daa.verifier.Models.joinM1Data;
import org.json.JSONObject;

/**
 * Created by DK on 11/27/16.
 */
public class HttpConnection {
    public HttpClient client = HttpClientBuilder.create().build();
    public HttpConnection() {
    }
    public JSONObject sendJoinMessage(String url, Service service) throws Exception {
        // user-agent in Config file
        String urlJoin = url+"/join";
        HttpPost post = new HttpPost(urlJoin);

        post.setHeader("User-Agent", Config.USER_AGENT);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

        urlParameters.add(new BasicNameValuePair(constantVariables.APP_ID, service.getApp_Id().toString()));
        urlParameters.add(new BasicNameValuePair(constantVariables.M, service.getM()));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = this.client.execute(post);
        String result = readResponse(response);
        return new JSONObject(result);
    }
    public JSONObject sendJoinMessage1(String url, joinM1Data jm1) throws Exception {
        String urlJoin = url+"/jm1";
        HttpPost post = new HttpPost(urlJoin);

        post.setHeader("User-Agent", Config.USER_AGENT);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

        urlParameters.add(new BasicNameValuePair(constantVariables.JOIN_MESSAGE_1, jm1.getJm1()));
        urlParameters.add(new BasicNameValuePair(constantVariables.FIELD, jm1.getField()));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = this.client.execute(post);
        String result = readResponse(response);

        return new JSONObject(result);
    }
    public String readResponse(HttpResponse response) {
        try {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            System.out.println("response: "+result.toString());
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    };


}
