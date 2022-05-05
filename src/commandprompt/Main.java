/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commandprompt;

import Time.WaitTime.Class.TimeMs;
import commandprompt.AbstractStream.SubClass.ReadStream;
import commandprompt.AbstractStream.SubClass.ReadStreamOverTime;
import commandprompt.Communicate.Cmd.Cmd;
import commandprompt.Communicate.Comport.ComPort;
import commandprompt.Communicate.Comport.IConnect;
import commandprompt.Communicate.Telnet.Telnet;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("1-Telnet 2-CMD  3-Comport: ");
        switch (scanner.nextInt()) {
            case 1 ->
                telnet(scanner);
            case 2 ->
                cmd(scanner);
            case 3 ->
                port(scanner);
        }
    }

    @SuppressWarnings("empty-statement")
    private static void telnet(Scanner scanner) {
        System.out.println("Telnet");
        Telnet telnet = new Telnet();
        while (!connect(scanner, telnet)) ;
        System.out.println(telnet.readAll(new TimeMs(100)));
        String input;
        while (scanner.hasNextLine()) {
            input = scanner.nextLine();
            telnet.insertCommand(input);
            String line;
            while ((line = telnet.readLine()) != null) {
                System.out.print(line);
            }
        }
    }

    private static boolean connect(Scanner scanner, IConnect telnet) {
        System.out.print("Input Host: ");
        scanner.nextLine();
        String host = scanner.nextLine();
        System.out.print("Input baud rate or port: ");
        int port = scanner.nextInt();
        return telnet.connect(host, port);
    }

    private static void cmd(Scanner scanner) {
        System.out.println("Command line");
        Cmd cmd = new Cmd(new ReadStream());
        while (scanner.hasNextLine()) {
            cmd.sendCommand(scanner.nextLine());
            String line;
            while ((line = cmd.readLine()) != null) {
                System.out.print(line);
            }
        }

    }

    @SuppressWarnings("empty-statement")
    private static void port(Scanner scanner) {
        System.out.println("ComPort");
        ComPort comPort = new ComPort();
        while (!connect(scanner, comPort)) ;
        String input;
        while (scanner.hasNextLine()) {
            input = scanner.nextLine();
            comPort.insertCommand(input);
            String line;
            while ((line = comPort.readLine()) != null) {
                System.out.print(line);
            }
        }
    }

}
