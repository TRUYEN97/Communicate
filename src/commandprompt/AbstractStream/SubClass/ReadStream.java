/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.AbstractStream.SubClass;

import Time.WaitTime.Class.TimeS;
import commandprompt.AbstractStream.AbsStreamReadable;
import java.io.InputStream;

/**
 *
 * @author Administrator
 */
public class ReadStream extends AbsStreamReadable {

    private static final int MAX = Integer.MAX_VALUE;

    public ReadStream(InputStream reader) {
        super(reader);
    }

    public ReadStream() {
    }
    
    @Override
    public String readAll() {
        return readAll(new TimeS(MAX));
    }

    @Override
    public String readUntil(String regex) {
        return readUntil(regex, new TimeS(MAX));
    }

    @Override
    public String readLine() {
        return readUntil("\n", new TimeS(MAX));
    }

    @Override
    public AbsStreamReadable getReader() {
       return this;
    }
}
