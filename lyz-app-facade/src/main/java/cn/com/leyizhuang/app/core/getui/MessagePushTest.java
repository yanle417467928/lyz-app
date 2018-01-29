package cn.com.leyizhuang.app.core.getui;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.foundation.pojo.message.Payload;
import cn.com.leyizhuang.app.foundation.pojo.message.TransmissionTemplateContent;
import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.payload.MultiMedia;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created on 2017-12-08 8:49
 **/
public class MessagePushTest {
    public static void main(String[] args) throws Exception {
        pushMessageToSingle();
        //pushMessageToApp();
    }

    /**
     * 群推App所有用户
     */
    public static void pushMessageToApp() {
        IGtPush push = new IGtPush(AppConstant.GE_TUI_HOST, AppConstant.APP_KEY, AppConstant.MASTER_SECRET);

        //LinkTemplate template = linkTemplateDemo();
        //NotificationTemplate template = notificationTemplateDemo(AppConstant.APP_ID,AppConstant.APP_KEY);
        //TransmissionTemplate template = transmissionTemplateDemo();
        TransmissionTemplate template = getTemplate();
        AppMessage message = new AppMessage();
        message.setData(template);

        message.setOffline(true);
        //离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        //推送给App的目标用户需要满足的条件
        AppConditions cdt = new AppConditions();
        List<String> appIdList = new ArrayList<String>();
        appIdList.add(AppConstant.APP_ID);
        message.setAppIdList(appIdList);
       /* //手机类型
        List<String> phoneTypeList = new ArrayList<String>();
        //省份
        List<String> provinceList = new ArrayList<String>();
        //自定义tag
        List<String> tagList = new ArrayList<String>();*/

        /*cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
        cdt.addCondition(AppConditions.REGION, provinceList);
        cdt.addCondition(AppConditions.TAG,tagList);*/
        message.setConditions(cdt);

        IPushResult ret = push.pushMessageToApp(message, "任务别名_toApp");
        System.out.println(ret.getResponse().toString());
    }

    /**
     * 单推个人
     */
    public static void pushMessageToSingle() {
        IGtPush push = new IGtPush(AppConstant.GE_TUI_HOST, AppConstant.APP_KEY, AppConstant.MASTER_SECRET);
        TransmissionTemplate template = getTemplate();
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(AppConstant.APP_ID);
        target.setClientId("c0ebf0688af4e7ea677f3572deb5fc52");
        //target.setAlias(Alias);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            ret = push.pushMessageToSingle(message, target, e.getRequestId());
        }
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }

    }


    public static LinkTemplate linkTemplateDemo() throws Exception {
        LinkTemplate template = new LinkTemplate();
        template.setAppId(AppConstant.APP_ID);
        template.setAppkey(AppConstant.APP_KEY);

        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle("你好");
        style.setText("测试link template");
        // 配置通知栏图标
        style.setLogo(AppConstant.APP_LOGO);
        // 配置通知栏网络图标
        style.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);

        template.setUrl("/page/personal/myOrderList.html");

        return template;
    }


    /**
     * 通知透传模板
     *
     * @param appId
     * @param appkey
     * @return
     */
    public static NotificationTemplate notificationTemplateDemo(String appId, String appkey) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appkey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(1);
        template.setTransmissionContent("我就试试能不能打开APP");
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");

        Style0 style = new Style0();
        // 设置通知栏标题与内容
        style.setTitle("透传模板");
        style.setText("透传模板");
        // 配置通知栏图标
        style.setLogo("icon.png");
        // 配置通知栏网络图标
        style.setLogoUrl(AppConstant.APP_LOGO);
        // 设置通知是否响铃，震动，或者可清除
        style.setRing(true);
        style.setVibrate(true);
        style.setClearable(true);
        template.setStyle(style);

        return template;
    }


    public static TransmissionTemplate transmissionTemplateDemo() {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(AppConstant.APP_ID);
        template.setAppkey(AppConstant.APP_KEY);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        TransmissionTemplateContent content = new TransmissionTemplateContent();
        content.setContent("跳转订单页面");
        content.setTitle("测试透传模板");
        content.setPayload(new Payload("/page/personal/myOrderList.html", "跳转我的订单"));
        System.out.println(content);
        template.setTransmissionContent(JSON.toJSONString(content));
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        return template;
    }

    public static TransmissionTemplate getTemplate() {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(AppConstant.APP_ID);
        template.setAppkey(AppConstant.APP_KEY);
        TransmissionTemplateContent content = new TransmissionTemplateContent();
        content.setContent("您的订单发货了");
        content.setTitle("发货通知");
        //content.setPayload(new Payload("page/personal/myOrderWL.html&CD_XN20180125145618869313", "跳转订单物流详情"));
        content.setPayload(new Payload("http://www.baidu.com", "跳转订单物流详情"));
        System.out.println(content);
        template.setTransmissionContent(JSON.toJSONString(content));
        template.setTransmissionType(2);
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge("+1");
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.setCategory("$由客户端定义");

        //简单模式APNPayload.SimpleMsg
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));

        //字典模式使用APNPayload.DictionaryAlertMsg
        //payload.setAlertMsg(getDictionaryAlertMsg());

        // 添加多媒体资源
        payload.addMultiMedia(new MultiMedia().setResType(MultiMedia.MediaType.video)
                .setResUrl("http://ol5mrj259.bkt.clouddn.com/test2.mp4")
                .setOnlyWifi(true));

        template.setAPNInfo(payload);
        return template;
    }

    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg() {
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        alertMsg.setBody("body");
        alertMsg.setActionLocKey("ActionLockey");
        alertMsg.setLocKey("LocKey");
        alertMsg.addLocArg("loc-args");
        alertMsg.setLaunchImage("launch-image");
        // iOS8.2以上版本支持
        alertMsg.setTitle("Title");
        alertMsg.setTitleLocKey("TitleLocKey");
        alertMsg.addTitleLocArg("TitleLocArg");
        return alertMsg;
    }
}
