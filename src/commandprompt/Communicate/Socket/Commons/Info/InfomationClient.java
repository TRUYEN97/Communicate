/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commandprompt.Communicate.Socket.Commons.Info;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class InfomationClient implements Serializable {

    private final Map<String, Object> info;

    public InfomationClient() {
        info = new HashMap<>();
    }

    public Object put(String key, Object value) {
        return info.put(key, value);
    }

    public Object get(String key) {
        return info.get(key);
    }

}
