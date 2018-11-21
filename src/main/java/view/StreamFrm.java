/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author ASUS
 */
public class StreamFrm extends JFrame {
    private StreamPanel panel;
    private JButton btnCancel;
    private JLabel lblTimer;
    private int seconds;
    private int minutes;
    
    public StreamFrm() {
        initComponents();
        new TimerThread().start();
    }
    
    public void initComponents() {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        panel = new StreamPanel();
        panel.setPreferredSize(new Dimension(600, 400));
        add(panel);
        add(Box.createRigidArea(new Dimension(0,10)));
        lblTimer = new JLabel("hello");
        lblTimer.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTimer.setFont(new Font("arial", Font.BOLD, 20));
        add(lblTimer);
        add(Box.createRigidArea(new Dimension(0,10)));
        btnCancel = new JButton("Cancel");
        btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(btnCancel);
        add(Box.createRigidArea(new Dimension(0,10)));
        pack();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        new StreamFrm();
    }
    
    public StreamPanel getStreamPanel() {
        return panel;
    }
    
    class TimerThread extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                minutes = seconds / 60;
                lblTimer.setText(String.format("%02d:%02d", minutes, seconds));
                seconds++;
                Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
} 
