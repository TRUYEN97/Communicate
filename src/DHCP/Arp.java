/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DHCP;

import Communicate.Impl.Cmd.Cmd;

/**
 *
 * @author Administrator
 */
public class Arp implements Runnable {

    @Override
    public void run() {
        try ( Cmd cmd = new Cmd()) {
            if(cmd.sendCommand("arp -d")){
                System.out.println(cmd.readAll());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
