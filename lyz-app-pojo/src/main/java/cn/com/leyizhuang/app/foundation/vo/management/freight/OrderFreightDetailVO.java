package cn.com.leyizhuang.app.foundation.vo.management.freight;

import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.SimpleOrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderFreightDetailVO {

    private Long id;
    //城市信息
    private String cityName;
    //门店信息
    private SimpleStoreParam storeId;
    //订单号
    private String ordNo;
    //下单人
    private String creatorName;
    //下单人电话
    private String creatorPhone;
    //简化订单账款明细
    private SimpleOrderBillingDetails simpleOrderBillingDetails;
    //下单时间
    private Date createTime;
    //商品总金额
    private BigDecimal totalGoodsPrice;
    //备注
    private String remark;
    //订单商品信息
    private List<MaOrderGoodsInfo> orderGoodsInfoList;
    //订单物流信息
    private MaOrderLogisticsInfo orderLogisticsInfo;
}


