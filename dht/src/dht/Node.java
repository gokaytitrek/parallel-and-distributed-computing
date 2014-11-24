/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dht;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Object;

import server.ServerThread;
/**
 * 
 * @author Gokay
 */

public class Node implements Runnable{
	private static String serverName = "localhost";
	
	protected String _name;
	protected int _portNumber;
	protected int _portNumberOtherNode;
	protected boolean _setupNode;
	protected boolean _firstNode;
	protected ArrayList<Node> _nodeList=new ArrayList<Node>();
	protected ServerSocket _serverSocket;
        protected Socket clientSocket;
	protected PrintWriter socketOut;
	protected BufferedReader socketIn;

        public Node()
        {
            this._firstNode=false;
            this._setupNode=false;
        }
        
	public Node(String Name, int PortNumber, int PortNumberOtherNode,
			boolean SetupNode, boolean Firstnode) {
		this._name = Name;
		this._portNumber = PortNumber;
		this._portNumberOtherNode = PortNumberOtherNode;
		this._setupNode = SetupNode;
		this._firstNode = Firstnode;

                if (this._firstNode)
                {
                    int portNumber = 4500;
                    Random r=new Random();
                    int j=0;
                    while (j<255)
                    {
                        int i= r.nextInt(255);
                        if(!this._nodeList.contains(new Node(Integer.toString(i), portNumber, 0, false, false)) && !Integer.toString(i).equals("0") && !Integer.toString(i).equals("1"))
                            this._nodeList.add(new Node(Integer.toString(i), portNumber, 0, false, false));
                        j++;
                        portNumber++;
                    }
                }
	}

	private String getHash(String Name) {
            try {
                    MessageDigest m = MessageDigest.getInstance("MD5");
                    m.reset();
                    m.update(Name.getBytes());
                    byte[] digest = m.digest();
                    BigInteger bigInt = new BigInteger(1, digest);
                    String hashtext = bigInt.toString(2);
                    // Now we need to zero pad it if you actually want the full 32
                    // chars.
                    //while (hashtext.length() < 32) {
                    //	hashtext = "0" + hashtext;
                    //}
                    return hashtext.substring(hashtext.length()-8, hashtext.length());
            } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
            }
        }
	
	public void connectOtherNode()
	{
		try {
			//create socket and connect to the server
			clientSocket = new Socket(serverName, this._portNumberOtherNode);
			//will use socketOut to send text to the server over the socket
			socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
			//will use socketIn to receive text from the server over the socket
			socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch(UnknownHostException e) { //if serverName cannot be resolved to an address
			System.out.println("Who is " + serverName + "?");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Cannot get I/O for the connection.");
			e.printStackTrace();
			System.exit(0);
		}
                String message=   "try to connect: Ports: " +  this._portNumber + " -> " +this._portNumberOtherNode;
                socketOut.println(message);
		System.out.println(message);
		String response = null;
		try {
			response = socketIn.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("Server's response was: \n\t\"" + response + "\"");
                System.out.println(response);
                System.out.println();

	}
        
        public void closeConnection()
        {
            System.out.println("try to close connection: Ports: " + this._portNumber + " -> "+ this._portNumberOtherNode );
            socketOut.println("close");
            
            String response = null;
            try {
                response = socketIn.readLine();
            } catch (IOException ex) {
                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(response);
            System.out.println();
            //close all streams
            socketOut.close();
            try {
                    socketIn.close();
                    clientSocket.close();
            } catch (IOException e) {
                    e.printStackTrace();
            }
            
        }
        
        public void insertNode(Node insertPointNode)
        {
            insertPointNode.closeConnection();
            this._portNumberOtherNode = insertPointNode._portNumberOtherNode;
            //this._nextNode = a;
            insertPointNode._portNumberOtherNode=this._portNumber;
            //a._nextNode = this;
            insertPointNode.connectOtherNode();
            this.connectOtherNode();
        }
        
        public Node lookUp(String Name) throws IOException
        {
            //Daha once aktive edilmemisse ilk olarak firstNode da varmı kontrol ediyor.
            Node x=null;
            if(this._firstNode)
            {
                for(Node n : this._nodeList)
                {
                    if(n._name.equals(Name))
                    {
                        
                        x = n;
                        break;
                    }
                }
                if(x!=null)
                {
                    for(Node n : this._nodeList)
                    {
                        if(
                                (
                                    getHash(x._name).compareTo(getHash(n._name)) == -1 
                                    ||
                                    getHash(x._name).compareTo(getHash(n._name)) == 0
                                )
                                && !n._name.equals("0") 
                                && !n._name.equals("1")
                          )
                            x._nodeList.add(n);
                    }
                    
                    for(Node n : x._nodeList)
                        this._nodeList.remove(n);
                   
                    return x;
                }
            }
            this.socketOut.println("NextName");
            String nextNodeName=socketIn.readLine();
            Node nextNode=new Node();
            while(!nextNode._firstNode || !nextNode._setupNode)
            {
                
                nextNode = DHT.hash.get(getHash(nextNodeName));
                
                if(
                    (
                    getHash(nextNode._name).compareTo(getHash(Name)) == 1 
                    ||
                    getHash(nextNode._name).compareTo(getHash(Name)) == 0
                    )
                     && !nextNode._name.equals("0") 
                     && !nextNode._name.equals("1")
                  )
                {
                    //Aradıgım Node
                    //Gelen Nameden daha buyuk yada esit
                }
                else if ( nextNode._name.equals("0") 
                     || nextNode._name.equals("1"))
                {
                    return null;
                }
                else
                {
                    nextNode.socketOut.println("NextName");
                    nextNodeName=socketIn.readLine();
                }
                
                
            }
            return null;
            /*
            while(portNumber != portNumberOtherNode)
            {
                this.socketOut.println("NextPort");
   
            }
            */

            //System.out.println("this Port "+this._portNumber);
            
            //System.out.println(socketIn.readLine());
        }

	@Override
	public void run() {
                DHT.hash.put(getHash(this._name), this);
                ServerThread    serverSocket=new ServerThread();
		serverSocket.establishConnection(this._serverSocket, this._portNumber,this._name);
	}
        
        @Override
        public boolean equals(Object object)
        {
            boolean isEqual= false;

            if (object != null && object instanceof Node)
            {
                isEqual = this._name.equals(((Node)object)._name);
            }

            return isEqual;
        }
}
