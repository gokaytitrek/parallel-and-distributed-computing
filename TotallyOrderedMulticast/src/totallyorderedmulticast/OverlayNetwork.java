/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totallyorderedmulticast;

import java.util.ArrayList;

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
            Node node=new Node(i, SIZEOFNETWORK, this._portNumbers);
            (new Thread(node)).start();
        }
    }
    
}
