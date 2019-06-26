package com.hnjca.wechat.wxsweep;

import com.hnjca.wechat.util.MyConfig;
import com.hnjca.wechat.wxUtil.WXPay;
import com.hnjca.wechat.wxUtil.WXPayUtil;
import com.hnjca.wechat.wxUtil.WXRequestUtil;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hnjca.wechat.wxUtil.WXRequestUtil.GetIp;


/**
 * Description:
 * User: Ellison
 * Date: 2019-06-19
 * Time: 15:26
 * Modified:
 */
public class MyWXPay {
    private static final String PAY_SUCCESS = "SUCCESS";
    private static final String PAY_USERPAYING = "USERPAYING";


  //  private static Log log = LogFactory.getLog(MyWXPay.class);

    /**
     * 扫码支付
     *
     * @throws Exception
     */
    public static String scanCodeToPay(String auth_code,String money) throws Exception {
        MyConfigInfo config = new MyConfigInfo();
        WXPay wxpay = new WXPay(config);
        int randomNum = (int)(Math.random()*(9999-1000+1))+1000;//商户订单号
        String out_trade_no= WXRequestUtil.createTimestamp()+randomNum ;//商户单号
      //  String out_trade_no = DateUtil.getCurrentTime();
        Map<String, String> map = new HashMap<>(16);

        map.put("auth_code", auth_code);//授权码
        map.put("body", "付款码支付测试");
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        map.put("out_trade_no", out_trade_no);
        map.put("spbill_create_ip", GetIp());
        map.put("total_fee", "1");
        //生成签名
        String sign = WXPayUtil.generateSignature(map, MyConfig.APIKEY);
        map.put("sign", sign);
        String mapToXml = null;
        try {
            //调用微信的扫码支付接口
            Map<String, String> resp = wxpay.microPay(map);
            mapToXml = WXPayUtil.mapToXml(resp);
        } catch (Exception e) {
            e.printStackTrace();
           //System.out.error("微信支付失败" + e);
        }
        //判断支付是否成功
        String return_code = null;
        String result_code = null;
        String err_code_des = null;
        String err_code = null;
        //获取Document对象（主要是获取支付接口的返回信息）
        Document doc = DocumentHelper.parseText(mapToXml);
        //获取对象的根节点<xml>
        Element rootElement = doc.getRootElement();
        //获取对象的子节点
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            if (element.getName().equals("return_code")) {
                return_code = element.getTextTrim();
            } else if (element.getName().equals("result_code")) {
                result_code = element.getTextTrim();
            } else if (element.getName().equals("err_code_des")) {
                err_code_des = element.getTextTrim();
            } else if (element.getName().equals("err_code")) {
                err_code = element.getTextTrim();
            }
        }
        if (PAY_SUCCESS.equals(return_code) && PAY_SUCCESS.equals(result_code)) {
            System.out.println("微信免密支付成功！");
            return PAY_SUCCESS;
        } else if (PAY_USERPAYING.equals(err_code)) {
            for (int i = 0; i < 4; i++) {
                Thread.sleep(3000);
                Map<String, String> data = new HashMap<>(16);
                data.put("out_trade_no", out_trade_no);
                //调用微信的查询接口
                Map<String, String> orderQuery = wxpay.orderQuery(data);
                String orderResp = WXPayUtil.mapToXml(orderQuery);
                String trade_state = null;
                //获取Document对象
                Document orderDoc = DocumentHelper.parseText(orderResp);
                //获取对象的根节点<xml>
                 Element rootElement1 = orderDoc.getRootElement();
                //获取对象的子节点
                List<Element> elements1 = rootElement1.elements();
                for (Element element : elements1) {
                    if (element.getName().equals("trade_state")) {
                        trade_state = element.getTextTrim();
                    }
                }
                if (PAY_SUCCESS.equals(trade_state)) {
                   System.out.println("微信加密支付成功！");
                    return PAY_SUCCESS;
                }
               System.out.println("正在支付" + orderResp);
            }
        } //log.error("微信支付失败！");
        return err_code_des;
    }
}


