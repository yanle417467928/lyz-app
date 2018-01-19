package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.foundation.dao.ProductCouponDAO;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.ProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderUsableProductCouponResponse;
import cn.com.leyizhuang.app.foundation.service.ProductCouponService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
        if (null != userId && null != goodsIds) {
            List<OrderUsableProductCouponResponse> productCouponResponseList = this.productCouponDAO.findProductCouponByCustomerIdAndGoodsId(userId, goodsIds);
            //计算订单可使用产品卷（先查可参加的活动，再减去赠品）


            for (int i = 0; i < productCouponResponseList.size(); i++) {
                if (null != productCouponResponseList.get(i).getCoverImageUri()) {
                    String[] url = productCouponResponseList.get(i).getCoverImageUri().split(",");
                    if (url.length > 0) {
                        productCouponResponseList.get(i).setCoverImageUri(url[0]);
                    } else {
                        productCouponResponseList.get(i).setCoverImageUri("");
                    }
                }
                productCouponResponseList.get(i).setUsableNumber(1);
            }

            return productCouponResponseList;
        }
        return null;
    }

    @Override
    public Long findGoodsIdByUserIdAndProductCouponId(Long userId, Long pcId) {
        if (userId != null && pcId != null) {
            return productCouponDAO.findGoodsIdByUserIdAndProductCouponId(userId, pcId);
        }
        return null;
    }

    @Override
    public  List<OrderCouponInfo>  findOrderCouponByCouponTypeAndUserId(Long orderId, OrderCouponType couponType) {
        return productCouponDAO.findOrderCouponByCouponTypeAndUserId(orderId,couponType);
    }

    @Override
    public CustomerProductCoupon findCusProductCouponByCouponId(Long cusProductCouponId) {
        return productCouponDAO.findCusProductCouponByCouponId(cusProductCouponId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCustomerProductCoupon(CustomerProductCoupon customerProductCoupon) {
        productCouponDAO.addCustomerProductCoupon(customerProductCoupon);
    }

    /**
     * 新增产品券模版
     * @param productCoupon
     */
    @Override
    public void addProductCoupon(ProductCoupon productCoupon){
        if (productCoupon != null){
            productCouponDAO.addProductCoupon(productCoupon);
        }
    }

    /**
     * 更新产品券模版
     * @param productCoupon
     */
    @Override
    public void updateProductCoupon(ProductCoupon productCoupon){
        if (productCoupon != null){
            productCouponDAO.updateProductCoupon(productCoupon);
        }
    }

    /**
     * 删除产品券模版
     */
    @Override
    @Transactional
    public void deletedProductCoupon(List<Long> ids){
        if (ids != null && ids.size() > 0){
            for (Long id : ids){
                ProductCoupon productCoupon = productCouponDAO.queryProductCouponById(id);
                productCoupon.setStatus(false);
                productCouponDAO.updateProductCoupon(productCoupon);
            }
        }
    }

    @Override
    public PageInfo<ProductCoupon> queryPage(Integer page, Integer size, String keywords) {

        PageHelper.startPage(page, size);
        List<ProductCoupon> list = productCouponDAO.queryByKeywords(keywords);
        return new PageInfo<ProductCoupon>(list);
    }

    @Override
    public ProductCoupon queryProductCouponById(Long id){

        if (id == null){
            return null;
        }

        ProductCoupon productCoupon = productCouponDAO.queryProductCouponById(id);
        return productCoupon;
    }

}
