package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.returnOrder.*;
import org.apache.ibatis.annotations.Param;

/**
 * Created by caiyu on 2017/12/4.
 */
public interface OrderReturnService {
    /**
     * 添加保存退单基础信息
     * @param orderReturnBaseInfo   退单基础信息类
     */
    void saveOrderReturnBaseInfo(OrderReturnBaseInfo orderReturnBaseInfo);

    /**
     * 修改退单基础信息
     * @param orderReturnBaseInfo   退单基础信息类
     */
    void modifyOrderReturnBaseInfo(OrderReturnBaseInfo orderReturnBaseInfo);

    /**
     * 根据退单号查询退单基础信息
     * @param returnNo  退单号
     * @return  退单基础信息
     */
    OrderReturnBaseInfo queryByReturnNo(@Param("returnNo") String returnNo);

    /**
     * 添加保存退单退款总情况
     * @param orderReturnBilling    退款总和
     */
    void saveOrderReturnBilling(OrderReturnBilling orderReturnBilling);

    /**
     * 添加保存退单退款明细
     * @param orderReturnBillingDetail  退单退款明细
     */
    void saveOrderReturnBillingDetail(OrderReturnBillingDetail orderReturnBillingDetail);

    /**
     * 保存退单现金券明细
     * @param orderReturnCashCoupon 退单现金券
     */
    void saveOrderReturnCashCoupon(OrderReturnCashCoupon orderReturnCashCoupon);

    /**
     * 保存退单产品券明细
     * @param orderReturnProductCoupon  退单产品券
     */
    void saveOrderReturnProductCoupon(OrderReturnProductCoupon orderReturnProductCoupon);

    /**
     * 保存退单商品明细
     * @param orderReturnGoodsInfo  退单商品
     */
    void saveOrderReturnGoodsInfo(OrderReturnGoodsInfo orderReturnGoodsInfo);

}
