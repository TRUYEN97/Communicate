/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package commandprompt.Communicate;

import Time.WaitTime.AbsTime;

/**
 *
 * @author Administrator
 */
public interface IReadable {

    
    String readLine();
    
    String readLine(AbsTime tiker);

    String readAll();

    String readAll(AbsTime tiker);

    String readUntil(String regex);

    String readUntil(String regex, AbsTime tiker);
}
