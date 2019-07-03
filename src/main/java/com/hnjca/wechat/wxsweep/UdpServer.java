package com.hnjca.wechat.wxsweep;

/**
 * Description:
 * User: Ellison
 * Date: 2019-06-17
 * Time: 16:23
 * Modified:
 */

import com.hnjca.wechat.controller.WeixinController;

import org.springframework.transaction.annotation.Transactional;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;
@Transactional
public class UdpServer{
    public static void Monitor() throws Exception {
        DatagramSocket  server = new DatagramSocket(8080);
        byte[] recvBuf = new byte[100];
        DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);

        while (true) {

            //server.setSoTimeout(3000);
            server.receive(recvPacket);

            byte[] recvStr = recvPacket.getData();

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
            int[] y= stringConvertInt(mac);
            for(int i=0;i<18;i++){
                String s=y[i]+"";
                int numInt=parseInt(s,10);//转十进制
                sum+=   Math.floorMod(numInt, 10)+"";//取模,付款码
            }
            System.out.println("付款码："+sum+">>>金额"+decMoney);
            String res= WeixinController.scanCode(sum,decMoney+"");//调用微信扫码支付

/*
            String url = "http://localhost:8080/api/weixin/scanCode";
            String res = MyRequestUtil.sendPost(url,"auth_code="+sum+"&money="+decMoney);*/
            System.out.println("结果:"+res);


           if(res=="SUCCESS"|| res.equals("SUCCESS")){ //微信扫码支付成功返回
                byte[] xMoney= toLH(intMoney);//将金额转为小端
                String sMoney=bytesToHexString(xMoney);
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
                System.out.println("校验码1："+intJiaoyan);
                byte[] xJiaoyan= toLH(intJiaoyan);//将校验码转为小端
                System.out.println("校验码2："+xJiaoyan);
                String sJiaoyan=bytesToHexString(xJiaoyan);
                System.out.println("校验码3："+sJiaoyan);
                String jJiaoyan= sJiaoyan.substring(0,4);//数据截取校验码
                System.out.println("校验码4："+jJiaoyan);
                //指令+金额+CRC校验
                String crcMaJiaoYan=jJiaoyan+sMoney+dInstrctions;
                System.out.println("检验返回数据："+crcMaJiaoYan);
                byte[] codeMa=toByteArray(crcMaJiaoYan);//返回一串十六进制码
                String codeString=bytesToHexString(codeMa);
                System.out.println("SHUCHU："+codeString);
                String ipone=recvPacket.getAddress().toString();//获取ip地址
                String ip= ipone.substring(1);//格式化ip地址
                UDPPortInfos.UdpportInfos(ip,recvPacket.getPort(),codeMa);

            }else{
               //付款失败
               byte[] xMoney= toLH(intMoney);//将金额转为小端
               String sMoney=bytesToHexString(xMoney);
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
               String sJiaoyan=bytesToHexString(xJiaoyan);
               String jJiaoyan= sJiaoyan.substring(0,4);//数据截取校验码
               //指令+金额+CRC校验
               String crcMaJiaoYan=jJiaoyan+sMoney+dInstrctions;
               System.out.println("检验返回数据："+crcMaJiaoYan);
               byte[] codeMa=toByteArray(crcMaJiaoYan);//返回一串十六进制码
               String codeString=bytesToHexString(codeMa);
               String ipone=recvPacket.getAddress().toString();//获取ip地址
               String ip= ipone.substring(1);//格式化ip地址
               System.out.println("ip地址："+ip+"》》》端口号:"+recvPacket.getPort());
               UDPPortInfos.UdpportInfos(ip,recvPacket.getPort(),codeMa);
           }
            //server.close();
        }
        }



       public static int[] stringConvertInt (String mac){
           int[] intArr = new int[0];
           if (isNull(mac)) {
               intArr = new int[0];
           } else {
               String[] valueArr = mac.split(",");
               intArr = new int[valueArr.length];
               for (int i = 0; i < valueArr.length; i++) {
                   intArr[i] = Integer.parseInt(valueArr[i]);
               }
           }
           return intArr;
       }

//处理byte
    public static byte[] str2bytearray(String str){
        int length=str.length();
        int arrlength=length>>1;
        if ((length & 1)==1){
            arrlength++;
        }
        byte[] ret=new byte[arrlength];
        int i=0,j=0;
        char ch0,ch1;
        if ((length & 1)==1){
            ch1=str.charAt(i++);
            if (ch1<='9' && ch1>='0'){
                ch1-='0';
            }else if(ch1>='A' && ch1<='F'){
                ch1-=('A'-10);
            }
            ret[j++]=(byte)ch1;
        }
        for(;i<length;i+=2,j++){
            ch0=str.charAt(i);
            ch1=str.charAt(i+1);
            if (ch0<='9' && ch0>='0'){
                ch0-='0';
            }else if(ch0>='A' && ch0<='F'){
                ch0-=('A'-10);
            }
            if (ch1<='9' && ch1>='0'){
                ch1-='0';
            }else if(ch1>='A' && ch1<='F'){
                ch1-=('A'-10);
            }
            ret[j]=(byte)((ch0<<4)|ch1);
        }
        return ret;
    }


//Sting 转byte[]
    public static byte[] toByteArray(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }



    //将整数按照小端存放，低字节出访低位
    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


}