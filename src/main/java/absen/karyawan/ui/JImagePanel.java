/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package absen.karyawan.ui;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author adi
 */
public class JImagePanel extends JPanel {
    
    private Image image;

    public Image getImage() {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (g != null) {
            image = new javax.swing.ImageIcon(getClass().getResource("/absen/karyawan/images/logo-unindra.png")).getImage();
            g.drawImage(image, 
                    (this.getSize().width-image.getWidth(null))/2, 
                    (this.getSize().height-image.getHeight(null))/2,
                    null);
        }
    }
    
    
    
}
