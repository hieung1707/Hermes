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

/**
 *
 * @author ASUS
 */
public class ServerCtr {
    private final int PORT = 2500;
    
    ServerSocket server;
    ArrayList<Socket> listSockets;
    
    public ServerCtr() {
        initServer();
    }
    
    private void initServer() {
        try {
            server = new ServerSocket(PORT);
            while (true) {
                Socket socket = server.accept();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            }
        } catch (Exception e) {
        }
    }
    
    public static void main(String[] args) {
        new ServerCtr();
    }
}
