package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.dao.ReturnOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.service.ReturnOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caiyu on 2017/12/4.
 */
@Service
public class ReturnOrderServiceImpl implements ReturnOrderService {
    @Autowired
    private ReturnOrderDAO returnOrderDAO;

    @Override
    public void saveReturnOrderBaseInfo(ReturnOrderBaseInfo returnOrderBaseInfo) {
        returnOrderDAO.saveReturnOrderBaseInfo(returnOrderBaseInfo);
    }

    @Override
    public void modifyReturnOrderBaseInfo(ReturnOrderBaseInfo returnOrderBaseInfo) {
        returnOrderDAO.modifyReturnOrderBaseInfo(returnOrderBaseInfo);
    }

    @Override
    public ReturnOrderBaseInfo queryByReturnNo(String returnNo) {
        return returnOrderDAO.queryByReturnNo(returnNo);
    }

    @Override
    public void saveReturnOrderBilling(ReturnOrderBilling returnOrderBilling) {
        returnOrderDAO.saveReturnOrderBilling(returnOrderBilling);
    }

    @Override
    public void saveReturnOrderBillingDetail(ReturnOrderBillingDetail returnOrderBillingDetail) {
        returnOrderDAO.saveReturnOrderBillingDetail(returnOrderBillingDetail);
    }

    @Override
    public void saveReturnOrderCashCoupon(ReturnOrderCashCoupon returnOrderCashCoupon) {
        returnOrderDAO.saveReturnOrderCashCoupon(returnOrderCashCoupon);
    }

    @Override
    public void saveReturnOrderProductCoupon(ReturnOrderProductCoupon returnOrderProductCoupon) {
        returnOrderDAO.saveReturnOrderProductCoupon(returnOrderProductCoupon);
    }

    @Override
    public void saveReturnOrderGoodsInfo(ReturnOrderGoodsInfo returnOrderGoodsInfo) {
        returnOrderDAO.saveReturnOrderGoodsInfo(returnOrderGoodsInfo);
    }

    @Override
    public void modifyReturnOrderBillingDetail(ReturnOrderBillingDetail orderReturnBillingDetail) {
        if (orderReturnBillingDetail != null) {
            returnOrderDAO.modifyReturnOrderBillingDetail(orderReturnBillingDetail);
        }
    }

    @Override
    public List<ReturnOrderBaseInfo> findReturnOrderListByUserIdAndIdentityType(Long userId, Integer identityType, Integer showStatus) {
        if (userId != null && identityType != null && showStatus != null) {
            return returnOrderDAO.findReturnOrderListByUserIdAndIdentityType(userId,
                    AppIdentityType.getAppIdentityTypeByValue(identityType), showStatus);
        }
        return null;
    }

    @Override
    public List<ReturnOrderGoodsInfo> findReturnOrderGoodsInfoByOrderNumber(String returnNo) {
        if (returnNo != null) {
            return returnOrderDAO.findReturnOrderGoodsInfoByOrderNumber(returnNo);
        }
        return null;
    }

    @Override
    public List<GiftListResponseGoods> getReturnOrderGoodsDetails(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            return returnOrderDAO.getReturnOrderGoodsDetails(returnNumber);
        }
        return null;
    }

}
