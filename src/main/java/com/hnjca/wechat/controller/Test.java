package com.hnjca.wechat.controller;


import java.util.*;

/**
 * Description:
 * User: YangYong
 * Date: 2019-04-23
 * Time: 14:33
 * Modified:
 */
public class Test {
    public static void main(String[] args) {
        int sumd = 0;
        int sumx = 0;
        for (int i = 0; i < 1; i++) {
            Random ra = new Random();
            int x = ra.nextInt(6) + 1;
            int y = ra.nextInt(6) + 1;
            int z = ra.nextInt(6) + 1;
            int sum = x + y + z;
            System.out.println("三个的值分别是" + x + "," + y + "," + z);
            if (sum >= 3 && sum <= 9) {
                sumx++;
                System.out.println("三个和为：" + sum + "    小");
            }
            if (sum > 9 && sum <= 18) {
                sumd++;
                System.out.println("三个和为：" + sum + "    大");

            }
        }
        System.out.println("大总数：" + sumd + "小总数：" + sumx);
    }
      /*  Scanner sc=new Scanner(System.in);
        System.out.println("请输入第一个数");
        int x=sc.nextInt();
        System.out.println("请输入第一个数");
        int y=sc.nextInt();
        System.out.println("请输入第一个数");
        int z=sc.nextInt();
        int sum=x+y+z;
        if((x<1||x>6)||(y<1||y>6)||(z<1||z>6)){
            System.out.println("输入的值有误");
        }else  if(sum>3&&sum<=9){
            System.out.print("三个骰子的和为："+sum+"  小");
        }
        else  if(sum>9&&sum<=18){
            System.out.print("三个骰子的和为："+sum+"  大");
        }
    }*/
}







    

