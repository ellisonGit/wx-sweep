package com.hnjca.wechat.wxsweep;

/**
 * Description:
 * User: Ellison
 * Date: 2019-06-17
 * Time: 16:23
 * Modified:
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

class UdpServer{
    public static void main(String[] args)throws IOException{
        DatagramSocket  server = new DatagramSocket(8002);
        byte[] recvBuf = new byte[100];
        DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
        while (true) {
            server.receive(recvPacket);
            String recvStr = new String(recvPacket.getData(), 0,
                    recvPacket.getLength());
            System.out.println("收到:" + recvStr.getBytes("utf-8"));
            System.out.println("getAddress:"+recvPacket.getAddress()+"getOffset:"+recvPacket.getOffset()+
                    " getPort:"+recvPacket.getPort()+"getSocketAddress:"+recvPacket.getSocketAddress());
           System.out.println("ellison>>>>>>>>>>>"+ recvPacket.getData());
            if(recvStr.endsWith("q")|| recvStr.endsWith("quit")){
                break;
            }
        }
        server.close();
    }
}