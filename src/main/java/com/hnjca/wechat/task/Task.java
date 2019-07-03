package com.hnjca.wechat.task;

import com.hnjca.wechat.constant.WechatAccount;
import com.hnjca.wechat.pojo.YYTest;
import com.hnjca.wechat.service.TestService;
import com.hnjca.wechat.util.AccessTokenUtil;
import com.hnjca.wechat.util.DateUtil;
import com.hnjca.wechat.util.WxServerUtil;
import com.hnjca.wechat.wxsweep.UdpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 */
@Component
public class Task {



   // @Scheduled(cron="*/1 * * * * ?")   //测试每1分钟执行一次
    public void getAccessTokenLocal() throws Exception {
        System.out.println("哈哈哈");
        UdpServer.Monitor();
   }



}
