package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 物流推送消息返回类
 * Created by caiyu on 2017/11/22.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsMessageResponse {
    /**
     * 消息
     */
    private String message;
    /**
     * 推送时间
     */
    private String createTime;
    /**
     * 订单号
     */
    private String orderNumber;
}
