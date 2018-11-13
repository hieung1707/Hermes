/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class Message implements Serializable {
    public static final int SERVER_REGISTRATION = 0;
    public static final int SEND_MESSAGE_USER = 1;    
    public static final int SEND_REQUEST_USER = 2;
    public static final int SEND_ACCEPT_USER = 3;
    public static final int SEND_DECLINE_USER = 4;
    public static final int SEND_MESSAGE_ROOM = 5;
    public static final int CREATE_ROOM = 6;
    public static final int SEND_REQUEST_ROOM = 7;
    public static final int SEND_ACCEPT_ROOM = 8;
    public static final int SEND_DECLINE_ROOM = 9;
    public static final int SERVER_LOG_OUT = 10;
    
    private User sender;
    private String content;
    private User receiver;
    private Room group;
    private int type;
    private int udpPort;
    private ArrayList<User> listUsers;
    
    public Message() {
        
    }

    public Message(User sender, String content, User receiver) {
        this.sender = sender;
        this.content = content;
        this.receiver = receiver;
        this.type = SEND_MESSAGE_USER;
    }

    public Message(User sender, String content, Room group) {
        this.sender = sender;
        this.content = content;
        this.group = group;
        this.type = SEND_MESSAGE_ROOM;
    }

    public User getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public User getReceiver() {
        return receiver;
    }

    public Room getGroup() {
        return group;
    }

    public int getType() {
        return type;
    }

    public int getUdpPort() {
        return udpPort;
    }
    
    public ArrayList<User> getUserList() {
        return listUsers;
    }
    
    public void setUserList(ArrayList<User> listUsers) {
        this.listUsers = listUsers;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public void setGroup(Room group) {
        this.group = group;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }
}
