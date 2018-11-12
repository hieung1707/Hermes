/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author ASUS
 */
public class AudioServer {

    private static final int BUFFER_SIZE = 1024;
    private static final int PORT = 501;

    private DatagramSocket serverSocket;
    SourceDataLine sourceDataLine;
    byte tempBuffer[] = new byte[BUFFER_SIZE];
    static Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

    public AudioServer() throws LineUnavailableException {
        try {
            serverSocket = new DatagramSocket(PORT);
            SoundHandler sh = new SoundHandler(serverSocket);
            sh.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class SoundHandler extends Thread {

        byte[] tempBuffer = new byte[BUFFER_SIZE];
        DatagramSocket socket;

        public SoundHandler(DatagramSocket socket) {
            try {
                this.socket = socket;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                sendAudio();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //catch packet sent from sender and send to receiver, in this case server return packet to the sender
    public void sendAudio() {
        try {
            while (true) {
                byte[] receivedData = new byte[BUFFER_SIZE];
                DatagramPacket rcvPacket = new DatagramPacket(receivedData, receivedData.length);
                serverSocket.receive(rcvPacket);
                
                //modify the line below to send it to another client
                DatagramPacket sendPacket = new DatagramPacket(rcvPacket.getData(), rcvPacket.getLength(), rcvPacket.getAddress(), rcvPacket.getPort());
                serverSocket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getBytes() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket rcvPacket = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(rcvPacket);
            return rcvPacket.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String s[]) throws LineUnavailableException {
        try {
            System.out.println(InetAddress.getLocalHost().toString());
        } catch (UnknownHostException ex) {

        }
        AudioServer s2 = new AudioServer();
    }
}
