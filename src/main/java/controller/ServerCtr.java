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
import java.util.ArrayList;
import java.util.HashMap;
import model.Room;
import model.Message;
import model.User;

/**
 *
 * @author ASUS
 */
public class ServerCtr {
    private final int PORT = 2500;
    
    ServerSocket server;
    HashMap<String, ObjectOutputStream> mapOos;
    HashMap<User, String> mapUsers;
    ArrayList<Room> listRooms;
    
    public ServerCtr() {
        initObjects();
        initServer();
    }
    
    private void initObjects() {
        mapOos = new HashMap<>();
        mapUsers = new HashMap<>();
        listRooms = new ArrayList<>();
    }
    
    private void initServer() {
        try {
            server = new ServerSocket(PORT);
            while (true) {
                Socket socket = server.accept();
                System.out.println(socket.getInetAddress().getHostAddress() + "|" + socket.getPort());
                mapOos.put(socket.getInetAddress().getHostAddress() + "|" + socket.getPort(), new ObjectOutputStream(socket.getOutputStream()));
                ServerListenerThread thread = new ServerListenerThread(new ObjectInputStream(socket.getInputStream()), mapUsers, mapOos);
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
