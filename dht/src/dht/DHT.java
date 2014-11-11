
package dht;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class DHT {

	//public static ArrayList<Node> _nodeList=new ArrayList<Node>();
        public static int firstNodePortNumber =   4445;
        public static void main(String[] args) {

    	Node setupNode=new Node("0",4444,firstNodePortNumber,true,false);
    	Node firstNode=new Node("1",firstNodePortNumber,4444,true,true);
    	
        //initial connection
    	(new Thread(setupNode)).start();
    	(new Thread(firstNode)).start();
        
        
        firstNode._nodeList.add(new Node("5",4490,firstNodePortNumber,false,false));
        firstNode._nodeList.add(new Node("30",4499,firstNodePortNumber,false,false));
        firstNode._nodeList.add(new Node("50",4500,firstNodePortNumber,false,false));
        firstNode._nodeList.add(new Node("11",4600,firstNodePortNumber,false,false));
        firstNode._nodeList.add(new Node("12",4601,firstNodePortNumber,false,false));
        firstNode._nodeList.add(new Node("13",4700,firstNodePortNumber,false,false));
        firstNode._nodeList.add(new Node("16",4770,firstNodePortNumber,false,false));
        firstNode._nodeList.add(new Node("44",4666,firstNodePortNumber,false,false));
        firstNode._nodeList.add(new Node("12",4777,firstNodePortNumber,false,false));

//        firstNode._nodeList.get(0)._portNumberOtherNode=4444;
//        (new Thread(firstNode._nodeList.get(0))).start();
//        (new Thread(newNode2)).start();
//        
          setupNode.connectOtherNode();
          firstNode.connectOtherNode();
          
          firstNode.closeConnection();
          //(new Thread( firstNode._nodeList.get(5))).start();
          //firstNode._nodeList.get(5).connectOtherNode();
          

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
    public void succ()
    {
            
    }
}
