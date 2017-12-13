package cn.com.leyizhuang.app.foundation.pojo.returnorder;

import cn.com.leyizhuang.app.foundation.pojo.request.ReturnDeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:创建退货单传入参数
 * Created with IntelliJ IDEA.
 * Date: 2017/12/7.
 * Time: 19:41.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReturnOrderCreateParam implements Serializable {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户身份类型
     */
    private Integer identityType;
    /**
     * 代下单顾客id
     */
    private Long cusId;
    /**
     * 原订单编号
     */
    private String orderNo;
    /**
     * 退货地址信息
     */
    private ReturnDeliverySimpleInfo returnDeliveryInfo;
    /**
     * 退货备注信息
     */
    private String remarksInfo;
    /**
     * 退货原因
     */
    private String reasonInfo;
    /**
     * 退货商品数组
     */
    private List<GoodsSimpleInfo> returnGoodsInfo;
}
