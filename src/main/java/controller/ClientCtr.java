/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import model.Group;
import model.User;
import view.LoginFrm;
import view.MainFrm;

/**
 *
 * @author ASUS
 */
public class ClientCtr implements ActionListener {
    private final String IP = "localhost";
    private final int PORT = 2500;
    
    //model objects
    private User user;
    private Socket clientSocket;
    private ArrayList<User> listRencentChat;
    private ArrayList<Group> listGroups;
    private HashMap<User, String> mapChatLogs;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    //GUI objects
    private LoginFrm frmLogin;
    private MainFrm frmMain;
    
    public ClientCtr() {
        initSocket();
    }
    
    private void initSocket() {
        try {
            clientSocket = new Socket(IP, PORT);
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("DONE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void initGUI(LoginFrm frm) {
        this.frmLogin = frm;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        switch(command) {
            case "login":
                login();
                break;
        }
    }
    
    private void login() {
        String alias = "";
        if (!(alias = frmLogin.getAlias()).trim().equals("")) {
            user = new User(alias);
            frmMain = new MainFrm();
            frmMain.setAlias(user.getAlias());
            frmMain.setVisible(true);
            frmLogin.dispose();
        }
        else {
            JOptionPane.showMessageDialog(frmLogin, "Please choose your alias");
        }
    }
}
