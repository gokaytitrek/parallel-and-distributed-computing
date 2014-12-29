/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totallyorderedmulticast;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gokay
 */
public class OverlayNetwork {
    
    private final int PORTNUMBER_START = 6000;
    private final int SIZEOFNETWORK;
    private ArrayList<Integer> _portNumbers;

    public OverlayNetwork(int SizeOfNetwork) {
        this._portNumbers = new ArrayList<>();
        
        SIZEOFNETWORK = SizeOfNetwork;
        for(int i=0; i<SizeOfNetwork;i++)
            this._portNumbers.add(PORTNUMBER_START+i);
        

    }
    
    public void create()
    {
        for(int i=0; i<SIZEOFNETWORK;i++)
        {
            try {
                Node node=new Node(i, SIZEOFNETWORK, this._portNumbers);
                (new Thread(node)).start();
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(OverlayNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
