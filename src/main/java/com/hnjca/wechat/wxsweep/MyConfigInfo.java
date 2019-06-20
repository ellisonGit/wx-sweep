package com.hnjca.wechat.wxsweep;

import com.hnjca.wechat.wxUtil.IWXPayDomain;
import com.hnjca.wechat.wxUtil.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


/**
 * Description:
 * User: Ellison
 * Date: 2019-06-19
 * Time: 16:16
 * Modified:
 */
public class MyConfigInfo extends WXPayConfig {
    private byte[] certData;

    public MyConfigInfo() throws Exception {

    }

   @Override
    public String getAppID() {
        return "wxbdcb4a18a86e232e";
    }

    @Override
    public String getMchID() {
        return "1536915551";
    }

    @Override
    public String getKey() {
        return "8077a397fc23e724f1f4ec8d8b413770";
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }



    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo("api.mch.weixin.qq.com", true);
            }
        };
        return iwxPayDomain;
    }
    }

  /*  IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo("api.mch.weixin.qq.com", true);
            }
        };
        return iwxPayDomain;
    }*/



