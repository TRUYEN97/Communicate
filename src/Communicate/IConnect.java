/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Communicate;

import java.io.Closeable;

/**
 *
 * @author Administrator
 */
public interface IConnect extends Closeable {

    boolean connect(String pram1, int pram2);

    boolean isConnect();
}
