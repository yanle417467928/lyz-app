package cn.com.leyizhuang.app.foundation.pojo.message;

import cn.com.leyizhuang.app.core.constant.AppMsgPushType;
import cn.com.leyizhuang.app.core.constant.AppMsgSendSystemType;
import cn.com.leyizhuang.app.core.constant.AppSystemType;
import lombok.*;

import java.util.Date;

/**
 * 消息信息表
 *
 * @author Richard
 * Created on 2017-12-07 10:59
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppMsgInfo {

    private Long id;

    /**
     * 消息发送终端系统类型
     */
    private AppMsgSendSystemType sendSystemType;

    /**
     * 消息推送用户群体
     */
    private String users;

    /**
     * 消息推送类别
     */
    private AppMsgPushType pushType;

    /**
     * 链接
     */
    private String link;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;


    /**
     * 创建时间
     */
    private Date createTime;
}
