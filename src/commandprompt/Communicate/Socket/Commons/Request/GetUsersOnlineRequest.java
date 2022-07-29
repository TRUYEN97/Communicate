/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commandprompt.Communicate.Socket.Commons.Request;

import commandprompt.Communicate.Socket.Commons.Enums.Action;

/**
 *
 * @author Administrator
 */
public class GetUsersOnlineRequest extends Request{

    public GetUsersOnlineRequest() {
        super(Action.GET_USER_ONLINE);
    }
}
