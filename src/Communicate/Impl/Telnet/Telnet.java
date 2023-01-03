/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communicate.Impl.Telnet;

import Time.WaitTime.AbsTime;
import AbstractStream.AbsStreamReadable;
import AbstractStream.SubClass.ReadStream;
import Communicate.AbsCommunicate;
import Communicate.IConnect;
import Communicate.IReadStream;
import Communicate.ISender;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.commons.net.telnet.TelnetClient;

/**
 *
 * @author Administrator
 */
public class Telnet extends AbsCommunicate implements ISender, IReadStream, IConnect, Closeable {

    private final TelnetClient telnet;
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
    public boolean connect(String host, int port) {
        try {
            telnet.connect(host, port);
            input.setReader(telnet.getInputStream());
            out = new PrintStream(telnet.getOutputStream());
            this.host = host;
            return true;
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
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

    @Override
    public void close() throws IOException {
        if (telnet != null) {
            telnet.disconnect();
        }
        closeInput();
    }
}
