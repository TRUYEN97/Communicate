/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commandprompt;

import commandprompt.AbstractStream.SubClass.ReadStreamOnTime;
import commandprompt.AbstractStream.SubClass.ReadStreamOverTime;
import commandprompt.Communicate.Cmd.Cmd;
import commandprompt.Communicate.Telnet.Telnet;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
//        Cmd cmd = new Cmd(new ReadStreamOnTime());
            Telnet telnet = new Telnet("192.168.1.1");
            System.out.println(telnet.readAll());
        while (scanner.hasNextLine()) {
            input = scanner.nextLine();
            telnet.insertCommand(input);
            String line;
            while ((line = telnet.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("kkkkkkkkkkkkkkkkkkkkkkkk");
        }
    }
}
