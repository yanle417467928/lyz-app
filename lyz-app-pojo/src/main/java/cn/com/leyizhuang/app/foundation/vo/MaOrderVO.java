package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台管理订单列表显示类
 * Created by caiyu on 2017/12/16.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaOrderVO {
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 会员号
     */
    private String meberNumber;
    /**
     * 会员姓名
     */
    private String meberName;
    /**
     * 配送方式
     */
    private AppDeliveryType deliveryType;
    /**
     * 订单状态
     */
    private AppOrderStatus status;
    /**
     * 订单创建时间
     */
    private Date createTime;
    /**
     * 门店
     */
    private String storeName;
    /**
     * 应付金额
     */
    private Double orderPrice;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 顾客姓名
     */
    private String customerName;
    /**
     * 顾客电话
     */
    private String customerPhone;

}
