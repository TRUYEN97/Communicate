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

    static {
        instance = new DhcpData();
    }
    private final Map<String, String> macIps;
    private final Map<String, String> ipMacs;
    private String netIP;
    private int macLength;

    private DhcpData() {
        this.macLength = 17;
        this.macIps = new HashMap<>();
        this.ipMacs = new HashMap<>();
    }

    public boolean setMacLength(int macLength) {
        if (macLength < 1) {
            return false;
        }
        this.macLength = macLength;
        return true;
    }

    public static DhcpData getInstance() {
        return instance;
    }

    public synchronized boolean put(String mac, int id) {
        try {
            if (id < 0 || id > 255 || mac == null) {
                return false;
            }
            mac = MacUtil.macFormat(mac, macLength);
            if (mac == null) {
                return false;
            }
            String ip = createIp(id);
            if (ip == null) {
                return false;
            }
            resetMACInfo(mac, ip);
            addMacIp(mac, ip);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean addMacIp(String mac, String ip) {
        if (mac == null || ip == null) {
            return false;
        }
        String macOld = this.ipMacs.put(ip, mac);
        if (macOld != null) {
            this.macIps.remove(macOld);
        }
        this.macIps.put(mac, ip);
        return true;
    }

    private void resetMACInfo(String mac, String ip) {
        if (this.ipMacs.containsKey(ip)) {
            this.ipMacs.remove(ip);
        } else if (this.macIps.containsKey(mac)) {
            this.ipMacs.remove(mac);
        }
    }

    public synchronized String getIP(String mac) throws Exception {
        if (mac == null || (mac = MacUtil.macFormat(mac, macLength)) == null) {
            throw new Exception(String.format("invalid MAC format. MAC is %s", mac));
        }
        return this.macIps.get(mac);
    }
    
    public synchronized String getMAC(String ip){
        if (ip == null) {
            return null;
        }
        return this.ipMacs.get(ip);
    }

    public boolean setNetIP(String netIp) {
        try {
            this.netIP = netIp.substring(0, netIp.lastIndexOf("."));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getMACLength() {
        return macLength;
    }

    private String createIp(Integer id) {
        if (id == null || id < 0 || id > 255) {
            return null;
        }
        return String.format("%s.%s", this.netIP, id);
    }
}
