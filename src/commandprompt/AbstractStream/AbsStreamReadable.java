/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.AbstractStream;

import Time.WaitTime.AbsTime;
import Time.WaitTime.Class.TimeS;
import commandprompt.Communicate.IReadable;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public abstract class AbsStreamReadable implements IReadable {

    protected InputStream reader;
    private StringBuffer stringResult;

    protected AbsStreamReadable() {
        this.stringResult = new StringBuffer();
    }

    protected AbsStreamReadable(InputStream reader) {
        this();
        this.reader = reader;
    }

    public boolean disConnect() {
        try {
            if (reader != null) {
                this.reader.close();
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void setReader(InputStream reader) {
        this.reader = reader;
    }

    @Override
    public String readLine(AbsTime time) {
        return readUntil("\n", time);
    }

    @Override
    public String readAll(AbsTime tiker) {
        return readUntil(null, tiker);
    }

    @Override
    public String readUntil(String regex, AbsTime tiker) {
        StringBuffer result;
        try {
            result = new StringBuffer();
            char kiTu;
            while (tiker.onTime() && reader != null) {
                if (reader.available() > 0) {
                    if ((kiTu = (char) reader.read()) == -1) {
                        continue;
                    }
                    result.append(kiTu);
                    if (isKeyWord(result.toString(), regex)) {
                        break;
                    }
                }else{
                    Thread.sleep(100);
                }
            }
            this.stringResult = result;
            return result.toString().isEmpty() ? null : result.toString();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (InterruptedException ex) {
            
        }
        return null;
    }

    @Override
    public StringBuffer getStringResult() {
        return stringResult;
    }

    protected boolean isKeyWord(String result, String keyWord) {
        return keyWord != null && result.endsWith(keyWord);
    }

    protected boolean stringNotNull(String string) {
        return string != null;
    }

    public void clearResult() {
        try {
            stringResult.delete(0, stringResult.length());
            while (reader != null && reader.available() > 0) {
                reader.read();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
