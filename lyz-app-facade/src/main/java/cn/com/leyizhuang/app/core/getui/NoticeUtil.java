package cn.com.leyizhuang.app.core.getui;

import cn.com.leyizhuang.app.core.constant.ApplicationConstant;
import cn.com.leyizhuang.app.foundation.pojo.message.AppMsgInfo;
import cn.com.leyizhuang.app.foundation.pojo.message.AppUserDevice;
import com.gexin.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息推送工具类
 *
 * @author Richard
 */
public class NoticeUtil {

    private static Logger log = LoggerFactory.getLogger(NoticeUtil.class);

    private static final String IOS = "iOS";
    private static final String ANDROID = "Android";

    public static void main(String[] args) throws Exception {

    }

    /**
     * 发送消息至所有安装app用户
     * 通过clientid 发送
     */
    public static IPushResult pushMsgToAll(AppMsgInfo appMsgInfo) {
        if (StringUtils.isEmpty(appMsgInfo)) {
            log.error("方法 pushMsgToAll 参数 appMsgInfo不能为空!");
            return null;
        }
        System.setProperty("gexin_pushList_needDetails", "true");

        IGtPush push = new IGtPush(ApplicationConstant.GE_TUI_HOST, ApplicationConstant.APP_KEY, ApplicationConstant.MASTER_SECRET);
        AppMessage message = new AppMessage();
        // 通知透传模板
        //TransmissionTemplate template = getTransmissionTemplate(appMsgInfo);
       //点击通知打开应用模板
        LinkTemplate linkTemplate = getLinkTemplate(appMsgInfo);
        message.setData(linkTemplate);
        message.setOffline(true);
        //离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        //推送给App的目标用户需要满足的条件
        AppConditions cdt = new AppConditions();

        List<String> appIdList = new ArrayList<String>();
        appIdList.add(ApplicationConstant.APP_ID);
        message.setAppIdList(appIdList);
        message.setConditions(cdt);
        IPushResult ret = push.pushMessageToApp(message);
        System.out.println(ret.getResponse().toString());
        return ret;
    }

    /**
     * 分平台发送消息至app用户 Android、iOS
     * 通过clientid 发送
     */
    public static IPushResult pushMsgByType(AppMsgInfo appMsgInfo, String type) {
        if (StringUtils.isEmpty(appMsgInfo) || !StringUtils.hasText(type)) {
            log.error("in NoticeUtil#pushMsgToUsers method,appMsgInfo or type is null");
            return null;
        }
        System.setProperty("gexin_pushList_needDetails", "true");
        IGtPush push = new IGtPush(ApplicationConstant.GE_TUI_HOST, ApplicationConstant.APP_KEY, ApplicationConstant.MASTER_SECRET);
        AppMessage message = new AppMessage();

        TransmissionTemplate template = getTransmissionTemplate(appMsgInfo);
        message.setData(template);
        message.setOffline(true);
        //离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        //推送给App的目标用户需要满足的条件
        AppConditions cdt = new AppConditions();
        List<String> appIdList = new ArrayList<String>();
        appIdList.add(ApplicationConstant.APP_ID);
        message.setAppIdList(appIdList);
        //手机类型
        List<String> phoneTypeList = new ArrayList<String>();
        // Android/iOS
        phoneTypeList.add(type.toUpperCase());
        cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
        message.setConditions(cdt);
        IPushResult ret = push.pushMessageToApp(message);
        System.out.println(ret.getResponse().toString());
        return ret;
    }

    /**
     * 发送消息至指定app用户
     * 通过clientid 发送
     */
    public static IPushResult pushMsgToUsers(AppMsgInfo appMsgInfo, List<AppUserDevice> appUserDeviceList) {
        if (appUserDeviceList == null || appUserDeviceList.size() == 0 || StringUtils.isEmpty(appMsgInfo)) {
            log.error("in NoticeUtil#pushMsgToUsers method,appMsgInfo or clientList is null");
            return null;
        }
        System.setProperty("gexin_pushList_needDetails", "true");
        List<Target> list = getTargets(appUserDeviceList);
        // 配置返回每个别名及其对应cid的用户状态，可选
        IGtPush push = new IGtPush(ApplicationConstant.GE_TUI_HOST, ApplicationConstant.APP_KEY, ApplicationConstant.MASTER_SECRET);
        ListMessage message = new ListMessage();
        TransmissionTemplate template = getTransmissionTemplate(appMsgInfo);
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, list);
        System.out.println(ret.getResponse().toString());
        return ret;
    }

    public static LinkTemplate getLinkTemplate(AppMsgInfo appMsgInfo) {
        LinkTemplate template = new LinkTemplate();
        template.setAppId(ApplicationConstant.APP_ID);
        template.setAppkey(ApplicationConstant.APP_KEY);

        // ********** 设置通知栏标题与内容 *********
        //推送标题
        template.setTitle(appMsgInfo.getTitle());
        //推送内容
        template.setText(appMsgInfo.getContent());
        // 配置通知栏图标
        template.setLogo("");
        // 配置通知栏网络图标
        template.setLogoUrl("");
        // 设置通知是否响铃，震动，或者可清除
        template.setIsRing(true);
        template.setIsVibrate(true);
        template.setIsClearable(true);
        //推送url路径
        template.setUrl(appMsgInfo.getLink());
        return template;
    }

    public static TransmissionTemplate getTransmissionTemplate(AppMsgInfo appMsgInfo) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(ApplicationConstant.APP_ID);
        template.setAppkey(ApplicationConstant.APP_KEY);
        template.setTransmissionContent(JSONObject.toJSON(appMsgInfo).toString());
        template.setTransmissionType(2);
        APNPayload payload = new APNPayload();
        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
        payload.setAutoBadge("+1");
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.setCategory("$由客户端定义");

        //简单模式APNPayload.SimpleMsg
        //  payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));
        // 自定义类型参数,推送类型1、全部，2android，3iOS，4 用户Id
        payload.addCustomMsg("type", appMsgInfo.getSendSystemType());
        //推送url路径
        payload.addCustomMsg("link", appMsgInfo.getLink());

        //字典模式使用APNPayload.DictionaryAlertMsg
        payload.setAlertMsg(getDictionaryAlertMsg(appMsgInfo));

        // 添加多媒体资源
   /* payload.addMultiMedia(new MultiMedia().setResType(MultiMedia.MediaType.video)
            .setResUrl("http://ol5mrj259.bkt.clouddn.com/test2.mp4")
            .setOnlyWifi(true));*/

        template.setAPNInfo(payload);
        return template;
    }

    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(AppMsgInfo appMsgInfo) {
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        //推送标题
        alertMsg.setTitle(appMsgInfo.getTitle());
        //推送内容
        alertMsg.setBody(appMsgInfo.getContent());
        alertMsg.setActionLocKey("ActionLockey");
        alertMsg.setLocKey("LocKey");
        alertMsg.addLocArg("loc-args");
        alertMsg.setLaunchImage("launch-image");
        alertMsg.setTitleLocKey("TitleLocKey");
        alertMsg.addTitleLocArg("TitleLocArg");
        return alertMsg;
    }

    public static List<Target> getTargets(List<AppUserDevice> appUserDeviceList) {
        List list = new ArrayList();
        for (int i = 0; i < appUserDeviceList.size(); i++) {
            Target temp = new Target();
            temp.setAppId(ApplicationConstant.APP_ID);
            temp.setClientId(appUserDeviceList.get(i).getClientId());
            list.add(temp);
        }
        return list;
    }
}