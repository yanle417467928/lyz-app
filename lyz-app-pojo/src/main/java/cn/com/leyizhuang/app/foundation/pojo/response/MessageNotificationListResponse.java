package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import com.hp.hpl.sparta.xpath.ThisNodeTest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 通知消息
 * @author GenerationRoad
 * @date 2017/10/10
 */
@Getter
@Setter
@ToString
public class MessageNotificationListResponse {
    private Long id;
    //消息标题
    private String title;
    //消息详细
    private String detailed;
    //消息创建时间
    private Date createTime;
    //已读标记
    private Boolean isRead;
    //身份类型
    private AppIdentityType  identityType;
    //开始时间
    private  Date beginTime;
    //结束时间
    private  Date endTime;
    //消息状态
    private String status;
    //消息Id
    private Long messageId;
    //通知类型
    private String messageType;






}
