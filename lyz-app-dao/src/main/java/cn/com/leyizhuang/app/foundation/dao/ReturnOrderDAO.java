package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by caiyu on 2017/12/4.
 */
@Repository
public interface ReturnOrderDAO {
    /**
     * 添加保存退单基础信息
     * @param returnOrderBaseInfo   退单基础信息类
     */
    void saveReturnOrderBaseInfo(ReturnOrderBaseInfo returnOrderBaseInfo);

    /**
     * 修改退单基础信息
     * @param returnOrderBaseInfo   退单基础信息类
     */
    void modifyReturnOrderBaseInfo(ReturnOrderBaseInfo returnOrderBaseInfo);

    /**
     * 根据退单号查询退单基础信息
     * @param returnNo  退单号
     * @return  退单基础信息
     */
    ReturnOrderBaseInfo queryByReturnNo(@Param("returnNo") String returnNo);

    /**
     * 添加保存退单退款总情况
     * @param returnOrderBilling    退款总和
     */
    void saveReturnOrderBilling(ReturnOrderBilling returnOrderBilling);

    /**
     * 添加保存退单退款明细
     * @param returnOrderBillingDetail  退单退款明细
     */
    void saveReturnOrderBillingDetail(ReturnOrderBillingDetail returnOrderBillingDetail);

    /**
     * 保存退单现金券明细
     * @param returnOrderCashCoupon 退单现金券
     */
    void saveReturnOrderCashCoupon(ReturnOrderCashCoupon returnOrderCashCoupon);

    /**
     * 保存退单产品券明细
     * @param returnOrderProductCoupon  退单产品券
     */
    void saveReturnOrderProductCoupon(ReturnOrderProductCoupon returnOrderProductCoupon);

    /**
     * 保存退单商品明细
     * @param returnOrderGoodsInfo  退单商品
     */
    void saveReturnOrderGoodsInfo(ReturnOrderGoodsInfo returnOrderGoodsInfo);

    void modifyReturnOrderBillingDetail(ReturnOrderBillingDetail returnOrderBillingDetail);

    List<ReturnOrderBaseInfo> findReturnOrderListByUserIdAndIdentityType(@Param("userId") Long userId,
                                                                         @Param("identityType") AppIdentityType identityType,
                                                                         @Param("status") Integer showStatus);

    List<ReturnOrderGoodsInfo> findReturnOrderGoodsInfoByOrderNumber(String returnNo);

    List<GiftListResponseGoods> getReturnOrderGoodsDetails(String returnNo);

}