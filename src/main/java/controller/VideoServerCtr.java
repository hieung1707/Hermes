/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import javafx.util.Pair;

/**
 *
 * @author ASUS
 */
public class VideoServerCtr {

    private final int PORT = 10000;
    private final int BUFFER_SIZE = 1024;
    private HashMap<String, String> mapAddresses;
    private HashMap<Integer, Integer> mapPorts;

    private DatagramSocket videoServer;

    public VideoServerCtr() {
        init();
    }

    private void init() {
        try {
            mapAddresses = new HashMap<>();
            mapPorts = new HashMap<>();
            videoServer = new DatagramSocket(PORT);
            StreamThread thread = new StreamThread();
            thread.start();
//            thread.transferImageStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMap(Pair<String, Integer> p1, Pair<String, Integer> p2) {
        System.out.println(p1.getKey() + " " + p2.getKey());
        mapAddresses.put(p1.getKey()+"|"+Integer.toString(p1.getValue()), p2.getKey());
        mapAddresses.put(p2.getKey()+"|"+Integer.toString(p2.getValue()), p1.getKey());
        mapPorts.put(p1.getValue(), p2.getValue());
        mapPorts.put(p2.getValue(), p1.getValue());
        for (int port : mapPorts.keySet())
            System.out.println(port);
    }

    public void removeMaps(Pair<String, Integer> p1) {
        System.out.println(mapAddresses.size());
        String add2 = mapAddresses.get(p1.getKey() + "|" + Integer.toString(p1.getValue()));
        int port2 = mapPorts.get(p1.getValue());
        mapAddresses.remove(p1.getKey() + "|" + Integer.toString(p1.getValue()));
        mapAddresses.remove(add2 + "|" + port2);
        System.out.println(mapAddresses.size());
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
                    InetAddress receiver = InetAddress.getByName(mapAddresses.get(rcvPacket.getAddress().getHostAddress() + "|" + rcvPacket.getPort()));
                    if (receiver == null || mapPorts.get(rcvPacket.getPort()) == null)
                        continue;
                    DatagramPacket sendPacket = new DatagramPacket(rcvPacket.getData(), rcvPacket.getLength(), receiver, mapPorts.get(rcvPacket.getPort()));
                    videoServer.send(sendPacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
