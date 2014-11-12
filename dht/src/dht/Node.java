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
import java.util.logging.Level;
import java.util.logging.Logger;

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

	public Node(String Name, int PortNumber, int PortNumberOtherNode,
			boolean SetupNode, boolean Firstnode) {
		this._name = Name;
		this._portNumber = PortNumber;
		this._portNumberOtherNode = PortNumberOtherNode;
		this._setupNode = SetupNode;
		this._firstNode = Firstnode;
	}

	private String getHash(String Name) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(Name.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			String hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32
			// chars.
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
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
        
        public void lookUp(String Name) throws IOException
        {
            int portNumber = this._portNumber;
            int portNumberOtherNode = this._portNumberOtherNode;
            
            /*
            while(portNumber != portNumberOtherNode)
            {
                this.socketOut.println("NextPort");
   
            }
            */

            System.out.println("this Port "+this._portNumber);
            
            System.out.println(socketIn.readLine());
        }

	@Override
	public void run() {
                ServerThread    serverSocket=new ServerThread();
		serverSocket.establishConnection(this._serverSocket, this._portNumber,this._name);
	}

}
