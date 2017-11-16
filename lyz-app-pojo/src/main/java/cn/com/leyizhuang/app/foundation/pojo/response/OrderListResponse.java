package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

/**
 * 我的订单列表返回类
 * Created by caiyu on 2017/11/13.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse {
    //订单编号
    private String orderNo;
    //订单状态
    private String status;
    //商品图片
    private List<String> goodsImgList;
    //商品总数量
    private Integer count;
    //应付/实付金额
    private Double price;
    //过期时间
    private Long endTime;
    //配送方式
    private String deliveryType;
}
