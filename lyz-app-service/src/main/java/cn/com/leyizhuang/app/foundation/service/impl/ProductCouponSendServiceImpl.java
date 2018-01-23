package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppCustomerType;
import cn.com.leyizhuang.app.core.constant.CouponGetType;
import cn.com.leyizhuang.app.foundation.dao.AppCustomerDAO;
import cn.com.leyizhuang.app.foundation.dao.MaEmployeeDAO;
import cn.com.leyizhuang.app.foundation.dao.MaGoodsBrandDAO;
import cn.com.leyizhuang.app.foundation.dao.ProductCouponDAO;
import cn.com.leyizhuang.app.foundation.pojo.CustomerProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.ProductCoupon;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.service.MaEmployeeService;
import cn.com.leyizhuang.app.foundation.service.ProductCouponSendService;
import cn.com.leyizhuang.app.foundation.vo.management.employee.EmployeeDetailVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by panjie on 2018/1/11.
 */
@Service
public class ProductCouponSendServiceImpl implements ProductCouponSendService {

    @Resource
    private ProductCouponDAO productCouponDAO;

    @Resource
    private AppCustomerDAO cusertomerDAO;

    @Resource
    private MaEmployeeDAO maEmployeeDAO;


    @Override
    @Transactional
    public ResultDTO<String> send(Long customerId, Long productCouponId,Long sellerId, Integer qty){

        AppCustomer appCustomer = cusertomerDAO.findById(customerId);
        EmployeeDO employeeDO = maEmployeeDAO.queryEmployeeById(sellerId);

        if(appCustomer == null || employeeDO == null){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发送失败，导购或者顾客信息有误", null);
        }

        ProductCoupon productCoupon = productCouponDAO.queryProductCouponById(productCouponId);

        if (productCoupon.getEffectiveEndTime().before(new Date())){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发送失败，产品券已过期", null);
        }

        // 创建一张券
        CustomerProductCoupon customerProductCoupon = new CustomerProductCoupon();
        customerProductCoupon.setCustomerId(customerId);
        customerProductCoupon.setGoodsId(productCoupon.getGid());
        customerProductCoupon.setQuantity(1);
        customerProductCoupon.setGetType(CouponGetType.MANUAL_GRANT);
        customerProductCoupon.setGetTime(new Date());
        customerProductCoupon.setEffectiveStartTime(productCoupon.getEffectiveStartTime());
        customerProductCoupon.setEffectiveEndTime(productCoupon.getEffectiveEndTime());
        customerProductCoupon.setIsUsed(false);

        customerProductCoupon.setStoreId(employeeDO.getStoreId().getStoreId());
        customerProductCoupon.setSellerId(employeeDO.getEmpId());
        customerProductCoupon.setStatus(true);

        for (int i = 0;i < qty ; i++){
            productCouponDAO.addCustomerProductCoupon(customerProductCoupon);
        }

        // 扣除券模版数量
        Integer remainingQuantity = productCoupon.getRemainingQuantity();
        if(remainingQuantity <= 0 || qty > remainingQuantity){
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发送失败，优惠券剩余数量不足", null);
        }

        productCoupon.setRemainingQuantity(remainingQuantity - qty);
        productCouponDAO.updateProductCoupon(productCoupon);

        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "发送成功", null);
    }

    /**
     * 一键发送
     * @param customerIdList
     * @param productCouponId
     * @param sellerId
     * @param qty
     * @return
     */
    @Transactional
    public ResultDTO<String> sendBatch(List<Long> customerIdList, Long productCouponId, Long sellerId, Integer qty){
        ResultDTO<String> result = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "未发送任何券", null);
        for (Long customerId : customerIdList) {
            result = this.send(customerId,productCouponId,sellerId,qty);
            if (result.getCode().equals(-1)){
                break;
            }
        }
        return result;
    }

}
