/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import model.Room;
import model.User;

/**
 *
 * @author ASUS
 */
public class ServerCtr {
    private final int PORT = 2500;
    
    ServerSocket server;
//    DatagramSocket udpSocket;
    HashMap<String, ObjectOutputStream> mapOos;
    HashMap<User, String> mapUsers;
    HashMap<Integer, Room> listRooms;
    
    public ServerCtr() {
        initObjects();
        initServer();
    }
    
    private void initObjects() {
        mapOos = new HashMap<>();
        mapUsers = new HashMap<>();
        listRooms = new HashMap<>();
    }
    
    
    private void initServer() {
        try {
//            udpSocket = new DatagramSocket(PORT);
            VideoServerCtr videoCtr = new VideoServerCtr();
            AudioServerCtr audioCtr = new AudioServerCtr();
            server = new ServerSocket(PORT);
            while (true) {
                Socket socket = server.accept();
                mapOos.put(socket.getInetAddress().getHostAddress() + "|" + socket.getPort(), new ObjectOutputStream(socket.getOutputStream()));
                ServerListenerThread thread = new ServerListenerThread(new ObjectInputStream(socket.getInputStream()), mapUsers, mapOos, listRooms, videoCtr, audioCtr);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
        new ServerCtr();
    }
}
