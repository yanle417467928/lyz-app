package cn.com.leyizhuang.app.core.getui;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.message.AppUserDevice;
import cn.com.leyizhuang.app.foundation.pojo.message.Payload;
import cn.com.leyizhuang.app.foundation.pojo.message.TransmissionTemplateContent;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppUserDeviceService;
import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Created on 2017-12-08 8:49
 **/
@Component
public class NoticePushUtils {
    public static void main(String[] args) throws Exception {
        pushMessageToList(AppConstant.GE_TUI_HOST, AppConstant.APP_KEY, AppConstant.MASTER_SECRET);
        //pushMessageToApp();
    }

    private static AppUserDeviceService userDeviceService;

    @Autowired
    public void setUserDeviceService(AppUserDeviceService userDeviceService) {
        NoticePushUtils.userDeviceService = userDeviceService;
    }


    private static AppOrderService orderService;

    @Autowired
    public void setOrderService(AppOrderService orderService) {
        NoticePushUtils.orderService = orderService;
    }

    /**
     * 群推App所有用户
     */
    public static void pushMessageToApp() {
        IGtPush push = new IGtPush(AppConstant.GE_TUI_HOST, AppConstant.APP_KEY, AppConstant.MASTER_SECRET);

        //LinkTemplate template = linkTemplateDemo();
        //NotificationTemplate template = notificationTemplateDemo(AppConstant.APP_ID,AppConstant.APP_KEY);
        //TransmissionTemplate template = transmissionTemplateDemo();
        TransmissionTemplate template = getTransmissionTemplate(AppConstant.APP_ID, AppConstant.APP_KEY,
                new Payload("page/personal/myOrderWL.html&CD_XN20180125145618869313", "跳转订单物流详情"), null, null);
        AppMessage message = new AppMessage();
        message.setData(template);

        message.setOffline(true);
        //离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        //推送给App的目标用户需要满足的条件
        AppConditions cdt = new AppConditions();
        List<String> appIdList = new ArrayList<String>();
        appIdList.add(AppConstant.APP_ID);
        message.setConditions(cdt);

        IPushResult ret = push.pushMessageToApp(message, "任务别名_toApp");
        System.out.println(ret.getResponse().toString());
    }

    /**
     * 单推个人
     */
    public static void pushMessageToSingle() {
        IGtPush push = new IGtPush(AppConstant.GE_TUI_HOST, AppConstant.APP_KEY, AppConstant.MASTER_SECRET);
        TransmissionTemplate template = getTransmissionTemplate(AppConstant.APP_ID, AppConstant.APP_KEY,
                new Payload("page/personal/myOrderWL.html&CD_XN20180125145618869313", "跳转订单物流详情"),
                null, null);
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

    /**
     * 列表推送
     *
     * @param host         appId
     * @param appKey       appKey
     * @param masterSecret masterSecret
     */
    public static void pushMessageToList(String host, String appKey, String masterSecret) {
        IGtPush push = new IGtPush(host, appKey, masterSecret);
        // 通知透传模板
        //NotificationTemplate template = notificationTemplateDemo();
        TransmissionTemplate template = getTransmissionTemplate(AppConstant.APP_ID, AppConstant.APP_KEY,
                new Payload("page/personal/myOrderWL.html&CD_XN20180129145733063612", "跳转订单物流详情"),
                null, null);
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List targets = new ArrayList();
        Target target1 = new Target();
        Target target2 = new Target();
        target1.setAppId(AppConstant.APP_ID);
        target1.setClientId("c0ebf0688af4e7ea677f3572deb5fc52");
        //     target1.setAlias(Alias1);
        target2.setAppId(AppConstant.APP_ID);
        target2.setClientId("13adf11d319e3e61861f1b0a1215ea63");
        //     target2.setAlias(Alias2);
        targets.add(target1);
        targets.add(target2);
        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targets);
        System.out.println(ret.getResponse().toString());

    }


    /**
     * 生成点击通知打开网页模板
     *
     * @return 点击通知打开网页模板
     */
    public static LinkTemplate getLinkTemplate(String appId, String appKey) {
        LinkTemplate template = new LinkTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);

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
     * 点击通知打开应用模板
     *
     * @param appId  应用id
     * @param appkey 应用key
     * @return 返回“点击通知打开应用模板”
     */
    public static NotificationTemplate getNotificationTemplate(String appId, String appkey) {
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


    /**
     * 透传消息模板
     *
     * @return 返回透传消息模板
     */
    public static TransmissionTemplate getTransmissionTemplate(String appId, String appKey, Payload contentPayload,
                                                               String contentBody, String contentTitle) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        TransmissionTemplateContent content = new TransmissionTemplateContent();
        content.setContent(contentBody);
        content.setTitle(contentTitle);
        content.setPayload(contentPayload);
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

    /**
     * 物流出货信息推送
     */
    public static void pushOrderLogisticInfo(String orderNo) {
        IGtPush push = new IGtPush(AppConstant.GE_TUI_HOST, AppConstant.APP_KEY, AppConstant.MASTER_SECRET);
        TransmissionTemplate template = getTransmissionTemplate(AppConstant.APP_ID, AppConstant.APP_KEY,
                new Payload("page/personal/myOrderWL.html&" + orderNo, "跳转订单物流详情"),
                "您单号为:" + orderNo + "的订单已经发货了!", "发货通知");
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List<Target> targetList = new ArrayList(5);
        List<AppUserDevice> userDeviceList = new ArrayList<>();
        OrderBaseInfo orderBaseInfo = orderService.getOrderDetail(orderNo);
        if (orderBaseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
            List<AppUserDevice> sellerDeviceList = userDeviceService.findAppUserDeviceByUserIdAndIdentityType(orderBaseInfo.getCreatorId(), AppIdentityType.SELLER);
            List<AppUserDevice> customerDeviceList = userDeviceService.findAppUserDeviceByUserIdAndIdentityType(orderBaseInfo.getCustomerId(), AppIdentityType.CUSTOMER);
            userDeviceList.addAll(sellerDeviceList);
            userDeviceList.addAll(customerDeviceList);
        } else {
            userDeviceList = userDeviceService.findAppUserDeviceByUserIdAndIdentityType(orderBaseInfo.getCreatorId(), orderBaseInfo.getCreatorIdentityType());
        }
        userDeviceList.forEach(p -> {
            Target target = new Target();
            target.setAppId(AppConstant.APP_ID);
            target.setClientId(p.getClientId());
            targetList.add(target);
        });
        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targetList);
        System.out.println(ret.getResponse().toString());

    }

    /**
     * 欠款审核信息推送
     */
    public static void pushApplyArrearageInfo(Long sellerId) {
        IGtPush push = new IGtPush(AppConstant.GE_TUI_HOST, AppConstant.APP_KEY, AppConstant.MASTER_SECRET);
        TransmissionTemplate template = getTransmissionTemplate(AppConstant.APP_ID, AppConstant.APP_KEY,
                new Payload("page/guide/debateAudit.html&", "跳转欠款审核列表"),
                "您有新的欠款审核，请及时处理!", "欠款审核");
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List<Target> targetList = new ArrayList(5);
        List<AppUserDevice> userDeviceList = new ArrayList<>();
        List<AppUserDevice> sellerDeviceList = userDeviceService.findAppUserDeviceByUserIdAndIdentityType(sellerId, AppIdentityType.SELLER);
        userDeviceList.addAll(sellerDeviceList);
        userDeviceList.forEach(p -> {
            Target target = new Target();
            target.setAppId(AppConstant.APP_ID);
            target.setClientId(p.getClientId());
            targetList.add(target);
        });
        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targetList);
        System.out.println(ret.getResponse().toString());

    }
}
