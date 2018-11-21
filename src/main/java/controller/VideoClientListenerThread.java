/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.imageio.ImageIO;
import view.StreamFrm;

/**
 *
 * @author ASUS
 */
public class VideoClientListenerThread extends Thread {

    private final int BUFFER_SIZE = 1024;

    private DatagramSocket client;
    private StreamFrm frmStream;
    private boolean running;

    public VideoClientListenerThread(DatagramSocket socket, StreamFrm frmStream) {
        this.client = socket;
        this.frmStream = frmStream;
    }

    @Override
    public void run() {
        getImage();
    }

    //listen to server to get byte array and display via opencv CanvasFrame
    public void getImage() {
        try {
            byte[] receivedata = new byte[BUFFER_SIZE];
            byte[] buffer;
            int count = 0;
//            CanvasFrame canvas = new CanvasFrame("test");
//            canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DatagramPacket packet;
            running = true;
            while (running) {
                //receive packet
                if (client.isClosed()) {
                    break;
                }
                packet = new DatagramPacket(receivedata, receivedata.length);
                client.receive(packet);

                buffer = packet.getData();
                baos.write(buffer);

                if (packet.getLength() < BUFFER_SIZE) {
                    //eliminate first frame
                    if (baos.size() > 25000) {
                        baos.reset();
                        continue;
                    }
                    if (count < 2) {
                        count++;
                        baos.reset();
                        continue;
                    }

                    //transfer data to frame
                    baos.flush();
                    byte[] data = baos.toByteArray();
                    ByteArrayInputStream bais = new ByteArrayInputStream(data);
                    BufferedImage img = ImageIO.read(bais);
                    frmStream.getStreamPanel().setImg(img);
                    //                    Frame frame = Java2DFrameUtils.toFrame(img);
                    //                    
                    //                    //show frame to canvas
                    //                    canvas.showImage(frame);

                    baos.reset();
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeVideo() {
        running = false;
    }
}
