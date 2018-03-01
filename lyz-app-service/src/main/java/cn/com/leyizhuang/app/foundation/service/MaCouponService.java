package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.coupon.MaCashCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.coupon.MaProductCouponInfo;

import java.util.List;

public interface MaCouponService {
    List<MaProductCouponInfo> findProductCouponTypeByReturnOrder(String returnOrderNo);

    List<MaCashCouponInfo> findCashCouponTypeByReturnOrderId(Long returnOrderId);
}
