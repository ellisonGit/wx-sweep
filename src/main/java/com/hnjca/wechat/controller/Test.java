package com.hnjca.wechat.controller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;


/**
 * Description:
 * User: Ellison
 * Date: 2019-05-23
 * Time: 14:33
 * Modified:
 */
public class Test {

    public static class Server {
        public void server(){
            try {
                System.out.println("start===================start");
                DatagramSocket socket = new DatagramSocket(9002);
                while(true){
                    byte[] buf = new byte[2048];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    byte[] data = packet.getData();
                    String msg = new String(data, 0, packet.getLength());
                    System.out.println(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        public static void main(String[] args) {

        }
    }
}







    

