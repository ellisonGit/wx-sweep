package com.hnjca.wechat.task;



import com.hnjca.wechat.controller.WeixinController;
import com.hnjca.wechat.wxsweep.BytesHexStrTranslate;
import com.hnjca.wechat.wxsweep.CRCjiaoyan;
import com.hnjca.wechat.wxsweep.UDPPortInfos;
import com.hnjca.wechat.wxsweep.UdpServer;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;

import static com.hnjca.wechat.wxsweep.UdpServer.toLH;
import static java.lang.Integer.parseInt;

/**
 * Description:
 * User: Ellison
 * Date: 2019-07-02
 * Time: 15:39
 * Modified:
 */
public class ServerThread extends Thread {
    Socket socket = null;
    InetAddress inetAddress=null;//接收客户端的连接
    byte[] recvStr=null;
    DatagramPacket recvPacket=null;
    String ip=null;
    int port;

    public ServerThread(byte[] data, String ip,int port, DatagramPacket recvPacket) {
        this.recvStr=data;
        this.ip =ip;
        this.port=port;
        this.recvPacket =recvPacket;

    }

    @Override
    public void run() {
        InputStream inputStream = null;//字节输入流
        InputStreamReader inputStreamReader = null;//将一个字节流中的字节解码成字符
        BufferedReader bufferedReader = null;//为输入流添加缓冲
        OutputStream outputStream = null;//字节输出流
        OutputStreamWriter writer = null;//将写入的字符编码成字节后写入一个字节流
        try {
           // inputStream = socket.getInputStream();
           // inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
           // bufferedReader = new BufferedReader(inputStreamReader);
            String info = null;//临时

            //循环读取客户端信息
            while (true) {
                //获取客户端的ip地址及发送数据
              //  System.out.println("服务器端接收："+"{'from_client':'"+socket.getInetAddress().getHostAddress()+"','data':'"+info+"'}");

                String a=  BytesHexStrTranslate.bytesToHexFun1(recvStr);
                String equipmentNumber= a.substring(6,10);//设备机号数据截取
                System.out.println("设备号："+equipmentNumber);
                String b= a.substring(0,48);//数据截取
                System.out.println("收到:" + a);
                String code= a.substring(18,54);//数据截取付款扫码
                System.out.println("code:"+code);
                String money= a.substring(54,62);//数据截取金钱 单位为分
                // int intMoney = Integer.parseInt(money);//将金额字符转int
                int intMoney = Integer.parseInt(money,16);
                System.out.println("money:"+money);
                long decMoney = Long.parseLong(money, 16);
                String sum="";
                String mac = code;
                String regex = "(.{2})";
                mac = mac.replaceAll(regex,"$1,");
                mac = mac.substring(0,mac.length() - 1);
                System.out.println("code格式化："+mac);
                //付款扫码
                int[] y= UdpServer.stringConvertInt(mac);
                for(int i=0;i<18;i++){
                    String s=y[i]+"";
                    int numInt=parseInt(s,10);//转十进制
                    sum+=   Math.floorMod(numInt, 10)+"";//取模,付款码
                }
                System.out.println("付款码："+sum+">>>金额"+decMoney);
                String res= null;//调用微信扫码支付
                try {
                    res = WeixinController.scanCode(sum,decMoney+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                System.out.println("结果:"+res);


                if(res=="SUCCESS"|| res.equals("SUCCESS")){ //微信扫码支付成功返回
                    byte[] xMoney=UdpServer.toLH(intMoney);//将金额转为小端
                    String sMoney=UdpServer.bytesToHexString(xMoney);
                    // String zmoney=  String.format("%08x", decMoney);//金额转需要使用4字节表示
                    String instructions="AE3100"+equipmentNumber+"070D0211";//指令(命令AE+命令码+扣款结果)

                    //11：成功，020D：命令码，07：指令长度，equipmentNumber：设备机号数，31:协议控制
                    String dInstrctions="11020D0700"+equipmentNumber+"31AE";//倒叙指令（扣款结果+命令码+AE）
                    String crcMa=instructions+money;//指令+金额
                    System.out.println("指令+金额:"+crcMa);
                    byte[] d=BytesHexStrTranslate.toBytes(crcMa);//将16进制字符串转换为byte[]
                    int intnum= CRCjiaoyan.CRC_XModem(d);//CRC校验
                    String jiaoyan=  String.format("%04x", intnum);//需要使用2字节表示
                    int intJiaoyan = Integer.parseInt(jiaoyan,16);
                    System.out.println("校验码："+intJiaoyan);
                    byte[] xJiaoyan= toLH(intJiaoyan);//将校验码转为小端
                    String sJiaoyan=UdpServer.bytesToHexString(xJiaoyan);
                    String jJiaoyan= sJiaoyan.substring(0,4);//数据截取校验码
                    //指令+金额+CRC校验
                    String crcMaJiaoYan=jJiaoyan+sMoney+dInstrctions;
                    System.out.println("检验返回数据："+crcMaJiaoYan);
                    byte[] codeMa=UdpServer.toByteArray(crcMaJiaoYan);//返回一串十六进制码
                    String codeString=UdpServer.bytesToHexString(codeMa);
                    System.out.println("SHUCHU1："+codeString);
                    String ipone=ip;//获取ip地址
                    String ip= ipone.substring(1);//格式化ip地址
                    System.out.println("ip地址："+ip+"》》》端口号1:"+port);
                    UDPPortInfos.UdpportInfos(ip,port,codeMa);
                }else{
                    //付款失败
                    byte[] xMoney= UdpServer.toLH(intMoney);//将金额转为小端
                    String sMoney=UdpServer.bytesToHexString(xMoney);
                    // String zmoney=  String.format("%08x", decMoney);//金额转需要使用4字节表示
                    String instructions="AE3100"+equipmentNumber+"070D0210";//指令(命令AE+命令码+扣款结果)

                    //11：失败，020D：命令码，07：指令长度，equipmentNumber：设备机号数，31:协议控制
                    String dInstrctions="10020D0700"+equipmentNumber+"31AE";//倒叙指令（扣款结果+命令码+AE）
                    String crcMa=instructions+money;//指令+金额
                    byte[] d=BytesHexStrTranslate.toBytes(crcMa);//将16进制字符串转换为byte[]
                    int intnum= CRCjiaoyan.CRC_XModem(d);//CRC校验
                    String jiaoyan=  String.format("%04x", intnum);//需要使用2字节表示
                    int intJiaoyan = Integer.parseInt(jiaoyan,16);
                    byte[] xJiaoyan= toLH(intJiaoyan);//将校验码转为小端
                    String sJiaoyan=UdpServer.bytesToHexString(xJiaoyan);
                    String jJiaoyan= sJiaoyan.substring(0,4);//数据截取校验码
                    //指令+金额+CRC校验
                    String crcMaJiaoYan=jJiaoyan+sMoney+dInstrctions;
                    System.out.println("检验返回数据："+crcMaJiaoYan);
                    byte[] codeMa=UdpServer.toByteArray(crcMaJiaoYan);//返回一串十六进制码
                   /* String codeString=UdpServer.bytesToHexString(codeMa);
                    System.out.println("SHUCHU2："+codeString);*/
                    String ipone=ip;//获取ip地址
                    System.out.println("SHUCHU3："+ipone);

                    String ip= ipone.substring(1);//格式化ip地址
                    System.out.println("ip地址："+ip+"》》》端口号2:"+port);
                    UDPPortInfos.UdpportInfos(ip,port,codeMa);
                }
                break;
            }




            //响应客户端请求
            //outputStream = socket.getOutputStream();
          //  writer = new OutputStreamWriter(outputStream, "UTF-8");
           // writer.write("{'to_client':'"+inetAddress.getHostAddress()+"','data':'我是服务器数据'}");
           // writer.flush();//清空缓冲区数据
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                if (writer != null) {
                    writer.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
