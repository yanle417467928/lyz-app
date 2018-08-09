package cn.com.leyizhuang.app.foundation.pojo.message;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import lombok.*;

import java.util.Date;

/**
 * @Author 王浩
 * @Date 2018/7/27 16:42
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageMemberConference {
    //自增id
    private Long id;
    //用户ID
    private Long userId;
    //消息ID
    private Long messageId;
    //创建时间
    private Date createTime;
    //是否已读
    private  Boolean isRead;
    //身份类型
    private AppIdentityType identityType;
    //门店ID
}
