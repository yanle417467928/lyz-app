package cn.com.leyizhuang.app.foundation.pojo.inventory.returning;


import cn.com.leyizhuang.app.core.constant.AppReturnOrderStatus;
import cn.com.leyizhuang.app.core.constant.ReturnOrderType;
import cn.com.leyizhuang.app.foundation.pojo.response.ReturnOrderDetailResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ReturnOrderGoodsResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes: app后台退货单管理实体类
 * Created with IntelliJ IDEA.
 * Date: 2018/1/19.
 * Time: 13:44.
 */
@Getter
@Setter
@ToString
public class Returning {

    private Long id;

    /**
     * 退单号
     */
    private String returnNo;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 退单类型（CANCEL_RETURN(1, "取消退货"), REFUSED_RETURN(2, "拒签退货"), NORMAL_RETURN(3, "正常退货")）
     */
    private ReturnOrderType returnType;

    /**
     * 物流相关信息
     */
    private ReturnOrderDetailResponse returnOrderDetailResponse;

    /**
     * 创建退单人电话号码
     */
    private String creatorPhone;
    /**
     * 顾客姓名
     */
    private String customerName;

    /**
     * 退货物流状态
     */
    private AppReturnOrderStatus returnStatus;

    /**
     * 备注
     */
    private String remarksInfo;

    /**
     * 退款金额
     */
    private Double returnPrice;
    /**
     * 商品信息
     */
    private List<ReturnOrderGoodsResponse> returnOrderGoodsList;
}
