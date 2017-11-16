package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

/**
 * 订单商品评价参数接受类
 * Created by caiyu on 2017/11/16.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderGoodsEvaluationRequest {
    //用户id
    private Long userId;
    //用户类型
    private Integer identityType;
    //产品星级
    private Integer productStar;
    //物流星级
    private Integer logisticsStar;
    //服务星级
    private Integer serviceStars;
    //订单编号
    private String orderNumber;
    //商品评价信息
    private String goodsList;

}
