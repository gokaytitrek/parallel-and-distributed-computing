/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totallyorderedmulticast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import socketprogramming.ServerThread;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import messageContent.MessageContent;
/**
 *
 * @author Gokay
 */
public class Node implements Runnable{
    private static String _serverName = "localhost";
    private int _id;
    private int _numberOfProcess;
    private ArrayList<Integer> _portNumbers;
    private boolean _firstNode;
    private boolean _lastNode;
    protected ServerSocket _serverSocket;
    protected Socket _clientSocket;
    protected ObjectOutputStream _socketOut;
    protected BufferedReader _socketIn;
    private int _portNumber;
    
    
    public Node(int ID,int NumbefOfProcess,ArrayList<Integer> PortNumbers)
    {
        this._id = ID;
        this._numberOfProcess = NumbefOfProcess;
        this._portNumbers = PortNumbers;
        this._firstNode = false;
        this._lastNode = false;
        this._portNumber = this._portNumbers.get(_id);
        
        if(this._id == 0)
           this._firstNode = true;
        
        if(this._id == this._numberOfProcess-1)
            this._lastNode = true;
    }

    @Override
    public void run() {
        if (!this._lastNode)
        {
             ServerThread serverSocket=new ServerThread(this._serverSocket, this._portNumber, Integer.toString(_id));
             (new Thread(serverSocket)).start();
        }
        
        if(!this._firstNode)
        {
            for (Integer portNumber : this._portNumbers) {
                if(portNumber<this._portNumber)
                    try {
                        this.connectOtherNode(portNumber);
                } catch (IOException ex) {
                    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
    
    synchronized public void connectOtherNode(int PortNumber) throws IOException
    {
        try {
                //create socket and connect to the server
                _clientSocket = new Socket(_serverName, PortNumber);
                //will use socketOut to send text to the server over the socket
                //socketOut = new PrintWriter(_clientSocket.getOutputStream(), true);
                _socketOut = new ObjectOutputStream(_clientSocket.getOutputStream());
                //will use socketIn to receive text from the server over the socket
                _socketIn = new BufferedReader(new InputStreamReader(_clientSocket.getInputStream()));
        } catch(UnknownHostException e) { //if serverName cannot be resolved to an address
                System.out.println("Who is " + _serverName + "?");
                e.printStackTrace();
                System.exit(0);
        } catch (IOException e) {
                System.out.println("Cannot get I/O for the connection.");
                e.printStackTrace();
                System.exit(0);
        }
        String message=   "try to connect: Ports: " +  this._portNumber + " -> " +PortNumber;
        _socketOut.writeObject(new MessageContent());
        System.out.println(message);
        String response = null;
        try {
                response = _socketIn.readLine();
        } catch (IOException e) {
                e.printStackTrace();
        }
        //System.out.println("Server's response was: \n\t\"" + response + "\"");
        System.out.println(response);
        System.out.println();
        
        //sendMessage();

    }
    
     synchronized public void sendMessage()
     {
        try {
            MessageContent message=new MessageContent(_id, _serverName, "deneme");
            _socketOut.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
}
