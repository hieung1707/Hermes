/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import javax.imageio.ImageIO;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameUtils;

/**
 *
 * @author ASUS
 */
public class VideoClientCtr {
    private final String IP = "192.168.1.35";
    private final int PORT = 10000;
    
    DatagramSocket socket;

    public VideoClientCtr() {
        init();
    }

    private void init() {
        try {
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void streamCam() {
        try {
            socket = new DatagramSocket();
            FrameGrabber grabber = FrameGrabber.createDefault(0);
            grabber.start();

            // Frame to capture
            Frame frame = null;
            VideoListenerThread videoListener = new VideoListenerThread(socket);
            while ((frame = grabber.grab()) != null) {
                BufferedImage img = Java2DFrameUtils.toBufferedImage(frame);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, "jpg", baos);
                baos.flush();
                byte[] imageInBytes = baos.toByteArray();
                baos.close();
                sendBytes(imageInBytes);
            }
            grabber.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sendBytes(byte [] data) {
        try {
            InetAddress address = InetAddress.getByName(IP);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int index = 0;
            while (index < data.length) {
                buffer = Arrays.copyOfRange(data, index, (index + bufferSize > data.length) ? data.length : index + bufferSize);
                index += bufferSize;
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
                socket.send(packet);
            }
        } catch (Exception e) {
            
        }
    }

    public static void main(String[] args) {
        new VideoClientCtr().streamCam();
    }
}
