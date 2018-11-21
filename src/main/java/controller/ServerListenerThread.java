/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;
import model.Message;
import model.Room;
import model.User;

/**
 *
 * @author ASUS
 */
public class ServerListenerThread extends Thread {

    private boolean running;
    private ObjectInputStream ois;
    private HashMap<User, String> mapUsers;
    private HashMap<String, ObjectOutputStream> mapOos;
    private HashMap<Integer, Room> mapRooms;
    private VideoServerCtr videoServer;
    private AudioServerCtr audioServer;

    public ServerListenerThread(ObjectInputStream ois, HashMap<User, String> mapUsers, HashMap<String, ObjectOutputStream> listOos, HashMap<Integer, Room> listRooms, VideoServerCtr videoServer, AudioServerCtr audioServer) {
        this.ois = ois;
        this.mapOos = listOos;
        this.mapUsers = mapUsers;
        this.mapRooms = listRooms;
        this.videoServer = videoServer;
        this.audioServer = audioServer;
    }

    @Override
    public void run() {
        try {
            running = true;
            Message msg = null;
            while (running) {
                if ((msg = (Message) ois.readObject()) != null) {
                    switch (msg.getType()) {
                        case Message.SERVER_REGISTRATION:
                            addUser(msg);
                            break;
                        case Message.SEND_MESSAGE_USER:
                            sendMsgToUser(msg);
                            break;
                        case Message.SEND_REQUEST_USER:
                            System.out.println("REQUEST " + msg.getVideoAddressSender().getValue());
                            sendMsgToUser(msg);
                            break;
                        case Message.SEND_RESPONSE_USER:
                            setStreamConnections(msg);
                            break;
                        case Message.SEND_MESSAGE_ROOM:
                            sendToAllMembers(msg);
                            break;
                        case Message.CREATE_ROOM:
                            createRoom(msg);
                            break;
                        case Message.SEND_REQUEST_ROOM:
                            sendRequestToKey(msg);
                            break;
                        case Message.SEND_RESPONSE_ROOM:
                            sendRoomResponse(msg);
                            break;
                        case Message.SERVER_LOG_OUT:
                            logout(msg);
                            break;
                        case Message.FIND_ROOM:
                            sendRoomList(msg);
                            break;
                        case Message.CLOSE_STREAM:
                            System.out.println("CLOSE STREAM");
                            closeStream(msg);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addUser(Message msg) {
        try {
            mapUsers.put(msg.getSender(), msg.getSender().getIp() + "|" + msg.getSender().getPort());
            ArrayList<User> listUsers = new ArrayList<>(mapUsers.keySet());
            msg.setUserList(listUsers);
            for (String key : mapOos.keySet()) {
                ObjectOutputStream oos = mapOos.get(key);
                oos.writeObject(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMsgToUser(Message msg) {
        try {
            String identity = msg.getReceiver().getIp() + "|" + msg.getReceiver().getPort();
            if (msg.getType() == Message.SEND_MESSAGE_USER) {
                mapOos.get(mapUsers.get(msg.getSender())).writeObject(msg);
            }
            ObjectOutputStream oos = mapOos.get(identity);
            oos.writeObject(msg);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout(Message msg) {
        try {
            User user = msg.getSender();
            mapOos.remove(mapUsers.get(user));
            mapUsers.remove(user);
            ArrayList<User> listUsers = new ArrayList<>(mapUsers.keySet());
            msg.setUserList(listUsers);
            Room r = null;
            for (int id : mapRooms.keySet()) {
                r = mapRooms.get(id);
                ArrayList<User> members = r.getMembers();
                if (r.getRoomMaster().getAlias().equals(user.getAlias())) {
                    mapRooms.remove(r.getId());
                }
                for (User member : members) {
                    if (member.getAlias().equals(user.getAlias())) {
                        members.remove(member);
                        break;
                    }
                }
            }
            for (String key : mapOos.keySet()) {
                ObjectOutputStream oos = mapOos.get(key);
                oos.writeObject(msg);
            }
            running = false;
        } catch (Exception e) {

        }
    }

    private void createRoom(Message message) {
        if (mapRooms == null) {
            mapRooms = new HashMap<>();
        }
        mapRooms.put(message.getRoom().getId(), message.getRoom());
        getAllUserRoom(message, message.getSender());
    }

    private void getAllUserRoom(Message message, User receiver) {
        try {
            ArrayList<Room> list = new ArrayList<>();
            for (Integer id : mapRooms.keySet()) {
                Room r = mapRooms.get(id);
                if (r.getMembers().contains(message.getSender())) {
                    list.add(r);
                }
            }
            message.setListRooms(list);
            String identity = receiver.getIp() + "|" + receiver.getPort();
            mapOos.get(identity).writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRoomList(Message msg) {
        try {
            System.out.println("ROOM LIST");
            ArrayList<Room> list = new ArrayList<>(mapRooms.values());
            msg.setListRooms(list);
            String identity = msg.getSender().getIp() + "|" + msg.getSender().getPort();
            mapOos.get(identity).writeObject(msg);

        } catch (Exception e) {

        }
    }

    private void sendRequestToKey(Message msg) {
        try {
            for (Integer id : mapRooms.keySet()) {
                Room r = mapRooms.get(id);
                if (r.getRoomName().endsWith(msg.getRoom().getRoomName())) {
                    msg.setRoom(r);
                    String identity = mapUsers.get(r.getRoomMaster());
                    mapOos.get(identity).writeObject(msg);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRoomResponse(Message msg) {
        try {
            if (!msg.isAccept()) {
                String identity = msg.getReceiver().getIp() + "|" + msg.getReceiver().getPort();
                mapOos.get(identity).writeObject(msg);
            } else {
                Room r = mapRooms.get(msg.getRoom().getId());
                r.addMember(msg.getReceiver());
                mapRooms.put(r.getId(), r);
                msg.setRoom(r);
                getAllUserRoom(msg, msg.getReceiver());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendToAllMembers(Message msg) {
        try {
            ArrayList<User> members = mapRooms.get(msg.getRoom().getId()).getMembers();
            for (User u : members) {
                String identity = u.getIp() + "|" + u.getPort();
                mapOos.get(identity).writeObject(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStreamConnections(Message msg) {
        if (msg.isAccept()) {
            videoServer.addMap(msg.getVideoAddressSender(), msg.getVideoAddressReceiver());
            audioServer.addMap(msg.getAddressAudioSender(), msg.getAddressAudioReceiver());
        }
        sendMsgToUser(msg);
    }
    
    private void closeStream(Message msg) {
        videoServer.removeMaps(msg.getVideoAddressSender());
        audioServer.removeMaps(msg.getAddressAudioSender());
        sendMsgToUser(msg);
    }
}
