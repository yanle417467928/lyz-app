package cn.com.leyizhuang.app.foundation.pojo.message;

import cn.com.leyizhuang.app.core.constant.ActStatusType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by 王浩 on 2018/7/23.
 * 消息基础实体
 */


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageListDO implements Serializable {
    private static final long serialVersionUID;

    static {
        serialVersionUID = 1L;
    }

    private Long id;

    //消息标题
    private  String title;

    //消息详情
    private  String detailed;


    //身份类型
    private  String identityType;


    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //城市ID
    private Long cityId;

    //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    // 消息状态
    private ActStatusType status;

    //推送范围
    private String scope;

    //消息类型
    private String messageType;


}
