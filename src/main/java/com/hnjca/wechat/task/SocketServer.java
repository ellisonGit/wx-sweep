package com.hnjca.wechat.task;



import java.io.IOException;
import java.net.*;

/**
 * Description:
 * User: Ellison
 * Date: 2019-07-02
 * Time: 15:38
 * Modified:
 */
public class SocketServer {
    public static void ellison() {
        try {
            DatagramSocket serverSocket = null;//创建绑定到特定端口的服务器Socket。
            try {
                serverSocket = new DatagramSocket(8080);

            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] recvBuf = new byte[100];
            DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
            Socket socket = null;//需要接收的客户端Socket
            int count = 0;//记录客户端数量
            System.out.println("服务器启动");
            //定义一个死循环，不停的接收客户端连接
            while (true) {

                serverSocket.receive(recvPacket);//侦听并接受到此套接字的连接
               // InetAddress inetAddress=socket.getInetAddress();//获取客户端的连接
                byte[] recvStr = recvPacket.getData();
                String ip=recvPacket.getAddress().toString();
                int port=recvPacket.getPort();
                ServerThread thread=new ServerThread(recvStr,ip,port,recvPacket);//自己创建的线程类
                thread.start();//启动线程
                count++;//如果正确建立连接
                System.out.println("客户端数量：" + count);//打印客户端数量
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
