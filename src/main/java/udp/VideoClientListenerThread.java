/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp;

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
public class VideoClientListenerThread extends Thread {
    private final int BUFFER_SIZE = 1024;
    
    private DatagramSocket client;

    public VideoClientListenerThread(DatagramSocket socket) {
        this.client = socket;
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
            boolean firstFrame = false;
            CanvasFrame canvas = new CanvasFrame("test");
            canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DatagramPacket packet;
            while (true) {
                //receive packet
                packet = new DatagramPacket(receivedata, receivedata.length);
                client.receive(packet);
                buffer = packet.getData();
                baos.write(buffer);
                
                if (packet.getLength() < BUFFER_SIZE) {
                    
                    //eliminate first frame
                    if (!firstFrame) {
                        firstFrame = true;
                        baos.reset();
                        continue;
                    }
                    
                    //transfer data to frame
                    baos.flush();
                    byte[] data = baos.toByteArray();
                    ByteArrayInputStream bais = new ByteArrayInputStream(data);
                    BufferedImage img = ImageIO.read(bais);
                    Frame frame = Java2DFrameUtils.toFrame(img);
                    
                    //show frame to canvas
                    canvas.showImage(frame);
                    baos.reset();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
