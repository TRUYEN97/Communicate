/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DHCP;

/**
 *
 * @author Administrator
 */
public class MacUtil {

    public static final String IP_REGEX = "\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b";
    public static final String MAC_REGEX = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";

    public static boolean isIPAddr(String mac) {
        return mac != null && mac.matches(IP_REGEX);
    }
    
    public static boolean isMACAddr(String mac) {
        return mac != null && mac.matches(MAC_REGEX);
    }

    public static String macFormat(String mac, int macLength) {
        mac = mac.toUpperCase();
        if (!mac.contains(":")) {
            mac = insertPare(mac);
        }
        if (!isMACAddr(mac)) {
            return null;
        }
        if (mac.length() > macLength && macLength > 0) {
            mac = mac.substring(0, macLength);
        }
        return mac;
    }
    
    private static String insertPare(String value) {
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
}
