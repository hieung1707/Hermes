/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import javafx.util.Pair;
import model.User;

/**
 *
 * @author ASUS
 */
public class VideoServerCtr {
    private final int PORT = 10000;
    private final int BUFFER_SIZE = 1024;
    private HashMap<InetAddress, InetAddress> mapAddresses;
    private HashMap<Integer, Integer> mapPorts;
    
    private DatagramSocket videoServer;
    
    public VideoServerCtr() {
        init();
    }
    
    private void setupConnection(Pair<InetAddress, Integer> p1, Pair<InetAddress, Integer> p2) {
        mapAddresses.put(p1.getKey(), p2.getKey());
        mapPorts.put(p1.getValue(), p2.getValue());
    }
    
    private void init() {
        try {
            mapAddresses = new HashMap<>();
            mapPorts = new HashMap<>();
            videoServer = new DatagramSocket(PORT);
            StreamThread thread = new StreamThread();
            thread.start();
        } catch (Exception e) {
        }
    }
    
    public void addMap(Pair<InetAddress, Integer> p1, Pair<InetAddress, Integer> p2) {
        mapAddresses.put(p1.getKey(), p2.getKey());
        mapAddresses.put(p2.getKey(), p1.getKey());
        mapPorts.put(p1.getValue(), p2.getValue());
        mapPorts.put(p2.getValue(), p1.getValue());
    }
    
    public void removeMaps(Pair<InetAddress, Integer> p1) {
        InetAddress add2 = mapAddresses.get(p1.getKey());
        int port2 = mapPorts.get(p1.getValue());
        mapAddresses.remove(p1.getKey());
        mapAddresses.remove(add2);
        mapPorts.remove(p1.getValue());
        mapPorts.remove(port2);
    } 
    
    
    //get byte array and send to receiver, in this case the receiver is the sender
    class StreamThread extends Thread {

        @Override
        public void run() {
            transferImageStream();
        }
        
        public void transferImageStream() {
        try {
            while (true) {
                byte[] receivedData = new byte[BUFFER_SIZE];
                DatagramPacket rcvPacket = new DatagramPacket(receivedData, receivedData.length);
                videoServer.receive(rcvPacket);
                //send back to the sender, modify this part to send to other receivers
                DatagramPacket sendPacket = new DatagramPacket(rcvPacket.getData(), rcvPacket.getLength(), mapAddresses.get(rcvPacket.getAddress()), mapPorts.get(rcvPacket.getPort()));
                videoServer.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
    
    public static void main(String[] args) {
        VideoServerCtr ctr = new VideoServerCtr();
        
    }
}
