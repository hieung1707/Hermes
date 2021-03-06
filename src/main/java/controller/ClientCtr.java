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
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import model.Room;
import model.Message;
import model.User;
import view.LoginFrm;
import view.MainFrm;
import view.ManageRoomFrm;
import view.MessageFrm;
import view.StreamFrm;

/**
 *
 * @author ASUS
 */
public class ClientCtr implements ActionListener, MouseListener, KeyListener {

    private final String IP = "localhost";
    private final int PORT = 2500;
    private final String NEW_LINE = "\n";

    //model objects
    private User user;
    private Socket clientSocket;
    private ArrayList<User> listLobby;
    private ArrayList<Room> listRooms;
    private HashMap<Integer, String> mapChatLogs;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String targetType = "";
    private DatagramSocket videoSocket;
    private DatagramSocket audioSocket;
    private User userStream; // user currently stream with

    //threads
    private VideoClientGrabberThread videoGrabber;
    private VideoClientListenerThread videoListener;
    private AudioClient audioClient;

    //GUI objects
    private LoginFrm frmLogin;
    private MainFrm frmMain;
    private ManageRoomFrm frmFind;
    private StreamFrm frmStream;

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
        listRooms = new ArrayList<>();
        mapChatLogs = new HashMap<>();
    }

    private void initMainFrm() {
        frmMain = new MainFrm();
        frmMain.setActionListeners(this);
        frmMain.setMouseListeners(this);
        frmMain.setKeyListeners(this);
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
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUserConversation(String userAlias) {
        frmMain.setRightPanelVisibility(true);
        targetType = "user";
        frmMain.setConversationName(userAlias);
        frmMain.setDescription("Have a nice conversation with him/her");
        frmMain.setRoomRelatedButtonsVisibility(false);
        for (User user : listLobby) {
            if (user.getAlias().equals(userAlias)) {
                String log = "";
                if (mapChatLogs.containsKey(user.getId())) {
                    log = mapChatLogs.get(user.getId());

                } else {
                    mapChatLogs.put(user.getId(), log);
                }
                frmMain.setContent(log);
            }
        }
    }

    private void initRoomConversation(String roomName) {
        frmMain.setRightPanelVisibility(true);
        targetType = "room";
        frmMain.setConversationName(roomName);
        frmMain.setDescription(IP);
        frmMain.setRoomRelatedButtonsVisibility(false);
        for (Room r : listRooms) {
            if (r.getRoomName().equals(roomName)) {
                String log = "";
                if (mapChatLogs.containsKey(r.getId())) {
                    log = mapChatLogs.get(r.getId());

                } else {
                    mapChatLogs.put(r.getId(), log);
                }
                frmMain.setContent(log);
            }
        }
    }

    public void setLoginFrame(LoginFrm frm) {
        this.frmLogin = frm;
    }

    public void sendContent() {
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
        } else {
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
        } else if (targetType.equals("room") && room == null) {
            JOptionPane.showMessageDialog(frmMain, "Room has been disbanded");
            return;
        }

        if (txtSend.getText().equals("")) {
            return;
        }

        if (receiver != null) {
            sendMessageToServer(new Message(user, txtSend.getText(), receiver));
        } else if (room != null) {
            sendMessageToServer(new Message(user, txtSend.getText(), room));
        }
        txtSend.setText("");
        txtSend.setCaretPosition(0);
    }

    private void createRoom() {
        String roomName = JOptionPane.showInputDialog(frmMain, "Please insert your room name");
        if (roomName == null) {
            return;
        }
        Room room = new Room(roomName, user);
        room.addMember(user);
        Message message = new Message();
        message.setSender(user);
        message.setRoom(room);
        message.setType(Message.CREATE_ROOM);

        sendMessageToServer(message);
    }

    private void manageRoom() {

    }

    private void findRoom() {
        if (frmFind != null) {
            return;
        }
        frmFind = new ManageRoomFrm(ManageRoomFrm.FIND);
        frmFind.setActionListeners(this);
        frmFind.setVisible(true);
        Message message = new Message();
        message.setSender(user);
        message.setType(Message.FIND_ROOM);
        sendMessageToServer(message);

    }

    private void foundRoom(ArrayList<Room> list) {
        ArrayList<String> nameList = new ArrayList<>();
        for (Room r : list) {
            if (listRooms.contains(r)) {
                continue;
            }
            nameList.add(r.getRoomName());
        }
        frmFind.updateRoomList(nameList);
    }

    private void requestEnter() {
        String name = frmFind.getSelectedRoom();
        if (name == null) {
            return;
        }
        Room room = new Room();
        room.setRoomName(name);
        Message msg = new Message();
        msg.setSender(user);
        msg.setRoom(room);
        msg.setType(Message.SEND_REQUEST_ROOM);
        sendMessageToServer(msg);
        frmFind.dispose();
        frmFind = null;

    }

    private void requestStream() {
        try {
            User receiver = null;
            if (!targetType.equals("user")) {
                JOptionPane.showMessageDialog(frmMain, "This version doesn't support groupp streaming yet");
                return;
            }
            for (User u : listLobby) {
                if (u.getAlias().equals(frmMain.getCurrentReceiver())) {
                    receiver = u;
                }
            }
            if (receiver == null) {
                JOptionPane.showMessageDialog(frmMain, "This user has gone offline");
                return;
            }
            Message msg = new Message();
            videoSocket = new DatagramSocket();
            audioSocket = new DatagramSocket();
            msg.setSender(user);
            msg.setReceiver(receiver);
            msg.setType(Message.SEND_REQUEST_USER);
            msg.setVideoAddressSender(new Pair<>(InetAddress.getLocalHost().getHostAddress(), videoSocket.getLocalPort()));
            msg.setAddressAudioSender(new Pair<>(InetAddress.getLocalHost().getHostAddress(), audioSocket.getLocalPort()));
            sendMessageToServer(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> convertUserToString(ArrayList<User> listUsers) {
        ArrayList<String> listNames = new ArrayList<>();
        for (User u : listUsers) {
            if (u.getAlias().equals(user.getAlias())) {
                continue;
            }
            listNames.add(u.getAlias());
        }
        return listNames;
    }

    private ArrayList<String> convertRoomToString(ArrayList<Room> listRoom) {
        ArrayList<String> listNames = new ArrayList<>();
        for (Room r : listRoom) {
            listNames.add(r.getRoomName());
        }
        return listNames;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        switch (command) {
            case "login":
                login();
                break;
            case "send":
                sendContent();
                break;
            case "create_room":
                createRoom();
                break;
            case "manage":
                manageRoom();
                break;
            case "find":
                findRoom();
                break;
            case "request":
                requestEnter();
                break;
            case "request_stream":
                requestStream();
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getSource() instanceof javax.swing.JList) {
            int tab = frmMain.getSelectedTabbedPane();
            switch (tab) {
                case 0:
                    initUserConversation(frmMain.getSelectedIndex());
                    break;
                case 1:
                    initRoomConversation(frmMain.getSelectedIndex());
            }
        }
    }

    //What happens when you press enter while messaging
    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getSource().equals(frmMain.getMessageBox()) && ke.getKeyCode() == KeyEvent.VK_ENTER) {
            sendContent();
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
                            updateRoomsAndLobby(msg);
                            break;
                        case Message.SERVER_LOG_OUT:
                            System.out.print("SOMEONE LOGOUT!");
                            updateRoomsAndLobby(msg);
                            break;
                        case Message.SEND_MESSAGE_USER:
                            System.out.println("MSG FROM USER");
                            appendMessage(msg);
                            break;
                        case Message.FIND_ROOM:
                            System.out.println("ROOMS FOUND");
                            foundRoom(msg.getListRooms());
                            break;
                        case Message.CREATE_ROOM:
                            JOptionPane.showMessageDialog(frmMain, "Room created successfully");
                            updateListRoom(msg.getListRooms());
                            break;
                        case Message.SEND_REQUEST_ROOM:
                            responseRequest(msg);
                            break;
                        case Message.SEND_RESPONSE_ROOM:
                            handleResponseRoom(msg);
                            break;
                        case Message.SEND_MESSAGE_ROOM:
                            appendMessageRoom(msg);
                            break;
                        case Message.SEND_REQUEST_USER:
                            responseStreamRequest(msg);
                            break;
                        case Message.SEND_RESPONSE_USER:
                            handleResponseStream(msg);
                            break;
                        case Message.CLOSE_STREAM:
                            System.out.println("CLOSE STREAM");
                            endStream(msg);
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void updateRoomsAndLobby(Message msg) {
            ArrayList<Room> removedRooms = new ArrayList<>();
            for (Room r : listRooms) {
                if (r.getRoomMaster().getAlias().equals(msg.getSender().getAlias())) {
                    JOptionPane.showMessageDialog(frmMain, "Room " + r.getRoomName() + " has been disbanded due to room master logging out");
                    removedRooms.add(r);
                }
            }
            listRooms.removeAll(removedRooms);
            updateListRoom(listRooms);
            listLobby = msg.getUserList();
            frmMain.updateList(0, convertUserToString(listLobby));
        }

        private void updateListRoom(ArrayList<Room> list) {
            listRooms = list;
            frmMain.updateList(1, convertRoomToString(listRooms));
        }

        private void appendMessage(Message msg) {
            String senderAlias = msg.getSender().getAlias() + ":";
            String strMsg = msg.getContent();
            String log = null;
            if (msg.getSender().getId() == user.getId()) {
                log = mapChatLogs.get(msg.getReceiver().getId());
            } else {
                log = mapChatLogs.get(msg.getSender().getId());
            }
            log = new StringBuilder(log != null ? log : "").append(senderAlias).append(NEW_LINE).append(strMsg).append(NEW_LINE).toString();

            if (msg.getSender().getId() == user.getId()) {
                mapChatLogs.put(msg.getReceiver().getId(), log);
                frmMain.setContent(log);
            } else {
                mapChatLogs.put(msg.getSender().getId(), log);
                if (frmMain.getCurrentReceiver().equals(msg.getSender().getAlias())) {
                    frmMain.setContent(log);
                } else {
                    MessageFrm frmMsg = new MessageFrm(msg.getSender().getAlias(), strMsg);
                    frmMsg.setActionListeners(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            initUserConversation(msg.getSender().getAlias());
                            frmMsg.dispose();
                        }
                    });
                    frmMsg.setVisible(true);
                }
            }

        }

        private void appendMessageRoom(Message msg) {
            String senderAlias = msg.getSender().getAlias() + ":";
            String strMsg = msg.getContent();
            String log = null;
            log = mapChatLogs.get(msg.getRoom().getId());
            System.out.println(msg.getRoom().getId());
            log = new StringBuilder(log != null ? log : "").append(senderAlias).append(NEW_LINE).append(strMsg).append(NEW_LINE).toString();
            mapChatLogs.put(msg.getRoom().getId(), log);
            if (frmMain.getCurrentReceiver().equals(msg.getRoom().getRoomName())) {
                frmMain.setContent(log);
            } else {
                MessageFrm frmMsg = new MessageFrm(msg.getRoom().getRoomName(), strMsg);
                frmMsg.setActionListeners(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        initRoomConversation(msg.getRoom().getRoomName());
                        frmMsg.dispose();
                    }
                });
                frmMsg.setVisible(true);
            }
        }

        private void responseRequest(Message msg) {
            try {
                String strReq = null;
                strReq = "Do you want to let " + msg.getSender().getAlias() + " join the room?";
                msg.setType(Message.SEND_RESPONSE_ROOM);
                int accept = JOptionPane.showConfirmDialog(frmMain, strReq);
                msg.setReceiver(msg.getSender());
                msg.setSender(user);
                msg.setAccept((accept == 0));
                sendMessageToServer(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void responseStreamRequest(Message msg) {
            try {
                String strReq = "Do you want to receive a call from " + msg.getSender().getAlias() + "?";
                msg.setType(Message.SEND_RESPONSE_USER);
                int accept = JOptionPane.showConfirmDialog(frmMain, strReq);
                if (accept == 0) {
                    frmStream = new StreamFrm();
                    userStream = msg.getSender();
                    videoSocket = new DatagramSocket();
                    audioSocket = new DatagramSocket();
                    videoGrabber = new VideoClientGrabberThread(videoSocket);
                    videoGrabber.start();
                    videoListener = new VideoClientListenerThread(videoSocket, frmStream);
                    videoListener.start();
                    audioClient = new AudioClient(audioSocket);
                    frmStream.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent we) {
                            closeWindow();
                        }
                    });
                    frmStream.setVisible(true);
                    msg.setVideoAddressSender(msg.getVideoAddressSender());
                    msg.setVideoAddressReceiver(new Pair<>(InetAddress.getLocalHost().getHostAddress(), videoSocket.getLocalPort()));
                    msg.setAddressAudioSender(msg.getAddressAudioSender());
                    msg.setAddressAudioReceiver(new Pair<>(InetAddress.getLocalHost().getHostAddress(), audioSocket.getLocalPort()));
                }
                User prevSender = msg.getSender();
                msg.setSender(user);
                msg.setReceiver(prevSender);
                msg.setAccept(accept == 0);
                sendMessageToServer(msg);
//                    new VideoClientListenerThread(streamSocket).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void handleResponseRoom(Message msg) {
            if (msg.isAccept()) {
                JOptionPane.showMessageDialog(frmMain, "Your request has been accepted");
                updateListRoom(msg.getListRooms());
            } else {
                JOptionPane.showMessageDialog(frmMain, "sorry your request has been denied");
            }
        }

        private void handleResponseStream(Message msg) {
            if (msg.isAccept()) {
                userStream = msg.getSender();
                frmStream = new StreamFrm();
                frmStream.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we) {
                        closeWindow();
                    }
                });
                videoListener = new VideoClientListenerThread(videoSocket, frmStream);
                videoListener.start();
                videoGrabber = new VideoClientGrabberThread(videoSocket);
                videoGrabber.start();
                audioClient = new AudioClient(audioSocket);
                frmStream.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frmMain, "Request has been denied");
            }
        }

        private void closeWindow() {
            try {
                Message msg = new Message();
                msg.setSender(user);
                msg.setReceiver(userStream);
                msg.setType(Message.CLOSE_STREAM);
                msg.setVideoAddressSender(new Pair<>(InetAddress.getLocalHost().getHostAddress(), videoSocket.getLocalPort()));
                msg.setAddressAudioSender(new Pair<>(InetAddress.getLocalHost().getHostAddress(), audioSocket.getLocalPort()));
                sendMessageToServer(msg);
                videoGrabber.closeVideo();
                videoListener.closeVideo();
                audioClient.closeThreads();
                videoGrabber = null;
                videoListener = null;
                audioClient = null;
                frmStream.dispose();
                frmStream = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void endStream(Message msg) {
            JOptionPane.showMessageDialog(frmStream, "The other user has end the stream");
            userStream = null;
            videoGrabber.closeVideo();
            videoListener.closeVideo();
            audioClient.closeThreads();
            videoGrabber = null;
            videoListener = null;
            audioClient = null;
            frmStream.dispose();
            frmStream = null;
        }
    }
}
