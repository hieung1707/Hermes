/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import udp.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author ASUS
 */
public class AudioClient {

    private final int BUFFER_SIZE = 1024;
    private final String IP = "localhost";
    private final int PORT = 501;

    boolean running = false;
    ByteArrayOutputStream byteArrayOutputStream;
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    AudioInputStream audioInputStream;
    BufferedOutputStream out = null;
    BufferedInputStream in = null;
    Socket sock = null;
    SourceDataLine sourceDataLine;
    DatagramSocket socket;
    
    private CaptureThread captureThread;
    private PlayThread playThread;
    
    public AudioClient(DatagramSocket socket) {
        init(socket);
    }

    private void init(DatagramSocket socket) {
        try {
            audioFormat = getAudioFormat();

            this.socket = socket;
            
            captureThread = new CaptureThread();
            captureThread.start();

            playThread = new PlayThread();
            playThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    
    //format of audio
    private AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;

        int sampleSizeInBits = 8;

        int channels = 1;

        boolean signed = true;

        boolean bigEndian = true;

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
                bigEndian);
    }
    
    public void closeThreads() {
        captureThread.closeThread();
        playThread.closeThread();
    }

    class CaptureThread extends Thread {

        byte tempBuffer[] = new byte[BUFFER_SIZE];

        private void initCaptureThread() {
            try {
                Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo(); //get available ports
                Mixer mixer = AudioSystem.getMixer(mixerInfo[2]);
                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
                targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);

                targetDataLine.open(audioFormat);
                targetDataLine.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        //capture sound and transfer to server
        @Override
        public void run() {
            initCaptureThread();
            running = true;
            try {
                while (running) {
                    //convert audio data to byte array
                    int cnt = targetDataLine.read(tempBuffer, 0,
                            tempBuffer.length);

                    sendBytes(tempBuffer);
                }
                targetDataLine.drain();
                targetDataLine.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        
        //send byte data to server
        private void sendBytes(byte[] data) {
            try {
                InetAddress address = InetAddress.getByName(IP);
                DatagramPacket packet = new DatagramPacket(data, data.length, address, PORT);
                socket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private void closeThread() {
            running = false;
        }
    }

    //listen to the sound data from server
    class PlayThread extends Thread {

        byte tempBuffer[] = new byte[BUFFER_SIZE];

        private void initPlayThread() {
            try {
                DataLine.Info dataLineInfo1 = new DataLine.Info(
                    SourceDataLine.class, audioFormat);
                sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo1);
                sourceDataLine.open(audioFormat);
                sourceDataLine.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            try {
                initPlayThread();
                while (running) {
                    tempBuffer = getBytes();
                    if (tempBuffer != null)
                        sourceDataLine.write(tempBuffer, 0, BUFFER_SIZE);
                }
                sourceDataLine.drain();
                sourceDataLine.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private byte[] getBytes() {
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                DatagramPacket rcvPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(rcvPacket);
                System.out.println(rcvPacket.getPort());
                return rcvPacket.getData();
            } catch (Exception e) {
                e.printStackTrace();
            }                
            return null;
        }
        
        private void closeThread() {
            running = false;
        }
    }
}
