/**
 *
 */
package com.tangchaoke.electrombilechargingpile.thread;

import android.content.Context;

import com.tangchaoke.electrombilechargingpile.R;


/**
 * @author mikes
 * @version 2016-2-26 上午9:52:03 Note: 根据服务器响应显示Toast
 */
public class HandleResponseCode {
    public static final int RESPONSE_UNKNOWN = -1;
    public static final String RESPONSE_NOTLOGIN = "11010";
    public static final int RESPONSE_UNKNOWHOST = 10002;
    public static final int RESPONSE_TIMEOUT = 10003;
    public static final int RESPONSE_THIRD_UPLOAD = 10004;
    public static final int RESPONSE_DOWNLOAD_FAIL = 10005;
    public static final int RESPONSE_LOGOUT = 10006;
    public static final int RESPONSE_SYSTEM_BUSY = 10007;
    public static final int RESPONSE_SEND_MSG_FAIL = 10008;
    public static final int RESPONSE_DELETE_MSG_FAIL = 10009;

    public static void handleResponseCode(Context context, int responseCode) {
        switch (responseCode) {
            case RESPONSE_UNKNOWN:
                MUIToast.toast(context, R.string.unknown_error_toast);
                break;
            case RESPONSE_UNKNOWHOST:
                MUIToast.toast(context, R.string.connect_failed_toast);
                break;
            case RESPONSE_TIMEOUT:
                MUIToast.toast(context, R.string.connect_timeout);
                break;
            case RESPONSE_THIRD_UPLOAD:
                MUIToast.toast(context, R.string.third_upload_fail);
                break;
            case RESPONSE_DOWNLOAD_FAIL:
                MUIToast.toast(context, R.string.download_fail);
                break;
            case RESPONSE_LOGOUT:
                MUIToast.toast(context, R.string.user_logout);
                break;
            case RESPONSE_SYSTEM_BUSY:
                MUIToast.toast(context, R.string.system_busy);
                break;
            case RESPONSE_SEND_MSG_FAIL:
                MUIToast.toast(context, R.string.msg_send_fail);
                break;
            case RESPONSE_DELETE_MSG_FAIL:
                MUIToast.toast(context, R.string.msg_delete_fail);
                break;
            case 405://rejected by blacklist
                MUIToast.toast(context, R.string.rong_response_405);
                break;
            case 22406://NOT_IN_GROUP
                MUIToast.toast(context, R.string.rong_response_22406);
                break;
            case 30001:// 进行通信操作过程中，当前 Socket 失效。
                MUIToast.toast(context, R.string.rong_response_30001);
                break;
            case 30002:// Socket 连接不可用。应该是您当前网络连接不可用。
                MUIToast.toast(context, R.string.rong_response_30002);
                break;
            case 30003:// 进行各种信令的通信操作过程中，信令 ACK 返回超时。
                MUIToast.toast(context, R.string.rong_response_30003);
                break;
            case 30004:// 网络通讯中，HTTP发送超时。
                MUIToast.toast(context, R.string.rong_response_30004);
                break;
            case 30005:// 网络通讯中，HTTP请求超时。
                MUIToast.toast(context, R.string.rong_response_30005);
                break;
            case 30007:// 通过 HTTP 获取连接网络必须的配置数据时，服务器返回的不是 200 OK，而是 HTTP 的其它错误码。
                MUIToast.toast(context, R.string.rong_response_30007);
                break;
            case 30008:// 通过 HTTP 获取配置数据时，成功获得数据，但得到的内容体部分是空。可能是你所在的网络被劫持，HTTP 被修改。
                MUIToast.toast(context, R.string.rong_response_30008);
                break;
            case 30011:// Socket 连接被断开，主要有两种情况，一是用户主动调用 disconnect 之后，Socket
                // 被服务器断开；二是中间路由原因导致 Socket 断开。
                MUIToast.toast(context, R.string.rong_response_30011);
                break;
            case 30014:// 消息发送失败。
                MUIToast.toast(context, R.string.rong_response_30014);
                break;
            case 31000:// 做 connect 连接时，收到的 ACK 超时。
                MUIToast.toast(context, R.string.rong_response_31000);
                break;
            case 31001:// 协议参数错误。
                MUIToast.toast(context, R.string.rong_response_31001);
                break;
            case 31002:// 参数错误，可能是您使用的 AppKey 错误。
                MUIToast.toast(context, R.string.rong_response_31002);
                break;
            case 31003:// 服务器不可用。融云的服务器实际上不会发生此错误
                MUIToast.toast(context, R.string.rong_response_31003);
                break;
            case 31004:// Token 错误
                MUIToast.toast(context, R.string.rong_response_31004);
                break;
            case 31007:// 您提交的包名跟后台注册包名不一致。
                MUIToast.toast(context, R.string.rong_response_31007);
                break;
            case 31008:// APP 被屏蔽、删除或不存在。
                MUIToast.toast(context, R.string.rong_response_31008);
                break;
            case 31009:// 当前用户被屏蔽。
                MUIToast.toast(context, R.string.rong_response_31009);
                break;
            case 31010:// 用户互踢，用户已在其他设备登录。
                MUIToast.toast(context, R.string.rong_response_31010);
                break;
            case 33001:// 未首先调用 init 函数。注意您必须先调用 init 进行初始化，才能通过connect 进行连接。
                MUIToast.toast(context, R.string.rong_response_33001);
                break;
            case 33002:// 数据库错误，可能是数据库不存在或者损坏。
                MUIToast.toast(context, R.string.rong_response_33002);
                break;
            case 33003:// 传入参数无效。
                break;
            case 33005:// 重新连接成功。
                break;
            case 33006:// 连接中，再调用 connect 被拒绝。
                MUIToast.toast(context, R.string.rong_response_33006);
                break;
            default:
                break;
        }
    }
}
