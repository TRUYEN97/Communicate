/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commandprompt.Communicate.Socket.Commons.Request;

import commandprompt.Communicate.Socket.Commons.Enums.Action;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public abstract class Request implements Serializable {

    protected Action action;

    protected Request(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

}
