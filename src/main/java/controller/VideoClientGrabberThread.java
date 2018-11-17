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
public class VideoClientGrabberThread extends Thread {
    private final String IP = "localhost";
    private final int PORT = 10000;
    
    private DatagramSocket socket;
    private FrameGrabber grabber;
    
    public VideoClientGrabberThread(DatagramSocket socket) {
        init(socket);
    }

    private void init(DatagramSocket socket) {
        try {
            this.socket = socket;
            this.grabber = FrameGrabber.createDefault(0);

            // Frame to capture
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //send byte array via UDP 
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
            e.printStackTrace();
        }
    }
    
    //stream cam
    public void streamCam() {
        try {
            grabber.start();
            Frame frame = null;
            while ((frame = grabber.grab()) != null) {
                //convert frame to byte array
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

    @Override
    public void run() {
        streamCam();
    }
}
