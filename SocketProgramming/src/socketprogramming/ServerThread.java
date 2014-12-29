package socketprogramming;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import socketprogramming.WorkerThread;

public class ServerThread implements Runnable{
	//the port on which server will listen for connections 
	//public static int portNumber = 4444;
            private ServerSocket _serverSocket;
            private int _portNumber;
            private String _name;
            public ServerThread (ServerSocket ServerSocket, int PortNumber, String Name)
            {
                this._serverSocket = ServerSocket;
                this._portNumber = PortNumber;
                this._name = Name;
            }
	
            @Override
            public void run() {
                	
            try {
                    //initialize server socket
                    _serverSocket = new ServerSocket(_portNumber);
                    System.out.println("Server socket initialized. Port: "+ _portNumber );
            } catch (IOException e) { //if this port is busy, an IOException is fired
                    System.out.println("Cannot listen on port " + _portNumber);
                    e.printStackTrace();
                    System.exit(0);
            }

            Socket clientSocket = null;

            try {
                    while(true) { //infinite loop - terminate manually
                            //wait for client connections
                            //System.out.println("Waiting for a client connection.");
                            try {
                                    clientSocket = _serverSocket.accept();
                            } catch (IOException e) {
                                    e.printStackTrace();
                                    System.exit(0);
                            }

                            //let us see who connected
                            //String clientName = clientSocket.getInetAddress().getHostName();
                            //System.out.println(clientName + " established a connection.");
                            //System.out.println();

                            //assign a worker thread
                            WorkerThread w = new WorkerThread(clientSocket,_name,this._portNumber);
                            new Thread(w).start();
                    }
            } finally {
                    //make sure that the socket is closed upon termination
                    try {
                            _serverSocket.close();
                    } catch (IOException e) {
                            e.printStackTrace();
                    }
            }
	}

}
