/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commandprompt.Communicate.Socket.Commons.Response;

import commandprompt.Communicate.Socket.Commons.Client.InfomationClient;
import commandprompt.Communicate.Socket.Commons.Enums.StatusCode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class UserOnlineRespon extends Response {

    private final List<InfomationClient> users;

    public UserOnlineRespon() {
        super(StatusCode.OK);
        this.users = new ArrayList<>();
    }

    public UserOnlineRespon(List<InfomationClient> users) {
        super(StatusCode.OK);
        this.users = users;
    }

    public boolean addUser(InfomationClient user) {
        return this.users.add(user);
    }

    public List<InfomationClient> getUesrsOnline() {
        return users;
    }

}
