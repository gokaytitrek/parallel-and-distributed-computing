/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package causallyOrderedMulticastComm;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Gokay
 */
public class MessageContent implements Serializable{
    public String _message;
    public String _senderName;
    public Integer[] _localTime;

    public MessageContent(String SenderName, String Message, Integer[] LocalTime) {
        this._senderName = SenderName;
        this._message = Message;
        this._localTime = LocalTime;
        
    }
    
}
