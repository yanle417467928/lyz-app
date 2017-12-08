package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CustomerCashCoupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by caiyu on 2017/12/8.
 */
@Repository
public interface CashCouponDAO {
    /**
     *  获取现金券信息
     * @param couponId
     * @return
     */
    CashCoupon findCashCouponByOrderNumber(@Param("couponId")Long couponId);

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
}
