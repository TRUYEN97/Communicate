/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.Communicate.Telnet;

import Time.WaitTime.ITimer;
import commandprompt.AbstractStream.AbsStreamReadable;
import commandprompt.AbstractStream.SubClass.ReadStreamOnTime;
import commandprompt.Communicate.IReadable;
import commandprompt.Communicate.ISender;
import java.io.PrintStream;
import org.apache.commons.net.telnet.TelnetClient;

/**
 *
 * @author Administrator
 */
public class Telnet implements ISender, IReadable {

    private TelnetClient telnet;
    private PrintStream out;
    private AbsStreamReadable input;

    public Telnet(String server) {
        telnet = new TelnetClient();
        this.input = new ReadStreamOnTime();
//        for (int i = 0; i < 3; i++) {
            try {
                telnet.connect(server, 23);
                input.setReader(telnet.getInputStream());
                out = new PrintStream(telnet.getOutputStream());
            } catch (Exception e) {
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
//        }
    }

    public Telnet(String server, AbsStreamReadable input) {
        this(server);
        this.input = input;
    }

    public boolean disconnect() {
        try {
            telnet.disconnect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean sendCommand(String command) {
        input.clearResult();
        return insertCommand(command);
    }

    @Override
    public boolean insertCommand(String command) {
        try {
            out.println(command);
            out.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String readLine() {
        return input.readLine();
    }

    @Override
    public String readAll() {
        return input.readAll();
    }

    @Override
    public String readAll(ITimer tiker) {
        return input.readAll(tiker);
    }

    @Override
    public String readUntil(String regex) {
        return input.readUntil(regex);
    }

    @Override
    public String readUntil(String regex, ITimer tiker) {
        return input.readUntil(regex, tiker);
    }
}
