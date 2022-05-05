/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package commandprompt.Communicate.Comport;

/**
 *
 * @author Administrator
 */
public interface IConnect {

    boolean connect(String port, int baudrate);

    boolean disConnect();

    boolean isConnect();
}
