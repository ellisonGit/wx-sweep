package com.hnjca.wechat.wxsweep;

/**
 * Description:
 * User: Ellison
 * Date: 2019-06-17
 * Time: 16:16
 * Modified:
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

 public class UDPPortInfos  {
    public static void UdpportInfos(String ip,int port, byte[] codeMa)throws IOException{
        DatagramSocket client = new DatagramSocket();
        InetAddress addr = InetAddress.getByName(ip);
        //int port = 10068;
        byte[] sendBuf;
        while(true) {
           /* Scanner sc = new Scanner(System.in);
            System.out.println("请输要发送的内容：");
            String str  = sc.nextLine();*/
            sendBuf = codeMa;
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, addr, port);
            client.send(sendPacket);
         break;
        }
    }
}