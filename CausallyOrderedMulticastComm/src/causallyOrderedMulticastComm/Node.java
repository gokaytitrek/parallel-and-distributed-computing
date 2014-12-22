/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package causallyOrderedMulticastComm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Math.max;
import static java.lang.System.arraycopy;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import javax.swing.JLabel;
import javax.swing.JTextArea;


/**
 *
 * @author Gokay
 */
public class Node implements Runnable{
    	private static final String _serverName = "localhost";
	private final InetAddress _group;
        private MulticastSocket _multicastSocket;
	public String _nodeName;
	private int _portNumber;
        public Integer  _ID;
        public Integer[] _localTime;
        protected ArrayList<MessageContent> _queue;
        private InetAddress _multicastAddress;
        private InetAddress _localHost;
        //private int _sleepTime;
        public JTextArea _textArea;
        public JLabel _localClockValue;
        
        public boolean _holdMessage;
        private ArrayList<MessageContent> _virtualDelayedMessageQueue;

    public Node(String Name) throws UnknownHostException {
        
            this._ID = Main._nodeSize++;
            checkVectorClockSize();
            this._nodeName = Name;
            //this._sleepTime = SleepTime;
            this._portNumber = 6000;
            this._group = InetAddress.getByName("228.5.6.7");
            this._multicastAddress = InetAddress.getByName("230.0.0.1");
            this._localHost = InetAddress.getLocalHost();
            this._queue = new ArrayList<>();
            this._virtualDelayedMessageQueue=new ArrayList<>();
          
            _textArea = new JTextArea(1000, 0);
            _textArea.setEditable(false);
            _localClockValue = new JLabel();
    }
    
    public void sendMessage(MessageContent Message) throws IOException, InterruptedException
    {
        checkVectorClockSize();
        DatagramSocket dgramSocket = new DatagramSocket();
        DatagramPacket packet = null;
      try {

        // serialize the multicast message

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream (bos);
        out.writeObject(Message);
        out.flush();
        out.close();

        // Create a datagram packet and send it

        packet = new DatagramPacket(bos.toByteArray(),
                                    bos.size(),
                                    _multicastAddress,
                                    _portNumber);



        // send the packet

        dgramSocket.send(packet);

        System.out.println("sending multicast message");
      }catch (IOException ioe) {

        System.out.println("error sending multicast");

        ioe.printStackTrace(); System.exit(1);

      }
        
    }
    
    public void run() {

    try {

      System.out.println("Setting up multicast receiver");

      _multicastSocket = new MulticastSocket(_portNumber);

      _multicastSocket.joinGroup(_multicastAddress);

    } catch(IOException ioe) {

      System.out.println("Trouble opening multicast port");

      ioe.printStackTrace();      System.exit(1);

    }


    DatagramPacket packet;

    System.out.println("Multicast receiver set up ");

    while (true) {

      try {
        //Thread.sleep(5000);
        
        byte[] buf = new byte[1000];

        packet = new DatagramPacket(buf,buf.length);

        //System.out.println("McastReceiver: waiting for packet");

        _multicastSocket.receive(packet);

        checkVectorClockSize();

        ByteArrayInputStream bistream = 

          new ByteArrayInputStream(packet.getData());

        ObjectInputStream ois = new ObjectInputStream(bistream);

        MessageContent value = (MessageContent) ois.readObject();
        
        _textArea.append(value._senderName + " " + value._message + " " + processVectorClock(value).toString() + "\n");

        // ignore packets from myself, print the rest

        //if (!(packet.getAddress().equals(localHost))) {

//          System.out.println(this._nodeName +" Received multicast packet: "+
//
//                           value + " from: " + packet.getAddress());

        //} 

        ois.close();

        bistream.close();

      } catch(IOException ioe) {

        System.out.println("Trouble reading multicast message");

        ioe.printStackTrace();  System.exit(1);

      } catch (ClassNotFoundException cnfe) {

        System.out.println("Class missing while reading mcast packet");

        cnfe.printStackTrace(); System.exit(1);

      } 
//      catch (InterruptedException ex) { 
//            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
//        } 

    }

  }
    
    public void closeConnection() throws IOException
    {
        this._multicastSocket.leaveGroup(_multicastAddress);
    }
    
    private void checkVectorClockSize()
    {
        if(this._localTime== null)
        {
            this._localTime = new Integer[Main._nodeSize];
            for(int i=0; i<this._localTime.length;i++)
                this._localTime[i]=0;
        }
        if(this._localTime.length != Main._nodeSize)
        {
            Integer[] newArray=new Integer[Main._nodeSize];
            arraycopy(this._localTime, 0, newArray, 0, this._localTime.length);
            for(int i=this._localTime.length; i<newArray.length;i++)
                newArray[i]=0;
            this._localTime = newArray;
        }
    }
    
    private VectorResult processVectorClock(MessageContent Message)
    {
//        int counter=0;
//        for (int i=0; i< clock.length ;i++)
//        {
//            if((clock[i]-this._localTime[i]) != 0)
//            {
//                counter++;
//                if((clock[i]-this._localTime[i])>1)
//                  counter++;
//            }
//        }
//        
//        if(counter == 0)
//            return VectorResult.PROCESSED;
//        else if(counter == 1)
//        {
//            for (int i=0; i< clock.length ;i++)
//            {
//                this._localTime[i] = max(this._localTime[i],clock[i]);
//            }            
//            return VectorResult.PROCESSED;
//        }
//        else 
//        {
//            this._queue.add(clock);
//            return VectorResult.QUEUED;
//        }   
        
        if (this._holdMessage)
        {
            this._holdMessage = false;
            this._virtualDelayedMessageQueue.add(Message);
            return VectorResult.DELAYED;
        }
        
        if (Objects.equals(this._ID, Message._ID))
        {
            this._localClockValue.setText(Arrays.toString(this._localTime));
            return VectorResult.PROCESSED;
        }
        int counter=0;
        for (int i=0; i< Message._receivedVectorClock.length ;i++)
        {
            if(
                    (
                        (
                            (Message._receivedVectorClock[i].equals(this._localTime[i])) || this._localTime[i] > Message._receivedVectorClock[i]
                        ) 
                        && i!=Message._ID
                    )
                    &&
                    Message._receivedVectorClock[Message._ID] == this._localTime[Message._ID]+1
              )
            {
                counter++;
                //this._localTime[i] = max(this._localTime[i],ReceivedVectorClock[i]);
            }
            else if (i!=Message._ID)
            {
                this._localClockValue.setText(Arrays.toString(this._localTime));
                
                this._queue.add(Message);
                return VectorResult.QUEUED;
            }
        }
        
        if(counter == Message._receivedVectorClock.length-1)
        {
            for (int i=0; i< Message._receivedVectorClock.length ;i++)
            {
                this._localTime[i] = max(this._localTime[i],Message._receivedVectorClock[i]);
            }
        }
        this._localClockValue.setText(Arrays.toString(this._localTime));
        return VectorResult.PROCESSED;
    }
    
    public void deliverVirtuallyQueuedMessage()
    {
        ArrayList<MessageContent> temp=new ArrayList<>();
        for (MessageContent i : this._virtualDelayedMessageQueue) {
            VectorResult vr=processVectorClock(i);
            _textArea.append(i._senderName + " " + i._message + " " + vr.toString() + "\n");
            if(VectorResult.PROCESSED == vr)
                temp.add(i);
        }
        for (MessageContent i : temp) 
            this._virtualDelayedMessageQueue.remove(i);
        
        deliverQueuedMessage();
    }
    
    public void deliverQueuedMessage()
    {
        ArrayList<MessageContent> temp=new ArrayList<>();
        for (MessageContent i : this._queue) {
            VectorResult vr=processVectorClock(i);
            _textArea.append(i._senderName + " " + i._message + " " + vr.toString() + "\n");
            if(VectorResult.PROCESSED == vr)
                temp.add(i);
        }
        for (MessageContent i : temp) 
            this._queue.remove(i);
    }
}
