/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.Communicate.Cmd;

import Time.WaitTime.AbsTime;
import commandprompt.Communicate.IReadable;
import commandprompt.Communicate.ISender;
import Time.WaitTime.Class.TimeS;
import commandprompt.AbstractStream.AbsStreamReadable;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class Cmd implements ISender, IReadable {

    private Process process;
    private final ProcessBuilder builder;
    private final AbsStreamReadable reader;

    public Cmd(AbsStreamReadable reader) {
        this.reader = reader;
        this.builder = new ProcessBuilder();
        this.builder.redirectErrorStream(true);
    }

    @Override
    public boolean sendCommand(String command) {
        reader.clearResult();
        return insertCommand(command);
    }

    public boolean pingTo(String url) {
        String cmd = "ping ".concat(url);
        if (sendCommand(cmd)) {
            return reader.readUntil("TTL=", new TimeS(5)).contains("TTL=");
        }
        return false;
    }

    public boolean pingUntilConnect(String url, AbsTime timer) {
        String cmd = String.format("ping %s -t", url);
        if (sendCommand(cmd)) {
            return reader.readUntil("TTL=", timer).contains("TTL=");
        }
        return false;
    }

    @Override
    public boolean insertCommand(String command) {
        destroy();
        this.builder.command("cmd.exe", "/c", command);
        try {
            this.process = builder.start();
            this.reader.setReader(process.getInputStream());
            return true;
        } catch (IOException ex) {
            System.err.println(ex);
            return false;
        }
    }

    private void destroy() {
        if (process != null) {
            process.destroy();
        }
    }

    @Override
    public String readLine() {
        return reader.readLine();
    }

    @Override
    public String readAll() {
        return reader.readAll();
    }

    @Override
    public String readAll(AbsTime tiker) {
        return reader.readAll(tiker);
    }

    @Override
    public String readUntil(String regex) {
        return reader.readUntil(regex);
    }

    @Override
    public String readUntil(String regex, AbsTime tiker) {
        return reader.readUntil(regex, tiker);
    }

}
