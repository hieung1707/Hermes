/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author ASUS
 */
public class Group {
    private User roomMaster;
    private String roomName;
    private ArrayList<User> members;
    private ArrayList<User> requests;

    public Group(String roomName, User roomMaster) {
        this.roomName = roomName;
        this.roomMaster = roomMaster;
    }
}
