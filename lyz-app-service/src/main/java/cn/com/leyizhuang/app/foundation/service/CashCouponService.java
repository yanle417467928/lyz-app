package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CustomerCashCoupon;
import com.github.pagehelper.PageInfo;

/**
 * Created by caiyu on 2017/12/8.
 */
public interface CashCouponService {
    /**
     *  获取现金券信息
     * @param couponId
     * @return
     */
    CashCoupon findCashCouponByOrderNumber(Long couponId);

    /**
     * 添加现金券模板
     * @param cashCoupon    现金券
     */
    void addCashCoupon(CashCoupon cashCoupon);

    /**
     * 给顾客添加现金券
     * @param customerCashCoupon
     */
    void addCustomerCashCoupon(CustomerCashCoupon customerCashCoupon);

    /**
     * 现金券 gridData
     */
    PageInfo<CashCoupon> queryPage(Integer page, Integer size, String keywords);

}
