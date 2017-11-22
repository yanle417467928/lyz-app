package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

/**
 * 订单商品评价参数接受类
 *
 * @author caiyu
 * @date 2017/11/16
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderGoodsEvaluationRequest {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户类型
     */
    private Integer identityType;
    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 评价内容
     */
    private String evaluationContent;

}
