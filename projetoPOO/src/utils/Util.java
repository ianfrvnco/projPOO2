/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Administrador
 */
public class Util {
    
    public static Image getIcone(){
        URL caminhoImagem = Util.class.getResource("/images/window_icon.png");
        
        ImageIcon icon = new ImageIcon(caminhoImagem);
        
        return icon.getImage();
    }
}
