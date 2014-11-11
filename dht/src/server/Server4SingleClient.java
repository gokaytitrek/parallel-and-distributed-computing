package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server4SingleClient {
	
	public void establishConnection (ServerSocket serverSocket, int portNumber, String name) {
		
		serverSocket = null;
		
		try {
			//initialize server socket
			serverSocket = new ServerSocket(portNumber);			
		} catch (IOException e) { //if this port is busy, an IOException is fired
			System.out.println("Cannot listen on port " + portNumber);
			e.printStackTrace();
			System.exit(0);
		}
		
		Socket clientSocket = null;
		PrintWriter socketOut = null;
		BufferedReader socketIn = null;
		
		try {
                       
                            //wait for client connections
                            System.out.println("Server socket initialized.\nWaiting for a client connection.");
                            clientSocket = serverSocket.accept();

                            //let us see who connected
                            String clientName = clientSocket.getInetAddress().getHostName();
                            System.out.println(clientName + " established a connection.");
                            System.out.println();

                            //will use socketOut to send text to the server over the socket
                            socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
                            //will use socketIn to receive text from the server over the socket
                            socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        
		} catch (IOException e) {
			System.out.println("Cannot get I/O for the connection.");
			e.printStackTrace();
			System.exit(0);
		}

		socketOut.println(name);
		System.out.println("Client's message was: \n\t\"" + name + "\"");
		System.out.println();
		
		
		//close all streams
		//socketOut.close();
		//try {
		//	socketIn.close();
		//	clientSocket.close();
		//	serverSocket.close();
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
	}
	
	

}
