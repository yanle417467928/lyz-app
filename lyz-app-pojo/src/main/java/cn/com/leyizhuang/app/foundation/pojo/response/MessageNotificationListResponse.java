package cn.com.leyizhuang.app.foundation.pojo.response;

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
    //活动图片
    private String pictureUrl;
}
