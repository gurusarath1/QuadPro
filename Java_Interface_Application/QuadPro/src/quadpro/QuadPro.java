/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quadpro;

import java.awt.EventQueue;
import static quadpro.GLOBAL_CONSTANTS.MAX_CAL_BUFFER_SIZE;

/**
 *
 * @author gsgur
 */
public class QuadPro {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        

        EventQueue.invokeLater(() -> {
        
        
        new MainWindow();
        
        });
        

    }
    
}
