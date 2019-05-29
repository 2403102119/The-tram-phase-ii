package com.tangchaoke.electrombilechargingpile.util;

public class UriUtil {

    //    public static final String ip = "http://192.168.0.114:8080/Electric/";
    public static final String ip = "http://www.whatspower.com/";
    //    public static final String ip = "http://47.100.46.53/Electricpile/";
//    public static final String ip = "http://www.tckzz.cn/ChargingPile/";



//    public static final String ip1="http://192.168.0.116:8001/";
//    public static final String ip2="http://192.168.0.116:17002/";

    public static final String ip1 ="http://auth.zztck.com:81/";
    public  static final String ip2 ="http://app.zztck.com:81/";


    /*本地存储的键值*/
    public static final String isLogin = "isLogined";
    public static final String token = "token";



    //App7<<上传头像 修改头像
    public static String ip_updateHead = ip+"action/UserInfoActionApp/updateHead";







    //App16<<电站详情 >
    public static String ip_getElectricPileDetail=ip+"action/ElectricPileActionApp/getElectricPileDetail";

    //App18<<电桩详情 >
    public static String ip_getPowerInfo=ip+"action/ChargeActionApp/goPowerInfo";
    //App19<<充电中 >
    public static String ip_getwait=ip+"action/ChargeActionApp/getwait";
    //App20<<去充电生成订单 >
    public static String ip_goElectric=ip+"action/ChargeActionApp/goElectric";
    //App23<<订单详情 >
    public static String ip_getWaitOrderDetail=ip+"action/CheckActionApp/getWaitOrderDetail";
    //App24<<删除订单 >
    public static String ip_getdeletOrder=ip+"action/CheckActionApp/getdeletOrder";
    //App40<<报修 >
    public static String ip_getRepair=ip+"action/ChargeActionApp/getRepair";
    //App41<<获取收费标准 >
    public static String ip_getRates=ip+"action/HomePageActionApp/getRates";
    //App45<<等待充电 >
    public static String ip_goElectricAfter=ip+"action/ChargeActionApp/goElectricAfter";
    //App46<<删除错误订单 >
    public static String ip_deleteWrong=ip+"action/ChargeActionApp/deleteWrong";
    //优惠券
    public static String ip_Coupon=ip+"action/CouponActionApp/getCoupon";
    //全部评论
    public static String ip_getAllevaluateList =ip+"action/ElectricPileActionApp/getAllevaluateList";
    //App9<<修改年龄
    public static String ip_updateAge = ip+"action/UserInfoActionApp/updateAge";
    //App9<<修改城市
    public static String ip_updateCity = ip+"action/UserInfoActionApp/updatCity";

    //充电中订单详情
    public static String ip_getOrderingDetail=ip+"action/CheckActionApp/getOrderingDetail";
    //余额支付
    public static String ip_getpayFinish=ip+"action/CheckActionApp/getpayFinish";
    //评价
    public static String ip_Evalluate=ip+"action/EvaluateActionApp/Evalluate";
    //评价上传图片
    public static String ip_uploadBusinessCase=ip+"action/EvaluateActionApp/uploadBusinessCase";
    //充电完成
    public static String ip_getelectricFinish=ip+"action/CheckActionApp/getelectricFinish";
    //App37<<获取账单信息 >
    public static String ip_getUserBill=ip+"action/CheckActionApp/getUserBill";


/*
* 二期开发
* */

    //注册获取验证码
    public static String ip_sendYzmMember=ip1+"verificationCode/";
    //验证验证码
    public static String ip_Verification=ip1;
    //保存密码
    public static  String ip_registone=ip1+"password";
    //账号注册
    public static String ip_addMember=ip2+"register";
    //账号登录
    public static String ip_LoginMember=ip1+"client/0";
    //初始化数据
    public static String ip_getUser=ip2+"user/details";
    //修改昵称
    public static String ip_updateNickName=ip2+"user/amend";
    //订单列表
    public static String ip_getOrderList=ip2+"order/getOrderList";
    //获取公司信息
    public static String ip_company=ip2+"appsystem/companyInformation/";
    //获取用户消息
    public static String ip_message=ip2+"news/list";
    //余额充值
    public static String ip_rechargeBalance=ip2+"pay/type/";
    //账户明细
    public static String ip_zhanghumingxi=ip2+"pay/getBalanceRecordList?page=";
    //电站列表
    public static String ip_getElectricPileList=ip2+"charge/place/list/?";
    //首页搜索
    public static String ip_getChargePlace=ip2+"chargePlace/title/";


}

