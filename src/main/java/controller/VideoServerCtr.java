/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.imageio.ImageIO;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;

/**
 *
 * @author ASUS
 */
public class VideoServerCtr {
    private final int PORT = 10000;
    private final int BUFFER_SIZE = 1024;
    
    private DatagramSocket videoServer;
    
    public VideoServerCtr() {
        init();
    }
    
    private void init() {
        try {
            videoServer = new DatagramSocket(PORT);
        } catch (Exception e) {
        }
    }
    
    public void transferImageStream() {
        try {
            while (true) {
                byte[] receivedData = new byte[BUFFER_SIZE];
                DatagramPacket rcvPacket = new DatagramPacket(receivedData, receivedData.length);
                videoServer.receive(rcvPacket);
                DatagramPacket sendPacket = new DatagramPacket(rcvPacket.getData(), rcvPacket.getLength(), rcvPacket.getAddress(), rcvPacket.getPort());
                videoServer.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new VideoServerCtr().transferImageStream();
    }
}
