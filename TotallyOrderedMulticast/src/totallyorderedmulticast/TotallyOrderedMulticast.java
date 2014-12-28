/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package totallyorderedmulticast;

/**
 *
 * @author Gokay
 */
public class TotallyOrderedMulticast {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        OverlayNetwork overlayNetwork=new OverlayNetwork(4);
        overlayNetwork.create();
    }
    
}
