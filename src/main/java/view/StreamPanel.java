/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author ASUS
 */
public class StreamPanel extends javax.swing.JPanel{

    BufferedImage img;
    
    public StreamPanel() {
        new VideoThread().start();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        if (img == null) {
            grphcs.setColor(Color.BLACK);
            grphcs.fillRect(0, 0, 600, 400);
        }
        else {
            int width = getWidth();
            int height = (int) (img.getHeight() * (float) (img.getWidth() / getWidth()));
            grphcs.drawImage(img, 0, 0, width, height, null);
        }
    }
    
    public void setImg(BufferedImage img) {
        this.img = img;
    }
    
    class VideoThread extends Thread {

        @Override
        public void run() {
            while (true)
                repaint();
        }
        
    }
}
