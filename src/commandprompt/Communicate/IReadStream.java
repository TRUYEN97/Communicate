/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package commandprompt.Communicate;

import commandprompt.AbstractStream.AbsStreamReadable;

/**
 *
 * @author Administrator
 */
public interface IReadStream extends IReadable{
    boolean setStreamReadable(AbsStreamReadable readable);
}
