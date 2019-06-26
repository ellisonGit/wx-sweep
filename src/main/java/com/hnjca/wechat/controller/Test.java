package com.hnjca.wechat.controller;


import com.hnjca.wechat.wxsweep.BytesHexStrTranslate;
import com.hnjca.wechat.wxsweep.CRCjiaoyan;
import com.sun.tools.javac.util.Convert;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;

/**
 * Description:
 * User: YangYong
 * Date: 2019-04-23
 * Time: 14:33
 * Modified:
 */
public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {

        String b="AE0D02110064e147";
        byte[] d= BytesHexStrTranslate.toBytes(b);//将16进制字符串转换为byte[]
        System.out.println("截取到的16进制数据:"+b);
        System.out.println("byte[]数据:"+d);

        int intnum= 500;
        byte[] dd= toLH(intnum);

        bytesToHexString(dd);
        System.out.println("大端："+  bytesToHexString(dd));
        String liu=  String.format("%08x", intnum);//需要使用2字节表示


        System.out.println("输出2："+liu);
        System.out.println("输出3："+  reverseString3(b));

     /*   String sum="";
        String mac = "313334353436383639313337313038303936";
        String regex = "(.{2})";
        mac = mac.replaceAll(regex,"$1,");
        mac = mac.substring(0,mac.length() - 1);
        System.out.println("123"+mac);

        int[] y= stringConvertInt(mac);
        for(int i=0;i<18;i++){
            String a=y[i]+"";
            int numInt=parseInt(a,10);
            sum+=   Math.floorMod(numInt, 10)+"";//取模
        }
          System.out.println(sum);
    }*/



   /* public static int[] stringConvertInt (String mac){
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
    }*/
    }
    public static byte[] toLH(int n) { byte[] b = new byte[4]; b[0] = (byte) (n & 0xff); b[1] = (byte) (n >> 8 & 0xff); b[2] = (byte) (n >> 16 & 0xff); b[3] = (byte) (n >> 24 & 0xff); return b; }

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

    public static byte[] toHH(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    public static byte[] changeBytes(byte[] a) {
        byte[] b = new byte[a.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = a[b.length - i - 1];
        }
        return b;
    }

    private static String reverseString3(String str) {

        StringBuffer buffer = new StringBuffer(str);
        return buffer.reverse().toString();

    }
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







    

