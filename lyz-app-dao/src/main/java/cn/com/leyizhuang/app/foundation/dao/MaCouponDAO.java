package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.coupon.MaCashCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.coupon.MaProductCouponInfo;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaCouponDAO {
    List<MaProductCouponInfo>  findProductCouponTypeByReturnOrder(String returnOrderNo);

    List<MaCashCouponInfo>  findCashCouponTypeByReturnOrderId(Long returnOrderId);
}
