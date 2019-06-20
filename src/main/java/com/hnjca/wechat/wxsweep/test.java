package com.hnjca.wechat.wxsweep;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Description:
 * User: Ellison
 * Date: 2019-06-18
 * Time: 9:19
 * Modified:
 */
public class test {
    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};



    public static void main(String[] args) throws UnsupportedEncodingException {
        byte[] b = new byte[]{'d', (byte) 0xff, -1, (byte) 255, (byte) 0x80, (byte) 128, -128};

        //将byte[]数组用字符串表示
        System.out.println(Arrays.toString(b));  //这个是byte[]中实际存的值

        //打印出byte[]的十六进制字符串
        System.out.println(bytesToHexString(b)); //这个存的是实际的每个字节的二级制表现形式

        //打印出byte[]的ascii的字符串
        System.out.println(new String(b).getBytes("utf-8"));  //ascii范围是如果不是0-127的字段，会转为乱码



    }


    /**
     * 将字节数字转换为16进制字符串
     *
     * @param bytes
     * @return
     */
    private static String bytesToHexString(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }

        return new String(buf);
    }

    /**
     * 将十六进制字符串转换字节数组
     *
     * @param str
     * @return
     */
    private static byte[] hexStringToBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }
}


