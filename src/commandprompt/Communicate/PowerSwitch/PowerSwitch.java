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
    private String result;

    public PowerSwitch(String username, String password) {
        this.httpclient = HttpClientBuilder.create().build();
        String auth = String.format("%s:%s", username, password);
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.ISO_8859_1));
        authHeader = "Basic " + new String(encodedAuth);
    }

    public int powerSwitch(String command) {
        HttpGet request = new HttpGet(command);
        try {
            request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(request);
            result = response.getStatusLine().getReasonPhrase();
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
            result = "Exception: "+e.getMessage();
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println(new PowerSwitch("admin", "1234").powerSwitch("http://192.168.0.10/outlet?1=ON"));
    }
}
