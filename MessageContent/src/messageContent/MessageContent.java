package messageContent;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gokay
 */
public class MessageContent implements Serializable{
    public Integer _ID;
    public String _message;
    public String _senderName;

    public MessageContent() {
        this._ID = 0;
        this._senderName = "";
        this._message = "";        
    }
    public MessageContent(Integer ID,String SenderName, String Message) {
        this._ID = ID;
        this._senderName = SenderName;
        this._message = Message;        
    }
}
