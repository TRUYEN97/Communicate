/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.AbstractStream.SubClass;

import commandprompt.AbstractStream.AbsStreamReadable;
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
            return null;
        }
    }

    @Override
    public String readAll() {
        return readUntil(null);
    }

    @Override
    public String readUntil(String regex) {
        StringBuilder data = new StringBuilder();
        String str;
        while (stringNotNull(str = readLine())) {
            data.append(str).append("\n");
            if (isKeyWord(data.toString(), regex)) {
                break;
            }
        }
        return data.toString();
    }

}
