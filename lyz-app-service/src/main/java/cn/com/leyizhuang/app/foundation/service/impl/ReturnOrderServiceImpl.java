package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.OrderSaveException;
import cn.com.leyizhuang.app.core.exception.SystemBusyException;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.OrderDAO;
import cn.com.leyizhuang.app.foundation.dao.ReturnOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.request.ReturnDeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by caiyu on 2017/12/4.
 */
@Service
public class ReturnOrderServiceImpl implements ReturnOrderService {
    private static final Logger logger = LoggerFactory.getLogger(ReturnOrderServiceImpl.class);
    @Resource
    private ReturnOrderDAO returnOrderDAO;
    @Resource
    private OrderDAO orderDAO;
    @Resource
    private AppOrderService appOrderService;
    @Resource
    private AppEmployeeService employeeService;
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
    private AppToWmsOrderService appToWmsOrderService;
    @Resource
    private ProductCouponService productCouponService;
    @Resource
    private LeBiVariationLogService leBiVariationLogService;
    @Resource
    private CashCouponService cashCouponService;
    @Resource
    private ReturnOrderService returnOrderService;
    @Resource
    private CityService cityService;
    @Resource
    private CommonService commonService;
    @Resource
    private  OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;

    @Override
    public ReturnOrderBaseInfo createReturnOrderBaseInfo(Long orderId, String orderNo, Date orderTime, String remarksInfo, Long creatorId,
                                                         Integer creatorIdentityType, String reasonInfo, String returnPic, AppOrderType orderType,
                                                         Long storeId, String storeCode, String storeStructureCode) {

        ReturnOrderBaseInfo baseInfo = new ReturnOrderBaseInfo();
        baseInfo.setOrderId(orderId);
        baseInfo.setOrderNo(orderNo);
        baseInfo.setOrderTime(orderTime);
        baseInfo.setOrderType(orderType);
        baseInfo.setRemarksInfo(remarksInfo);
        baseInfo.setStoreId(storeId);
        baseInfo.setStoreCode(storeCode);
        baseInfo.setStoreStructureCode(storeStructureCode);
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
                        //保存发送wms退货商品明细
                        AtwRequisitionOrderGoods orderGoods = AtwRequisitionOrderGoods.transform(returnOrderBaseInfo.getReturnNo(), orderGoodsInfo.getSku(),
                                orderGoodsInfo.getSkuName(), orderGoodsInfo.getRetailPrice(), orderGoodsInfo.getOrderQuantity(), orderGoodsInfo.getCompanyFlag());
                        appToWmsOrderService.saveAtwRequisitionOrderGoods(orderGoods);
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
    public PageInfo<ReturnOrderBaseInfo> findReturnOrderListByUserIdAndIdentityType(Long userId, Integer identityType, Integer page, Integer size) {
        if (userId != null && identityType != null) {
            PageHelper.startPage(page, size);
            List<ReturnOrderBaseInfo> returnOrderList = returnOrderDAO.findReturnOrderListByUserIdAndIdentityType(userId,
                    AppIdentityType.getAppIdentityTypeByValue(identityType));
            return new PageInfo<>(returnOrderList);
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

    @Override
    public void updateReturnOrderStatus(String returnNumber, AppReturnOrderStatus finished) {
        if (StringUtils.isNotBlank(returnNumber)) {
            returnOrderDAO.updateReturnOrderStatus(returnNumber, finished);
        }
    }

    @Override
    public void updateReturnableQuantityAndReturnQuantityById(Integer returnQty, Integer returnableQty, Long id) {
        returnOrderDAO.updateReturnableQuantityAndReturnQuantityById(returnQty, returnableQty, id);
    }

    @Override
    public List<ReturnOrderCashCoupon> getReturnOrderCashCouponByRoid(Long roid) {
        if (null != roid) {
            return returnOrderDAO.getReturnOrderCashCouponByRoid(roid);
        }
        return null;
    }

    @Override
    public List<ReturnOrderProductCoupon> getReturnOrderProductCouponByRoid(Long roid) {
        if (null != roid) {
            return returnOrderDAO.getReturnOrderProductCouponByRoid(roid);
        }
        return null;
    }

    @Override
    public List<ReturnOrderBillingDetail> getReturnOrderBillingDetailByRoid(Long roid) {
        if (null != roid) {
            return returnOrderDAO.getReturnOrderBillingDetailByRoid(roid);
        }
        return null;
    }

    @Override
    public List<ReturnOrderJxPriceDifferenceRefundDetails> getReturnOrderJxPriceDifferenceRefundDetailsByReturnNumber(String returnNo) {
        if (null != returnNo) {
            return returnOrderDAO.getReturnOrderJxPriceDifferenceRefundDetailsByReturnNumber(returnNo);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveReturnOrderJxPriceDifferenceRefundDetails(ReturnOrderJxPriceDifferenceRefundDetails refundDetails) {
        if (null != refundDetails) {
            returnOrderDAO.saveReturnOrderJxPriceDifferenceRefundDetails(refundDetails);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<Object, Object> cancelOrderUniversal(Long userId, Integer identityType, String orderNumber, String reasonInfo, String remarksInfo, OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails) {
        Map<Object, Object> maps = new HashedMap();
        try {
            //获取退单号
            String returnNumber = OrderUtils.getReturnNumber();
            //创建退单头
            ReturnOrderBaseInfo returnOrderBaseInfo = new ReturnOrderBaseInfo();
            //记录退单头信息
            returnOrderBaseInfo.setOrderId(orderBaseInfo.getId());
            returnOrderBaseInfo.setOrderNo(orderNumber);
            returnOrderBaseInfo.setOrderTime(orderBaseInfo.getCreateTime());
            returnOrderBaseInfo.setStoreId(orderBaseInfo.getStoreId());
            returnOrderBaseInfo.setStoreCode(orderBaseInfo.getStoreCode());
            returnOrderBaseInfo.setStoreStructureCode(orderBaseInfo.getStoreStructureCode());
            returnOrderBaseInfo.setReturnTime(new Date());
            returnOrderBaseInfo.setReturnNo(returnNumber);
            returnOrderBaseInfo.setReturnType(ReturnOrderType.CANCEL_RETURN);
            //退款金额
            Double returnPrice = 0.00;
            //获取订单商品
            List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderNumber);

            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                returnPrice += (orderGoodsInfo.getOrderQuantity() * orderGoodsInfo.getReturnPrice());
            }
            returnOrderBaseInfo.setReturnPrice(returnPrice);
            returnOrderBaseInfo.setRemarksInfo(remarksInfo);
            returnOrderBaseInfo.setCreatorId(userId);
            returnOrderBaseInfo.setCreatorIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
            if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.CUSTOMER)) {
                AppCustomer customer = appCustomerService.findById(userId);
                returnOrderBaseInfo.setCreatorName(customer.getName());
                returnOrderBaseInfo.setCreatorPhone(customer.getMobile());
            } else {
                AppEmployee employee = employeeService.findById(userId);
                returnOrderBaseInfo.setCreatorName(employee.getName());
                returnOrderBaseInfo.setCreatorPhone(employee.getMobile());
            }
            returnOrderBaseInfo.setCustomerId(orderBaseInfo.getCustomerId());
            returnOrderBaseInfo.setCustomerName(orderBaseInfo.getCustomerName());
            returnOrderBaseInfo.setReasonInfo(reasonInfo);
            returnOrderBaseInfo.setOrderType(orderBaseInfo.getOrderType());
            returnOrderBaseInfo.setReturnStatus(AppReturnOrderStatus.FINISHED);
            //保存退单头信息
            returnOrderService.saveReturnOrderBaseInfo(returnOrderBaseInfo);
            //获取退单头id
            Long returnOrderId = returnOrderBaseInfo.getRoid();

            Date date = new Date();
            //创建退货商品实体类
            ReturnOrderGoodsInfo returnGoodsInfo = new ReturnOrderGoodsInfo();
            List<ReturnOrderGoodsInfo> returnOrderGoodsInfos = new ArrayList<>(orderGoodsInfoList.size());
            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                //记录退单商品
                returnGoodsInfo.setRoid(returnOrderId);
                returnGoodsInfo.setOrderGoodsId(orderGoodsInfo.getId());
                returnGoodsInfo.setReturnNo(returnNumber);
                returnGoodsInfo.setSku(orderGoodsInfo.getSku());
                returnGoodsInfo.setSkuName(orderGoodsInfo.getSkuName());
                returnGoodsInfo.setRetailPrice(orderGoodsInfo.getRetailPrice());
                returnGoodsInfo.setVipPrice(orderGoodsInfo.getVIPPrice());
                returnGoodsInfo.setWholesalePrice(orderGoodsInfo.getWholesalePrice());
                returnGoodsInfo.setReturnPrice(orderGoodsInfo.getReturnPrice());
                returnGoodsInfo.setReturnQty(orderGoodsInfo.getOrderQuantity());
                returnGoodsInfo.setSettlementPrice(orderGoodsInfo.getSettlementPrice());
                returnGoodsInfo.setCompanyFlag(orderGoodsInfo.getCompanyFlag());
                returnOrderGoodsInfos.add(returnGoodsInfo);
                //保存退单商品信息
                returnOrderService.saveReturnOrderGoodsInfo(returnGoodsInfo);
                //更改订单头商品已退数量和可退数量
                returnOrderService.updateReturnableQuantityAndReturnQuantityById(orderGoodsInfo.getReturnableQuantity(), 0, orderGoodsInfo.getId());


                //退还库存量
                if ("送货上门".equals(orderBaseInfo.getDeliveryType().getDescription())) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取现有城市库存量
                        CityInventory cityInventory = cityService.findCityInventoryByCityIdAndGoodsId(orderBaseInfo.getCityId(), orderGoodsInfo.getGid());
                        Integer affectLine;
                        //退还城市库存量
                        if ("顾客".equals(AppIdentityType.getAppIdentityTypeByValue(identityType).getDescription())) {
                            affectLine = cityService.updateCityInventoryByCustomerIdAndGoodsIdAndInventoryAndVersion(userId, orderGoodsInfo.getGid(), orderGoodsInfo.getOrderQuantity(), cityInventory.getLastUpdateTime());
                        } else {
                            affectLine = cityService.updateCityInventoryByEmployeeIdAndGoodsIdAndInventoryAndVersion(userId, orderGoodsInfo.getGid(), orderGoodsInfo.getOrderQuantity(), cityInventory.getLastUpdateTime());
                        }
                        if (affectLine > 0) {
                            //记录城市库存变更日志
                            CityInventoryAvailableQtyChangeLog cityInventoryAvailableQtyChangeLog = new CityInventoryAvailableQtyChangeLog();
                            cityInventoryAvailableQtyChangeLog.setCityId(orderBaseInfo.getCityId());
                            cityInventoryAvailableQtyChangeLog.setCityName(orderBaseInfo.getCityName());
                            cityInventoryAvailableQtyChangeLog.setGid(orderGoodsInfo.getGid());
                            cityInventoryAvailableQtyChangeLog.setSku(orderGoodsInfo.getSku());
                            cityInventoryAvailableQtyChangeLog.setSkuName(orderGoodsInfo.getSkuName());
                            cityInventoryAvailableQtyChangeLog.setChangeTime(date);
                            cityInventoryAvailableQtyChangeLog.setChangeQty(orderGoodsInfo.getOrderQuantity());
                            cityInventoryAvailableQtyChangeLog.setAfterChangeQty((cityInventory.getAvailableIty() + orderGoodsInfo.getOrderQuantity()));
                            cityInventoryAvailableQtyChangeLog.setChangeType(CityInventoryAvailableQtyChangeType.HOUSE_DELIVERY_ORDER_CANCEL);
                            cityInventoryAvailableQtyChangeLog.setChangeTypeDesc("配送单取消");
                            cityInventoryAvailableQtyChangeLog.setReferenceNumber(orderNumber);
                            //保存记录
                            cityService.addCityInventoryAvailableQtyChangeLog(cityInventoryAvailableQtyChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                } else if ("门店自提".equals(orderBaseInfo.getDeliveryType().getDescription())) {
                    OrderLogisticsInfo orderLogisticsInfo = appOrderService.getOrderLogistice(orderNumber);
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //查询现有门店库存
                        StoreInventory storeInventory = appStoreService.findStoreInventoryByStoreCodeAndGoodsId(orderLogisticsInfo.getBookingStoreCode(), orderGoodsInfo.getGid());
                        //退还门店可用量
                        Integer affectLine = appStoreService.updateStoreInventoryByStoreCodeAndGoodsIdAndVersion(orderLogisticsInfo.getBookingStoreCode(), orderGoodsInfo.getGid(), orderGoodsInfo.getOrderQuantity(), storeInventory.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店库存变更日志
                            StoreInventoryAvailableQtyChangeLog storeInventoryAvailableQtyChangeLog = new StoreInventoryAvailableQtyChangeLog();
                            storeInventoryAvailableQtyChangeLog.setCityId(orderBaseInfo.getCityId());
                            storeInventoryAvailableQtyChangeLog.setCityName(orderBaseInfo.getCityName());
                            storeInventoryAvailableQtyChangeLog.setStoreId(appStoreService.findByStoreCode(orderLogisticsInfo.getBookingStoreCode()).getStoreId());
                            storeInventoryAvailableQtyChangeLog.setStoreName(orderLogisticsInfo.getBookingStoreName());
                            storeInventoryAvailableQtyChangeLog.setStoreCode(orderLogisticsInfo.getBookingStoreCode());
                            storeInventoryAvailableQtyChangeLog.setGid(orderGoodsInfo.getGid());
                            storeInventoryAvailableQtyChangeLog.setSku(orderGoodsInfo.getSku());
                            storeInventoryAvailableQtyChangeLog.setSkuName(orderGoodsInfo.getSkuName());
                            storeInventoryAvailableQtyChangeLog.setChangeTime(date);
                            storeInventoryAvailableQtyChangeLog.setChangeQty(orderGoodsInfo.getOrderQuantity());
                            storeInventoryAvailableQtyChangeLog.setAfterChangeQty((storeInventory.getAvailableIty() + orderGoodsInfo.getOrderQuantity()));
                            storeInventoryAvailableQtyChangeLog.setChangeType(StoreInventoryAvailableQtyChangeType.SELF_TAKE_ORDER_CANCEL);
                            storeInventoryAvailableQtyChangeLog.setChangeTypeDesc("自提单取消");
                            storeInventoryAvailableQtyChangeLog.setReferenceNumber(orderNumber);
                            //保存记录
                            appStoreService.addStoreInventoryAvailableQtyChangeLog(storeInventoryAvailableQtyChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
            }
            //创建退单退款总记录实体
            ReturnOrderBilling returnOrderBilling = new ReturnOrderBilling();
            returnOrderBilling.setRoid(returnOrderId);
            returnOrderBilling.setReturnNo(returnNumber);
            returnOrderBilling.setPreDeposit(orderBillingDetails.getCusPreDeposit() == null ? 0.00 : orderBillingDetails.getCusPreDeposit());
            returnOrderBilling.setCreditMoney(orderBillingDetails.getEmpCreditMoney() == null ? 0.00 : orderBillingDetails.getEmpCreditMoney());
            returnOrderBilling.setStPreDeposit(orderBillingDetails.getStPreDeposit() == null ? 0.00 : orderBillingDetails.getStPreDeposit());
            returnOrderBilling.setStCreditMoney(orderBillingDetails.getStoreCreditMoney() == null ? 0.00 : orderBillingDetails.getStoreCreditMoney());
            returnOrderBilling.setStSubvention(orderBillingDetails.getStoreSubvention() == null ? 0.00 : orderBillingDetails.getStoreSubvention());
            returnOrderBilling.setOnlinePay(orderBillingDetails.getOnlinePayAmount() == null ? 0.00 : orderBillingDetails.getOnlinePayAmount());
            returnOrderBilling.setCash(0.00);
            //添加保存退单退款总记录
            returnOrderService.saveReturnOrderBilling(returnOrderBilling);

            //********************************返还虚拟货币********************************
            if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.CUSTOMER)) {
                //返回乐币
                if (orderBillingDetails.getLebiQuantity() != null && orderBillingDetails.getLebiQuantity() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取顾客当前乐币数量
                        CustomerLeBi customerLeBi = appCustomerService.findCustomerLebiByCustomerId(userId);
                        //返还乐币后顾客乐币数量
                        Integer lebiTotal = (customerLeBi.getQuantity() + orderBillingDetails.getLebiQuantity());
                        //更改顾客乐币数量
                        Integer affectLine = leBiVariationLogService.updateLeBiQtyByUserId(lebiTotal, customerLeBi.getLastUpdateTime(), userId);
                        if (affectLine > 0) {
                            //记录乐币日志
                            CustomerLeBiVariationLog leBiVariationLog = new CustomerLeBiVariationLog();
                            leBiVariationLog.setCusId(userId);
                            leBiVariationLog.setVariationQuantity(orderBillingDetails.getLebiQuantity());
                            leBiVariationLog.setAfterVariationQuantity(lebiTotal);
                            leBiVariationLog.setVariationTime(date);
                            leBiVariationLog.setLeBiVariationType(LeBiVariationType.CANCEL_ORDER);
                            leBiVariationLog.setVariationTypeDesc("取消订单");
                            leBiVariationLog.setOrderNum(orderNumber);
                            //保存日志
                            leBiVariationLogService.addCustomerLeBiVariationLog(leBiVariationLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
                //返回顾客预存款
                if (orderBillingDetails.getCusPreDeposit() != null && orderBillingDetails.getCusPreDeposit() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取顾客预存款
                        CustomerPreDeposit customerPreDeposit = appCustomerService.findByCusId(userId);
                        //返还预存款后顾客预存款金额
                        Double cusPreDeposit = (customerPreDeposit.getBalance() + orderBillingDetails.getCusPreDeposit());
                        //更改顾客预存款金额
                        Integer affectLine = appCustomerService.updateDepositByUserIdAndVersion(userId, orderBillingDetails.getCusPreDeposit(), customerPreDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录预存款日志
                            CusPreDepositLogDO cusPreDepositLogDO = new CusPreDepositLogDO();
                            cusPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            cusPreDepositLogDO.setChangeMoney(orderBillingDetails.getCusPreDeposit());
                            cusPreDepositLogDO.setOrderNumber(orderNumber);
                            cusPreDepositLogDO.setChangeType(CustomerPreDepositChangeType.CANCEL_ORDER);
                            cusPreDepositLogDO.setChangeTypeDesc("取消订单返还");
                            cusPreDepositLogDO.setCusId(userId);
                            cusPreDepositLogDO.setOperatorId(userId);
                            cusPreDepositLogDO.setOperatorType(AppIdentityType.CUSTOMER);
                            cusPreDepositLogDO.setBalance(cusPreDeposit);
                            cusPreDepositLogDO.setDetailReason("取消订单");
                            cusPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            cusPreDepositLogDO.setMerchantOrderNumber(null);
                            //保存日志
                            appCustomerService.addCusPreDepositLog(cusPreDepositLogDO);

                            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                            returnOrderBillingDetail.setCreateTime(new Date());
                            returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.CUS_PREPAY);
                            returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getCusPreDeposit());
                            returnOrderBillingDetail.setIntoAmountTime(new Date());
                            returnOrderBillingDetail.setReplyCode(null);
                            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
            }
            if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.SELLER)) {
                //返回门店预存款
                if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取门店预存款
                        StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(userId);
                        //返还预存款后门店预存款金额
                        Double stPreDeposit = (storePreDeposit.getBalance() + orderBillingDetails.getStPreDeposit());
                        //修改门店预存款
                        Integer affectLine = storePreDepositLogService.updateStPreDepositByStoreIdAndVersion(stPreDeposit, storePreDeposit.getStoreId(), storePreDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店预存款变更日志
                            StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                            stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            stPreDepositLogDO.setChangeMoney(orderBillingDetails.getStPreDeposit());
                            stPreDepositLogDO.setRemarks("取消订单返还门店预存款");
                            stPreDepositLogDO.setOrderNumber(orderNumber);
                            stPreDepositLogDO.setChangeType(StorePreDepositChangeType.CANCEL_ORDER);
                            stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                            stPreDepositLogDO.setOperatorId(userId);
                            stPreDepositLogDO.setOperatorType(AppIdentityType.SELLER);
                            stPreDepositLogDO.setBalance(stPreDeposit);
                            stPreDepositLogDO.setDetailReason("取消订单");
                            stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            //保存日志
                            storePreDepositLogService.save(stPreDepositLogDO);

                            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                            returnOrderBillingDetail.setCreateTime(new Date());
                            returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.ST_PREPAY);
                            returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getStPreDeposit());
                            returnOrderBillingDetail.setIntoAmountTime(new Date());
                            returnOrderBillingDetail.setReplyCode(null);
                            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
                //返回导购信用额度
                if (orderBillingDetails.getEmpCreditMoney() != null && orderBillingDetails.getEmpCreditMoney() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取导购信用金
                        EmpCreditMoney empCreditMoney = appEmployeeService.findEmpCreditMoneyByEmpId(userId);
                        //返还信用金后导购信用金额度
                        Double creditMoney = (empCreditMoney.getCreditLimitAvailable() + orderBillingDetails.getEmpCreditMoney());
                        //修改导购信用额度
                        Integer affectLine = appEmployeeService.unlockGuideCreditByUserIdAndGuideCreditAndVersion(userId, orderBillingDetails.getEmpCreditMoney(), empCreditMoney.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录导购信用金变更日志
                            EmpCreditMoneyChangeLog empCreditMoneyChangeLog = new EmpCreditMoneyChangeLog();
                            empCreditMoneyChangeLog.setEmpId(userId);
                            empCreditMoneyChangeLog.setCreateTime(date);
                            empCreditMoneyChangeLog.setCreditLimitAvailableChangeAmount(orderBillingDetails.getEmpCreditMoney());
                            empCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                            empCreditMoneyChangeLog.setReferenceNumber(orderNumber);
                            empCreditMoneyChangeLog.setChangeType(EmpCreditMoneyChangeType.CANCEL_ORDER);
                            empCreditMoneyChangeLog.setChangeTypeDesc("取消订单返还信用金");
                            empCreditMoneyChangeLog.setOperatorId(userId);
                            empCreditMoneyChangeLog.setOperatorType(AppIdentityType.SELLER);
                            //保存日志
                            appEmployeeService.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
            }
            if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.DECORATE_MANAGER)) {
                //返回门店预存款
                if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取门店预存款
                        StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(userId);
                        //返还预存款后门店预存款金额
                        Double stPreDeposit = (storePreDeposit.getBalance() + orderBillingDetails.getStPreDeposit());
                        //修改门店预存款
                        Integer affectLine = storePreDepositLogService.updateStPreDepositByStoreIdAndVersion(stPreDeposit, storePreDeposit.getStoreId(), storePreDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店预存款变更日志
                            StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                            stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            stPreDepositLogDO.setChangeMoney(orderBillingDetails.getStPreDeposit());
                            stPreDepositLogDO.setRemarks("取消订单返还门店预存款");
                            stPreDepositLogDO.setOrderNumber(orderNumber);
                            stPreDepositLogDO.setChangeType(StorePreDepositChangeType.CANCEL_ORDER);
                            stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                            stPreDepositLogDO.setOperatorId(userId);
                            stPreDepositLogDO.setOperatorType(AppIdentityType.DECORATE_MANAGER);
                            stPreDepositLogDO.setBalance(stPreDeposit);
                            stPreDepositLogDO.setDetailReason("取消订单");
                            stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            //保存日志
                            storePreDepositLogService.save(stPreDepositLogDO);

                            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                            returnOrderBillingDetail.setCreateTime(new Date());
                            returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.ST_PREPAY);
                            returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getStPreDeposit());
                            returnOrderBillingDetail.setIntoAmountTime(new Date());
                            returnOrderBillingDetail.setReplyCode(null);
                            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
                //返回门店信用金（装饰公司）
                if (orderBillingDetails.getStoreCreditMoney() != null && orderBillingDetails.getStoreCreditMoney() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //查询门店信用金
                        StoreCreditMoney storeCreditMoney = storeCreditMoneyLogService.findStoreCreditMoneyByUserId(userId);
                        //返还后门店信用金额度
                        Double creditMoney = (storeCreditMoney.getCreditLimitAvailable() + orderBillingDetails.getStoreCreditMoney());
                        //修改门店可用信用金
                        Integer affectLine = appStoreService.updateStoreCreditByUserIdAndVersion(userId, orderBillingDetails.getStoreCreditMoney(), storeCreditMoney.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店信用金变更日志
                            StoreCreditMoneyChangeLog storeCreditMoneyChangeLog = new StoreCreditMoneyChangeLog();
                            storeCreditMoneyChangeLog.setStoreId(storeCreditMoney.getStoreId());
                            storeCreditMoneyChangeLog.setCreateTime(date);
                            storeCreditMoneyChangeLog.setChangeAmount(orderBillingDetails.getStoreCreditMoney());
                            storeCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                            storeCreditMoneyChangeLog.setReferenceNumber(orderNumber);
                            storeCreditMoneyChangeLog.setChangeType(StoreCreditMoneyChangeType.CANCEL_ORDER);
                            storeCreditMoneyChangeLog.setChangeTypeDesc("取消订单返还门店信用金");
                            storeCreditMoneyChangeLog.setOperatorId(userId);
                            storeCreditMoneyChangeLog.setOperatorType(AppIdentityType.DECORATE_MANAGER);
                            storeCreditMoneyChangeLog.setRemark("取消订单");
                            //保存日志
                            appStoreService.addStoreCreditMoneyChangeLog(storeCreditMoneyChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
                //返回门店现金返利（装饰公司）
                if (orderBillingDetails.getStoreSubvention() != null && orderBillingDetails.getStoreSubvention() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取门店现金返利
                        StoreSubvention storeSubvention = appStoreService.findStoreSubventionByEmpId(userId);
                        //返还后门店现金返利余额
                        Double subvention = (storeSubvention.getBalance() + orderBillingDetails.getStoreSubvention());
                        //修改门店现金返利
                        Integer affectLine = appStoreService.updateStoreSubventionByUserIdAndVersion(orderBillingDetails.getStoreSubvention(), userId, storeSubvention.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店现金返利变更日志
                            StoreSubventionChangeLog storeSubventionChangeLog = new StoreSubventionChangeLog();
                            storeSubventionChangeLog.setStoreId(storeSubvention.getStoreId());
                            storeSubventionChangeLog.setCreateTime(date);
                            storeSubventionChangeLog.setChangeAmount(orderBillingDetails.getStoreSubvention());
                            storeSubventionChangeLog.setBalance(subvention);
                            storeSubventionChangeLog.setReferenceNumber(orderNumber);
                            storeSubventionChangeLog.setChangeType(StoreSubventionChangeType.CANCEL_ORDER);
                            storeSubventionChangeLog.setChangeTypeDesc("取消订单返还门店现金返利");
                            storeSubventionChangeLog.setOperatorId(userId);
                            storeSubventionChangeLog.setOperatorType(AppIdentityType.DECORATE_MANAGER);
                            storeSubventionChangeLog.setRemark("取消订单");
                            //保存日志
                            appStoreService.addStoreSubventionChangeLog(storeSubventionChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
            }
            //*******************************退券*********************************
            //获取订单使用产品券
            List<OrderCouponInfo> orderProductCouponList = productCouponService.findOrderCouponByCouponTypeAndOrderId(orderBaseInfo.getId(), OrderCouponType.PRODUCT_COUPON);
            if (orderProductCouponList != null && orderProductCouponList.size() > 0) {
                for (OrderCouponInfo orderProductCoupon : orderProductCouponList) {
                    //查询使用产品券信息
                    CustomerProductCoupon customerProductCoupon = productCouponService.findCusProductCouponByCouponId(orderProductCoupon.getCouponId());
                    customerProductCoupon.setLastUpdateTime(new Date());
                    customerProductCoupon.setIsUsed(Boolean.FALSE);
                    //修改原产品券是否使用和修改时间
                    productCouponService.updateCustomerProductCoupon(customerProductCoupon);

                    //增加日志
                    CustomerProductCouponChangeLog changeLog = new CustomerProductCouponChangeLog();
                    if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.CUSTOMER)) {
                        changeLog.setCusId(userId);
                    } else if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.SELLER)) {
                        changeLog.setCusId(orderBaseInfo.getCustomerId());
                    }
                    changeLog.setCouponId(orderProductCoupon.getCouponId());
                    changeLog.setChangeType(CustomerProductCouponChangeType.CANCEL_ORDER);
                    changeLog.setChangeTypeDesc(CustomerProductCouponChangeType.CANCEL_ORDER.getDescription());
                    changeLog.setReferenceNumber(orderNumber);
                    changeLog.setOperatorId(userId);
                    changeLog.setOperatorIp(null);
                    changeLog.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                    changeLog.setUseTime(new Date());
                    // 日志变更保存
                    productCouponService.addCustomerProductCouponChangeLog(changeLog);

                    ReturnOrderProductCoupon returnOrderProductCoupon = new ReturnOrderProductCoupon();
                    returnOrderProductCoupon.setOrderNo(orderBaseInfo.getOrderNumber());
                    returnOrderProductCoupon.setRoid(returnOrderBaseInfo.getRoid());
                    returnOrderProductCoupon.setReturnNo(returnOrderBaseInfo.getReturnNo());
                    returnOrderProductCoupon.setGid(null);
                    returnOrderProductCoupon.setPcid(orderProductCoupon.getCouponId());
                    returnOrderProductCoupon.setQty(1);
                    returnOrderProductCoupon.setReturnQty(1);
                    returnOrderProductCoupon.setSku(orderProductCoupon.getSku());
                    returnOrderProductCoupon.setPurchasePrice(orderProductCoupon.getPurchasePrice());
                    returnOrderProductCoupon.setIsReturn(Boolean.TRUE);

                    returnOrderService.saveReturnOrderProductCoupon(returnOrderProductCoupon);
                }
            }
            //获取订单使用现金券
            List<OrderCouponInfo> orderCashCouponList = productCouponService.findOrderCouponByCouponTypeAndOrderId(orderBaseInfo.getId(), OrderCouponType.CASH_COUPON);
            if (orderCashCouponList != null && orderCashCouponList.size() > 0) {
                for (OrderCouponInfo orderCashCoupon : orderCashCouponList) {
                    //查询现金券原信息
                    CustomerCashCoupon customerCashCoupon = cashCouponService.findCusCashCouponByCouponId(orderCashCoupon.getCouponId());
                    customerCashCoupon.setLastUpdateTime(new Date());
                    customerCashCoupon.setIsUsed(Boolean.FALSE);
                    //修改原现金券是否使用和修改时间
                    cashCouponService.updateCustomerCashCoupon(customerCashCoupon);

                    //记录现金券变更日志
                    CustomerCashCouponChangeLog customerCashCouponChangeLog = new CustomerCashCouponChangeLog();
                    if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.CUSTOMER)) {
                        customerCashCouponChangeLog.setCusId(userId);
                    } else if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.SELLER)) {
                        customerCashCouponChangeLog.setCusId(orderBaseInfo.getCustomerId());
                    }
                    customerCashCouponChangeLog.setUseTime(date);
                    customerCashCouponChangeLog.setCouponId(orderCashCoupon.getCouponId());
                    customerCashCouponChangeLog.setReferenceNumber(orderNumber);
                    customerCashCouponChangeLog.setChangeType(CustomerCashCouponChangeType.CANCEL_ORDER);
                    customerCashCouponChangeLog.setChangeTypeDesc("取消订单返还");
                    customerCashCouponChangeLog.setOperatorId(userId);
                    customerCashCouponChangeLog.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                    customerCashCouponChangeLog.setRemark("取消订单");
                    //保存日志
                    appCustomerService.addCustomerCashCouponChangeLog(customerCashCouponChangeLog);

                    ReturnOrderCashCoupon returnOrderCashCoupon = new ReturnOrderCashCoupon();
                    returnOrderCashCoupon.setRoid(returnOrderBaseInfo.getRoid());
                    returnOrderCashCoupon.setOrderNo(orderBaseInfo.getOrderNumber());
                    returnOrderCashCoupon.setCcid(orderCashCoupon.getCouponId());
                    returnOrderCashCoupon.setPurchasePrice(orderCashCoupon.getPurchasePrice());
                    returnOrderCashCoupon.setIsReturn(Boolean.TRUE);

                    returnOrderService.saveReturnOrderCashCoupon(returnOrderCashCoupon);

                }
            }
            //********************************退经销差价退还*************************
            AppStore appStore = appStoreService.findStoreByUserIdAndIdentityType(userId, identityType);

            if (AssertUtil.isNotEmpty(appStore) && appStore.getStoreType().equals(StoreType.FX) || appStore.getStoreType().equals(StoreType.JM)) {
                commonService.deductionOrderJxPriceDifferenceRefund(returnOrderBaseInfo, orderBaseInfo, returnOrderGoodsInfos);
            }
            //修改订单状态为已取消
            appOrderService.updateOrderStatusAndDeliveryStatusByOrderNo(AppOrderStatus.CANCELED, null, orderBaseInfo.getOrderNumber());
            maps.put("returnOrderBaseInfo", returnOrderBaseInfo);
            maps.put("code", "SUCCESS");
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("异常错误",e);
            maps.put("code", "FAILURE");
            return maps;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<Object, Object> refusedOrder(Logger logger,Long userId, Integer identityType, String orderNumber, String reasonInfo,
                                            String remarksInfo, OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails,String returnPic) {
        Map<Object, Object> maps = new HashedMap();
        try {
            //获取退单号
            String returnNumber = OrderUtils.getReturnNumber();
            //创建退单头
            ReturnOrderBaseInfo returnOrderBaseInfo = new ReturnOrderBaseInfo();
            //记录退单头信息
            returnOrderBaseInfo.setOrderId(orderBaseInfo.getId());
            returnOrderBaseInfo.setOrderNo(orderNumber);
            returnOrderBaseInfo.setOrderTime(orderBaseInfo.getCreateTime());
            returnOrderBaseInfo.setStoreId(orderBaseInfo.getStoreId());
            returnOrderBaseInfo.setStoreCode(orderBaseInfo.getStoreCode());
            returnOrderBaseInfo.setStoreStructureCode(orderBaseInfo.getStoreStructureCode());
            returnOrderBaseInfo.setReturnTime(new Date());
            returnOrderBaseInfo.setReturnNo(returnNumber);
            returnOrderBaseInfo.setReturnPic(returnPic);
            returnOrderBaseInfo.setReturnType(ReturnOrderType.REFUSED_RETURN);
            //退款金额
            Double returnPrice = 0.00;
            //获取订单商品
            List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderNumber);

            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                returnPrice += (orderGoodsInfo.getOrderQuantity() * orderGoodsInfo.getReturnPrice());
            }
            returnOrderBaseInfo.setReturnPrice(returnPrice);
            returnOrderBaseInfo.setRemarksInfo(remarksInfo);
            returnOrderBaseInfo.setCreatorId(userId);
            returnOrderBaseInfo.setCreatorIdentityType(AppIdentityType.getAppIdentityTypeByValue(identityType));
            AppEmployee employee = employeeService.findById(userId);
            returnOrderBaseInfo.setCreatorName(employee.getName());
            returnOrderBaseInfo.setCreatorPhone(employee.getMobile());
            returnOrderBaseInfo.setCustomerId(orderBaseInfo.getCustomerId());
            returnOrderBaseInfo.setCustomerName(orderBaseInfo.getCustomerName());
            returnOrderBaseInfo.setReasonInfo(reasonInfo);
            returnOrderBaseInfo.setOrderType(orderBaseInfo.getOrderType());
            returnOrderBaseInfo.setReturnStatus(AppReturnOrderStatus.FINISHED);
            //保存退单头信息
            returnOrderService.saveReturnOrderBaseInfo(returnOrderBaseInfo);
            //获取退单头id
            Long returnOrderId = returnOrderBaseInfo.getRoid();

            Date date = new Date();
            //创建退货商品实体类
            ReturnOrderGoodsInfo returnGoodsInfo = new ReturnOrderGoodsInfo();
            List<ReturnOrderGoodsInfo> returnOrderGoodsInfos = new ArrayList<>(orderGoodsInfoList.size());
            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                //记录退单商品
                returnGoodsInfo.setRoid(returnOrderId);
                returnGoodsInfo.setOrderGoodsId(orderGoodsInfo.getId());
                returnGoodsInfo.setReturnNo(returnNumber);
                returnGoodsInfo.setSku(orderGoodsInfo.getSku());
                returnGoodsInfo.setSkuName(orderGoodsInfo.getSkuName());
                returnGoodsInfo.setRetailPrice(orderGoodsInfo.getRetailPrice());
                returnGoodsInfo.setVipPrice(orderGoodsInfo.getVIPPrice());
                returnGoodsInfo.setWholesalePrice(orderGoodsInfo.getWholesalePrice());
                returnGoodsInfo.setSettlementPrice(orderGoodsInfo.getSettlementPrice());
                returnGoodsInfo.setReturnPrice(orderGoodsInfo.getReturnPrice());
                returnGoodsInfo.setReturnQty(orderGoodsInfo.getOrderQuantity());
                returnGoodsInfo.setGoodsLineType(orderGoodsInfo.getGoodsLineType());
                returnGoodsInfo.setCompanyFlag(orderGoodsInfo.getCompanyFlag());
                returnOrderGoodsInfos.add(returnGoodsInfo);
                //保存退单商品信息
                returnOrderService.saveReturnOrderGoodsInfo(returnGoodsInfo);
                //更改订单头商品已退数量和可退数量
                returnOrderService.updateReturnableQuantityAndReturnQuantityById(orderGoodsInfo.getReturnableQuantity(), 0, orderGoodsInfo.getId());
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    Integer affectLine;
                    //获取现有库存量
                    CityInventory cityInventory = cityService.findCityInventoryByCityIdAndGoodsId(orderBaseInfo.getCityId(), orderGoodsInfo.getGid());
                    //退还库存量
                    if ("顾客".equals(AppIdentityType.getAppIdentityTypeByValue(identityType).getDescription())) {
                        affectLine = cityService.updateCityInventoryByCustomerIdAndGoodsIdAndInventoryAndVersion(userId, orderGoodsInfo.getGid(), orderGoodsInfo.getOrderQuantity(), cityInventory.getLastUpdateTime());
                    } else {
                        affectLine = cityService.updateCityInventoryByEmployeeIdAndGoodsIdAndInventoryAndVersion(userId, orderGoodsInfo.getGid(), orderGoodsInfo.getOrderQuantity(), cityInventory.getLastUpdateTime());
                    }
                    if (affectLine > 0) {
                        //记录城市库存变更日志
                        CityInventoryAvailableQtyChangeLog cityInventoryAvailableQtyChangeLog = new CityInventoryAvailableQtyChangeLog();
                        cityInventoryAvailableQtyChangeLog.setCityId(orderBaseInfo.getCityId());
                        cityInventoryAvailableQtyChangeLog.setCityName(orderBaseInfo.getCityName());
                        cityInventoryAvailableQtyChangeLog.setGid(orderGoodsInfo.getGid());
                        cityInventoryAvailableQtyChangeLog.setSku(orderGoodsInfo.getSku());
                        cityInventoryAvailableQtyChangeLog.setSkuName(orderGoodsInfo.getSkuName());
                        cityInventoryAvailableQtyChangeLog.setChangeTime(date);
                        cityInventoryAvailableQtyChangeLog.setChangeQty(orderGoodsInfo.getOrderQuantity());
                        cityInventoryAvailableQtyChangeLog.setAfterChangeQty((cityInventory.getAvailableIty() + orderGoodsInfo.getOrderQuantity()));
                        cityInventoryAvailableQtyChangeLog.setChangeType(CityInventoryAvailableQtyChangeType.HOUSE_DELIVERY_ORDER_RETURN);
                        cityInventoryAvailableQtyChangeLog.setChangeTypeDesc("拒签退货");
                        cityInventoryAvailableQtyChangeLog.setReferenceNumber(orderNumber);
                        //保存记录
                        cityService.addCityInventoryAvailableQtyChangeLog(cityInventoryAvailableQtyChangeLog);
                        break;
                    } else {
                        if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                            logger.info("refusedOrder OUT,拒签退货失败，退还库存量失败");
                            throw new SystemBusyException("系统繁忙，请稍后再试!");
                        }
                    }
                }
            }
            //创建退单退款总记录实体
            ReturnOrderBilling returnOrderBilling = new ReturnOrderBilling();
            returnOrderBilling.setRoid(returnOrderId);
            returnOrderBilling.setReturnNo(returnNumber);
            returnOrderBilling.setPreDeposit(orderBillingDetails.getCusPreDeposit() == null ? 0.00 : orderBillingDetails.getCusPreDeposit());
            returnOrderBilling.setCreditMoney(orderBillingDetails.getEmpCreditMoney() == null ? 0.00 : orderBillingDetails.getEmpCreditMoney());
            returnOrderBilling.setStPreDeposit(orderBillingDetails.getStPreDeposit() == null ? 0.00 : orderBillingDetails.getStPreDeposit());
            returnOrderBilling.setStCreditMoney(orderBillingDetails.getStoreCreditMoney() == null ? 0.00 : orderBillingDetails.getStoreCreditMoney());
            returnOrderBilling.setStSubvention(orderBillingDetails.getStoreSubvention() == null ? 0.00 : orderBillingDetails.getStoreSubvention());
            returnOrderBilling.setOnlinePay(orderBillingDetails.getOnlinePayAmount() == null ? 0.00 : orderBillingDetails.getOnlinePayAmount());
            returnOrderBilling.setCash(0.00);
            //添加保存退单退款总记录
            returnOrderService.saveReturnOrderBilling(returnOrderBilling);

            //********************************返还虚拟货币********************************
            if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.CUSTOMER)) {
                //返回乐币
                if (orderBillingDetails.getLebiQuantity() != null && orderBillingDetails.getLebiQuantity() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取顾客当前乐币数量
                        CustomerLeBi customerLeBi = appCustomerService.findCustomerLebiByCustomerId(orderBaseInfo.getCreatorId());
                        //返还乐币后顾客乐币数量
                        Integer lebiTotal = (customerLeBi.getQuantity() + orderBillingDetails.getLebiQuantity());
                        //更改顾客乐币数量
                        Integer affectLine = leBiVariationLogService.updateLeBiQtyByUserId(lebiTotal, customerLeBi.getLastUpdateTime(), orderBaseInfo.getCreatorId());
                        if (affectLine > 0) {
                            //记录乐币日志
                            CustomerLeBiVariationLog leBiVariationLog = new CustomerLeBiVariationLog();
                            leBiVariationLog.setCusId(orderBaseInfo.getCreatorId());
                            leBiVariationLog.setVariationQuantity(orderBillingDetails.getLebiQuantity());
                            leBiVariationLog.setAfterVariationQuantity(lebiTotal);
                            leBiVariationLog.setVariationTime(date);
                            leBiVariationLog.setLeBiVariationType(LeBiVariationType.RETURN_ORDER);
                            leBiVariationLog.setVariationTypeDesc("拒签退货");
                            leBiVariationLog.setOrderNum(orderNumber);
                            //保存日志
                            leBiVariationLogService.addCustomerLeBiVariationLog(leBiVariationLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                logger.info("refusedOrder OUT,拒签退货失败，更改顾客乐币数量失败");
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
                //返回顾客预存款
                if (orderBillingDetails.getCusPreDeposit() != null && orderBillingDetails.getCusPreDeposit() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取顾客预存款
                        CustomerPreDeposit customerPreDeposit = appCustomerService.findByCusId(orderBaseInfo.getCreatorId());
                        //返还预存款后顾客预存款金额
                        Double cusPreDeposit = (customerPreDeposit.getBalance() + orderBillingDetails.getCusPreDeposit());
                        //更改顾客预存款金额
                        Integer affectLine = appCustomerService.updateDepositByUserIdAndVersion(orderBaseInfo.getCreatorId(), orderBillingDetails.getCusPreDeposit(), customerPreDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录预存款日志
                            CusPreDepositLogDO cusPreDepositLogDO = new CusPreDepositLogDO();
                            cusPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            cusPreDepositLogDO.setChangeMoney(orderBillingDetails.getCusPreDeposit());
                            cusPreDepositLogDO.setOrderNumber(orderNumber);
                            cusPreDepositLogDO.setChangeType(CustomerPreDepositChangeType.RETURN_ORDER);
                            cusPreDepositLogDO.setChangeTypeDesc("拒签退货返还");
                            cusPreDepositLogDO.setCusId(orderBaseInfo.getCreatorId());
                            cusPreDepositLogDO.setOperatorId(userId);
                            cusPreDepositLogDO.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            cusPreDepositLogDO.setBalance(cusPreDeposit);
                            cusPreDepositLogDO.setDetailReason("拒签退货");
                            cusPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            cusPreDepositLogDO.setMerchantOrderNumber(null);
                            //保存日志
                            appCustomerService.addCusPreDepositLog(cusPreDepositLogDO);

                            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                            returnOrderBillingDetail.setCreateTime(new Date());
                            returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.CUS_PREPAY);
                            returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getCusPreDeposit());
                            returnOrderBillingDetail.setIntoAmountTime(new Date());
                            returnOrderBillingDetail.setReplyCode(null);
                            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                logger.info("refusedOrder OUT,拒签退货失败，更改顾客预存款金额失败");
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
            }
            if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.SELLER)) {
                //返回门店预存款
                if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取门店预存款
                        StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(orderBaseInfo.getSalesConsultId());
                        //返还预存款后门店预存款金额
                        Double stPreDeposit = (storePreDeposit.getBalance() + orderBillingDetails.getStPreDeposit());
                        //修改门店预存款
                        Integer affectLine = storePreDepositLogService.updateStPreDepositByStoreIdAndVersion(stPreDeposit, storePreDeposit.getStoreId(), storePreDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店预存款变更日志
                            StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                            stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            stPreDepositLogDO.setChangeMoney(orderBillingDetails.getStPreDeposit());
                            stPreDepositLogDO.setRemarks("拒签退货返还门店预存款");
                            stPreDepositLogDO.setOrderNumber(orderNumber);
                            stPreDepositLogDO.setChangeType(StorePreDepositChangeType.RETURN_ORDER);
                            stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                            stPreDepositLogDO.setOperatorId(userId);
                            stPreDepositLogDO.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            stPreDepositLogDO.setBalance(stPreDeposit);
                            stPreDepositLogDO.setDetailReason("拒签退货");
                            stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            //保存日志
                            storePreDepositLogService.save(stPreDepositLogDO);

                            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                            returnOrderBillingDetail.setCreateTime(new Date());
                            returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.ST_PREPAY);
                            returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getStPreDeposit());
                            returnOrderBillingDetail.setIntoAmountTime(new Date());
                            returnOrderBillingDetail.setReplyCode(null);
                            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                logger.info("refusedOrder OUT,拒签退货失败，修改门店预存款失败");
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
                //返回导购信用额度
                if (orderBillingDetails.getEmpCreditMoney() != null && orderBillingDetails.getEmpCreditMoney() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取导购信用金
                        EmpCreditMoney empCreditMoney = appEmployeeService.findEmpCreditMoneyByEmpId(orderBaseInfo.getSalesConsultId());
                        //返还信用金后导购信用金额度
                        Double creditMoney = (empCreditMoney.getCreditLimitAvailable() + orderBillingDetails.getEmpCreditMoney());
                        //修改导购信用额度
                        Integer affectLine = appEmployeeService.unlockGuideCreditByUserIdAndGuideCreditAndVersion(userId, orderBillingDetails.getEmpCreditMoney(), empCreditMoney.getLastUpdateTime());

                        if (affectLine > 0) {
                            //记录导购信用金变更日志
                            EmpCreditMoneyChangeLog empCreditMoneyChangeLog = new EmpCreditMoneyChangeLog();
                            empCreditMoneyChangeLog.setEmpId(orderBaseInfo.getSalesConsultId());
                            empCreditMoneyChangeLog.setCreateTime(date);
                            empCreditMoneyChangeLog.setCreditLimitAvailableChangeAmount(orderBillingDetails.getEmpCreditMoney());
                            empCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                            empCreditMoneyChangeLog.setReferenceNumber(orderNumber);
                            empCreditMoneyChangeLog.setChangeType(EmpCreditMoneyChangeType.RETURN_ORDER);
                            empCreditMoneyChangeLog.setChangeTypeDesc("拒签退货返还信用金");
                            empCreditMoneyChangeLog.setOperatorId(userId);
                            empCreditMoneyChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            //保存日志
                            appEmployeeService.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                logger.info("refusedOrder OUT,拒签退货失败，修改导购信用额度失败");
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
            }
            if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.DECORATE_MANAGER)) {
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    //返回门店预存款
                    if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                        //获取门店预存款
                        StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(orderBaseInfo.getCreatorId());
                        //返还预存款后门店预存款金额
                        Double stPreDeposit = (storePreDeposit.getBalance() + orderBillingDetails.getStPreDeposit());
                        //修改门店预存款
                        Integer affectLine = storePreDepositLogService.updateStPreDepositByStoreIdAndVersion(stPreDeposit, storePreDeposit.getStoreId(), storePreDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店预存款变更日志
                            StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                            stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            stPreDepositLogDO.setChangeMoney(orderBillingDetails.getStPreDeposit());
                            stPreDepositLogDO.setRemarks("拒签退货返还门店预存款");
                            stPreDepositLogDO.setOrderNumber(orderNumber);
                            stPreDepositLogDO.setChangeType(StorePreDepositChangeType.RETURN_ORDER);
                            stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                            stPreDepositLogDO.setOperatorId(userId);
                            stPreDepositLogDO.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            stPreDepositLogDO.setBalance(stPreDeposit);
                            stPreDepositLogDO.setDetailReason("取消订单");
                            stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            //保存日志
                            storePreDepositLogService.save(stPreDepositLogDO);

                            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                            returnOrderBillingDetail.setCreateTime(new Date());
                            returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.ST_PREPAY);
                            returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getStPreDeposit());
                            returnOrderBillingDetail.setIntoAmountTime(new Date());
                            returnOrderBillingDetail.setReplyCode(null);
                            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                logger.info("refusedOrder OUT,拒签退货失败，修改门店预存款失败");
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
                //返回门店信用金（装饰公司）
                if (orderBillingDetails.getStoreCreditMoney() != null && orderBillingDetails.getStoreCreditMoney() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //查询门店信用金
                        StoreCreditMoney storeCreditMoney = storeCreditMoneyLogService.findStoreCreditMoneyByUserId(orderBaseInfo.getCreatorId());
                        //返还后门店信用金额度
                        Double creditMoney = (storeCreditMoney.getCreditLimitAvailable() + orderBillingDetails.getStoreCreditMoney());
                        //修改门店可用信用金
                        Integer affectLine = appStoreService.updateStoreCreditByUserIdAndVersion(userId, orderBillingDetails.getStoreCreditMoney(), storeCreditMoney.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店信用金变更日志
                            StoreCreditMoneyChangeLog storeCreditMoneyChangeLog = new StoreCreditMoneyChangeLog();
                            storeCreditMoneyChangeLog.setStoreId(storeCreditMoney.getStoreId());
                            storeCreditMoneyChangeLog.setCreateTime(date);
                            storeCreditMoneyChangeLog.setChangeAmount(orderBillingDetails.getStoreCreditMoney());
                            storeCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                            storeCreditMoneyChangeLog.setReferenceNumber(orderNumber);
                            storeCreditMoneyChangeLog.setChangeType(StoreCreditMoneyChangeType.RETURN_ORDER);
                            storeCreditMoneyChangeLog.setChangeTypeDesc("拒签退货返还门店信用金");
                            storeCreditMoneyChangeLog.setOperatorId(userId);
                            storeCreditMoneyChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            storeCreditMoneyChangeLog.setRemark("拒签退货");
                            //保存日志
                            appStoreService.addStoreCreditMoneyChangeLog(storeCreditMoneyChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                logger.info("refusedOrder OUT,拒签退货失败，修改门店可用信用金失败");
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
                //返回门店现金返利（装饰公司）
                if (AssertUtil.isNotEmpty(orderBillingDetails.getStoreSubvention())) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取门店现金返利
                        StoreSubvention storeSubvention = appStoreService.findStoreSubventionByEmpId(orderBaseInfo.getCreatorId());
                        //返还后门店现金返利余额
                        Double subvention = (storeSubvention.getBalance() + orderBillingDetails.getStoreSubvention());
                        //修改门店现金返利
                        Integer affectLine = appStoreService.updateStoreSubventionByUserIdAndVersion(orderBillingDetails.getStoreSubvention(), userId, storeSubvention.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店现金返利变更日志
                            StoreSubventionChangeLog storeSubventionChangeLog = new StoreSubventionChangeLog();
                            storeSubventionChangeLog.setStoreId(storeSubvention.getStoreId());
                            storeSubventionChangeLog.setCreateTime(date);
                            storeSubventionChangeLog.setChangeAmount(orderBillingDetails.getStoreSubvention());
                            storeSubventionChangeLog.setBalance(subvention);
                            storeSubventionChangeLog.setReferenceNumber(orderNumber);
                            storeSubventionChangeLog.setChangeType(StoreSubventionChangeType.RETURN_ORDER);
                            storeSubventionChangeLog.setChangeTypeDesc("拒签退货返还门店现金返利");
                            storeSubventionChangeLog.setOperatorId(userId);
                            storeSubventionChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                            storeSubventionChangeLog.setRemark("拒签退货");
                            //保存日志
                            appStoreService.addStoreSubventionChangeLog(storeSubventionChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                logger.info("refusedOrder OUT,拒签退货失败，修改门店现金返利失败");
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
            }
            //*******************************退券*********************************
            //获取订单使用产品券
            List<OrderCouponInfo> orderProductCouponList = productCouponService.findOrderCouponByCouponTypeAndOrderId(orderBaseInfo.getId(), OrderCouponType.PRODUCT_COUPON);
            if (AssertUtil.isNotEmpty(orderProductCouponList)) {
                for (OrderCouponInfo orderProductCoupon : orderProductCouponList) {
                    //查询使用产品券信息
                    CustomerProductCoupon customerProductCoupon = productCouponService.findCusProductCouponByCouponId(orderProductCoupon.getCouponId());
                    //创建新的产品券
                    customerProductCoupon.setLastUpdateTime(new Date());
                    customerProductCoupon.setIsUsed(Boolean.FALSE);
                    //修改原产品券是否使用和修改时间
                    productCouponService.updateCustomerProductCoupon(customerProductCoupon);

                    //增加日志
                    CustomerProductCouponChangeLog changeLog = new CustomerProductCouponChangeLog();
                    if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.CUSTOMER)) {
                        changeLog.setCusId(userId);
                    } else if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.SELLER)) {
                        changeLog.setCusId(orderBaseInfo.getCustomerId());
                    }
                    changeLog.setCouponId(orderProductCoupon.getCouponId());
                    changeLog.setChangeType(CustomerProductCouponChangeType.RETURN_ORDER);
                    changeLog.setChangeTypeDesc(CustomerProductCouponChangeType.RETURN_ORDER.getDescription());
                    changeLog.setReferenceNumber(orderNumber);
                    changeLog.setOperatorId(userId);
                    changeLog.setOperatorIp(null);
                    changeLog.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                    changeLog.setUseTime(new Date());
                    // 日志变更保存
                    productCouponService.addCustomerProductCouponChangeLog(changeLog);

                    ReturnOrderProductCoupon returnOrderProductCoupon = new ReturnOrderProductCoupon();
                    returnOrderProductCoupon.setOrderNo(orderBaseInfo.getOrderNumber());
                    returnOrderProductCoupon.setRoid(returnOrderBaseInfo.getRoid());
                    returnOrderProductCoupon.setReturnNo(returnOrderBaseInfo.getReturnNo());
                    returnOrderProductCoupon.setGid(null);
                    returnOrderProductCoupon.setPcid(orderProductCoupon.getCouponId());
                    returnOrderProductCoupon.setQty(1);
                    returnOrderProductCoupon.setReturnQty(1);
                    returnOrderProductCoupon.setSku(orderProductCoupon.getSku());
                    returnOrderProductCoupon.setPurchasePrice(orderProductCoupon.getPurchasePrice());
                    returnOrderProductCoupon.setIsReturn(Boolean.TRUE);

                    returnOrderService.saveReturnOrderProductCoupon(returnOrderProductCoupon);
                }
            }
            //获取订单使用现金券
            List<OrderCouponInfo> orderCashCouponList = productCouponService.findOrderCouponByCouponTypeAndOrderId(orderBaseInfo.getCreatorId(), OrderCouponType.CASH_COUPON);
            if (AssertUtil.isNotEmpty(orderCashCouponList)) {
                for (OrderCouponInfo orderCashCoupon : orderCashCouponList) {
                    //查询现金券原信息
                    CustomerCashCoupon customerCashCoupon = cashCouponService.findCusCashCouponByCouponId(orderCashCoupon.getCouponId());
                    customerCashCoupon.setLastUpdateTime(new Date());
                    customerCashCoupon.setIsUsed(Boolean.FALSE);
                    //修改原现金券是否使用和修改时间
                    cashCouponService.updateCustomerCashCoupon(customerCashCoupon);

                    //记录现金券变更日志
                    CustomerCashCouponChangeLog customerCashCouponChangeLog = new CustomerCashCouponChangeLog();
                    if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.CUSTOMER)) {
                        customerCashCouponChangeLog.setCusId(orderBaseInfo.getCreatorId());
                    } else if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.SELLER)) {
                        customerCashCouponChangeLog.setCusId(orderBaseInfo.getCustomerId());
                    }
                    customerCashCouponChangeLog.setUseTime(date);
                    customerCashCouponChangeLog.setCouponId(orderCashCoupon.getCouponId());
                    customerCashCouponChangeLog.setReferenceNumber(orderNumber);
                    customerCashCouponChangeLog.setChangeType(CustomerCashCouponChangeType.CANCEL_ORDER);
                    customerCashCouponChangeLog.setChangeTypeDesc("拒签退单返还");
                    customerCashCouponChangeLog.setOperatorId(userId);
                    customerCashCouponChangeLog.setOperatorType(AppIdentityType.DELIVERY_CLERK);
                    customerCashCouponChangeLog.setRemark("拒签退单");
                    //保存日志
                    appCustomerService.addCustomerCashCouponChangeLog(customerCashCouponChangeLog);

                    ReturnOrderCashCoupon returnOrderCashCoupon = new ReturnOrderCashCoupon();
                    returnOrderCashCoupon.setRoid(returnOrderBaseInfo.getRoid());
                    returnOrderCashCoupon.setOrderNo(orderBaseInfo.getOrderNumber());
                    returnOrderCashCoupon.setCcid(orderCashCoupon.getCouponId());
                    returnOrderCashCoupon.setPurchasePrice(orderCashCoupon.getPurchasePrice());
                    returnOrderCashCoupon.setIsReturn(Boolean.TRUE);

                    returnOrderService.saveReturnOrderCashCoupon(returnOrderCashCoupon);
                }
            }
            //********************************退经销差价退还*************************
            AppStore appStore = appStoreService.findStoreByUserIdAndIdentityType(userId, identityType);

            if (AssertUtil.isNotEmpty(appStore) && appStore.getStoreType().equals(StoreType.FX) || appStore.getStoreType().equals(StoreType.JM)) {
                commonService.deductionOrderJxPriceDifferenceRefund(returnOrderBaseInfo, orderBaseInfo, returnOrderGoodsInfos);
            }
            //获取物流状态明细
            OrderDeliveryInfoDetails orderDeliveryInfoDetails = orderDeliveryInfoDetailsService.findByOrderNumberAndLogisticStatus(orderNumber, LogisticStatus.SEALED_CAR);
            //记录物流明细表
            OrderDeliveryInfoDetails newOrderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
            newOrderDeliveryInfoDetails.setOrderNo(orderNumber);
            newOrderDeliveryInfoDetails.setLogisticStatus(LogisticStatus.REJECT);
            newOrderDeliveryInfoDetails.setCreateTime(date);
            newOrderDeliveryInfoDetails.setDescription("拒签");
            newOrderDeliveryInfoDetails.setOperationType("拒签退货");
            newOrderDeliveryInfoDetails.setOperatorNo(orderDeliveryInfoDetails.getOperatorNo());
            newOrderDeliveryInfoDetails.setWarehouseNo(orderDeliveryInfoDetails.getWarehouseNo());
            newOrderDeliveryInfoDetails.setTaskNo(orderDeliveryInfoDetails.getTaskNo());

            orderDeliveryInfoDetailsService.addOrderDeliveryInfoDetails(newOrderDeliveryInfoDetails);
            //修改订单状态为拒签,物流状态拒签
            appOrderService.updateOrderStatusAndDeliveryStatusByOrderNo(AppOrderStatus.REJECTED, LogisticStatus.REJECT, orderBaseInfo.getOrderNumber());
            maps.put("returnOrderBaseInfo", returnOrderBaseInfo);
            maps.put("code", "SUCCESS");
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
            maps.put("code", "FAILURE");
            return maps;
        }
    }
        @Override
        public void updateReturnLogisticInfo (String driver, String returnNo){

            AppEmployee employee = employeeService.findDeliveryByClerkNo(driver);
            ReturnOrderLogisticInfo logisticInfo = new ReturnOrderLogisticInfo();
            logisticInfo.setDeliveryClerkId(employee.getEmpId());
            logisticInfo.setDeliveryClerkName(employee.getName());
            logisticInfo.setDeliveryClerkPhone(employee.getMobile());
            logisticInfo.setDeliveryClerkNo(driver);
            logisticInfo.setReturnNO(returnNo);
            returnOrderDAO.updateReturnLogisticInfo(logisticInfo);
        }
}

