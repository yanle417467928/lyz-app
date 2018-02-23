package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaCouponDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.coupon.MaCashCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.coupon.MaProductCouponInfo;
import cn.com.leyizhuang.app.foundation.service.MaCouponService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class MaCouponServiceImpl implements MaCouponService {
    @Resource
    private MaCouponDAO maCouponDAO;

    @Override
    public List<MaProductCouponInfo> findProductCouponTypeByReturnOrder(String returnOrderNo){
       return maCouponDAO.findProductCouponTypeByReturnOrder(returnOrderNo);
    }

    @Override
    public List<MaCashCouponInfo> findCashCouponTypeByReturnOrderId(Long returnOrderId){
        return maCouponDAO.findCashCouponTypeByReturnOrderId(returnOrderId);
    }
}
