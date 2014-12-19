/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package causallyOrderedMulticastComm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Gokay
 */
public class Node {
    	private static final String serverName = "localhost";
	InetAddress group;
        
	protected String _name;
	protected int _portNumber;
	protected int _portNumberOtherNode;
//	protected boolean _setupNode;
//	protected boolean _firstNode;
//	protected ArrayList<Node> _nodeList=new ArrayList<Node>();
//	protected ServerSocket _serverSocket;
//        protected Socket clientSocket;
//	protected PrintWriter socketOut;
//	protected BufferedReader socketIn;

    public Node() {
                try {
                    this.group = InetAddress.getByName("228.5.6.7");
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
    
    public void sendMessage() throws IOException
    {
        String msg = "Hello";
        MulticastSocket s = new MulticastSocket(6789);
        s.joinGroup(this.group);
        DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(),group, 6789);
        s.send(hi);
        
         // get their responses!
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        s.receive(recv);
        // OK, I'm done talking - leave the group...
        s.leaveGroup(group);
    }
}
