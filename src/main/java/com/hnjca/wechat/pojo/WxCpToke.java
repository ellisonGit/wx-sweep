package com.hnjca.wechat.pojo;

import lombok.Data;

import java.util.Date;

/**
 * Description: 企业号token。 对应数据库WX_CP_TOKE表
 * User: Ellison
 * Date: 2019-05-27
 * Time: 8:56
 * Modified:
 */
@Data
public class WxCpToke {

    private String id ;
    private String tokeTime ;

    private String accessToken ;







}
