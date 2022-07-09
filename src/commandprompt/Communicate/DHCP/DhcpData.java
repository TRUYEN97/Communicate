/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commandprompt.Communicate.DHCP;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class DhcpData {

    private static volatile DhcpData instance;
    private final Map<String, String> idMac;
    private String netIP;

    private DhcpData() {
        this.idMac = new HashMap<>();
        this.idMac.put("649714048d60", "172.168.1.0");
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

    public void put(String mac, int id) {
        String ip = this.netIP + id;
        deleteIpOlder(ip);
        this.idMac.put(mac, ip);
    }

    public String getIP(String mac) {
        return this.idMac.get(mac.replaceAll(":", ""));
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

    private void deleteIpOlder(String ip) {
        for (String key : this.idMac.keySet()) {
            if (this.idMac.get(key).equals(ip)) {
                this.idMac.remove(key);
            }
        }
    }
}
