/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.input.KeyCode;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import model.Room;
import model.Message;
import model.User;
import org.bytedeco.javacpp.RealSense.frame;
import view.LoginFrm;
import view.MainFrm;

/**
 *
 * @author ASUS
 */
public class ClientCtr implements ActionListener, MouseListener, KeyListener {

    private final String IP = "localhost";
    private final int PORT = 2500;

    //model objects
    private User user;
    private Socket clientSocket;
    private ArrayList<User> listLobby;
    private ArrayList<User> listRencentChat;
    private ArrayList<Room> listRooms;
    private HashMap<Integer, String> mapChatLogs;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String targetType = "";

    //GUI objects
    private LoginFrm frmLogin;
    private MainFrm frmMain;

    public ClientCtr() {
    }

    private void initSocket() {
        try {
            clientSocket = new Socket(IP, PORT);
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            TCPListener listener = new TCPListener();
            listener.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListsAndMaps() {
        listLobby = new ArrayList<>();
        listRencentChat = new ArrayList<>();
        listRooms = new ArrayList<>();
        mapChatLogs = new HashMap<>();
    }

    private void initMainFrm() {
        frmMain = new MainFrm();
        frmMain.setActionListeners(this);
        frmMain.setMouseListeners(this);
        frmMain.setAlias(user.getAlias());
        frmMain.setVisible(true);
        frmMain.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frmMain.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Ask for confirmation before terminating the program.
                closeWindow();
            }
        });
    }

    private void login() {
        String alias = "";
        if (!(alias = frmLogin.getAlias()).trim().equals("")) {
            user = new User(alias);
            //init main frame
            initMainFrm();
            //close login frame
            frmLogin.dispose();
            //init client socket
            initSocket();
            //init maps and arrays
            initListsAndMaps();
            //send User info to server
            user.setIp(clientSocket.getLocalAddress().getHostAddress());
            user.setPort(clientSocket.getLocalPort());
            Message msg = new Message();
            msg.setSender(user);
            msg.setType(Message.SERVER_REGISTRATION);
            sendMessageToServer(msg);
        } else {
            JOptionPane.showMessageDialog(frmLogin, "Please choose your alias");
        }
    }

    private void closeWindow() {
        Message msg = new Message();
        msg.setSender(user);
        msg.setType(Message.SERVER_LOG_OUT);
        sendMessageToServer(msg);
        System.exit(0);
    }

    private void sendMessageToServer(Message msg) {
        try {
            oos.writeObject(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUserConversation() {
        targetType = "user";
        String userAlias = frmMain.getSelectedIndex();
        frmMain.setConversationName(userAlias);
        frmMain.setDescription("Have a nice conversation with him/her");
        for (User user : listLobby) {
            if (user.getAlias().equals(userAlias)) {
                String log = "";
                if (mapChatLogs.containsKey(user.getId())) {
                    log = mapChatLogs.get(user.getId());
                    frmMain.setContent(log);
                } else {
                    mapChatLogs.put(user.getId(), log);
                }
            }
        }
    }

    public void setLoginFrame(LoginFrm frm) {
        this.frmLogin = frm;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        switch (command) {
            case "login":
                login();
                break;
            case "send":
                sendMessageToServer(new Message());
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getSource() instanceof javax.swing.JList) {
            int tab = frmMain.getSelectedTabbedPane();
            switch (tab) {
                case 0:
                    initUserConversation();
                    break;
                case 1:
                case 2:
            }
        }
    }

    //What happens when you press enter while messaging
    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getSource().equals(frmMain.getMessageBox()) && ke.getKeyCode() == KeyEvent.VK_ENTER) {
            User receiver = null;
            Room room = null;
            
            JTextField txtSend = frmMain.getMessageBox();
            String receiverAlias = frmMain.getCurrentReceiver();
            if (targetType.equals("user")) {
                for (User u : listLobby) {
                    if (u.getAlias().equals(receiverAlias)) {
                        receiver = u;
                        break;
                    }
                }
            }
            else {
                for (Room r : listRooms) {
                    if (r.getRoomName().equals(receiverAlias)) {
                        room = r;
                        break;
                    }
                }
            }
            
            if (targetType.equals("user") && receiver == null) {
                JOptionPane.showMessageDialog(frmMain, "This user has gone offline");
                return;
            }
            else if (targetType.equals("room") && room == null) {
                JOptionPane.showConfirmDialog(frmMain, "Room has been disbanded");
                return;
            }
            
            if (receiver != null)
                sendMessageToServer(new Message(user, txtSend.getText(), user));
            else if (room != null)
                sendMessageToServer(new Message(user, txtSend.getText(), room));
            txtSend.setText("");
            txtSend.setCaretPosition(0);
        }
    }

//<editor-fold defaultstate="collapsed" desc="unused">
    @Override
    public void mousePressed(MouseEvent me) {
        //not used
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //not used
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //not used
    }

    @Override
    public void mouseExited(MouseEvent me) {
        //not used
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        //not used
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        //not used
    }
//</editor-fold>

    class TCPListener extends Thread {

        @Override
        public void run() {
            try {
                Message msg;
                while ((msg = (Message) ois.readObject()) != null) {
                    switch (msg.getType()) {
                        case Message.SERVER_REGISTRATION:
                            System.out.println("MORE USERS");
                            updateLobbyList(msg);
                            break;
                        case Message.SERVER_LOG_OUT:
                            System.out.print("SOMEONE LOGOUT!");
                            updateLobbyList(msg);
                            break;
                        case Message.SEND_MESSAGE_USER:
                            System.out.println("MSG FROM USER");
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void updateLobbyList(Message message) {
            listLobby = message.getUserList();
            listLobby.remove(user);
            ArrayList<String> listNames = new ArrayList<>();
            for (User u : listLobby) {
                listNames.add(u.getAlias());
            }
            frmMain.updateList(frmMain.getLobbyList(), listNames);
        }
        
        private void appendMessage
    }
}
