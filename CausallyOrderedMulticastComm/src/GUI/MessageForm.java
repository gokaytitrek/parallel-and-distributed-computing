/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import causallyOrderedMulticastComm.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import static java.lang.System.arraycopy;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author Gokay
 */
public class MessageForm {
    private JFrame _mainFrame;
    private JTextField _textMessage;
    private JPanel _panel;
    private Node _node;
    public MessageForm(String Name) throws UnknownHostException
    {
         _node=new Node(Name);
        (new Thread(_node)).start();
        _mainFrame = new JFrame(Name);
        prepareGUI();
    }

    private void prepareGUI() {
      
      _mainFrame.setSize(500,400);
      _mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
             try {
                 _node.closeConnection();
             } catch (IOException ex) {
                 Logger.getLogger(MessageForm.class.getName()).log(Level.SEVERE, null, ex);
             }
            _mainFrame.setVisible(false);
            _mainFrame.dispose(); 
         }        
      });    
      
      _panel = new JPanel();
      _panel.setLayout(new FlowLayout());

      _textMessage=new JTextField(10);
      JScrollPane _scrollPane = new JScrollPane(_node._textArea); 
      
      JButton _buttonHold=new JButton("Hold Next Message");
      _buttonHold.setSize(100,100);
      _buttonHold.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              _node._holdMessage = true;
          }
      });     
      
      JButton _buttonDeliver=new JButton("Deliver Message");
      _buttonDeliver.setSize(100,100);
      _buttonDeliver.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              _node.deliverVirtuallyQueuedMessage();
          }
      }); 
      
      JButton _button=new JButton("Send Message");
      _button.setSize(100, 100);
      _button.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              try {
                  //_textArea.append(_textMessage.getText() + "\n");
                  _node._localTime[_node._ID] = _node._localTime[_node._ID] + 1;
                  checkVectorClockSize();
                  _node.sendMessage(new MessageContent(_node._ID,_node._nodeName, _textMessage.getText(), _node._localTime));
              } catch (IOException | InterruptedException ex) {
                  Logger.getLogger(MessageForm.class.getName()).log(Level.SEVERE, null, ex);
              }
          }
      });
    
      _panel.add(new JLabel("Message"));
      _panel.add(_textMessage);
      _panel.add(_button);
      _panel.add(new JLabel("Local Clock"));
      _panel.add(_node._localClockValue);
      _panel.add(_scrollPane);
      _panel.add(_buttonHold);
      _panel.add(_buttonDeliver);
      _panel.setLayout(new GridLayout(0, 3));
      
      _mainFrame.add(_panel);
      _mainFrame.setVisible(true);  

    }
    
    private void checkVectorClockSize()
    {
        if(_node._localTime== null)
        {
            _node._localTime = new Integer[Main._nodeSize];
            for(int i=0; i<_node._localTime.length;i++)
                _node._localTime[i]=0;
        }
        if(_node._localTime.length != Main._nodeSize)
        {
            Integer[] newArray=new Integer[Main._nodeSize];
            arraycopy(_node._localTime, 0, newArray, 0, _node._localTime.length);
            for(int i=_node._localTime.length; i<newArray.length;i++)
                newArray[i]=0;
            _node._localTime = newArray;
        }
    }
    
    
}
