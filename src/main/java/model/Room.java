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
public class Room implements Serializable {
    private int id;
    private User roomMaster;
    private String roomName;
    private ArrayList<User> members;
    private ArrayList<User> requests;

    public Room(String roomName, User roomMaster) {
        id = 1000000 + (int) (Math.random() * 1000000);
        this.roomName = roomName + "(#" + Integer.toString(id) + ")";
        this.roomMaster = roomMaster;
    }

    public int getId() {
        return id;
    }

    public User getRoomMaster() {
        return roomMaster;
    }

    public String getRoomName() {
        return roomName;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public ArrayList<User> getRequests() {
        return requests;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoomMaster(User roomMaster) {
        this.roomMaster = roomMaster;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public void setRequests(ArrayList<User> requests) {
        this.requests = requests;
    }
    
    
}
