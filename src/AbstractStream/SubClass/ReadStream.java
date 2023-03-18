/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AbstractStream.SubClass;

import Time.WaitTime.Class.TimeS;
import AbstractStream.AbsStreamReadable;
import java.io.InputStream;

/**
 *
 * @author Administrator
 */
public class ReadStream extends AbsStreamReadable {

    public ReadStream(InputStream reader) {
        super(reader);
    }

    public ReadStream() {
    }
    
    @Override
    public String readAll() {
        return readAll(new TimeS(MAX_TIME));
    }

    @Override
    public String readUntil(String... keywords) {
        return readUntil(new TimeS(MAX_TIME), keywords );
    }

    @Override
    public String readLine() {
        return readLine(new TimeS(MAX_TIME));
    }

}
