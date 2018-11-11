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
    private String alias;
    private String ip;
    private String port;
    
    public User(String alias) {
        this.alias = alias + "#" + Integer.toString((int) (Math.random() * 1000000));
    }
    
    public String getAlias() {
        return alias;
    }
}
