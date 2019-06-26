package com.hnjca.wechat.controller;


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
        String no = "6E600400000029D2000001000000905824772000000000020B17000A0091AE";//返回一串十六进制码
        byte[] arr=str2bytearray("6E600400000029D2000001000000905824772000000000020B17000A0091AE");
        for (byte b:arr){
            System.out.print(Integer.toHexString(b & 0xff)+' ');
        }
        System.out.println();

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







    

