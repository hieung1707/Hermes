/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author ASUS
 */
public class Message {
    private static final int SEND_USER = 1;
    private static final int SEND_GROUP = 2;
    
    private User sender;
    private String content;
    private User receiver;
    private Group group;
    private int type;

    public Message(User sender, String content, User receiver) {
        this.sender = sender;
        this.content = content;
        this.receiver = receiver;
        this.type = SEND_USER;
    }

    public Message(User sender, String content, Group group) {
        this.sender = sender;
        this.content = content;
        this.group = group;
        this.type = SEND_GROUP;
    }    
}
