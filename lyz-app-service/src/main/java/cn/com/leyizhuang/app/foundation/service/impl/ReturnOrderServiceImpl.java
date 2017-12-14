package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.OrderSaveException;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.dao.ReturnOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.ReturnDeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by caiyu on 2017/12/4.
 */
@Service
public class ReturnOrderServiceImpl implements ReturnOrderService {
    @Resource
    private ReturnOrderDAO returnOrderDAO;
    @Resource
    private OrderDAO orderDAO;
    @Resource
    private AppStoreService appStoreService;
    @Resource
    private StorePreDepositLogService storePreDepositLogService;
    @Resource
    private StoreCreditMoneyLogService storeCreditMoneyLogService;
    @Resource
    private AppEmployeeService appEmployeeService;
    @Resource
    private AppCustomerService appCustomerService;
    @Resource
    private ProductCouponService productCouponService;
    @Resource
    private LeBiVariationLogService leBiVariationLogService;
    @Resource
    private CashCouponService cashCouponService;
    @Resource
    private ReturnOrderService returnOrderService;

    @Override
    public ReturnOrderBaseInfo createReturnOrderBaseInfo(Long orderId, String orderNo, Date orderTime, String remarksInfo, Long creatorId,
                                                         Integer creatorIdentityType, String reasonInfo, String returnPic, AppOrderType orderType) {

        ReturnOrderBaseInfo baseInfo = new ReturnOrderBaseInfo();
        baseInfo.setOrderId(orderId);
        baseInfo.setOrderNo(orderNo);
        baseInfo.setOrderTime(orderTime);
        baseInfo.setOrderType(orderType);
        baseInfo.setRemarksInfo(remarksInfo);
        baseInfo.setCreatorId(creatorId);
        baseInfo.setCreatorIdentityType(AppIdentityType.getAppIdentityTypeByValue(creatorIdentityType));
        baseInfo.setReasonInfo(reasonInfo);
        baseInfo.setReturnPic(returnPic);
        baseInfo.setReturnTime(Calendar.getInstance().getTime());
        baseInfo.setReturnNo(OrderUtils.getReturnNumber());
        baseInfo.setReturnType(ReturnOrderType.NORMAL_RETURN);
        baseInfo.setReturnStatus(AppReturnOrderStatus.PENDING_PICK_UP);
        if (creatorIdentityType == 6) {
            AppCustomer customer = appCustomerService.findById(creatorId);
            baseInfo.setCustomerType(customer.getCustomerType());
            baseInfo.setCreatorPhone(customer.getMobile());
        } else if (creatorIdentityType == 2) {
            AppEmployee employee = appEmployeeService.findById(creatorId);
            baseInfo.setCreatorPhone(employee.getMobile());
        }
        return baseInfo;
    }

    @Override
    public ReturnOrderLogisticInfo createReturnOrderLogisticInfo(ReturnDeliverySimpleInfo returnDeliveryInfo) {
        ReturnOrderLogisticInfo returnOrderLogisticInfo = new ReturnOrderLogisticInfo();
        if (returnDeliveryInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.RETURN_STORE.getValue())) {
            returnOrderLogisticInfo.setDeliveryType(AppDeliveryType.RETURN_STORE);
            returnOrderLogisticInfo.setReturnStoreCode(returnDeliveryInfo.getReturnStoreCode());
            returnOrderLogisticInfo.setReturnStoreName(returnDeliveryInfo.getReturnStoreName());
            returnOrderLogisticInfo.setReturnStoreAddress(returnDeliveryInfo.getReturnStoreAddress());
        } else if (returnDeliveryInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.HOUSE_PICK.getValue())) {
            returnOrderLogisticInfo.setDeliveryType(AppDeliveryType.HOUSE_PICK);
            returnOrderLogisticInfo.setDeliveryTime(returnDeliveryInfo.getDeliveryTime());
            returnOrderLogisticInfo.setRejecter(returnDeliveryInfo.getRejecter());
            returnOrderLogisticInfo.setRejecterPhone(returnDeliveryInfo.getRejecterPhone());
            returnOrderLogisticInfo.setDeliveryCity(returnDeliveryInfo.getDeliveryCity());
            returnOrderLogisticInfo.setDeliveryCounty(returnDeliveryInfo.getDeliveryCounty());
            returnOrderLogisticInfo.setDeliveryStreet(returnDeliveryInfo.getDeliveryStreet());
            returnOrderLogisticInfo.setResidenceName(returnDeliveryInfo.getResidenceName());
            returnOrderLogisticInfo.setDetailedAddress(returnDeliveryInfo.getDetailedAddress());
            returnOrderLogisticInfo.setReturnFullAddress(returnDeliveryInfo.getDeliveryCity() +
                    returnDeliveryInfo.getDeliveryCounty() +
                    returnDeliveryInfo.getDeliveryStreet() +
                    returnDeliveryInfo.getResidenceName() +
                    returnDeliveryInfo.getDetailedAddress());

        }

        return returnOrderLogisticInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveReturnOrderRelevantInfo(ReturnOrderBaseInfo returnOrderBaseInfo, ReturnOrderLogisticInfo returnOrderLogisticInfo,
                                            List<ReturnOrderGoodsInfo> returnOrderGoodsInfos, ReturnOrderBilling returnOrderBilling,
                                            List<ReturnOrderProductCoupon> productCouponList, List<OrderGoodsInfo> orderGoodsInfoList) throws OrderSaveException {

        if (null != returnOrderBaseInfo) {
            returnOrderDAO.saveReturnOrderBaseInfo(returnOrderBaseInfo);
            if (null != returnOrderBaseInfo.getRoid()) {
                Long roid = returnOrderBaseInfo.getRoid();
                if (null != returnOrderLogisticInfo) {
                    returnOrderLogisticInfo.setRoid(roid);
                    returnOrderDAO.saveReturnOrderLogisticsInfo(returnOrderLogisticInfo);
                }
                if (null != returnOrderGoodsInfos && !returnOrderGoodsInfos.isEmpty()) {
                    for (ReturnOrderGoodsInfo goodsInfo : returnOrderGoodsInfos) {
                        goodsInfo.setRoid(roid);
                        returnOrderDAO.saveReturnOrderGoodsInfo(goodsInfo);
                    }
                }
                if (null != returnOrderBilling) {
                    returnOrderBilling.setRoid(roid);
                    returnOrderDAO.saveReturnOrderBilling(returnOrderBilling);
                }
                if (null != productCouponList && !productCouponList.isEmpty()) {
                    for (ReturnOrderProductCoupon productCoupon : productCouponList) {
                        productCoupon.setRoid(roid);
                        returnOrderDAO.saveReturnOrderProductCoupon(productCoupon);
                    }
                }
                if (null != orderGoodsInfoList && !orderGoodsInfoList.isEmpty()) {
                    for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                        //修改这个数量
                        orderDAO.updateOrderGoodsInfo(orderGoodsInfo);
                    }
                }
            } else {
                throw new OrderSaveException("退单主键生成失败!");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public void saveReturnOrderGoodsInfo(ReturnOrderGoodsInfo returnOrderGoodsInfo) {
        returnOrderDAO.saveReturnOrderGoodsInfo(returnOrderGoodsInfo);
    }


    @Override
    public void saveReturnOrderLogisticsInfo(ReturnOrderLogisticInfo returnOrderLogisticInfo) {
        returnOrderDAO.saveReturnOrderLogisticsInfo(returnOrderLogisticInfo);
    }

    @Override
    public void modifyReturnOrderBillingDetail(ReturnOrderBillingDetail orderReturnBillingDetail) {
        if (orderReturnBillingDetail != null) {
            returnOrderDAO.modifyReturnOrderBillingDetail(orderReturnBillingDetail);
        }
    }

    @Override
    public List<ReturnOrderBaseInfo> findReturnOrderListByUserIdAndIdentityType(Long userId, Integer identityType) {
        if (userId != null && identityType != null) {
            return returnOrderDAO.findReturnOrderListByUserIdAndIdentityType(userId,
                    AppIdentityType.getAppIdentityTypeByValue(identityType));
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

    @Override
    public ReturnOrderLogisticInfo getReturnOrderLogisticeInfo(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            return returnOrderDAO.getReturnOrderLogisticeInfo(returnNumber);
        }
        return null;
    }
}
