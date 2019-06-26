package com.hnjca.wechat.wxsweep;

/**
 * Description:
 * User: Ellison
 * Date: 2019-06-17
 * Time: 16:23
 * Modified:
 */

import com.hnjca.wechat.controller.WeixinController;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;

public class UdpServertest {
    public static void  main(String[] args) throws Exception {
        DatagramSocket  server = new DatagramSocket(8002);
        byte[] recvBuf = new byte[100];
        DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
        while (true) {
            server.receive(recvPacket);
            byte[] recvStr = recvPacket.getData();
            String a=  BytesHexStrTranslate.bytesToHexFun1(recvStr);
            String b= a.substring(0,56);//数据截取
            System.out.println("收到:" + a);


          //  int  x = Integer.parseInt(code,16);
            //System.out.println("codetwo:"+x);
            byte[] d=BytesHexStrTranslate.toBytes(b);//将16进制字符串转换为byte[]
            System.out.println("截取到的16进制数据:"+b);
            System.out.println("byte[]数据:"+d);

            int intnum= CRCjiaoyan.CRC_XModem(d);
          String liu=  String.format("%04x", intnum);//需要使用2字节表示
            System.out.println("输出："+intnum);
            System.out.println("输出2："+liu);
          /* int c= CRC16.CalcCRC16(d,d.length);
       int crc = CRC16.calcCrc16(d);
              BytesHexStrTranslate.byteToHex(crc);
            System.out.println("crc16::"+String.format("0x%04x", crc));
            System.out.println("crc162,:"+ BytesHexStrTranslate.byteToHex(crc));*/

          /* System.out.println("getAddress:"+recvPacket.getAddress()+"getOffset:"+recvPacket.getOffset()+
                    " getPort:"+recvPacket.getPort()+"getSocketAddress:"+recvPacket.getSocketAddress());
           System.out.println("ellison>>>>>>>>>>>"+ recvPacket.getData());

           int crc = CRC16.calcCrc16( recvStr.getBytes().clone() );
            System.out.println(String.format("0x%04x", crc));
            if(recvStr.endsWith("q")|| recvStr.endsWith("quit")){
                break;*/
            server.close();
            }



       }

}