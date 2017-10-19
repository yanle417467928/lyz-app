package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.ProductCouponDAO;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderUsableProductCouponResponse;
import cn.com.leyizhuang.app.foundation.service.ProductCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/19
 */
@Service
@Transactional
public class ProductCouponServiceImpl implements ProductCouponService {

    @Autowired
    private ProductCouponDAO productCouponDAO;

    @Override
    public List<OrderUsableProductCouponResponse> findProductCouponByCustomerIdAndGoodsId(Long userId, List<Long> goodsIds) {
        return this.productCouponDAO.findProductCouponByCustomerIdAndGoodsId(userId, goodsIds);
    }
}
