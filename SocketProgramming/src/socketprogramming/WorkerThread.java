package socketprogramming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import messageContent.MessageContent;

public class WorkerThread implements Runnable{
	private Socket _clientSocket;
        private String _name;
        private Integer _portNumber;
	public WorkerThread(Socket Socket,String Name,Integer PortNumber) {
		this._clientSocket = Socket;
                this._name = Name;
                this._portNumber = PortNumber;
	}
	public void run() {
		//taken from Server4SingleClient
		PrintWriter socketOut = null;
		ObjectInputStream socketIn = null;

		
		try {			
			//will use socketOut to send text to the server over the socket
			socketOut = new PrintWriter(_clientSocket.getOutputStream(), true);
			//will use socketIn to receive text from the server over the socket
			socketIn = new ObjectInputStream(_clientSocket.getInputStream());
                      
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
                    //System.out.println("Thread Port Number" + this._portNumber);
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
                    
                    if(message._message.equals("NextPort"))
                        socketOut.println(_clientSocket.getLocalPort());
                    else if(message._message.equals("NextName"))
                        socketOut.println(this._name);
                    else if(message._message.equals("Connect"))
                        socketOut.println("Connected");
                    else
                    {
                        socketOut.println(message._message);
                        //System.out.println();
                    }
                }
		
		//close all streams
		socketOut.close();
		
		try {
			socketIn.close();
			_clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
