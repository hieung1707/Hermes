/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

/**
 *
 * @author ASUS
 */
public class AudioListener {
    private final int PORT = 10000;
    private final int BUFFER_SIZE = 1024;
    private final AudioFormat FORMAT = getFormat();
    private final DatagramSocket socket = getSocket();
    private final DataLine.Info INFO = new DataLine.Info(TargetDataLine.class, FORMAT);

    
    private boolean running;
    
    private DatagramSocket getSocket() {
        try {
            return new DatagramSocket(PORT);
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
    
    public void captureAudio() {
        try {
            while (true) {
                byte[] receivedData = new byte[(int) FORMAT.getSampleRate() * FORMAT.getFrameSize()];
                DatagramPacket packet = new DatagramPacket(receivedData, BUFFER_SIZE);
                socket.receive(packet);
                playAudio(packet.getData());
            }
        } catch (Exception e) {
        }
    }
    
    public void playAudio(byte[] audio) {
        try {
            InputStream input = new ByteArrayInputStream(audio);
            final AudioFormat f = getFormat();
            final AudioInputStream ais = new AudioInputStream(input, f, audio.length / f.getFrameSize());
            DataLine.Info fo = new DataLine.Info(SourceDataLine.class, f);
            final SourceDataLine l = (SourceDataLine) AudioSystem.getLine(fo);
            l.open(f);
            l.start();
            Runnable runner = new Runnable() {
                int bufferSize = (int) f.getSampleRate() * f.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                @Override
                public void run() {
                    try {
                        running = true;
                        int count;
                        while (running && (count = ais.read(buffer, 0, buffer.length)) != -1) {
                            if (count > 0) {
                                l.write(buffer, 0, count);
                            }
                        }
                        l.drain();
                        l.close();
                    } catch (IOException e) {
                        System.err.println("I/O problems: " + e);
                    }
                }
            };
            Thread playThread = new Thread(runner);
            playThread.start();
        } catch (Exception e) {
            System.err.println("Line unavailable: " + e);
        }
    }
    
    public static void main(String[] args) {
        new AudioListener().captureAudio();
    }
}
