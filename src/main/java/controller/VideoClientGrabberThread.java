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
import java.net.SocketException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
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
    private boolean running;
    
    public VideoClientGrabberThread(DatagramSocket socket) {
        init(socket);
    }

    private void init(DatagramSocket socket) {
        try {
            this.socket = socket;
            System.out.println(socket.getLocalAddress().getHostAddress() + " " + socket.getLocalPort());
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
            running = true;
            while (running) {
                //convert frame to byte array
                frame = grabber.grab();
                if (frame == null)
                    continue;
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
            JOptionPane.showMessageDialog(null, "Your camera is being used by another user");
            closeVideo();
        }
    }

    @Override
    public void run() {
        streamCam();
    }

    
    public void closeVideo() {
        running = false;
    }
}
