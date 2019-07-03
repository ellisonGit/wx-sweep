package com.hnjca.wechat.wxsweep;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Description:
 * User: Ellison
 * Date: 2019-07-02
 * Time: 10:54
 * Modified:
 */
public class Testsend {
    public static void send(){
        try {
            DatagramSocket socket = new DatagramSocket();
            String text = "hello world!";
            byte[] buf = text.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 9002);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void send2(){
        try {
            DatagramSocket socket = new DatagramSocket();
            StringBuilder a=new StringBuilder();
            String text = "helloellison!";
            byte[] buf = text.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 9002);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static void send3(){
        try {
            DatagramSocket socket = new DatagramSocket();
            String text = "3!";
            byte[] buf = text.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 9002);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static void main(String[] args) throws InterruptedException {
        for (int i=0;i<5;i++){
            send();

            send2();
            send3();

        }

    }
}

