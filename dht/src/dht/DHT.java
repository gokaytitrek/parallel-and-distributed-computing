
package dht;

import java.util.ArrayList;


public class DHT {

	public static ArrayList<Node> _nodeList=new ArrayList<Node>();
    public static void main(String[] args) {

    	Node setupNode=new Node("0",4444,4445,true,false);
    	Node firstNode=new Node("1",4445,4444,true,true);
    	
    	_nodeList.add(setupNode);
    	_nodeList.add(firstNode);
    	
    	(new Thread(setupNode)).start();
    	(new Thread(firstNode)).start();
        
        firstNode._nodeList.add(new Node("5",4490,4445,false,false));

        firstNode._nodeList.get(0)._portNumberOtherNode=4444;
        (new Thread(firstNode._nodeList.get(0))).start();
//        (new Thread(newNode2)).start();
//        
        setupNode.connectOtherNode();
        firstNode.connectOtherNode();
//        
//        newNode1.connectOtherNode();
//        newNode2.connectOtherNode();
        
//        try {
//        	if(firstNode._serverSocket!=null)	
//        		firstNode._serverSocket.close();
//  		} catch (IOException e) {
//  			// TODO Auto-generated catch block
//  			e.printStackTrace();
//  		}
//        
//        firstNode._portNumberOtherNode=firstNode._nodeList.get(0)._portNumber;
//        (new Thread(firstNode)).start();
    }    
}
