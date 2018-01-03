package cn.com.leyizhuang.app.foundation.pojo.queue;

import lombok.*;

/**
 * 消息队列消息模型
 *
 * @author Richard
 * Created on 2018-01-02 8:57
 **/
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MqMessage {

    private String content;

    private MqMessageType type;

}
