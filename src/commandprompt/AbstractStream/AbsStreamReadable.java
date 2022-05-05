/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.AbstractStream;

import Time.WaitTime.Class.TimeS;
import commandprompt.Communicate.IReadable;
import Time.WaitTime.ITimer;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Administrator
 */
public abstract class AbsStreamReadable implements IReadable {

    protected InputStream reader;

    protected AbsStreamReadable() {
    }

    protected AbsStreamReadable(InputStream reader) {
        this();
        this.reader = reader;
    }

    public boolean disConnect() {
        try {
            this.reader.close();
            return true;
        } catch (IOException ex) {
            System.out.println(ex);
            return false;
        }
    }

    public abstract AbsStreamReadable getReader();

    public void setReader(InputStream reader) {
        this.reader = reader;
    }

    public String readLine(ITimer time) {
        return readUntil("\n", time);
    }

    @Override
    public String readAll(ITimer tiker) {
        return readUntil(null, tiker);
    }

    @Override
    public String readUntil(String regex, ITimer tiker) {
        StringBuffer result;
        ITimer timer;
        try {
            result = new StringBuffer();
            timer = new TimeS(5);
            char kiTu;
            while (tiker.onTime() && reader != null) {
                if (reader.available() > 0) {
                    if ((kiTu = (char) reader.read()) == -1) {
                        break;
                    }
                    result.append(kiTu);
                    if (isKeyWord(result.toString(), regex)) {
                        break;
                    }
                    timer.update();
                    continue;
                }
                if (!timer.onTime()) {
                    break;
                }
            }
            return result.toString().isEmpty() ? null : result.toString();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    protected boolean isKeyWord(String result, String keyWord) {
        return keyWord != null && result.endsWith(keyWord);
    }

    protected boolean stringNotNull(String string) {
        return string != null;
    }

    public void clearResult() {
        try {
            while (reader != null && reader.available() > 0) {
                reader.read();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
