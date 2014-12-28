package socketprogramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import messageContent.MessageContent;

public class WorkerThread implements Runnable{
	private Socket clientSocket;
        private String name;
	public WorkerThread(Socket s,String name) {
		clientSocket = s;
                this.name = name;
	}
	public void run() {
		//taken from Server4SingleClient
		PrintWriter socketOut = null;
		ObjectInputStream socketIn = null;
                
                /*
                        byte[] buf = new byte[1000];

                    packet = new DatagramPacket(buf,buf.length);

                    //System.out.println("McastReceiver: waiting for packet");

                    _multicastSocket.receive(packet);

                    checkVectorClockSize();

                    ByteArrayInputStream bistream = 

                      new ByteArrayInputStream(packet.getData());

                    ObjectInputStream ois = new ObjectInputStream(bistream);

                    MessageContent value = (MessageContent) ois.readObject();
                */
		
		try {			
			//will use socketOut to send text to the server over the socket
			socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
			//will use socketIn to receive text from the server over the socket
			socketIn = new ObjectInputStream(clientSocket.getInputStream());
                      
		} catch (IOException e) {
			System.out.println("Cannot get I/O for the connection.");
			e.printStackTrace();
			System.exit(0);
		}
		
		/**
		 * Protocol (known to both parties): 
		 * Server blocks on a message from the client.
		 * Client sends a message and blocks on the server's response.
		 * Upon receipt of message, server will respond: "You said: " + message
		 * This continues for 3 rounds.
		 */
		
                //int i=0;
		//String message = null;
                MessageContent message=new MessageContent();
                boolean x=true;
                while (x) {                
                    //System.out.println("Round (" + (i+1) + ")");
                    //System.out.println("Waiting for a message from the client.");
                    
                    try {
                            message = (MessageContent)socketIn.readObject();
                    } catch (IOException e) {
                            e.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if(message._message.equals("close"))
                    {
                        socketOut.println("Connection Closed");
                        x=false;
                    } 
                    socketOut.println("You said: " + message._message);
                    if(message.equals("NextPort"))
                        socketOut.println(clientSocket.getLocalPort());
                    else if(message.equals("NextName"))
                        socketOut.println(this.name);
                    else
                    {
                        socketOut.println("Connected");
                        //System.out.println();
                    }
                }
		
		//close all streams
		socketOut.close();
		
		try {
			socketIn.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
