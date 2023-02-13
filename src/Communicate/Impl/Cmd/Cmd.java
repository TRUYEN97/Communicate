/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communicate.Impl.Cmd;

import Communicate.ISender;
import AbstractStream.AbsStreamReadable;
import AbstractStream.SubClass.ReadStreamOverTime;
import Communicate.AbsCommunicate;
import Communicate.IReadStream;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class Cmd extends AbsCommunicate implements ISender, IReadStream {

    private Process process;
    private final ProcessBuilder builder;

    public Cmd() {
        this.input = new ReadStreamOverTime();
        this.builder = new ProcessBuilder();
        this.builder.redirectErrorStream(true);
    }

    public Cmd(AbsStreamReadable reader) {
        this.input = reader;
        this.builder = new ProcessBuilder();
        this.builder.redirectErrorStream(true);
    }

    @Override
    public boolean insertCommand(String command) {
        destroy();
        this.builder.command("cmd.exe", "/c", command);
        try {
            this.process = builder.start();
            this.input.setReader(process.getInputStream());
            return true;
        } catch (IOException ex) {
            showException(ex);
            return false;
        }
    }

    private void destroy() {
        try {
            close();
        } catch (IOException e) {
            showException(e);
        }
    }

    @Override
    protected void closeThis() throws IOException {
        if (process != null) {
            process.destroy();
        }
    }

}
