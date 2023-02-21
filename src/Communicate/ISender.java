/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Communicate;

import java.io.Closeable;

/**
 *
 * @author Administrator
 */
public interface ISender extends Closeable{
    
    boolean sendCommand(String command);
    
    boolean insertCommand(String command);
}
