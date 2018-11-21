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
public class User implements Serializable {
    private int id;
    private String alias;
    private String ip;
    private int port;
    
    public User(String alias) {
        id = (int) (Math.random() * 1000000);
        this.alias = alias + "(#" + Integer.toString(id) + ")";
    }
    
    public int getId() {
        return id;
    }
    
    public String getAlias() {
        return alias;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
