package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppCustomerDAO;
import cn.com.leyizhuang.app.foundation.dao.CashCouponDAO;
import cn.com.leyizhuang.app.foundation.pojo.CashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.CustomerCashCoupon;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.service.CashCouponSendService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
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
    public ResultDTO<String> send(Long customerId, Long cashCouponId, Integer qty,Long optId){

        if(customerId != null && cashCouponId != null && qty > 0 ){
            AppCustomer appCustomer = cusertomerDAO.findById(customerId);
            CashCoupon cashCoupon = cashCouponDAO.queryById(cashCouponId);

            List<Long> storeIds = cashCouponDAO.queryStoreIdsByCcid(cashCoupon.getId());

            if (cashCoupon.getEffectiveEndTime().before(new Date())){
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发送失败，优惠券已经过期", null);
            }

//            if(cashCoupon.getCityId() != appCustomer.getCityId()){
//                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发送失败，"+appCustomer.getName()+"不再城市范围下！", null);
//            }

            if(cashCoupon.getIsSpecifiedStore() && !storeIds.contains(appCustomer.getStoreId())){
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发送失败，"+appCustomer.getName()+"不再指定门店范围下！", null);
            }else{
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
                    customerCashCoupon.setOptUserid(optId);


                    for (int i = 0;i < qty ; i++){
                        cashCouponDAO.addCustomerCashCoupon(customerCashCoupon);
                    }

                    Integer remainingQuantity = cashCoupon.getRemainingQuantity();
                    if(remainingQuantity <= 0 || qty > remainingQuantity){
                        return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发送失败，优惠券剩余数量不足", null);
                    }
                    cashCoupon.setRemainingQuantity(remainingQuantity - qty);
                    cashCouponDAO.updateCashCoupon(cashCoupon);
                }
            }
        }
        return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "发送成功", null);
    }

    /**
     * 批量发券
     * @param customerIdList
     * @param cashCouponId
     * @param qty
     */
    @Transactional
    public ResultDTO<String> sendBatch(List<Long> customerIdList,Long cashCouponId,Integer qty,Long optId){
        ResultDTO<String> result = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "未发送任何券", null);
        for (Long customerId : customerIdList) {
            result = this.send(customerId,cashCouponId,qty,optId);
            if (result.getCode().equals(-1)){
                break;
            }
        }

        return result;
    }

}
