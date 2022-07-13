/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.Communicate.Telnet;

import Time.WaitTime.AbsTime;
import commandprompt.AbstractStream.AbsStreamReadable;
import commandprompt.AbstractStream.SubClass.ReadStream;
import commandprompt.Communicate.IConnect;
import commandprompt.Communicate.IReadStream;
import commandprompt.Communicate.ISender;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.net.telnet.TelnetClient;

/**
 *
 * @author Administrator
 */
public class Telnet implements ISender, IReadStream, IConnect {

    private final TelnetClient telnet;
    private PrintStream out;
    private AbsStreamReadable input;
    private String host;

    public Telnet() {
        this.telnet = new TelnetClient();
        this.input = new ReadStream();
    }

    public Telnet(AbsStreamReadable readable) {
        this.telnet = new TelnetClient();
        this.input = readable;
    }

    @Override
    public boolean setStreamReadable(AbsStreamReadable readable) {
        if (this.input.disConnect()) {
            this.input = readable;
            return true;
        }
        return true;
    }

    @Override
    public StringBuffer getStringResult() {
        return this.input.getStringResult();
    }

    @Override
    public boolean connect(String host, int port) {
        try {
            telnet.connect(host, port);
            input.setReader(telnet.getInputStream());
            out = new PrintStream(telnet.getOutputStream());
            this.host = host;
            return true;
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public boolean disConnect() {
        try {
            telnet.disconnect();
            return true;
        } catch (IOException e) {
            System.err.println(e);
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
    public String readLine(AbsTime time) {
        return input.readLine(time);
    }

    @Override
    public String readAll() {
        return input.readAll();
    }

    @Override
    public String readAll(AbsTime tiker) {
        return input.readAll(tiker);
    }

    @Override
    public String readUntil(String regex) {
        return input.readUntil(regex);
    }

    @Override
    public String readUntil(String regex, AbsTime tiker) {
        return input.readUntil(regex, tiker);
    }

    public int getPort() {
        return telnet.getLocalPort();
    }

    public String getHost() {
        return this.host;
    }

    @Override
    public boolean isConnect() {
        return telnet.isConnected();
    }
}
