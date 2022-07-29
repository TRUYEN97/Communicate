/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commandprompt.Communicate.Socket.Commons.Response;

import commandprompt.Communicate.Socket.Commons.Enums.StatusCode;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public abstract class Response implements Serializable {

    protected StatusCode statusCode;

    protected Response(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
