/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author ASUS
 */
public class Message implements Serializable {

    public static final int SERVER_REGISTRATION = 0;
    public static final int SEND_MESSAGE_USER = 1;
    public static final int SEND_REQUEST_USER = 2;
    public static final int SEND_RESPONSE_USER = 3;
    public static final int FIND_ROOM = 4;
    public static final int SEND_MESSAGE_ROOM = 5;
    public static final int CREATE_ROOM = 6;
    public static final int SEND_REQUEST_ROOM = 7;
    public static final int SEND_RESPONSE_ROOM = 8;
    public static final int SERVER_LOG_OUT = 9;
    public static final int CLOSE_STREAM = 10;

    private User sender;
    private String content;
    private User receiver;
    private Room room;
    private int type;
    private boolean accept;
    private ArrayList<User> listUsers;
    private ArrayList<Room> listRooms;
    private Pair<String, Integer> addressVideoSender;
    private Pair<String, Integer> addressVideoReceiver;
    private Pair<String, Integer> addressAudioSender;
    private Pair<String, Integer> addressAudioReceiver;

//    private 

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
        this.room = group;
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

    public Room getRoom() {
        return room;
    }

    public int getType() {
        return type;
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

    public void setRoom(Room group) {
        this.room = group;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public ArrayList<Room> getListRooms() {
        return listRooms;
    }

    public void setListRooms(ArrayList<Room> listRooms) {
        this.listRooms = listRooms;
    }

    public Pair<String, Integer> getVideoAddressSender() {
        return addressVideoSender;
    }

    public void setVideoAddressSender(Pair<String, Integer> add1) {
        this.addressVideoSender = add1;
    }

    public Pair<String, Integer> getVideoAddressReceiver() {
        return addressVideoReceiver;
    }

    public void setVideoAddressReceiver(Pair<String, Integer> add2) {
        this.addressVideoReceiver = add2;
    }

    public Pair<String, Integer> getAddressAudioSender() {
        return addressAudioSender;
    }

    public Pair<String, Integer> getAddressAudioReceiver() {
        return addressAudioReceiver;
    }

    public void setAddressAudioSender(Pair<String, Integer> addressAudioSender) {
        this.addressAudioSender = addressAudioSender;
    }

    public void setAddressAudioReceiver(Pair<String, Integer> addressAudioReceiver) {
        this.addressAudioReceiver = addressAudioReceiver;
    }
    
    
}
