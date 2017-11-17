package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

/**
 * 订单评价参数类
 *
 * @author caiyu
 * @date 2017/11/16
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvaluationRequest {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户类型
     */
    private Integer identityType;
    /**
     * 产品星级
     */
    private Integer productStar;
    /**
     * 物流星级
     */
    private Integer logisticsStar;
    /**
     * 服务星级
     */
    private Integer serviceStars;
    /**
     * 订单编号
     */
    private String orderNumber;

}
