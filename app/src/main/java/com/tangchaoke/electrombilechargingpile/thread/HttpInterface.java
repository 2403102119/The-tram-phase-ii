package com.tangchaoke.electrombilechargingpile.thread;

import android.content.Context;
import android.util.Log;

import com.tangchaoke.electrombilechargingpile.App;
import com.tangchaoke.electrombilechargingpile.util.UriUtil;
import com.zhy.http.okhttp.OkHttpUtils;

public class HttpInterface {
    private Context context;
    public LoadingDialog loadingDialog;

    public HttpInterface(Context context) {
        this.context = context;
        if (loadingDialog == null) {
            Log.e("httpInterface", "new LoadingDialog");
            loadingDialog = new LoadingDialog(context);
            loadingDialog.setCancelable(true);
        }
    }













    /*电桩详情*/
    public void getPowerInfo( String token, String identity1, String identity2,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_getPowerInfo);
        try {
            //传参数
            userClient.AddParam("token", token);//token信息
            userClient.AddParam("powerInfo.identity", identity1);//充电桩编号
            userClient.AddParam("model.identity", identity2);//充电地点表主键

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/*头像*//*
    public void HeadPortrait( String token,String headImage,String PicType,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_HeadPortrait);
        try {
            //传参数
            userClient.AddParam("token", token);//token信息
            userClient.AddParam("headImage", headImage);//base64
            userClient.AddParam("PicType", PicType);//图片格式(jpeg)

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    /*优惠券*/
    public void Coupon( String token,String index,String num,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_Coupon);
        try {
            //传参数
            userClient.AddParam("token", token);//token信息
            userClient.AddParam("page.index", index);//分页（开始）
            userClient.AddParam("page.num", num);//分页（结束）

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }











    /*
  App6<<获取用户信息
  */
    public void updateHead( String token, String headImage, String PicType, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_updateHead);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("headImage", headImage);
            userClient.AddParam("PicType", PicType);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /*
    App9<<修改年龄
*/
    public void updateAge( String token, String age, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_updateAge);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("model.age", age);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
App9<<修改城市
*/
    public void updateCity( String token, String city,String province ,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_updateCity);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("model.city", city);
            userClient.AddParam("model.province", province);

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }









    /*去充电生成订单*/
    public void getorder( String token, String oid, String chargingmode,String identity ,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_goElectric);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("powerInfo.oid", oid);
            userClient.AddParam("chargingmode", chargingmode);
            userClient.AddParam("model.identity", identity);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /*电站详情*/
    public void getElectricPileDetail( String lat, String lng, String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_getElectricPileDetail);
        try {
            userClient.AddParam("lat", lat);
            userClient.AddParam("lng", lng);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*订单详情*/
    public void getWaitOrderDetai( String token, String oid, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_getWaitOrderDetail);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("userOrder.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*充电中订单详情*/
    public void getOrderingDetail(String oid,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_getOrderingDetail);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("userOrder.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*余额支付*/
    public void getpayFinish(String token,String oid,String oid2,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_getpayFinish);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("userOrder.oid", oid);
            userClient.AddParam("coupon.oid", oid2);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*全部评论*/
    public void getAllevaluateList(String index,String num,String oid,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_getAllevaluateList);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("page.index", index);
            userClient.AddParam("page.num", num);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*评价*/
    public void Evalluate(String token,String stars,String content,String oid,String oid1,String oid2,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_Evalluate);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("evaluate.stars", stars);
            userClient.AddParam("evaluate.content", content);
            userClient.AddParam("userOrder.oid", oid);
            userClient.AddParam("chargePlace.oid", oid1);
            userClient.AddParam("evaluateImage.oid", oid2);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*评价上传图片*/
    public void uploadBusinessCase(String token,String imageUrl,String imgType,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_uploadBusinessCase);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("imageUrl", imageUrl);
            userClient.AddParam("imgType", imgType);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*删除订单*/
    public void getdeletOrder(String token,String oid,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_getdeletOrder);
        try {
            userClient.AddParam("token", token);
            userClient.AddParam("userOrder.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*App40<<报修 >*/
    public void getRepair(String content, String oid,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_getRepair);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("content", content);
            userClient.AddParam("socket.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*App41<<获取收费标准 >*/
    public void getRates(String oid,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_getRates);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("model.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*App45<<等待充电 >*/
    public void goElectricAfter(String oid,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_goElectricAfter);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("userOrder.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*App46<<删除错误订单 > */
    public void deleteWrong(String oid,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_deleteWrong);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("userOrder.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*等待充电*/
    public void getwait(String oid,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_getwait);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("userOrder.oid", oid);
            userClient.executePost(callback, null, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*充电完成*/
    public void getelectricFinish(String oid,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_getelectricFinish);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("userOrder.oid", oid);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * App37<<获取账单信息 >
    */
    public void getUserBill(String oid,MApiResultCallback callback){
        UserClient userClient = new UserClient(UriUtil.ip_getUserBill);
        try {
            userClient.AddParam("token", App.token);
            userClient.AddParam("userOrder.oid", oid);

            loadingDialog.setHint("正在获取结算信息");
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    * 二期开发
    * */

    /*账号登录*/
    public void LoginMember(String json, boolean showLoading, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_LoginMember);
        try {

            userClient.addJson(json);
            OkHttpUtils.getInstance().cancelTag("ALL");
            if (showLoading) {
                loadingDialog.setCancelable(false);
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.setBackkeyWork(false);
                userClient.executePost(callback, loadingDialog, context);
            } else {
                userClient.executePost(callback, null, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*注册获取验证码*/
    public void sendYzmMember( String json,String phone,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_sendYzmMember+phone);
        try {
            userClient.addJson(json);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*验证验证码*/
    public void ip_Verification( String json,String phone,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_Verification+phone+"/client/5c230e7aafe674000144dd1c ");
        try {
            userClient.addJson(json);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*保存密码*/
    public void registone(String json,MApiResultCallback callback){
        UserClient userClient=new UserClient(UriUtil.ip_registone);
        try {
            userClient.addJson(json);
            userClient.addRequestType(UserClient.RequestMethod.PUT);
            loadingDialog.setCancelable(false);
            userClient.executePost(callback,loadingDialog,context);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /*保存用户*/
    public void addMember(MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_addMember);
        try {

            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*初始化数据*/
    public void getUser(MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_getUser);
        try {
            userClient.addRequestType(UserClient.RequestMethod.GET);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*修改昵称*/
    public void updateNickName( String json,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_updateNickName);
        try {
            userClient.addJson(json);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*修改性别*/
    public void updateSex( String json, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_updateNickName);
        try {
            userClient.addJson(json);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*账户余额*/
    public void getbalance(MApiResultCallback callback){
        UserClient userClient=new UserClient(UriUtil.ip_getUser);
        try {
            userClient.addRequestType(UserClient.RequestMethod.GET);
            userClient.executePost(callback,loadingDialog,context);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*订单列表*/
    public void getOrderList( String json, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_getOrderList);
        try {
            userClient.addJson(json);
            userClient.addRequestType(UserClient.RequestMethod.GET);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*获取公司信息*/
    public void getCompany( MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_company);
        try {

            userClient.addRequestType(UserClient.RequestMethod.GET);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*消息*/
    public void message( String json, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_message);
        try {
            userClient.addRequestType(UserClient.RequestMethod.GET);
            userClient.addJson(json);


            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*App13<<余额充值 >*/
    public void rechargeBalance(String type, String money, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_rechargeBalance+type+"/money/"+money);
        try {

            userClient.addRequestType(UserClient.RequestMethod.GET);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*账户明细*/
    public void zhanghumingxi(  String index, String num, MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_zhanghumingxi+index+"&size="+num);
        try {
            userClient.addRequestType(UserClient.RequestMethod.GET);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*电站列表*/
    public void getElectricPileList(String loweRrightCorner,String loweRrightCorner1,String topleftcorner,String topleftcorner1,String userCoordinates,String userCoordinates1,String roomType,String distance,String amountType,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_getElectricPileList+loweRrightCorner+loweRrightCorner1+topleftcorner+topleftcorner1+userCoordinates+userCoordinates1+roomType+distance+amountType);
        try {

            userClient.addRequestType(UserClient.RequestMethod.GET);
            userClient.executePost(callback, null, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**搜索电桩列表*/
    public void getChargePlace(String searchText,MApiResultCallback callback) {
        UserClient userClient = new UserClient(UriUtil.ip_getChargePlace+searchText);
        try {
            userClient.addRequestType(UserClient.RequestMethod.GET);
            userClient.executePost(callback, loadingDialog, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
