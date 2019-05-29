package com.tangchaoke.electrombilechargingpile.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/11.
 */

public class Bean {
    public  int status;
    public  String message;


    /*
    地理坐标
     */
    public static class Location implements Serializable{
        public double lat;
        public double lng;

        public Location(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }

    /*
    登陆成功的返回信息实体类  一级
     */
    public class LoginMsgAll{
        public  int status;
        public  String message;
        public LoginMsg model;
    }


    /*
    登陆成功的返回信息实体类
     */
    public static class LoginMsg implements Serializable{
        public String oid;
        public String account;
        public String head;
        public String nickName;
        public String sex;
        public String age;
        public String province;
        public String city;
        public String token;
    }


    /*
    优惠券实体类  一级
     */
    public class CouponAll{
        public  int status;
        public  String message;
        public List<Coupon> list;
    }


    /*
    优惠券实体类
     */
    public class Coupon{
        public String oid;
        public String text;
        public String cutOffTime;
        public String state;
        public double subtractMoney;
    }

    /*
消息实体类  一级
 */
    public class MessageAll{
        public  boolean last;
        public  String pageable;
        public List<content> content;
        public int totalElements;
        public int totalPages;
        public boolean first;
        public  sort sort;
        public  int numberOfElements;
        public int size;
        public int number;
        public boolean empty;
    }
    public class sort{
        public boolean sorted;
        public boolean unsorted;
        public boolean empty;
    }


    /*
    消息实体类
     */
    public class content{
        //        public String ctime;
//        public String context;
//        public String type;
//        public String title;
        public String id;
        public String status;
        public String title;
        public String context;
        public String sendStatus;
        public String createAdminInfoId;
        public long createTime;
    }



    /*
    验证码返回的实体类
     */
    public class VerificationCode{
        public int status;
        public String message;
        public String yzm;
    }


    /*
    上传图片返回的实体类
     */
    public class UploadPic{
        public int status;
        public String message;
        public String head;
    }


    /*
    修改性别返回的实体类
     */
    public class UploadSex{
        public int status;
        public String message;
        public String sex;
    }
    /*
    修改年龄
     */
    public class UploadAge{
        public int status;
        public String message;
        public String age;
    }

    /*
    修改城市
      */
    public class Fogcity{
        public int status;
        public String message;
        public String city;
        public String province;
    }
    /*
     获取账户余额  一级
  */
    public class balanceAll{
        public int status;
        public String message;
        public getbalance model;

    }
    /*
    获取账户余额  二级
     */
    public class getbalance{
        public double balance;

    }


    /*
    修改密码
    */
    public class Fogpws{
        public  int status;
        public  String message;
    }


    /*
    修改昵称
    */
    public class Foguser{
        public String nickName;
        public int status;
        public String message;
    }


    /*
    联系我们返回的实体类
     */
    public class CallUs{
        public PhoneStr phone;
        public int status;
        public String message;
    }


    /*
    电话实体类
     */
    public class PhoneStr{
        public String compayTel;
    }

    /*地图上电桩列表*/
    public class ElectricPileListModel{
        public List<ElectricPileList> list;
        public String message;
        public int status;
    }

    /*
    "oid":"1",
            "title":"电车电站",
            "address":"这是地址",
            "powerRange":1,
            "lat":"34.79664",
            "lng":"113.678835",
            "socketNum":10,
            "socketUsenum":5,
            "socketLeisurenum":2,
            "socketAbnormalnum":3,
            "NsocketNum":10,
            "NsocketLeisurenum":2,
            "WsocketNum":0,
            "WsocketLeisurenum":0,
            "businessHours":24,
            "image":"",
            "distance":55
     */

    /*地图上电桩列表*/
    public class ElectricPileList{
        public String oid;
        public String title;
        public String address;
        public double powerRange;
        public String lat;
        public String lng;
        public int socketNum;
        public int socketUsenum;
        public int socketLeisurenum;
        public int socketAbnormalnum;
        public int NsocketNum;
        public int NsocketLeisurenum;
        public int WsocketNum;
        public int WsocketLeisurenum;
        public int businessHours;
        public String image;
        public double distance;
    }



    /*地图上电桩列表*/
    public class SearchPileListModel{
        public List<SearchPileList> model;
        public String message;
        public int status;
    }

    /*地图上电桩列表*/
    public class SearchPileList{
        public String oid;
        public String title;
        public String address;
        public String businessHours;
        public String lat;
        public String lng;
        public int totalQuantity;
        public int leisureNum;
        public int employNum;
        public int anomalyNum;
        public float powerRange;
        public String image;
        public String distance;
    }

    /*
    电桩详情实体类   一级
     */
    public class PowerInfoall{
        public PowerInfo model;
        public int status;
        public String message;
    }

    /*
    "socket":"1234",
        "useState":0,
        "socketSite":1,
        "socketState":0,
        "oid":"1",
        "currPlaceOid":"1",
        "identity":"001",
        "currPlace":"电车电站",
        "power":"200w~400w",
        "powerRange":1,
        "voltage":"220V",
        "stopStatus":"0",
        "location":"1",
     */

    /*
       电桩详情实体类   二级
       */
    public class PowerInfo implements Serializable{
        public String socket;
        public String socketOid;                    //插座id
        public int useState;                        //0：空闲  1：使用中
        public int socketSite;                      //0：未插枪 1：已插枪
        public int socketState;                     //0:正常  1：故障
        public String oid;
        public String currPlaceOid;
        public String identity;
        public String currPlace;
        public String power;
        public double powerRange;
        public String voltage;
        public String stopStatus;                  //0：正常  1：故障
        public String location;
        public double balance;
        public List<ChargingMode> chargingmode;
    }

    /*
   电桩详情实体类   三级
   */
    public class ChargingMode implements Serializable{
        public String value;
        public String chargingmode;
    }
    /*
     订单生成
     */
    public class getorder {
        public String oid;
        public int status;
        public String message;
    }

    /*
      订单列表实体类
    */
    public class getOrderList{
        public List<Order> list;
        public  int status;
        public  String message;
    }
    /*
    "oid":"I7WC5MXCF1",
            "orderTime":"2018-07-25 10:11:06",
            "chargePlace":"电车电站",
            "identity":"001",
            "oederState":"0",
            "socket":"1234",
            "cost":0,
            "duration":null
     */

        /*
      订单列表实体类 2
    */

    public class Order{
        public String oid;
        public String orderTime;
        public String chargePlace;
        public String identity;
        public String socket;
        public String oederState;
        public double cost;
    }

    public class PileDetailsModel {
        public PileDetails model;
        public String message;
        public int status;
    }

    /*

     */

    public class PileDetails {
        public String oid;
        public String title;
        public String address;
        public double powerRange;
        public int socketNum;
        public int socketLeisurenum;
        public String businessStartDays;
        public String businessEndDays;
        public String businessHours;
        public String servicePhone;
        public String lat;
        public String lng;
        public String chargeimageUrl;
        public double distance;
        public List<PowerInfoDetail> PowerInfoList;
    }

    public class PowerInfoDetail{
        public String oid;
        public String identity;
        public String rechargePort;
        public String power;
        public String voltage;
        public int socketNum;
        public int socketUsenum;
        public int socketLeisurenum;
        public int socketAbnormalnum;
        public String location;
    }

    public class EvaluateModel {
        public ArrayList<Evaluate> list;
        public String message;
        public int status;
    }

    public class Evaluate {
        public ArrayList<String> imageUrl;
        public String ctime;
        public String oid;
        public String headimage;
        public String nickname;
        public int stars;
        public String content;
    }

    //订单详情  1
    public class getWaitOrderDetaiAll{
        public OrderDetai model;
        public int status;
        public String message;
    }
    /*
     "oid":"1",
        "chargePlace":"电车电站",
        "identity":"001",
        "soketIdentity":"1234",
        "orderNumber":"20180724141201858001",
        "oederState":"0",
        "paytype":"余额支付",
        "chargingmode":"8小时",
        "startDate":"2018-07-24 14:12:02",
        "endDate":null,
        "cost":0,
        "orderOid":"I7WAYSWIL1"
     */

    //订单详情  2
    public class OrderDetai{
        public String oid;
        public String soketOid;
        public String chargePlace;
        public String identity;
        public String orderNumber;
        public String oederState;
        public String soketIdentity;
        public String startDate;
        public String endDate;
        public String paytype;
        public String chargingmode;
        public float cost;
        public int duration;
        public String orderOid;
    }


    //充电中订单详情  1
    public class OrderingAll{
        public Ordering list;
        public Ordering model;
        public int status;
        public String message;
    }

    /*
    "chargingmode":"自动充满",
        "oid":"1",
        "title":"电车电站",
        "identity":"001",
        "soketIdentity":"1234",
        "alreadyTime":0,
        "terminationCause":"4",
        "systemFaultReason":"",
        "powerFaultReason":""
     */

    //充电中订单详情   2
    public class Ordering{
        public String chargingmode;
        public String oid;
        public String title;
        public String identity;
        public String soketIdentity;
        public int alreadyTime;
        public String terminationCause;
        public String systemFaultReason;
        public String powerFaultReason;
        public String soketOid;
    }


    //余额支付    1
    public class getpayFinish{
        public GetPay list;
        public int status;
        public String message;
    }

    //余额支付    2
    public class GetPay {
        public String startDate;
        public String  endDate;
        public int  duration;
        public float electric;
        public float cost;
    }

    //评论上传图片
    public class UploadBusinessCase{
        public String oid;
        public String message;
        public int status;
    }

    //充电结束返回数据
    public class FinishChargeModel{
        public String thumb;
        public int status;
    }

    //充电中订单详情       1
    public class ChargingOrderDatailAll{
        public ChargingOrderDatail list;
        public String message;
        public int status;
    }

    //充电中订单详情       2
    public class ChargingOrderDatail{
        public String currPlaceOid;
        public String chargePlace;
        public String identity;
        public String gun;
        public String startDate;
        public String oid;
        public String launch;
        public String terminationCause;
        public String powerFault;
        public double electric;
        public double cost;
        public int alreadyTime;
    }

    //充电结束返回实体类   1
    public class ChargingFinishAll{
        public ChargingFinish list;
        public String message;
        public int status;
    }

    //充电结束返回实体类  2
    public class ChargingFinish{
        public String startDate;
        public String endDate;
        public double electric;
        public double cost;
    }

    public class ChargingStandard{
        public ChargingStandardList list;
        public int status;
        public String message;
    }

    public class ChargingStandardList{
        public String first;
        public String second;
        public String third;
        public String fourth;
        public String fifth;
    }

    /*
    * 充电订单返回的实体类
    * */
    public class ElectricAfter{
        public DelectionStatus model;
        public int status;
        public String message;
    }

    /*
    * 删除状态返回的实体类
    * */
    public class DelectionStatus{
        public int detectionStatus;
    }
    /*验证验证码*/
    public static class ssid{
        public String key;
        public String code;
    }
    /*初始化数据*/
    public static class Init_data{
        public String id;
        public String status;
        public String type;
        public String account;
        public String nickName;
        public String sex;
        public String age;
        public String city;
        public String headPath;
        public double balance;
        public long createTime;
        public long deleteTime;
    }

    /*公司信息*/
    public static class Conpany implements Serializable {
        public String id;
        public String status;
        public String appName;
        public String androidAppImg;
        public String androidAppUrl;
        public String iosAppImg;
        public String iosAppUrl;
        public String backstageAddress;
        public String promotersAddress;
        public String extensionDonainName;
        public String appDonainName;
        public String corporateName;
        public String copyrightInformation;
        public String customerPhone;
        public String complaintPhone;
        public String logo;
        public String customerUrl;
        public String weChat;
    }
    /*搜索电桩*/
    public static class Fitt implements  Serializable{
        public String id;
        public String status;
        public String province;
        public String city;
        public String area;
        public String title;
        public String address;
        public String lat;
        public String lng;
        public String businessHours;
        public String businessStartdays;
        public String businessEnddays;
        public String servicePhone;
        public String totalQuantity;
        public String anomalyNum;
        public String employNum;
        public String leisureNum;
        public String chargeImageUrll;
        public String optional;
        public String agentId;
        public String agentName;
        public String powerCostRangeId;
        public String powerCostRangeName;
        public String createTime;
        public String deleteTime;
    }

}
