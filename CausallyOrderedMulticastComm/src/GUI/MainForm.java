/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Gokay
 */
public class MainForm {
    private JFrame _mainFrame;
    private JTextField _textName;
    //private JTextField _textSleepTime;
    private JPanel _panel;
    
    public MainForm()
    {
        prepareGUI();
    }

    private void prepareGUI() {
      _mainFrame = new JFrame("Create Node");
      _mainFrame.setSize(300,300);
      _mainFrame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      
      _textName=new JTextField(10);
      //_textSleepTime=new JTextField(4);
      
      _panel = new JPanel();
      _panel.setLayout(new FlowLayout());
      
      _panel.add(new JLabel("Name"));
      _panel.add(_textName);
//      _panel.add(new JLabel("Sleep Time"));
//      _panel.add(_textSleepTime);
      JButton _button=new JButton("Create Node");
      _button.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
              try {
                  //MessageForm messageForm = new MessageForm(_textName.getText(), Integer.parseInt(_textSleepTime.getText()));
                  MessageForm messageForm = new MessageForm(_textName.getText());
              } catch (UnknownHostException ex) {
                  Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
              }
          }
      });
      _panel.add(_button);
      _panel.setLayout(new GridLayout(0, 2));
      _mainFrame.add(_panel);
      _mainFrame.setVisible(true);  
    }
    
      public void show(){
       
      _mainFrame.setVisible(true);      
   }  
    
}
