/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.AbstractStream.SubClass;

import commandprompt.AbstractStream.AbsStreamReadable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author Administrator
 */
public class ReadStreamOverTime extends AbsStreamReadable {

    private Scanner scanner = null;

    public ReadStreamOverTime() {
        super();
    }

    public ReadStreamOverTime(InputStream reader) {
        super(reader);
    }

    @Override
    public void setReader(InputStream reader) {
        super.setReader(reader);
        scanner = new Scanner(new InputStreamReader(this.reader));
    }

    @Override
    public String readLine() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return null;
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

    @Override
    public AbsStreamReadable getReader() {
        return this;
    }

}
