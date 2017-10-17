package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
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
    //消息创建时间
    private Date createTime;
}
