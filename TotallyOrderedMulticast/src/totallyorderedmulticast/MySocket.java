/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totallyorderedmulticast;

import java.io.BufferedReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Gokay
 */
public class MySocket {
    protected Socket _clientSocket;
    protected ObjectOutputStream _socketOut;
    protected BufferedReader _socketIn;
    
    public MySocket (Socket ClientSocket,ObjectOutputStream SocketOut,BufferedReader SocketIn )
    {
        this._clientSocket = ClientSocket;
        this._socketOut = SocketOut;
        this._socketIn = SocketIn;
    }
}
