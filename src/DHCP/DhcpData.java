/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DHCP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class DhcpData {

    private static volatile DhcpData instance;
    private final Map<String, String> idMac;
    private String netIP;
    private int macLength;

    private DhcpData() {
        this.idMac = new HashMap<>();
        this.macLength = 17;
    }

    public boolean setMacLength(int macLength) {
        if (macLength < 1) {
            return false;
        }
        this.macLength = macLength;
        return true;
    }
    
    public static DhcpData getInstance() {
        DhcpData ins = DhcpData.instance;
        if (ins == null) {
            synchronized (DhcpData.class) {
                ins = DhcpData.instance;
                if (ins == null) {
                    DhcpData.instance = ins = new DhcpData();
                }
            }
        }
        return ins;
    }

    public boolean put(String mac, int id) {
        String ip = this.netIP + id;
        try {
            String newMac = macFormat(mac);
            if (newMac.length() != macLength) {
                return false;
            }
            deleteIpOlder(ip);
            this.idMac.put(newMac, ip);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String macFormat(String mac) {
        mac = mac.toUpperCase();
        if (!mac.contains(":")) {
            mac = createTrueMac(mac);
        }
        if (mac.length() > macLength) {
            mac = mac.substring(0, macLength);
        }
        return mac;
    }

    public String getIP(String mac) {
        return this.idMac.get(macFormat(mac));
    }

    public boolean setNetIP(String netIp) {
        try {
            this.netIP = netIp.substring(0, netIp.lastIndexOf(".") + 1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String createTrueMac(String value) {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (char kitu : value.toCharArray()) {
            if (index != 0 && index % 2 == 0) {
                builder.append(':');
            }
            builder.append(kitu);
            index++;
        }
        return builder.toString();
    }

    private void deleteIpOlder(String ip) {
        if (!this.idMac.containsValue(ip)) {
            return;
        }
        List<String> keys = new ArrayList<>();
        for (String key : this.idMac.keySet()) {
            if (this.idMac.get(key).equals(ip)) {
                keys.add(key);
            }
        }
        for (String key : keys) {
            this.idMac.remove(key);
        }
    }

    public int getMACLength() {
        return macLength;
    }
}
