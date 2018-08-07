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
import java.net.ServerSocket;
import java.net.Socket;
import socketprogramming.ServerThread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private int _portNumber;
    private HashMap<Integer,MySocket> _socketMap;
    protected ArrayList<MessageContent> _queue;
    
    
    public Node(int ID,int NumbefOfProcess,ArrayList<Integer> PortNumbers)
    {
        this._id = ID;
        this._numberOfProcess = NumbefOfProcess;
        this._portNumbers = PortNumbers;
        this._firstNode = false;
        this._lastNode = false;
        this._portNumber = this._portNumbers.get(_id);
        this._socketMap = new HashMap<>();
        
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
        
        //sendMessage();
    }
    
    synchronized public void connectOtherNode(int PortNumber) throws IOException
    {
        Socket clientSocket = new Socket(_serverName, PortNumber);
        
        BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
   
        ObjectOutputStream socketOut = new ObjectOutputStream(clientSocket.getOutputStream());
        this._socketMap.put(PortNumber, new MySocket(clientSocket, socketOut, socketIn));
        
        String message=   "try to connect: Ports: " +  this._portNumber + " -> " +PortNumber;
        socketOut.writeObject(new MessageContent(0,"","Connect"));
        System.out.println(message);
        String response = socketIn.readLine();
        //System.out.println("Server's response was: \n\t\"" + response + "\"");
        System.out.println(response);
    }
    
     synchronized public void sendMessage()
     {
        //System.out.println("Sender:" + this._id);
        try {
            for (Map.Entry<Integer, MySocket> entrySet : _socketMap.entrySet()) {
                Integer port= entrySet.getKey();
                MySocket value = entrySet.getValue();
                //value._socketOut.reset();
                //value._socketOut = new ObjectOutputStream(value._clientSocket.getOutputStream());
                MessageContent message=new MessageContent(_id, _serverName,"Send Message from : "+this._portNumber+ " to : " + port );
                value._socketOut.writeObject(message);
                String response = null;
                try {
                        response = value._socketIn.readLine();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                //System.out.println("Server's response was: \n\t\"" + response + "\"");
                System.out.println(response);
                //System.out.println();
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
}
