package com.tangchaoke.electrombilechargingpile.thread;

public class UriUtil {

    public static final String ip = "http://192.168.1.253:8080/ChargingPile/";



    /*本地存储的键值*/
    public static final String isLogin = "isLogined";
    public static final String token = "token";

    //账号注册
    public static java.lang.String ip_addMember=ip+"action/UserInfoActionApp/addMember";
    //账号登录
    public static java.lang.String ip_LoginMember=ip+"action/UserInfoActionApp/userLogin";
    //个人信息
    public static java.lang.String ip_userinfo=ip+"action/UserInfoActionApp/getUser";
    //头像
    public static java.lang.String ip_HeadPortrait=ip+"action/UserInfoActionApp/updateHead";
    //账户明细
    public static java.lang.String ip_zhanghumingxi=ip+"action/CheckActionApp/getCheck";
    //优惠券
    public static java.lang.String ip_Coupon=ip+"action/CouponActionApp/getCoupon";
    //消息
    public static java.lang.String ip_message=ip+"action/NewsActionApp/getUser";
    //修改密码
    public static java.lang.String ip_ModifyPassword=ip+"action/UserInfoActionApp/forgetPasswords";
    //全部评论
    public static java.lang.String ip_AllComments=ip+"action/ElectricPileActionApp/getAllevaluateList";
    //电桩信息  2
    public static java.lang.String ip_ElectricPileInformation=ip+"action/ElectricPileActionApp/getElectricPileList";
    //电桩详情  2
    public static java.lang.String ip_DetailOfElectricPile=ip+"action/ElectricPileActionApp/getElectricPileDetail";
    //首页搜索  2
    public static java.lang.String ip_HomepageSearch=ip+"action/HomePageActionApp/getChargePlace";
}

