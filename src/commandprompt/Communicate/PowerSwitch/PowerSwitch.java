/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commandprompt.Communicate.PowerSwitch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Administrator
 */
public class PowerSwitch {

    private final HttpClient httpclient;
    private final String authHeader;
    private final String host;
    private String result;

    public PowerSwitch(String host, String username, String password) {
        this.host = host;
        this.httpclient = HttpClientBuilder.create().build();
        String auth = String.format("%s:%s", username, password);
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.ISO_8859_1));
        authHeader = "Basic " + new String(encodedAuth);
    }

    public boolean setOn(int index) {
        if (checkIndex(index)) {
            return false;
        }
        String command = String.format("http://%s/outlet?%d=ON", this.host, index);
        return powerSwitch(command) == 200;
    }

    public boolean setOff(int index) {
         if (checkIndex(index)) {
            return false;
        }
        String command = String.format("http://%s/outlet?%d=OFF", this.host, index);
        return powerSwitch(command) == 200;
    }
    
    public boolean setOnAll() {
        String command = String.format("http://%s/outlet?a=ON", this.host);
        return powerSwitch(command) == 200;
    }
    
    public boolean setCycle(int index) {
         if (checkIndex(index)) {
            return false;
        }
        String command = String.format("http://%s/outlet?%d=CCL", this.host, index);
        return powerSwitch(command) == 200;
    }
    
    private boolean setOffAll() {
        String command = String.format("http://%s/outlet?a=OFF", this.host);
        return powerSwitch(command) == 200;
    }

    public int powerSwitch(String command) {
        HttpGet request = new HttpGet(command);
        try {
            request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            HttpResponse response = httpclient.execute(request);
            result = response.getStatusLine().toString();
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
            result = "Exception: " + e.getMessage();
            return -1;
        }
    }
    
    public String getResult() {
        return result;
    }

    private boolean checkIndex(int index) {
        if (index < 1 || index > 8) {
            result = "index out of range!";
            return true;
        }
        return false;
    }
    
}
