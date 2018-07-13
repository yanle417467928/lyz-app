package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.OrderCouponType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.dao.ProductCouponDAO;
import cn.com.leyizhuang.app.foundation.pojo.CusProductCouponMsgInfo;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCouponChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.ProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderGoodsInf;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderUsableProductCouponResponse;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.ProductCouponService;
import cn.com.leyizhuang.app.foundation.service.SmsAccountService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/19
 */
@Service("productCouponService")
@Transactional
public class ProductCouponServiceImpl implements ProductCouponService {

    @Autowired
    private ProductCouponDAO productCouponDAO;

    @Autowired
    private OrderDAO orderDAO;

    private GoodsService goodsService;

    @Autowired
    private SmsAccountService smsAccountService;

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
    public List<OrderCouponInfo> findOrderCouponByCouponTypeAndOrderId(Long orderId, OrderCouponType couponType) {
        return productCouponDAO.findOrderCouponByCouponTypeAndOrderId(orderId, couponType);
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
     *
     * @param productCoupon
     */
    @Override
    public void addProductCoupon(ProductCoupon productCoupon) {
        if (productCoupon != null) {
            productCouponDAO.addProductCoupon(productCoupon);
        }
    }

    /**
     * 更新产品券模版
     *
     * @param productCoupon
     */
    @Override
    public void updateProductCoupon(ProductCoupon productCoupon) {
        if (productCoupon != null) {
            productCouponDAO.updateProductCoupon(productCoupon);
        }
    }

    /**
     * 删除产品券模版
     */
    @Override
    @Transactional
    public void deletedProductCoupon(List<Long> ids) {
        if (ids != null && ids.size() > 0) {
            for (Long id : ids) {
                ProductCoupon productCoupon = productCouponDAO.queryProductCouponById(id);
                productCoupon.setStatus(false);
                productCouponDAO.updateProductCoupon(productCoupon);
            }
        }
    }

    @Override
    public PageInfo<ProductCoupon> queryPage(Integer page, Integer size, String keywords, String startTime, String endTime) {
        PageHelper.startPage(page, size);
        if (null != startTime && !"".equals(startTime)) {
            startTime += " 00:00:00";
        }
        if (null != endTime && !"".equals(endTime)) {
            endTime += " 23:59:59";
        }
        List<ProductCoupon> list = productCouponDAO.queryByKeywords(keywords, startTime, endTime);
        return new PageInfo<ProductCoupon>(list);
    }

    @Override
    public ProductCoupon queryProductCouponById(Long id) {

        if (id == null) {
            return null;
        }

        ProductCoupon productCoupon = productCouponDAO.queryProductCouponById(id);
        return productCoupon;
    }

    @Override
    public void updateCustomerProductCoupon(CustomerProductCoupon customerProductCoupon) {
        productCouponDAO.updateCustomerProductCoupon(customerProductCoupon);
    }

    @Override
    public void updateProductCouponIsReturn(Long id, Boolean isReturn) {
        productCouponDAO.updateProductCouponIsReturn(id, isReturn);
    }


    @Override
    public void addCustomerProductCouponChangeLog(CustomerProductCouponChangeLog customerProductCouponChangeLog) {
        productCouponDAO.addCustomerProductCouponChangeLog(customerProductCouponChangeLog);
    }

    @Override
    public void activateCusProductCoupon(String ordNo) {
        if (StringUtils.isNotBlank(ordNo)) {
            // 取半年后时间为失效时间
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime effectiveEndTime = now.plusMonths(6);

            productCouponDAO.activateCusProductCoupon(ordNo, effectiveEndTime);
        }

        // 找到这一单中的FW产品券 并扣减产品券原订单高端产品可退货数量
//        List<OrderGoodsInfo> productCouponList = orderDAO.getOrderGoodsByOrderNumberAndSkuAndGoodsLineType(ordNo,null,AppGoodsLineType.PRODUCT_COUPON.getValue());
//        for (OrderGoodsInfo productCoupon : productCouponList){
//            Long gid = productCoupon.getGid();
//
//            if (goodsService.isFWGoods(gid)){
//                // 服务产品券提货 如果是券类型为 present 则扣除bindsku 对应商品可退数量
//            }
//
//            String bindSku = "";
//            orderDAO.getOrderGoodsByOrderNumberAndSkuAndGoodsLineType(ordNo,bindSku, AppGoodsLineType.GOODS.getValue());
//        }

    }

    @Override
    public List<CusProductCouponMsgInfo> findExpiringSoonProductCoupon() {
       return productCouponDAO.findExpiringSoonProductCoupon();
    }


    @Override
    public void sendMsgForExpiringSoonProductCoupon(){
        List<CusProductCouponMsgInfo> ExpiringSoonProductCouponList = this.findExpiringSoonProductCoupon();
        for(CusProductCouponMsgInfo cusProductCouponMsgInfo:ExpiringSoonProductCouponList){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String efTime =  sdf.format(cusProductCouponMsgInfo.getEffectiveEndTime()).toString();
            String msg ="尊敬的顾客,您有"+ cusProductCouponMsgInfo.getQty() +"张的"+cusProductCouponMsgInfo.getSkuName()+"产品劵将于"+efTime+"到期，请及时至华润漆门店提货";
            smsAccountService.commonSendGBKSms(cusProductCouponMsgInfo.getMobile(), msg);
        }
    }

    @Override
    public CustomerProductCoupon findCustomerProductCouponByOrdNoAndSku(Long couponId) {
        if (null != couponId){
            return  productCouponDAO.findCustomerProductCouponByOrdNoAndSku(couponId);
        }
        return null;
    }
}
