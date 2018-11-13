/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import model.Message;
import model.User;

/**
 *
 * @author ASUS
 */
public class ServerListenerThread extends Thread {

    private boolean running;
    private ObjectInputStream ois;
    private HashMap<User, String> mapUsers;
    private HashMap<String, ObjectOutputStream> mapOos;

    public ServerListenerThread(ObjectInputStream ois, HashMap<User, String> mapUsers, HashMap<String, ObjectOutputStream> listOos) {
        this.ois = ois;
        this.mapOos = listOos;
        this.mapUsers = mapUsers;
    }

    @Override
    public void run() {
        try {
            running = true;
            Message msg = null;
            while (running) {
                if ((msg = (Message) ois.readObject()) != null) {
                    switch (msg.getType()) {
                        case Message.SERVER_REGISTRATION:
                            addUser(msg);
                            break;
                        case Message.SEND_MESSAGE_USER:
                            sendMsgToUser(msg);
                            break;
                        case Message.SEND_REQUEST_USER:

                            break;
                        case Message.SEND_ACCEPT_USER:

                            break;
                        case Message.SEND_DECLINE_USER:

                            break;
                        case Message.SEND_MESSAGE_ROOM:

                            break;
                        case 6:
                            break;
                        case 7:

                            break;
                        case 8:
                            break;
                        case 9:
                            break;
                        case Message.SERVER_LOG_OUT:
                            logout(msg);
                            break;
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private void addUser(Message msg) {
        try {
            mapUsers.put(msg.getSender(), msg.getSender().getIp() + "|" + msg.getSender().getPort());
            ArrayList<User> listUsers = new ArrayList<>(mapUsers.keySet());
            msg.setUserList(listUsers);
            for (String key : mapOos.keySet()) {
                ObjectOutputStream oos = mapOos.get(key);
                oos.writeObject(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMsgToUser(Message msg) {
        try {
            String identity = mapUsers.get(msg.getReceiver());
            mapOos.get(identity).writeObject(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout(Message msg) {
        try {
            User user = msg.getSender();
            mapOos.remove(mapUsers.get(user));
            mapUsers.remove(user);
            ArrayList<User> listUsers = new ArrayList<>(mapUsers.keySet());
            msg.setUserList(listUsers);
            for (String key : mapOos.keySet()) {
                ObjectOutputStream oos = mapOos.get(key);
                oos.writeObject(msg);
            }
            running = false;
        } catch (Exception e) {

        }
    }
}
