/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author ASUS
 */
public class AudioSenderThread {
    private final String IP = "192.168.1.35";
    private final int PORT = 10000;
    private final AudioFormat FORMAT = getFormat();
    private final DatagramSocket socket = getSocket();
    private final DataLine.Info INFO = new DataLine.Info(TargetDataLine.class, FORMAT);
    private boolean running = false;
    private TargetDataLine line;
    
    private DatagramSocket getSocket() {
        try {
            return new DatagramSocket();
        } catch (Exception e) {
        }
        return null;
    }
    
    private AudioFormat getFormat() {
        //44100 Hz, CD standard
        float sampleRate = 44100.0f;
        //sample size in bits = bit depth, is the resolution of sample
        int sampleSizeInBits = 16;
        //number of channels, equal to 2 channels stereo
        int channels = 2;
        //represent sample with value from -2^15 to 2^15
        boolean signed = true;
        //
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
    
    public AudioSenderThread() {
        try {
            line = (TargetDataLine) AudioSystem.getLine(INFO);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(AudioSenderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void captureAudio() {
        try {
            line.open(FORMAT);
            line.start();
            Runnable runner = new Runnable() {
                int bufferSize = (int) FORMAT.getSampleRate() * FORMAT.getFrameSize();
                byte[] buffer = new byte[bufferSize];

                @Override
                public void run() {
                    running = true;
                    try {
                        while (running) {
                            int count = line.read(buffer, 0, buffer.length);
                            System.out.println(count);
                            if (count > 0) {
                                sendBytes(Arrays.copyOfRange(buffer, 0, count));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread captureThread = new Thread(runner);
            captureThread.start();
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: " + e);
        }
    }
    
    private void sendBytes(byte [] data) {
        try {
            InetAddress address = InetAddress.getByName(IP);
            int bufferSize = 1024;
            byte[] buffer;
            int index = 0;
            while (index < data.length) {
                buffer = Arrays.copyOfRange(data, index, (index + bufferSize > data.length) ? data.length : index + bufferSize);
                index += bufferSize;
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
                socket.send(packet);
            }
        } catch (Exception e) {
            
        }
    }
    
    public static void main(String[] args) {
        new AudioSenderThread().captureAudio();
    }
}
