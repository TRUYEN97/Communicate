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
    private final Map<String, Integer> macIDs;
    private final List<String> macs;
    private String netIP;
    private int macLength;

    private DhcpData() {
        this.macLength = 17;
        this.macIDs = new HashMap<>();
        this.macs = new ArrayList<>();
        for (int i = 0; i < 255; i++) {
            macs.add(null);
        }
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

    public synchronized boolean put(String mac, int id) {
        try {
            if (id < 0 || id > 255 || mac == null) {
                return false;
            }
            mac = MacUtil.macFormat(mac, macLength);
            if (mac == null) {
                return false;
            }
            resetMACInfo(mac, id);
            addMacID(mac, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addMacID(String mac, int id) {
        this.macIDs.put(mac, id);
        this.macs.add(id, mac);
    }

    private void resetMACInfo(String mac, int id) {
        Integer otherID = this.macIDs.get(mac);
        if (otherID != null && otherID != id) {
            String otherMac = this.macs.get(otherID);
            if (otherMac != null) {
                this.macIDs.remove(otherMac);
                this.macs.add(otherID, null);
            }
        }
    }

    public String getIP(String mac) {
        if (mac == null || (mac = MacUtil.macFormat(mac, macLength)) == null) {
            return null;
        }
        Integer id = this.macIDs.get(mac);
        return createIp(id);
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
