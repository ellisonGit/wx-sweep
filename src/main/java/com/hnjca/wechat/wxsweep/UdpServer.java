package com.hnjca.wechat.wxsweep;

/**
 * Description:
 * User: Ellison
 * Date: 2019-06-17
 * Time: 16:23
 * Modified:
 */

import com.hnjca.wechat.controller.WeixinController;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;

public class UdpServer{
    public static void Monitor() throws Exception {
        DatagramSocket  server = new DatagramSocket(8080);
        byte[] recvBuf = new byte[100];
        DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
        while (true) {
            server.receive(recvPacket);
            byte[] recvStr = recvPacket.getData();
            String a=  BytesHexStrTranslate.bytesToHexFun1(recvStr);
            String b= a.substring(0,48);//数据截取
            System.out.println("收到:" + a);
            String code= a.substring(18,54);//数据截取付款扫码
            System.out.println("code:"+code);
            String money= a.substring(59,62);//数据截取金钱 单位为分
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

            if(res=="SUCCESS"|| res.equals("SUCCESS")){//微信扫码支付返回
                byte[] codeMa=str2bytearray("91B417000000741300000A000000226480203900000011020D1700010031AE");//返回一串十六进制码
                String ipone=recvPacket.getAddress().toString();//获取ip地址
                String ip= ipone.substring(1);//格式化ip地址
                UDPPortInfos.UdpportInfos(ip,recvPacket.getPort(),codeMa);
            }
            //server.close();
        }
           /* int  x = Integer.parseInt(code,16);
            System.out.println("codetwo:"+x);
            byte[] d=BytesHexStrTranslate.toBytes(b);//将16进制字符串转换为byte[]
            System.out.println("截取到的16进制数据:"+b);
           int c= CRC16.CalcCRC16(d,d.length);*/
           /* int crc = CRC16.calcCrc16(d);
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

}