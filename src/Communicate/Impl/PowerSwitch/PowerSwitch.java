/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Communicate.Impl.PowerSwitch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Administrator
 */
public class PowerSwitch {

    private final String authHeader;
    private final String host;
    private final StringBuilder result;

    public PowerSwitch(String host, String username, String password) {
        this.host = host;
        this.result = new StringBuilder();
        String auth = String.format("%s:%s", username, password);
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.ISO_8859_1));
        this.authHeader = "Basic " + new String(encodedAuth);
    }

    public boolean setOn(int index) {
        if (!checkIndex(index)) {
            return false;
        }
        String command = String.format("http://%s/outlet?%d=ON", this.host, index);
        addLog(command);
        return powerSwitch(command) == 200;
    }

    public boolean setOff(int index) {
        if (!checkIndex(index)) {
            return false;
        }
        String command = String.format("http://%s/outlet?%d=OFF", this.host, index);
        addLog(command);
        return powerSwitch(command) == 200;
    }

    public boolean setOnAll() {
        String command = String.format("http://%s/outlet?a=ON", this.host);
        return powerSwitch(command) == 200;
    }

    public boolean setOffAll() {
        String command = String.format("http://%s/outlet?a=OFF", this.host);
        addLog(command);
        return powerSwitch(command) == 200;
    }

    public boolean setCycle(int index) {
        if (!checkIndex(index)) {
            return false;
        }
        String command = String.format("http://%s/outlet?%d=CCL", this.host, index);
        addLog(command);
        return powerSwitch(command) == 200;
    }

    public int powerSwitch(String command) {
        HttpGet request = new HttpGet(command);
        request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        try ( CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            try ( CloseableHttpResponse response = httpClient.execute(request)) {
                addLog(response.getStatusLine().toString());
                return response.getStatusLine().getStatusCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.append("Exception: ").append(e.getLocalizedMessage());
            return -1;
        }
    }

    public String getResult() {
        String tempRs = result.toString();
        result.setLength(0);
        return tempRs;
    }

    private void addLog(String log) {
        result.append(log).append("\r\n");
    }

    private boolean checkIndex(int index) {
        if (index < 1 || index > 8) {
            result.append("index out of range!");
            return false;
        }
        return true;
    }

}
