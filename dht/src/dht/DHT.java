package dht;

import java.io.IOException;

public class DHT {

    public static void main(String[] args) throws IOException {

    	Node setupNode=new Node("0",4444,4445,true,false);
    	Node firstNode=new Node("1",4445,4444,true,true);
    	
    	(new Thread(setupNode)).start();
    	(new Thread(firstNode)).start();
        
        /*
        firstNode._nodeList.add(new Node("5",4491,4445,false,false));
        firstNode._nodeList.add(new Node("50",4492,4445,false,false));
        firstNode._nodeList.add(new Node("15",4493,4445,false,false));
        firstNode._nodeList.add(new Node("20",4494,4445,false,false));
        firstNode._nodeList.add(new Node("13",4495,4445,false,false));
        firstNode._nodeList.add(new Node("14",4496,4445,false,false));
        firstNode._nodeList.add(new Node("11",4497,4445,false,false));
        */
        
        setupNode.connectOtherNode();
        firstNode.connectOtherNode();
        
        Node firstNode2=new Node("3",4447,0,true,true);

        (new Thread(firstNode2)).start();
        
        firstNode2.insertNode(firstNode);
        
        
        Node firstNode3=new Node("30",4500,0,true,true);

        (new Thread(firstNode3)).start();
        
        firstNode3.insertNode(firstNode);
        
        //firstNode.lookUp("2");
        //firstNode.lookUp("3");

        
    }    
}
