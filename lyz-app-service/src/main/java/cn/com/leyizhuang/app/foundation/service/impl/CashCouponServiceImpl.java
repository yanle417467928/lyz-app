package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.CashCouponDAO;
import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CustomerCashCoupon;
import cn.com.leyizhuang.app.foundation.service.CashCouponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by caiyu on 2017/12/8.
 */
@Service
public class CashCouponServiceImpl implements CashCouponService{
    @Resource
    private CashCouponDAO cashCouponDAO;

    @Override
    public CashCoupon findCashCouponByOrderNumber(Long couponId) {
        return cashCouponDAO.findCashCouponByOrderNumber(couponId);
    }

    @Override
    public void addCashCoupon(CashCoupon cashCoupon) {
        cashCouponDAO.addCashCoupon(cashCoupon);
    }

    @Override
    public void addCustomerCashCoupon(CustomerCashCoupon customerCashCoupon) {

    }
}
