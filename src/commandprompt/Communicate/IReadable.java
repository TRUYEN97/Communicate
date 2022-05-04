/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.Communicate;

import Time.WaitTime.ITimer;

/**
 *
 * @author Administrator
 */
public interface IReadable {

    
    String readLine();

    String readAll();

    String readAll(ITimer tiker);

    String readUntil(String regex);

    String readUntil(String regex, ITimer tiker);
}
