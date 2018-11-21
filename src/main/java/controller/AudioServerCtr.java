/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import udp.*;
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
import javafx.util.Pair;
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
public class AudioServerCtr {

    private static final int BUFFER_SIZE = 1024;
    private static final int PORT = 501;

    private HashMap<String, String> mapAddresses;
    private HashMap<Integer, Integer> mapPorts;

    private DatagramSocket audioSocket;
    SourceDataLine sourceDataLine;
    byte tempBuffer[] = new byte[BUFFER_SIZE];
    static Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

    public AudioServerCtr() {
        try {
            mapAddresses = new HashMap<>();
            mapPorts = new HashMap<>();
            audioSocket = new DatagramSocket(PORT);
            SoundHandler sh = new SoundHandler();
            sh.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMap(Pair<String, Integer> p1, Pair<String, Integer> p2) {
        mapAddresses.put(p1.getKey() + "|" + Integer.toString(p1.getValue()), p2.getKey());
        mapAddresses.put(p2.getKey() + "|" + Integer.toString(p2.getValue()), p1.getKey());
        mapPorts.put(p1.getValue(), p2.getValue());
        mapPorts.put(p2.getValue(), p1.getValue());
        for (int port : mapPorts.keySet()) {
            System.out.println(port);
        }
    }

    public void removeMaps(Pair<String, Integer> p1) {
        String add2 = mapAddresses.get(p1.getKey() + "|" + Integer.toString(p1.getValue()));
        int port2 = mapPorts.get(p1.getValue());
        mapAddresses.remove(p1.getKey() + "|" + Integer.toString(p1.getValue()));
        mapAddresses.remove(add2 + "|" + port2);
        mapPorts.remove(p1.getValue());
        mapPorts.remove(port2);
    }

    //catch packet sent from sender and send to receiver, in this case server return packet to the sender
    public void sendAudio() {
        try {
            while (true) {
                byte[] receivedData = new byte[BUFFER_SIZE];
                DatagramPacket rcvPacket = new DatagramPacket(receivedData, receivedData.length);
                audioSocket.receive(rcvPacket);

                InetAddress receiver = InetAddress.getByName(mapAddresses.get(rcvPacket.getAddress().getHostAddress() + "|" + rcvPacket.getPort()));
                if (receiver == null || mapPorts.get(rcvPacket.getPort()) == null) {
                    continue;
                }

                DatagramPacket sendPacket = new DatagramPacket(rcvPacket.getData(), rcvPacket.getLength(), receiver, mapPorts.get(rcvPacket.getPort()));
                audioSocket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getBytes() {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket rcvPacket = new DatagramPacket(buffer, buffer.length);
            audioSocket.receive(rcvPacket);
            return rcvPacket.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class SoundHandler extends Thread {

        byte[] tempBuffer = new byte[BUFFER_SIZE];

        public SoundHandler() {
            try {
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
}
