package cn.com.leyizhuang.app.foundation.pojo.message;

import lombok.*;

/**
 * 个推透传模板消息体
 *
 * @author Richard
 * Created on 2018-01-26 13:41
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Payload {

    /**
     * 通知跳转链接
     */
    private String banner;

    /**
     * 通知内容
     */
    private String info;


}
