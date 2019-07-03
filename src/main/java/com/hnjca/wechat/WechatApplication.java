package com.hnjca.wechat;


import com.hnjca.wechat.controller.Test;
import com.hnjca.wechat.task.SocketServer;
import com.hnjca.wechat.wxsweep.UdpServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@MapperScan("com.hnjca.wechat.dao")
@EnableScheduling//启动定时任务配置
@SpringBootApplication
public class WechatApplication {

    public static void main(String[] args)  {
        SpringApplication.run(WechatApplication.class, args);
        SocketServer.ellison();
       // UdpServer.Monitor();
    }



}
