/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AbstractStream;

import Time.WaitTime.AbsTime;
import Communicate.IReadable;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Administrator
 */
public abstract class AbsStreamReadable implements IReadable, Closeable {

    protected InputStream reader;
    private StringBuffer stringResult;

    protected AbsStreamReadable() {
        this.stringResult = new StringBuffer();
    }

    protected AbsStreamReadable(InputStream reader) {
        this();
        this.reader = reader;
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            this.reader.close();
        }
    }

    public void setReader(InputStream reader) {
        this.reader = reader;
    }

    @Override
    public String readLine(AbsTime time) {
        return readUntil( time,"\n");
    }

    @Override
    public String readAll(AbsTime tiker) {
        return readUntil(tiker);
    }

    @Override
    public String readUntil(AbsTime tiker, String... keywords) {
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
                    if (isKeyWord(result.toString(), keywords)) {
                        break;
                    }
                }
            }
            this.stringResult = result;
            return result.toString().trim().isBlank() ? null : result.toString().trim();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public StringBuffer getStringResult() {
        return stringResult;
    }

    protected boolean isKeyWord(String result, String... keyWords) {
        if (keyWords != null && result!= null && keyWords.length > 0) {
            for (String keyWord : keyWords) {
                if (!keyWord.isEmpty() && result.endsWith(keyWord)) {
                    return true;
                }
            }
        }
        return false;
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
