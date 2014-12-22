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
    protected Integer _ID;
    protected String _message;
    protected String _senderName;
    protected Integer[] _receivedVectorClock;

    public MessageContent(Integer ID,String SenderName, String Message, Integer[] LocalTime) {
        this._ID = ID;
        this._senderName = SenderName;
        this._message = Message;
        this._receivedVectorClock = LocalTime;
        
    }
    
}
