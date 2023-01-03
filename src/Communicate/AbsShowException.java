/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Communicate;

/**
 *
 * @author Administrator
 */
public class AbsShowException {
    
    private boolean debug;

    protected AbsShowException() {
        this.debug = false;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    protected void showException(Exception e) {
        if (debug) {
            e.printStackTrace();
        }
    }
    
}
