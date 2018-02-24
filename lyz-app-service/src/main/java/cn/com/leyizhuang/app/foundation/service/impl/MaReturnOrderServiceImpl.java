package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.SystemBusyException;
import cn.com.leyizhuang.app.core.remote.ebs.EbsSenderService;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.MaReturnOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.management.coupon.MaCashCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.coupon.MaProductCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.MaCustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.MaReturnGoods;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderTempInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaPaymentData;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.*;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventoryChange;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderCouponInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositVO;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liuh on 2017/12/16.
 */
@Service
public class MaReturnOrderServiceImpl implements MaReturnOrderService {

    @Resource
    private MaReturnOrderDAO maReturnOrderDAO;
    @Resource
    private EbsSenderService ebsSenderService;
    @Resource
    private MaStoreInventoryService maStoreInventoryService;
    @Resource
    private MaOrderService maOrderService;
    @Resource
    private MaCustomerService maCustomerService;
    @Resource
    private MaStoreService maStoreService;
    @Resource
    private MaCouponService maCouponService;
    @Resource
    private ProductCouponService productCouponService;

    @Resource
    private CashCouponService cashCouponService;
    @Resource
    private AppCustomerService appCustomerService;

    @Override
    public PageInfo<MaReturnOrderInfo> findMaReturnOrderList(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<MaReturnOrderInfo> maReturnOrderInfoList = maReturnOrderDAO.findMaReturnOrderList();
        return new PageInfo<>(maReturnOrderInfoList);
    }


    @Override
    public PageInfo<MaReturnOrderInfo> findMaReturnOrderListByScreen(Integer page, Integer size, Long storeId, String status) {
        PageHelper.startPage(page, size);
        if (storeId == -1) {
            storeId = null;
        }
        if ("-1".equals(status)) {
            status = null;
        }
        List<MaReturnOrderInfo> maReturnOrderList = maReturnOrderDAO.findMaReturnOrderListByScreen(storeId, status);
        return new PageInfo<>(maReturnOrderList);
    }


    @Override
    public PageInfo<MaReturnOrderInfo> findMaReturnOrderPageGirdByInfo(Integer page, Integer size, String info) {
        PageHelper.startPage(page, size);
        List<MaReturnOrderInfo> maReturnOrderInfoList = maReturnOrderDAO.findMaReturnOrderPageGirdByInfo(info);
        return new PageInfo<>(maReturnOrderInfoList);
    }

    @Override
    public MaReturnOrderDetailInfo queryMaReturnOrderByReturnNo(String returnNumber) {
        MaReturnOrderDetailInfo maReturnOrderInfo = maReturnOrderDAO.queryMaReturnOrderByReturnNo(returnNumber);
        return maReturnOrderInfo;
    }


    @Override
    public MaReturnOrderLogisticInfo getMaReturnOrderLogisticeInfo(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            return maReturnOrderDAO.getMaReturnOrderLogisticeInfo(returnNumber);
        }
        return null;
    }

    @Override
    public List<MaReturnGoods> getMaReturnOrderGoodsDetails(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            return maReturnOrderDAO.getMaReturnOrderGoodsDetails(returnNumber);
        }
        return null;
    }

    @Override
    public List<MaReturnOrderProductCouponInfo> getReturnOrderProductCoupon(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            return maReturnOrderDAO.getReturnOrderProductCoupon(returnNumber);
        }
        return null;
    }

    @Override
    public List<MaReturnOrderBillingDetail> getMaReturnOrderBillingDetails(Long returnBillingID) {
        if (null != returnBillingID) {
            return maReturnOrderDAO.getMaReturnOrderBillingDetails(returnBillingID);
        }
        return null;
    }

    @Override
    public String findReturnOrderTypeByReturnNumber(String returnNumber) {
        if (null != returnNumber) {
            return maReturnOrderDAO.findReturnOrderTypeByReturnNumber(returnNumber);
        }
        return null;
    }

    @Override
    public List<MaReturnOrderGoodsInfo> findMaReturnOrderGoodsInfoByOrderNumber(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            return maReturnOrderDAO.findMaReturnOrderGoodsInfoByOrderNumber(returnNumber);
        }
        return null;
    }

    @Override
    public Long findReturnOrderBillingId(String returnNumber) {
        if (StringUtils.isNotBlank(returnNumber)) {
            return maReturnOrderDAO.findReturnOrderBillingId(returnNumber);
        }
        return null;
    }

    @Override
    public void updateReturnOrderStatus(String returnNumber,String status) {
        if (StringUtils.isNotBlank(returnNumber)&&StringUtils.isNotBlank(status)) {
            maReturnOrderDAO.updateReturnOrderStatus(returnNumber,status);
        }
    }

    @Override
    public List<MaOrderGoodsInfo> findReturnOrderGoodsList(String returnNumber) {
        return maReturnOrderDAO.findReturnOrderGoodsList(returnNumber);
    }

    @Override
    public MaOrdReturnBilling findReturnOrderBillingList(String returnNumber) {
        return maReturnOrderDAO.findReturnOrderBillingList(returnNumber);
    }

    @Override
    public void sendReturnOrderReceiptInfAndRecord(String returnNumber) {
        if (null == returnNumber) {
            throw new RuntimeException("发送接口失败，退单号为空");
        }
        MaStoreReturnOrderAppToEbsBaseInfo maStoreReturnOrderAppToEbsBaseInfo = maReturnOrderDAO.findMaStoreReturnOrderAppToEbsInfoByReturnNumber(returnNumber);
        //调用ebsSenderService接口传ebs
        this.ebsSenderService.sendReturnOrderReceiptInfAndRecord(maStoreReturnOrderAppToEbsBaseInfo);
    }

    @Override
    public void saveReturnOrderBillingDetail(List<MaOrdReturnBillingDetail> maOrdReturnBillingDetailList) {
        this.maReturnOrderDAO.saveReturnOrderBillingDetail(maOrdReturnBillingDetailList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnOrderReceive(String returnNumber, MaReturnOrderDetailInfo maReturnOrderDetailInfo, MaOrdReturnBilling maOrdReturnBillingList, ShiroUser shiroUser) {
        Date date = new Date();
        if (null == maReturnOrderDetailInfo || null == maReturnOrderDetailInfo.getStoreId()) {
            throw new RuntimeException("该订单门店ID为空,无法更新门店库存");
        }

        //查询该订单下的所有商品
        List<MaOrderGoodsInfo> MaOrderGoodsInfoList = this.findReturnOrderGoodsList(returnNumber);

        //门店库存,可用量及生成日志信息
        for (MaOrderGoodsInfo maOrderGoodsInfo : MaOrderGoodsInfoList) {
            //查看门店下 该商品的库存
            MaStoreInventory storeInventory = maStoreInventoryService.findStoreInventoryByStoreCodeAndGoodsId(maReturnOrderDetailInfo.getStoreId(), maOrderGoodsInfo.getGid());
            if (null == storeInventory) {
                throw new RuntimeException("未找到该门店或该门店下没有该商品库存,门店id:"+maReturnOrderDetailInfo.getStoreId()+"商品id:" + maOrderGoodsInfo.getGid());
            }
            for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                //更新门店库存数量及可用量
                Integer goodsQtyAfterChange = storeInventory.getRealIty() + maOrderGoodsInfo.getOrderQty();
                Integer goodsAvailableItyAfterChange = storeInventory.getAvailableIty() + maOrderGoodsInfo.getOrderQty();
                Integer affectLine = maStoreInventoryService.updateStoreInventoryAndAvailableIty(maReturnOrderDetailInfo.getStoreId(), maOrderGoodsInfo.getGid(), goodsQtyAfterChange, goodsAvailableItyAfterChange, storeInventory.getLastUpdateTime());
                //增加门店库变更日志存可用量
                if (affectLine > 0) {
                    MaStoreInventoryChange storeInventoryChange = new MaStoreInventoryChange();
                    storeInventoryChange.setCityId(storeInventory.getCityId());
                    storeInventoryChange.setCityName(storeInventory.getCityName());
                    storeInventoryChange.setStoreId(storeInventory.getStoreId());
                    storeInventoryChange.setStoreCode(storeInventory.getStoreCode());
                    storeInventoryChange.setReferenceNumber(returnNumber);
                    storeInventoryChange.setGid(maOrderGoodsInfo.getGid());
                    storeInventoryChange.setSku(maOrderGoodsInfo.getSku());
                    storeInventoryChange.setSkuName(maOrderGoodsInfo.getSkuName());
                    storeInventoryChange.setChangeTime(date);
                    storeInventoryChange.setAfterChangeQty(goodsQtyAfterChange);
                    storeInventoryChange.setChangeQty(maOrderGoodsInfo.getOrderQty());
                    storeInventoryChange.setChangeType(StoreInventoryAvailableQtyChangeType.STORE_EXPORT_GOODS);
                    storeInventoryChange.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.STORE_EXPORT_GOODS.getDescription());
                    maStoreInventoryService.addInventoryChangeLog(storeInventoryChange);
                    break;
                } else {
                    if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                        throw new SystemBusyException("系统繁忙，请稍后再试!");
                    }
                }
            }
        }

        //生成ebs接口表数据
        MaOrderTempInfo maOrderTempInfo = maOrderService.getOrderInfoByOrderNo(maReturnOrderDetailInfo.getOrderNo());
        MaStoreReturnOrderAppToEbsBaseInfo maStoreReturnOrderAppToEbs = new MaStoreReturnOrderAppToEbsBaseInfo();
        maStoreReturnOrderAppToEbs.setReturnDate(maReturnOrderDetailInfo.getReturnTime());
        maStoreReturnOrderAppToEbs.setMainReturnNumber(maReturnOrderDetailInfo.getReturnNo());
        maStoreReturnOrderAppToEbs.setRtHeaderId(maReturnOrderDetailInfo.getRoid());
        maStoreReturnOrderAppToEbs.setSobId(maOrderTempInfo.getSobId());
        maStoreReturnOrderAppToEbs.setDiySiteCode(maReturnOrderDetailInfo.getStoreCode());
        maStoreReturnOrderAppToEbs.setMainOrderNumber(maReturnOrderDetailInfo.getOrderNo());
        maStoreReturnOrderAppToEbs.setSellerId(maOrderTempInfo.getSalesConsultId());
        maStoreReturnOrderAppToEbs.setUserId(maOrderTempInfo.getCustomerId());
        maStoreReturnOrderAppToEbs.setCreateTime(new Date());
        maStoreReturnOrderAppToEbs.setReturnType(maReturnOrderDetailInfo.getReturnType());
        maStoreReturnOrderAppToEbs.setDeliverTypeTitle(maOrderTempInfo.getDeliveryType());
        maStoreReturnOrderAppToEbs.setStoreOrgCode(maOrderTempInfo.getStoreStructureCode());
        maReturnOrderDAO.saveAppToEbsReturnOrderInf(maStoreReturnOrderAppToEbs);

        //退款
        Double cashAmount = maOrdReturnBillingList.getCash();
        Double creditMoneyAmount = maOrdReturnBillingList.getCreditMoney();
        Double onlinePayAmount = maOrdReturnBillingList.getOnlinePay();
        Double preDepositAmount = maOrdReturnBillingList.getPreDeposit();
        Double stCreditMoneyAmount = maOrdReturnBillingList.getStCreditMoney();
        Double stPreDepositAmount = maOrdReturnBillingList.getStPreDeposit();
        Long roid = maOrdReturnBillingList.getRoid();

        List<MaOrdReturnBillingDetail> maOrdReturnBillingDetailList = new ArrayList<MaOrdReturnBillingDetail>();
        Date ReturnDate = new Date();
        //退现金
        if (null != cashAmount && cashAmount > 0) {
            MaOrdReturnBillingDetail maOrdReturnBillingCash = new MaOrdReturnBillingDetail();
            maOrdReturnBillingCash.setCreateTime(ReturnDate);
            maOrdReturnBillingCash.setIntoAmountTime(ReturnDate);
            maOrdReturnBillingCash.setRefundNumber(OrderUtils.getRefundNumber());
            maOrdReturnBillingCash.setReturnMoney(BigDecimal.valueOf(cashAmount));
            maOrdReturnBillingCash.setReturnPayType(OrderBillingPaymentType.CASH.getValue());
            maOrdReturnBillingCash.setRoid(roid);
            maOrdReturnBillingDetailList.add(maOrdReturnBillingCash);
        }

        //退线上支付
        if (null != onlinePayAmount && onlinePayAmount > 0) {
            //查询原订单线上支付方式
            List<MaPaymentData> paymentDataList = maOrderService.findPaymentDataByOrderNo(maReturnOrderDetailInfo.getOrderNo());
            for (MaPaymentData maPaymentData : paymentDataList) {
                MaOrdReturnBillingDetail maOrdReturnBillingDetail = new MaOrdReturnBillingDetail();
                maOrdReturnBillingDetail.setCreateTime(ReturnDate);
                maOrdReturnBillingDetail.setIntoAmountTime(ReturnDate);
                maOrdReturnBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                maOrdReturnBillingDetail.setReturnMoney(BigDecimal.valueOf(maPaymentData.getTotalFee()));
                maOrdReturnBillingDetail.setReturnPayType(maPaymentData.getOnlinePayType().toString());
                maOrdReturnBillingDetail.setRoid(roid);
                maOrdReturnBillingDetailList.add(maOrdReturnBillingDetail);
            }

        }

        //退顾客预存款
        if (null != preDepositAmount && preDepositAmount > 0) {
            MaOrdReturnBillingDetail maOrdReturnBillingPreDeposit = new MaOrdReturnBillingDetail();
            maOrdReturnBillingPreDeposit.setCreateTime(ReturnDate);
            maOrdReturnBillingPreDeposit.setIntoAmountTime(ReturnDate);
            maOrdReturnBillingPreDeposit.setRefundNumber(OrderUtils.getRefundNumber());
            maOrdReturnBillingPreDeposit.setReturnMoney(BigDecimal.valueOf(preDepositAmount));
            maOrdReturnBillingPreDeposit.setReturnPayType(OrderBillingPaymentType.CUS_PREPAY.getValue());
            maOrdReturnBillingPreDeposit.setRoid(roid);
            maOrdReturnBillingDetailList.add(maOrdReturnBillingPreDeposit);
            //获得顾客预存款
            Long customerId = maReturnOrderDetailInfo.getCustomerId();
            for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                CustomerPreDepositVO customerPreDepositVO = maCustomerService.queryCusPredepositByCusId(customerId);
                Double cusPreDeposit = (Double.parseDouble(customerPreDepositVO.getBalance()) + preDepositAmount);
                Integer affectLine = maCustomerService.updateDepositByUserIdAndVersion(customerId, cusPreDeposit, customerPreDepositVO.getLastUpdateTime());
                //记录预存款日志
                if (affectLine > 0) {
                    MaCustomerPreDeposit customerPreDeposit = new MaCustomerPreDeposit();
                    customerPreDeposit.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(ReturnDate));
                    customerPreDeposit.setChangeMoney(preDepositAmount);
                    customerPreDeposit.setOrderNumber(maReturnOrderDetailInfo.getOrderNo());
                    customerPreDeposit.setChangeType(CustomerPreDepositChangeType.RETURN_ORDER);
                    customerPreDeposit.setChangeTypeDesc(CustomerPreDepositChangeType.RETURN_ORDER.getDescription());
                    customerPreDeposit.setCusId(customerId);
                    customerPreDeposit.setOperatorId(maReturnOrderDetailInfo.getCreatorId());
                    customerPreDeposit.setOperatorType(maReturnOrderDetailInfo.getCreatorIdentityType());
                    customerPreDeposit.setBalance(cusPreDeposit);
                    customerPreDeposit.setDetailReason("到店退货");
                    customerPreDeposit.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(ReturnDate));
                    customerPreDeposit.setMerchantOrderNumber(null);
                    //保存日志
                    maCustomerService.saveCusPreDepositLog(customerPreDeposit);
                    break;
                } else {
                    if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                        throw new SystemBusyException("系统繁忙，请稍后再试!");
                    }
                }
            }
        }
        //退门店预存款
        if (null != stPreDepositAmount && stPreDepositAmount > 0) {
            MaOrdReturnBillingDetail maOrdReturnBillingStPreDepositAmount = new MaOrdReturnBillingDetail();
            maOrdReturnBillingStPreDepositAmount.setCreateTime(ReturnDate);
            maOrdReturnBillingStPreDepositAmount.setIntoAmountTime(ReturnDate);
            maOrdReturnBillingStPreDepositAmount.setRefundNumber(OrderUtils.getRefundNumber());
            maOrdReturnBillingStPreDepositAmount.setReturnMoney(BigDecimal.valueOf(stPreDepositAmount));
            maOrdReturnBillingStPreDepositAmount.setReturnPayType(OrderBillingPaymentType.ST_PREPAY.getValue());
            maOrdReturnBillingStPreDepositAmount.setRoid(roid);
            maOrdReturnBillingDetailList.add(maOrdReturnBillingStPreDepositAmount);
            for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                //获取门店预存款
                StorePreDepositVO storePreDeposit = maStoreService.queryStorePredepositByStoreId(maReturnOrderDetailInfo.getStoreId());
                //返还预存款后门店预存款金额
                Double stPreDeposit = (Double.parseDouble(storePreDeposit.getBalance()) + stPreDepositAmount);
                //修改门店预存款
                Integer affectLine = maStoreService.updateStPreDepositByUserIdAndVersion(stPreDeposit, maReturnOrderDetailInfo.getStoreId(), storePreDeposit.getLastUpdateTime());
                if (affectLine > 0) {
                    //记录门店预存款变更日志
                    StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                    stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                    stPreDepositLogDO.setChangeMoney(stPreDepositAmount);
                    stPreDepositLogDO.setOrderNumber(maReturnOrderDetailInfo.getOrderNo());
                    stPreDepositLogDO.setChangeType(StorePreDepositChangeType.RETURN_ORDER);
                    stPreDepositLogDO.setStoreId(maReturnOrderDetailInfo.getStoreId());
                    stPreDepositLogDO.setOperatorId(null);
                    stPreDepositLogDO.setOperatorType(AppIdentityType.ADMINISTRATOR);
                    stPreDepositLogDO.setBalance(stPreDeposit);
                    stPreDepositLogDO.setDetailReason(StorePreDepositChangeType.RETURN_ORDER.getDescription());
                    stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                    //保存日志
                    maStoreService.saveStorePreDepositLog(stPreDepositLogDO);
                    break;
                } else {
                    if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                        throw new SystemBusyException("系统繁忙，请稍后再试!");
                    }
                }
            }
            //TODO 是否退导购信用额度
            if (null != creditMoneyAmount && creditMoneyAmount > 0) {
            }

            //TODO 是否退门店信用额度
            if (null != stCreditMoneyAmount && stCreditMoneyAmount > 0) {
            }

            this.saveReturnOrderBillingDetail(maOrdReturnBillingDetailList);
            //退劵
            //获取订单使用产品券
            List<MaProductCouponInfo> orderProductCouponList = maCouponService.findProductCouponTypeByReturnOrder(returnNumber);
            if (orderProductCouponList != null && orderProductCouponList.size() > 0) {
                for (MaProductCouponInfo maProductCouponInfo : orderProductCouponList) {
                    //查询使用产品券信息
                    CustomerProductCoupon customerProductCoupon = productCouponService.findCusProductCouponByCouponId(maProductCouponInfo.getPcid());
                    customerProductCoupon.setLastUpdateTime(new Date());
                    customerProductCoupon.setIsUsed(Boolean.FALSE);
                    //修改原产品券是否使用和修改时间
                    productCouponService.updateCustomerProductCoupon(customerProductCoupon);

                    //增加日志
                    CustomerProductCouponChangeLog changeLog = new CustomerProductCouponChangeLog();
                    changeLog.setCusId(maReturnOrderDetailInfo.getCustomerId());
                    changeLog.setCouponId(maProductCouponInfo.getPcid());
                    changeLog.setChangeType(CustomerProductCouponChangeType.RETURN_ORDER);
                    changeLog.setChangeTypeDesc(CustomerProductCouponChangeType.RETURN_ORDER.getDescription());
                    changeLog.setReferenceNumber(returnNumber);
                    changeLog.setOperatorId(shiroUser.getId());
                    changeLog.setOperatorIp(null);
                    changeLog.setOperatorType(AppIdentityType.ADMINISTRATOR);
                    changeLog.setUseTime(new Date());
                    // 日志变更保存
                    productCouponService.addCustomerProductCouponChangeLog(changeLog);
                }
            }

            //获取订单使用现金券
            List<MaCashCouponInfo> orderCashCouponList = maCouponService.findCashCouponTypeByReturnOrderId(maReturnOrderDetailInfo.getRoid());
            if (orderCashCouponList != null && orderCashCouponList.size() > 0) {
                for (MaCashCouponInfo maCashCouponInfo : orderCashCouponList) {
                    //查询现金券原信息
                    CustomerCashCoupon customerCashCoupon = cashCouponService.findCusCashCouponByCouponId(maCashCouponInfo.getCcid());
                    customerCashCoupon.setLastUpdateTime(new Date());
                    customerCashCoupon.setIsUsed(Boolean.FALSE);
                    //修改原现金券是否使用和修改时间
                    cashCouponService.updateCustomerCashCoupon(customerCashCoupon);

                    //记录现金券变更日志
                    CustomerCashCouponChangeLog customerCashCouponChangeLog = new CustomerCashCouponChangeLog();
                    customerCashCouponChangeLog.setCusId(maReturnOrderDetailInfo.getCustomerId());
                    customerCashCouponChangeLog.setUseTime(date);
                    customerCashCouponChangeLog.setCouponId(maCashCouponInfo.getCcid());
                    customerCashCouponChangeLog.setReferenceNumber(returnNumber);
                    customerCashCouponChangeLog.setChangeType(CustomerCashCouponChangeType.RETURN_ORDER);
                    customerCashCouponChangeLog.setChangeTypeDesc(CustomerCashCouponChangeType.RETURN_ORDER.getDescription());
                    customerCashCouponChangeLog.setOperatorId(shiroUser.getId());
                    customerCashCouponChangeLog.setOperatorType(AppIdentityType.ADMINISTRATOR);
                    customerCashCouponChangeLog.setRemark(null);
                    //保存日志
                    appCustomerService.addCustomerCashCouponChangeLog(customerCashCouponChangeLog);

                }
            }
        }
        //更新订单状态
        this.updateReturnOrderStatus(returnNumber,AppReturnOrderStatus.FINISHED.toString());
    }
}

