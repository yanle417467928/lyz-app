package cn.com.leyizhuang.app.foundation.pojo.message;

import lombok.*;

import java.util.Date;

/**
 * App消息发送表
 *
 * @author Richard
 * Created on 2017-12-07 10:59
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppMsgClientInfo {

    private Long id;

    /**
     * 消息编号
     */
    private Long msgId;

    /**
     * 客户端编号
     */
    private String clientId;

    /**
     * 消息内容
     */
    private String msgContent;

    /**
     * 创建时间
     */
    private Date createTime;
}
