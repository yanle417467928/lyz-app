package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppCustomerDAO;
import cn.com.leyizhuang.app.foundation.dao.CashCouponDAO;
import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CustomerCashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.service.CashCouponSendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 优惠券发放服务类
 * Created by panjie on 2017/12/27.
 */
@Service
public class CashCouponSendServiceImpl implements CashCouponSendService{

    @Resource
    private CashCouponDAO cashCouponDAO;

    @Resource
    private AppCustomerDAO cusertomerDAO;

    /**
     *
     * @param customerId 顾客id
     * @param cashCouponId 优惠券id
     * @param qty 优惠券数量
     */
    @Transactional
    public void send(Long customerId,Long cashCouponId,Integer qty){

        if(customerId != null && cashCouponId != null && qty > 0 ){
            AppCustomer appCustomer = cusertomerDAO.findById(customerId);
            CashCoupon cashCoupon = cashCouponDAO.queryById(cashCouponId);

            if(appCustomer != null && cashCoupon != null ){
                CustomerCashCoupon customerCashCoupon = new CustomerCashCoupon();

                customerCashCoupon.setCusId(appCustomer.getCusId());
                customerCashCoupon.setCcid(cashCoupon.getId());
                customerCashCoupon.setQty(1);
                customerCashCoupon.setIsUsed(false);
                customerCashCoupon.setGetTime(new Date());
                customerCashCoupon.setCondition(cashCoupon.getCondition());
                customerCashCoupon.setDenomination(cashCoupon.getDenomination());
                customerCashCoupon.setEffectiveStartTime(cashCoupon.getEffectiveStartTime());
                customerCashCoupon.setEffectiveEndTime(cashCoupon.getEffectiveEndTime());
                customerCashCoupon.setDescription(cashCoupon.getDescription());
                customerCashCoupon.setTitle(cashCoupon.getTitle());
                customerCashCoupon.setStatus(true);
                customerCashCoupon.setCityId(cashCoupon.getCityId());
                customerCashCoupon.setCityName(cashCoupon.getCityName());
                customerCashCoupon.setIsSpecifiedStore(cashCoupon.getIsSpecifiedStore());
                customerCashCoupon.setType(cashCoupon.getType());

                for (int i = 0;i < qty ; i++){
                    cashCouponDAO.addCustomerCashCoupon(customerCashCoupon);
                }
            }

        }
    }

    /**
     * 批量发券
     * @param customerIdList
     * @param cashCouponId
     * @param qty
     */
    @Transactional
    public void sendBatch(List<Long> customerIdList,Long cashCouponId,Integer qty){
        for (Long customerId : customerIdList) {
            this.send(customerId,cashCouponId,qty);
        }

    }

}
