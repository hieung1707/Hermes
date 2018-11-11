/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hermes;

import controller.ClientCtr;
import view.LoginFrm;
import view.MainFrm;

/**
 *
 * @author ASUS
 */
public class Main {

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoginFrm frm = new LoginFrm();
                        
                ClientCtr client = new ClientCtr();
                client.initGUI(frm);
                frm.setActionListener(client);
                frm.setVisible(true);
            }
        });

    }
}
