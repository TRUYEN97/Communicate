/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AbstractStream.SubClass;

import AbstractStream.AbsStreamReadable;
import Time.WaitTime.AbsTime;
import Time.WaitTime.Class.TimeH;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Administrator
 */
public class ReadStreamOverTime extends AbsStreamReadable {

    private BufferedReader scanner = null;

    public ReadStreamOverTime() {
        super();
    }

    public ReadStreamOverTime(InputStream reader) {
        super(reader);
    }

    @Override
    public void setReader(InputStream reader) {
        super.setReader(reader);
        scanner = new BufferedReader(new InputStreamReader(this.reader));
    }

    @Override
    public String readLine() {
        try {
            return scanner.readLine();
        } catch (IOException ex) {
            showException(ex);
            return null;
        }
    }

    @Override
    public String readAll() {
        return readAll(new TimeH(MAX_TIME));
    }

    @Override
    public String readAll(AbsTime tiker) {
        StringBuilder data = new StringBuilder();
        String str;
        while (tiker.onTime() && stringNotNull(str = readLine())) {
            data.append(str).append("\r\n");
        }
        return data.toString();
    }

    @Override
    public String readUntil(String... keywords) {
        return readUntil(new TimeH(MAX_TIME), keywords);
    }

}
