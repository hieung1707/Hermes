/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author ASUS
 */
public class MainFrm extends javax.swing.JFrame {
    /**
     * Creates new form MainFrm
     */
    public MainFrm() {
        setResizable(false);
        initComponents();
        updateList(0, null);
        updateList(1, null);
        updateList(2, null);
        setLocationRelativeTo(null);
        txtChatLog.setEditable(false);
        panelRight.setVisible(false);
    }
   
    
    public void updateList(int tab, ArrayList<String> values) {
        javax.swing.JList<String> list = null;
        switch(tab) {
            case 0:
                list = listLobby;
                break;
            case 1:
                list = listRooms;
                break;
        }
        if (list == null)
            return;
        DefaultListModel model = new DefaultListModel();
        list.setModel(model);
//        model = (DefaultListModel) list.getModel();
        model.removeAllElements();
        if (values == null)
            return;
        for (String alias : values) {
            model.addElement(alias);
        }
    }
    
    public void setActionListeners(ActionListener listener) {
        topBtn1.addActionListener(listener);
        topBtn2.addActionListener(listener);
        btnSend.addActionListener(listener);
        btnCreateRoom.addActionListener(listener);
        btnFindRoom.addActionListener(listener);
    }
    
    public void setMouseListeners(MouseListener listener) {
        listLobby.addMouseListener(listener);
        listRooms.addMouseListener(listener);
    }
    
    public void setKeyListeners(KeyListener listener) {
        txtSend.addKeyListener(listener);
    }
    
    public void setRightPanelVisibility(boolean isVisible) {
        panelRight.setVisible(isVisible);
    }
    
    public void setConversationName(String name) {
        lblName.setText(name);
    }
    
    public void setContent(String msg) {
        txtChatLog.setText(msg);
    }
    
    public void setDescription(String desc) {
        lblDesc.setText(desc);
    }

    public void setRoomRelatedButtonsVisibility(boolean isVisible) {
        topBtn1.setVisible(isVisible);
        topBtn2.setVisible(isVisible);
    }
    
    public int getSelectedTabbedPane() {
        return tpContacts.getSelectedIndex();
    }
    
    public String getContent() {
        return txtChatLog.getText();
    }
    
    public JTextField getMessageBox() {
        return txtSend;
    }
    
    public javax.swing.JList getList(int tab) {
        switch(tab) {
            case 0:
                return listLobby;
            case 1:
                return listRooms;
        }
        return null;
    }
    
    public String getMessage() {
        return txtSend.getText();
    }
    
    public String getCurrentReceiver() {
        return lblName.getText();
    }
    
    public String getSelectedIndex() {
        switch (tpContacts.getSelectedIndex()) {
            case 0:
                return listLobby.getSelectedValue();
            case 1:
                return listRooms.getSelectedValue();
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leftPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnLogout = new javax.swing.JButton();
        lblWelcome = new javax.swing.JLabel();
        tpContacts = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listLobby = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listRooms = new javax.swing.JList<>();
        btnCreateRoom = new javax.swing.JButton();
        btnFindRoom = new javax.swing.JButton();
        panelRight = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        lblDesc = new javax.swing.JLabel();
        topBtn1 = new javax.swing.JButton();
        topBtn2 = new javax.swing.JButton();
        txtSend = new javax.swing.JTextField();
        btnFile = new javax.swing.JButton();
        btnVideo = new javax.swing.JButton();
        btnAudio = new javax.swing.JButton();
        btnSend = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtChatLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        leftPanel.setBackground(new java.awt.Color(204, 204, 204));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        btnLogout.setText("Log out");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        lblWelcome.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblWelcome.setText("Hello, ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWelcome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogout))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogout)
                    .addComponent(lblWelcome))
                .addGap(0, 5, Short.MAX_VALUE))
        );

        listLobby.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listLobby);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );

        tpContacts.addTab("Lobby", jPanel6);

        listRooms.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listRooms);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        tpContacts.addTab("Rooms", jPanel2);

        btnCreateRoom.setText("Create Room");
        btnCreateRoom.setActionCommand("create_room");
        btnCreateRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateRoomActionPerformed(evt);
            }
        });

        btnFindRoom.setText("Find Room");
        btnFindRoom.setActionCommand("find");

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tpContacts, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(leftPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnCreateRoom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnFindRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tpContacts, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFindRoom, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(btnCreateRoom))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        leftPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnCreateRoom, btnFindRoom});

        panelRight.setBackground(new java.awt.Color(204, 204, 204));

        lblName.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblName.setText("Name");

        lblDesc.setText("text");

        topBtn1.setText("Leave Room");
        topBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                topBtn1ActionPerformed(evt);
            }
        });

        topBtn2.setText("Manage Room");
        topBtn2.setActionCommand("manage");
        topBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                topBtn2ActionPerformed(evt);
            }
        });

        btnFile.setText("File");

        btnVideo.setText("Video");

        btnAudio.setText("Audio");

        btnSend.setText("Send");
        btnSend.setActionCommand("send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        txtChatLog.setColumns(20);
        txtChatLog.setRows(5);
        jScrollPane4.setViewportView(txtChatLog);

        javax.swing.GroupLayout panelRightLayout = new javax.swing.GroupLayout(panelRight);
        panelRight.setLayout(panelRightLayout);
        panelRightLayout.setHorizontalGroup(
            panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRightLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRightLayout.createSequentialGroup()
                        .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRightLayout.createSequentialGroup()
                                .addComponent(lblName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(topBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(topBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelRightLayout.createSequentialGroup()
                                .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRightLayout.createSequentialGroup()
                                        .addComponent(btnFile)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnVideo)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnAudio)
                                        .addGap(0, 363, Short.MAX_VALUE))
                                    .addComponent(txtSend))
                                .addGap(18, 18, 18)
                                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(12, 12, 12))
                    .addGroup(panelRightLayout.createSequentialGroup()
                        .addComponent(lblDesc)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelRightLayout.createSequentialGroup()
                        .addComponent(jScrollPane4)
                        .addContainerGap())))
        );

        panelRightLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAudio, btnFile, btnVideo});

        panelRightLayout.setVerticalGroup(
            panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRightLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(topBtn1)
                        .addComponent(topBtn2))
                    .addGroup(panelRightLayout.createSequentialGroup()
                        .addComponent(lblName)
                        .addGap(21, 21, 21)
                        .addComponent(lblDesc)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 15, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRightLayout.createSequentialGroup()
                        .addGroup(panelRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnFile)
                            .addComponent(btnVideo)
                            .addComponent(btnAudio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSend, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(leftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCreateRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCreateRoomActionPerformed

    private void topBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_topBtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_topBtn1ActionPerformed

    private void topBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_topBtn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_topBtn2ActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoutActionPerformed

    public void setAlias(String alias) {
        lblWelcome.setText("Welcome, "+ alias);
    }
    
    public static void main(String[] args) {
        new MainFrm().setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAudio;
    private javax.swing.JButton btnCreateRoom;
    private javax.swing.JButton btnFile;
    private javax.swing.JButton btnFindRoom;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton btnVideo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblDesc;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JList<String> listLobby;
    private javax.swing.JList<String> listRooms;
    private javax.swing.JPanel panelRight;
    private javax.swing.JButton topBtn1;
    private javax.swing.JButton topBtn2;
    private javax.swing.JTabbedPane tpContacts;
    private javax.swing.JTextArea txtChatLog;
    private javax.swing.JTextField txtSend;
    // End of variables declaration//GEN-END:variables
}
