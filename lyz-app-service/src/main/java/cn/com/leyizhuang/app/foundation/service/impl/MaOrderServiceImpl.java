package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.core.remote.ebs.EbsSenderService;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.*;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsShippingInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideAvailableCreditChange;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditChangeDetail;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditMoneyDetail;
import cn.com.leyizhuang.app.foundation.pojo.management.order.*;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreRealInventoryChange;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderReceiveInf;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCompanyOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.MaGoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.ProductCouponSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.DetailFitOrderVO;
import cn.com.leyizhuang.app.foundation.vo.FitOrderVO;
import cn.com.leyizhuang.app.foundation.vo.MaOrderVO;
import cn.com.leyizhuang.app.foundation.vo.OrderGoodsVO;
import cn.com.leyizhuang.app.foundation.vo.management.goodscategory.MaOrderGoodsDetailResponse;
import cn.com.leyizhuang.app.foundation.vo.management.order.*;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.CountUtil;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by caiyu on 2017/12/16.
 */
@Service("maOrderService")
public class MaOrderServiceImpl implements MaOrderService {

    @Resource
    private MaOrderDAO maOrderDAO;
    @Resource
    private MaStoreInventoryService maStoreInventoryService;
    @Resource
    private MaCityInventoryService maCityInventoryService;
    @Resource
    private MaGoodsDAO maGoodsDAO;
    @Resource
    private EbsSenderService ebsSenderService;
    @Resource
    private AppOrderService orderService;
    @Resource
    private AppStoreService storeService;
    @Resource
    private StatisticsSellDetailsService statisticsSellDetailsService;
    @Resource
    private MaEmpCreditMoneyService maEmpCreditMoneyService;
    @Resource
    private AppOrderService appOrderService;
    @Resource
    private AppCustomerService customerService;
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
    @Resource
    private CityService cityService;
    @Resource
    private MaEmpCreditMoneyDAO maEmpCreditMoneyDAO;
    @Resource
    private OrderAgencyFundService orderAgencyFundServiceImpl;
    @Resource
    private AppOrderService appOrderServiceImpl;
    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsServiceImpl;
    @Resource
    private TimingTaskErrorMessageDAO timingTaskErrorMessageDAO;
    @Resource
    private RechargeService rechargeService;
    @Resource
    private ReturnOrderDAO returnOrderDAO;
    @Resource
    private MaDecorativeCompanyCreditService maDecorativeCompanyCreditService;

    @Resource
    private CommonService commonService;

    @Resource
    private GoodsService goodsService;
    @Resource
    private MaterialListDAO materialListDAO;
    @Resource
    private AppSeparateOrderService separateOrderService;

    @Autowired
    private AppActService actService;

    @Override
    public List<MaOrderVO> findMaOrderVOAll(List<Long> storeIds) {
        List<MaOrderVO> maOrderVOList = maOrderDAO.findMaOrderVOAll(storeIds);
//        List<MaOrderVO> orderVOList = new ArrayList<>();
//        for (int i = 0;i<maOrderVOList.size();i++){
//            for (int j =0;j<storeIds.size();j++){
//                if (maOrderVOList.get(i).getStoreId().equals(storeIds.get(j))){
//                    orderVOList.add(maOrderVOList.get(i));
//                }
//            }
//        }
        return maOrderVOList;
    }

    @Override
    public List<MaOrderVO> findMaOrderVOByCityId(Long cityId, List<Long> storeIds) {
        return maOrderDAO.findMaOrderVOByCityId(cityId, storeIds);
    }

    @Override
    public List<MaOrderVO> findMaOrderVOByStoreId(Long storeId) {
        return maOrderDAO.findMaOrderVOByStoreId(storeId);
    }

    @Override
    public List<MaOrderVO> findMaOrderVOByStoreIdAndCityIdAndDeliveryType(String deliveryType, Long cityId, Long storeId) {
        return maOrderDAO.findMaOrderVOByStoreIdAndCityIdAndDeliveryType(deliveryType, cityId, storeId);
    }

    @Override
    public List<MaOrderVO> findMaOrderVOByOrderNumber(String orderNumber) {
        return maOrderDAO.findMaOrderVOByOrderNumber(orderNumber);
    }

    @Override
    public List<MaOrderVO> findMaOrderVOByCondition(MaOrderVORequest maOrderVORequest, List<Long> storeIds) {
        List<MaOrderVO> maOrderVOList = maOrderDAO.findMaOrderVOByCondition(maOrderVORequest);

        for (int i = 0; i < maOrderVOList.size(); i++) {
            for (int j = 0; j < storeIds.size(); j++) {
                if (maOrderVOList.get(i).equals(storeIds.get(j))) {
                    maOrderVOList.remove(maOrderVOList.get(i));
                }
            }
        }

        return maOrderVOList;
    }

    @Override
    public List<MaOrderVO> findCompanyOrderAll(List<Long> storeIds) {
        List<MaOrderVO> maOrderVOList = maOrderDAO.findCompanyOrderAll(storeIds);
//        for (int i = 0;i<maOrderVOList.size();i++){
//            for (int j =0;j<storeIds.size();j++){
//                if (maOrderVOList.get(i).equals(storeIds.get(j))){
//                    maOrderVOList.remove(maOrderVOList.get(i));
//                }
//            }
//        }
        return maOrderVOList;
    }

    @Override
    public List<MaOrderVO> findCompanyOrderByOrderNumber(String orderNumber) {
        return maOrderDAO.findCompanyOrderByOrderNumber(orderNumber);
    }

    @Override
    public List<MaOrderVO> findCompanyOrderByCondition(MaCompanyOrderVORequest maCompanyOrderVORequest) {
        List<MaOrderVO> maOrderVOList = maOrderDAO.findCompanyOrderByCondition(maCompanyOrderVORequest);
//        for (int i = 0;i<maOrderVOList.size();i++){
//            for (int j =0;j<storeIds.size();j++){
//                if (maOrderVOList.get(i).equals(storeIds.get(j))){
//                    maOrderVOList.remove(maOrderVOList.get(i));
//                }
//            }
//        }
        return maOrderVOList;
    }

    @Override
    public List<MaOrderVO> findPendingShipmentOrder() {
        return maOrderDAO.findPendingShipmentOrder();
    }

    @Override
    public List<MaOrderVO> findPendingShipmentOrderByCondition(MaCompanyOrderVORequest maCompanyOrderVORequest) {
        return maOrderDAO.findPendingShipmentOrderByCondition(maCompanyOrderVORequest);
    }

    @Override
    public List<MaOrderVO> findPendingShipmentOrderByOrderNumber(String orderNumber) {
        return maOrderDAO.findPendingShipmentOrderByOrderNumber(orderNumber);
    }

    @Override
    public MaOrderDetailResponse findMaOrderDetailByOrderNumber(String orderNmber) {
        return maOrderDAO.findMaOrderDetailByOrderNumber(orderNmber);
    }

    @Override
    public MaCompanyOrderDetailResponse findMaCompanyOrderDetailByOrderNumber(String orderNmber) {
        return maOrderDAO.findMaCompanyOrderDetailByOrderNumber(orderNmber);
    }

    @Override
    public MaOrderBillingDetailResponse getMaOrderBillingDetailByOrderNumber(String orderNmber) {
        return maOrderDAO.getMaOrderBillingDetailByOrderNumber(orderNmber);
    }

    @Override
    public List<MaOrderBillingPaymentDetailResponse> getMaOrderBillingPaymentDetailByOrderNumber(String orderNmber) {
        return maOrderDAO.getMaOrderBillingPaymentDetailByOrderNumber(orderNmber);
    }

    @Override
    public Double queryRepaymentAmount(String orderNmber) {
        return maOrderDAO.queryRepaymentAmount(orderNmber);
    }

    @Override
    public MaOrderDeliveryInfoResponse getDeliveryInfoByOrderNumber(String orderNmber) {
        return maOrderDAO.getDeliveryInfoByOrderNumber(orderNmber);
    }

    @Override
    public PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderList(Integer page, Integer size, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findSelfTakeOrderList(storeIds);
        return new PageInfo<>(maSelfTakeOrderVOList);
    }

    @Override
    public PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderListByScreen(Integer page, Integer size, Long cityId, Long storeId, Integer status, Integer isPayUp, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        if (storeId != -1) {
            cityId = null;
        }
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findSelfTakeOrderListByScreen(cityId, storeId, status, isPayUp, storeIds);
        return new PageInfo<>(maSelfTakeOrderVOList);
    }


    @Override
    public PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderListByInfo(Integer page, Integer size, String info, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findSelfTakeOrderListByInfo(info, storeIds);
        return new PageInfo<>(maSelfTakeOrderVOList);
    }

    @Override
    public PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderByCondition(Integer page, Integer size, MaOrderVORequest maOrderVORequest) {
        PageHelper.startPage(page, size);
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findSelfTakeOrderListByCondition(maOrderVORequest);
        return new PageInfo<>(maSelfTakeOrderVOList);
    }


    @Override
    public MaOrderTempInfo getOrderInfoByOrderNo(String orderNo) {
        return this.maOrderDAO.getOrderInfoByOrderNo(orderNo);
    }

    @Override
    public void updateOrderStatus(String orderNo, String status) {
        this.maOrderDAO.updateOrderStatus(orderNo, status);
    }

    @Override
    public void updateOrderReceivablesStatus(MaOrderAmount maOrderAmount) {
        this.maOrderDAO.updateOrderReceivablesStatus(maOrderAmount);
    }


    @Override
    public List<MaOrderGoodsInfo> findOrderGoodsList(String orderNo, Long storeId) {
        return this.maOrderDAO.findOrderGoodsList(orderNo, storeId);
    }

    @Override
    public void saveOrderShipping(OrderShipping orderShipping) {
        this.maOrderDAO.saveOrderShipping(orderShipping);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderShipping(String orderNumber, ShiroUser shiroUser, MaOrderTempInfo maOrderTempInfo) throws RuntimeException {
        if (null == maOrderTempInfo || null == maOrderTempInfo.getStoreId()) {
            throw new RuntimeException("该订单门店ID为空,无法更新门店库存");
        }
        Date date = new Date();
        //更新订单状态
        this.updateOrderStatus(orderNumber, AppOrderStatus.FINISHED.getValue());
        //存入发货表
        OrderShipping orderShipping = new OrderShipping();
        orderShipping.setOid(maOrderTempInfo.getId());
        orderShipping.setOrdNo(maOrderTempInfo.getOrderNumber());
        orderShipping.setShippingTime(date);
        this.saveOrderShipping(orderShipping);
        //查询该订单下的所有商品
        Long storeId = maOrderTempInfo.getStoreId();
        List<MaOrderGoodsInfo> MaOrderGoodsInfoList = this.findOrderGoodsList(orderNumber, storeId);

        for (MaOrderGoodsInfo maOrderGoodsInfo : MaOrderGoodsInfoList) {
            //生成出货记录
            GoodsShippingInfo goodsShippingInfo = new GoodsShippingInfo();
            goodsShippingInfo.setStore(maOrderTempInfo.getStoreCode());
            goodsShippingInfo.setCity(maOrderTempInfo.getCityName());
            goodsShippingInfo.setCreator(shiroUser.getName());
            goodsShippingInfo.setDeliveryType(AppDeliveryType.SELF_TAKE);
            goodsShippingInfo.setShippingTime(date);
            goodsShippingInfo.setPrice(maOrderGoodsInfo.getReturnPrice());
            goodsShippingInfo.setGid(maOrderGoodsInfo.getGid());
            goodsShippingInfo.setQty(maOrderGoodsInfo.getOrderQty());
            goodsShippingInfo.setSku(maOrderGoodsInfo.getSku());
            goodsShippingInfo.setSkuName(maOrderGoodsInfo.getSkuName());
            goodsShippingInfo.setReferenceNumber(maOrderTempInfo.getOrderNumber());
            goodsShippingInfo.setChangeType(StoreInventoryAvailableQtyChangeType.SELF_TAKE_ORDER.toString());
            goodsShippingInfo.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.SELF_TAKE_ORDER.getDescription());
            maGoodsDAO.saveGoodsShippingInfo(goodsShippingInfo);
            //更新门店库存
            //查看门店下该商品的库存
            MaStoreInventory storeInventory = maStoreInventoryService.findStoreInventoryByStoreIdAndGoodsId(maOrderTempInfo.getStoreId(), maOrderGoodsInfo.getGid());
            if (null == storeInventory || null == storeInventory.getAvailableIty()) {
                throw new RuntimeException("该门店下没有该商品,商品id:" + maOrderGoodsInfo.getGid());
            }
            if (storeInventory.getRealIty() < maOrderGoodsInfo.getOrderQty()) {
                throw new RuntimeException("门店下该商品库存不足,无法发货,商品id:" + maOrderGoodsInfo.getGid());
            }
            //更新门店库存数量
            Integer goodsQtyAfterChange = storeInventory.getRealIty() - maOrderGoodsInfo.getOrderQty();
            for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                int affectLine = maStoreInventoryService.updateStoreInventory(maOrderTempInfo.getStoreId(), maOrderGoodsInfo.getGid(), goodsQtyAfterChange, storeInventory.getLastUpdateTime());
                if (affectLine > 0) {
                    MaStoreRealInventoryChange storeInventoryChange = new MaStoreRealInventoryChange();
                    storeInventoryChange.setCityId(storeInventory.getCityId());
                    storeInventoryChange.setCityName(storeInventory.getCityName());
                    storeInventoryChange.setStoreId(storeInventory.getStoreId());
                    storeInventoryChange.setStoreCode(storeInventory.getStoreCode());
                    storeInventoryChange.setStoreName(storeInventory.getStoreName());
                    storeInventoryChange.setReferenceNumber(orderNumber);
                    storeInventoryChange.setGid(maOrderGoodsInfo.getGid());
                    storeInventoryChange.setSku(maOrderGoodsInfo.getSku());
                    storeInventoryChange.setSkuName(maOrderGoodsInfo.getSkuName());
                    storeInventoryChange.setChangeTime(date);
                    storeInventoryChange.setAfterChangeQty(goodsQtyAfterChange);
                    storeInventoryChange.setChangeQty(-maOrderGoodsInfo.getOrderQty());
                    storeInventoryChange.setChangeType(StoreInventoryRealQtyChangeType.ORDER_DELIVERY);
                    storeInventoryChange.setChangeTypeDesc(StoreInventoryRealQtyChangeType.ORDER_DELIVERY.getDescription());
                    maStoreInventoryService.addRealInventoryChangeLog(storeInventoryChange);
                    break;
                } else {
                    if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                        throw new SystemBusyException("系统繁忙，请稍后再试!");
                    }
                }
            }
        }
        //生成ebs接口表数据
        MaOrderReceiveInf maOrderReceiveInf = new MaOrderReceiveInf();
        maOrderReceiveInf.setDeliverTypeTitle(AppDeliveryType.SELF_TAKE);
        maOrderReceiveInf.setOrderNumber(orderNumber);
        maOrderReceiveInf.setReceiveDate(date);
        maOrderReceiveInf.setSobId(maOrderTempInfo.getSobId());
        maOrderReceiveInf.setInitDate(maOrderTempInfo.getCreateTime());
        maOrderReceiveInf.setHeaderId(maOrderTempInfo.getId());
        //maOrderReceiveInf.setSendTime(new Date());
        this.saveAppToEbsOrderReceiveInf(maOrderReceiveInf);
        //记录销量
        //statisticsSellDetailsService.addOrderSellDetails(orderNumber);
    }

    @Override
    public MaOrderReceiveInf queryOrderReceiveInf(String orderNumber) {
        MaOrderReceiveInf maOrderReceiveInf = this.maOrderDAO.queryOrderReceiveInf(orderNumber);
        return maOrderReceiveInf;
    }


    @Override
    public void sendOrderReceiveInfAndRecord(String orderNumber) {
        if (null == orderNumber) {
            throw new RuntimeException("发送接口失败，订单ID为空");
        }
        MaOrderReceiveInf maOrderReceiveInf = this.queryOrderReceiveInf(orderNumber);
        //调用ebsSenderService接口传ebs
        this.ebsSenderService.sendOrderReceiveInfAndRecord(maOrderReceiveInf);
    }


    @Override
    public void updateOrderArrearsAudit(String orderNumber, Date date) {
        this.maOrderDAO.updateOrderArrearsAudit(orderNumber, date);
    }

    @Override
    public List<OrderBaseInfo> scanningUnpaidOrder(String findDate) {
        return maOrderDAO.scanningUnpaidOrder(findDate);
    }

    @Override
    public PageInfo<MaOrderVO> findMaOrderVOPageInfo(Integer page, Integer size, List<Long> storeIds) {
        if (null != page && null != size && AssertUtil.isNotEmpty(storeIds)) {
            PageHelper.startPage(page, size);
            List<MaOrderVO> maOrderVOList = maOrderDAO.findStoreAndSmallFitOrderVO(storeIds);
            return new PageInfo<>(maOrderVOList);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> selfTakeOrderReceivables(MaOrderAmount maOrderAmount, MaOrderBillingDetailResponse maOrderBillingDetailResponse, GuideCreditChangeDetail guideCreditChangeDetail) {
        // 更新订单支付信息
        List<String> receiptNumberList = this.orderReceivables(maOrderAmount, maOrderBillingDetailResponse, guideCreditChangeDetail);
        return receiptNumberList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> orderReceivables(MaOrderAmount maOrderAmount, MaOrderBillingDetailResponse maOrderBillingDetailResponse, GuideCreditChangeDetail guideCreditChangeDetail) {
        //得到订单基本信息
        MaOrderTempInfo maOrderTempInfo = this.getOrderInfoByOrderNo(maOrderAmount.getOrderNumber());
        //更新订单收款信息
        maOrderAmount.setPayUpTime(new Date());
        this.updateOrderReceivablesStatus(maOrderAmount);
        //更新欠款审核表
        //this.updateOrderArrearsAudit(maOrderAmount.getOrderNumber(),maOrderAmount.getDate());
        //存入ebs接口表
        //返回收款单号
        List<String> receiptNumberList = new ArrayList<String>();

        //设置订单收款信息并存入订单账款支付明细表
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        MaOrderBillingPaymentDetails maOrderBillingPaymentDetails = new MaOrderBillingPaymentDetails();
        maOrderBillingPaymentDetails.setOrdNo(maOrderAmount.getOrderNumber());
        maOrderBillingPaymentDetails.setPayTime(sdf.format(new Date()));
        maOrderBillingPaymentDetails.setCreateTime(new Date());
        maOrderBillingPaymentDetails.setPaymentSubjectType(AppIdentityType.SELLER);
        maOrderBillingPaymentDetails.setPaymentSubjectTypeDesc(AppIdentityType.SELLER.getDescription());
        maOrderBillingPaymentDetails.setOid(maOrderTempInfo.getId());
        maOrderBillingPaymentDetails.setPaymentSubjectId(maOrderTempInfo.getSalesConsultId());
        if (maOrderAmount.getCashAmount().compareTo(BigDecimal.ZERO) != 0) {
            String receiptNumber = OrderUtils.generateReceiptNumber(maOrderTempInfo.getCityId());
            if (maOrderAmount.getCashAmount().compareTo(BigDecimal.ZERO) > 0) {
                receiptNumberList.add(receiptNumber);
            }
            maOrderBillingPaymentDetails.setReceiptNumber(receiptNumber);
            maOrderBillingPaymentDetails.setPayType(OrderBillingPaymentType.CASH);
            maOrderBillingPaymentDetails.setPayTypeDesc(OrderBillingPaymentType.CASH.getDescription());
            maOrderBillingPaymentDetails.setAmount(maOrderAmount.getCashAmount());
            this.saveOrderBillingPaymentDetails(maOrderBillingPaymentDetails);
        }
        if (maOrderAmount.getOtherAmount().compareTo(BigDecimal.ZERO) > 0) {
            String receiptNumber = OrderUtils.generateReceiptNumber(maOrderTempInfo.getCityId());
            receiptNumberList.add(receiptNumber);
            maOrderBillingPaymentDetails.setReceiptNumber(receiptNumber);
            maOrderBillingPaymentDetails.setPayType(OrderBillingPaymentType.OTHER);
            maOrderBillingPaymentDetails.setPayTypeDesc(OrderBillingPaymentType.OTHER.getDescription());
            maOrderBillingPaymentDetails.setAmount(maOrderAmount.getOtherAmount());
            this.saveOrderBillingPaymentDetails(maOrderBillingPaymentDetails);
        }
        if (maOrderAmount.getPosAmount().compareTo(BigDecimal.ZERO) > 0) {
            String receiptNumber = OrderUtils.generateReceiptNumber(maOrderTempInfo.getCityId());
            receiptNumberList.add(receiptNumber);
            maOrderBillingPaymentDetails.setReceiptNumber(receiptNumber);
            maOrderBillingPaymentDetails.setPayType(OrderBillingPaymentType.POS);
            maOrderBillingPaymentDetails.setPayTypeDesc(OrderBillingPaymentType.POS.getDescription());
            maOrderBillingPaymentDetails.setAmount(maOrderAmount.getPosAmount());
            this.saveOrderBillingPaymentDetails(maOrderBillingPaymentDetails);
        }

        //得到导购id
        Long sellerId = maOrderTempInfo.getSalesConsultId();
        if (null == sellerId) {
            throw new RuntimeException("该订单导购ID为空");
        }
        //更新导购额度
        Double empCreditMoney = maOrderBillingDetailResponse.getEmpCreditMoney();
        if (null != empCreditMoney && empCreditMoney > 0D) {
            //得到该销售的可用额度
            GuideCreditMoney guideCreditMoney = maEmpCreditMoneyService.findGuideCreditMoneyAvailableByEmpId(sellerId);
            BigDecimal availableCreditMoney = guideCreditMoney.getCreditLimitAvailable().add(maOrderAmount.getAllAmount());
            //更改该销售的可用额度
            for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                int affectLine = maEmpCreditMoneyService.updateGuideCreditMoneyByRepayment(sellerId, availableCreditMoney, guideCreditMoney.getLastUpdateTime());
                if (affectLine > 0) {
                    //生成信用金额变成日志

                    GuideAvailableCreditChange guideAvailableCreditChange = new GuideAvailableCreditChange();
                    guideAvailableCreditChange.setCreditLimitAvailableAfterChange(availableCreditMoney);
                    guideAvailableCreditChange.setCreditLimitAvailableChangeAmount(maOrderAmount.getAllAmount());
                    guideAvailableCreditChange.setChangeType(EmpCreditMoneyChangeType.ORDER_REPAYMENT.toString());
                    guideAvailableCreditChange.setChangeTypeDesc(EmpCreditMoneyChangeType.ORDER_REPAYMENT.getDescription());
                    maEmpCreditMoneyDAO.saveCreditLimitAvailableChange(guideAvailableCreditChange);

                    GuideCreditMoneyDetail guideCreditMoneyDetail = new GuideCreditMoneyDetail();
                    guideCreditMoneyDetail.setEmpId(sellerId);
                    guideCreditMoneyDetail.setOriginalCreditLimitAvailable(guideCreditMoney.getCreditLimitAvailable());
                    guideCreditMoneyDetail.setCreditLimitAvailable(availableCreditMoney);
                    guideCreditChangeDetail.setEmpId(sellerId);
                    guideCreditChangeDetail.setAvailableCreditChangId(guideAvailableCreditChange.getId());
                    maEmpCreditMoneyService.saveCreditChange(guideCreditChangeDetail);
                    break;
                } else {
                    if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                        throw new SystemBusyException("系统繁忙，请稍后再试!");
                    }
                }
            }
        }
        return receiptNumberList;
    }

    @Override
    public Boolean isPayUp(String orderNumber) {
        return this.maOrderDAO.isPayUp(orderNumber);
    }

    @Override
    public String getShippingTime(String orderNumber) {
        System.out.println(orderNumber);
        return this.maOrderDAO.getShippingTime(orderNumber);
    }


    @Override
    public Boolean judgmentVerification(String orderNumber, String code) {
        MaOrderTempInfo maOrderTempInfo = this.getOrderInfoByOrderNo(orderNumber);
        if (code.equals(maOrderTempInfo.getPickUpCode())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void saveOrderBillingPaymentDetails(MaOrderBillingPaymentDetails maOrderBillingPaymentDetails) {
        this.maOrderDAO.saveOrderBillingPaymentDetails(maOrderBillingPaymentDetails);
    }

    @Override
    public void saveAppToEbsOrderReceiveInf(MaOrderReceiveInf maOrderReceiveInf) {
        this.maOrderDAO.saveAppToEbsOrderReceiveInf(maOrderReceiveInf);
    }


    @Override
    public PageInfo<MaAgencyAndArrearsOrderVO> findArrearsAndAgencyOrderList(Integer page, Integer size, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<MaAgencyAndArrearsOrderVO> arrearsAndAgencyOrderVOList = maOrderDAO.findArrearsAndAgencyOrderList(storeIds);
        return new PageInfo<>(arrearsAndAgencyOrderVOList);
    }


    @Override
    public PageInfo<MaAgencyAndArrearsOrderVO> findMaAgencyAndArrearsOrderListByScreen(Integer page, Integer size, Long cityId, Long storeId, Integer status, Integer isPayUp, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        if (storeId != -1) {
            cityId = null;
        }
        List<MaAgencyAndArrearsOrderVO> maAgencyAndArrearsOrderVOList = maOrderDAO.findMaAgencyAndArrearsOrderListByScreen(cityId, storeId, status, isPayUp, storeIds);
        return new PageInfo<>(maAgencyAndArrearsOrderVOList);
    }

    @Override
    public PageInfo<MaAgencyAndArrearsOrderVO> findMaAgencyAndArrearsOrderListByInfo(Integer page, Integer size, String info, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<MaAgencyAndArrearsOrderVO> maAgencyAndArrearsOrderVOList = maOrderDAO.findMaAgencyAndArrearsOrderListByInfo(info, storeIds);
        return new PageInfo<>(maAgencyAndArrearsOrderVOList);
    }

    @Override
    public List<MaOrderGoodsDetailResponse> getOrderGoodsDetailResponseList(String orderNumber) {
        List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderNumber);
        //创建商品返回list
        List<MaOrderGoodsDetailResponse> maOrderGoodsDetailResponseList = new ArrayList<>();
        for (OrderGoodsInfo orderGoodsDetailInfo : orderGoodsInfoList) {
            //创建商品返回对象
            MaOrderGoodsDetailResponse maOrderGoodsDetailResponse = new MaOrderGoodsDetailResponse();
            maOrderGoodsDetailResponse.setSku(orderGoodsDetailInfo.getSku());
            maOrderGoodsDetailResponse.setGoodsName(orderGoodsDetailInfo.getSkuName());
            maOrderGoodsDetailResponse.setQty(orderGoodsDetailInfo.getOrderQuantity() == null ? 0 : orderGoodsDetailInfo.getOrderQuantity());
            maOrderGoodsDetailResponse.setUnitPrice(orderGoodsDetailInfo.getRetailPrice() == null ? 0.00 : orderGoodsDetailInfo.getRetailPrice());
            //计算商品小计（零售）
            Double subTotalPrice = (orderGoodsDetailInfo.getOrderQuantity() == null ? 0 : orderGoodsDetailInfo.getOrderQuantity()) * (orderGoodsDetailInfo.getRetailPrice() == null ? 0.00 : orderGoodsDetailInfo.getRetailPrice());
            maOrderGoodsDetailResponse.setSubTotalPrice(subTotalPrice);
            //计算商品实付金额（分摊）
            Double reslPayment = (orderGoodsDetailInfo.getOrderQuantity() == null ? 0 : orderGoodsDetailInfo.getOrderQuantity()) * (orderGoodsDetailInfo.getReturnPrice() == null ? 0.00 : orderGoodsDetailInfo.getReturnPrice());
            maOrderGoodsDetailResponse.setRealPayment(reslPayment);
            if ("本品".equals(orderGoodsDetailInfo.getGoodsLineType().getDescription())) {
                maOrderGoodsDetailResponse.setGoodsType("本品");
            } else if ("赠品".equals(orderGoodsDetailInfo.getGoodsLineType().getDescription())) {
                maOrderGoodsDetailResponse.setGoodsType("赠品");
            } else if ("产品券".equals(orderGoodsDetailInfo.getGoodsLineType().getDescription())) {
                maOrderGoodsDetailResponse.setGoodsType("产品券");
            }
            maOrderGoodsDetailResponseList.add(maOrderGoodsDetailResponse);
        }
        return maOrderGoodsDetailResponseList;
    }


    @Override
    @Transactional
    public String auditOrderStatus(String orderNumber, String status,Long auditId) throws RuntimeException {
        if (ArrearsAuditStatus.AUDIT_PASSED.toString().equals(status)) {
            Date date = new Date();
            //获取订单基本信息
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);

            OrderTempInfo orderTempInfo = this.appOrderServiceImpl.getOrderInfoByOrderNo(orderNumber);
            MaOrderArrearsAudit maOrderArrearsAudit = this.getArrearsAuditInfoById(auditId);
            //生成收款单号
            String receiptNumber = OrderUtils.generateReceiptNumber(orderTempInfo.getCityId());
            Double collectionAmount = orderTempInfo.getCollectionAmount();
            Double realMoney = maOrderArrearsAudit.getRealMoney();
            //生成代收款记录
            OrderAgencyFundDO orderAgencyFundDO = new OrderAgencyFundDO();
            orderAgencyFundDO.setCustomerName(orderTempInfo.getCustomerName());
            orderAgencyFundDO.setOrderInfo(maOrderArrearsAudit.getUserId(), orderNumber, collectionAmount);
            orderAgencyFundDO.setCustomerAndSeller(orderTempInfo.getCustomerName(), maOrderArrearsAudit.getCustomerPhone(),
                    maOrderArrearsAudit.getSellerId(), maOrderArrearsAudit.getSellerName(), maOrderArrearsAudit.getSellerphone());
            orderAgencyFundDO.setAgencyFundInfo(maOrderArrearsAudit.getPaymentMethod(), realMoney, 0D, maOrderArrearsAudit.getRemarks());
            this.orderAgencyFundServiceImpl.save(orderAgencyFundDO);
            //创建收款记录
            OrderBillingPaymentDetails paymentDetails = new OrderBillingPaymentDetails(null, Calendar.getInstance().getTime(),
                    orderTempInfo.getOrderId(), Calendar.getInstance().getTime(), OrderBillingPaymentType.getOrderBillingPaymentTypeByDescription(maOrderArrearsAudit.getPaymentMethod()),
                    maOrderArrearsAudit.getPaymentMethod(), orderNumber, maOrderArrearsAudit.getUserId(), PaymentSubjectType.DELIVERY_CLERK,
                    PaymentSubjectType.DELIVERY_CLERK.getDescription(), realMoney, null, receiptNumber);
            this.appOrderServiceImpl.savePaymentDetails(paymentDetails);
            if (realMoney >= 0) {
                //修改订单欠款
                OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
                orderBillingDetails.setOrderNumber(orderNumber);
                orderBillingDetails.setArrearage(CountUtil.sub(orderTempInfo.getOwnMoney(), realMoney));
                if (orderBillingDetails.getArrearage() > 0D) {
                    orderBillingDetails.setIsPayUp(false);
                } else {
                    orderBillingDetails.setIsPayUp(true);
                    orderBillingDetails.setPayUpTime(date);
                }
                if (OrderBillingPaymentType.CASH.equals(paymentDetails.getPayType())) {
                    orderBillingDetails.setDeliveryCash(realMoney);
                    orderBillingDetails.setDeliveryPos(0D);
                } else if (OrderBillingPaymentType.POS.equals(paymentDetails.getPayType())) {
                    orderBillingDetails.setDeliveryCash(0D);
                    orderBillingDetails.setDeliveryPos(realMoney);
                }
                this.appOrderServiceImpl.updateOwnMoneyByOrderNo(orderBillingDetails);

                //获取导购信用金
                EmpCreditMoney empCreditMoney = appEmployeeService.findEmpCreditMoneyByEmpId(orderTempInfo.getSellerId());
                //返还信用金后导购信用金额度
                Double creditMoney = CountUtil.add(empCreditMoney.getCreditLimitAvailable() + realMoney);
                //修改导购信用额度
               // Integer affectLine = appEmployeeService.unlockGuideCreditByUserIdAndGuideCreditAndVersion(empCreditMoney.getEmpId(), realMoney, empCreditMoney.getLastUpdateTime());
               // if (affectLine > 0) {
                    //记录导购信用金变更日志
                    EmpCreditMoneyChangeLog empCreditMoneyChangeLog = new EmpCreditMoneyChangeLog();
                    empCreditMoneyChangeLog.setEmpId(empCreditMoney.getEmpId());
                    empCreditMoneyChangeLog.setCreateTime(date);
                    empCreditMoneyChangeLog.setCreditLimitAvailableChangeAmount(realMoney);
                    empCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                    empCreditMoneyChangeLog.setReferenceNumber(orderNumber);
                    empCreditMoneyChangeLog.setChangeType(EmpCreditMoneyChangeType.ORDER_REPAYMENT);
                    empCreditMoneyChangeLog.setChangeTypeDesc(EmpCreditMoneyChangeType.ORDER_REPAYMENT.getDescription());
                    empCreditMoneyChangeLog.setOperatorType(AppIdentityType.ADMINISTRATOR);
                    //保存日志
                   // appEmployeeService.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLog);
               // }
            }
            //生成订单物流详情
            OrderDeliveryInfoDetails orderDeliveryInfoDetails = new OrderDeliveryInfoDetails();
            orderDeliveryInfoDetails.setDeliveryInfo(orderNumber, LogisticStatus.CONFIRM_ARRIVAL, "确认到货！", "送达", orderTempInfo.getOperatorNo(), maOrderArrearsAudit.getPicture(), "", "");
            this.orderDeliveryInfoDetailsServiceImpl.addOrderDeliveryInfoDetails(orderDeliveryInfoDetails);
            //修改订单状态
            OrderBaseInfo orderInfo = new OrderBaseInfo();
            orderInfo.setOrderNumber(orderNumber);
            orderInfo.setStatus(AppOrderStatus.FINISHED);
            orderInfo.setDeliveryStatus(LogisticStatus.CONFIRM_ARRIVAL);
            this.appOrderServiceImpl.updateOrderStatusByOrderNo(orderInfo);
            //修改审核状态
            this.maOrderDAO.auditOrderStatus(orderNumber, status,auditId);
            //传ebs收款接口
            return receiptNumber;
        } else if (ArrearsAuditStatus.AUDIT_NO.toString().equals(status)) {
            //修改审核状态
            this.maOrderDAO.auditOrderStatus(orderNumber, status,auditId);
        }
        return null;
    }


    @Override
    public MaOrderArrearsAudit getArrearsAuditInfo(String orderNumber) {
        return this.maOrderDAO.getArrearsAuditInfo(orderNumber);
    }

    @Override
    public MaOrderArrearsAudit getArrearsAuditInfoById(Long id) {
        return this.maOrderDAO.getArrearsAuditInfoById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> arrearsOrderRepayment(MaOrderAmount maOrderAmount, MaOrderBillingDetailResponse maOrderBillingDetailResponse, GuideCreditChangeDetail guideCreditChangeDetail) {
        // 更新订单支付信息
        List<String> receiptNumberList = this.orderReceivables(maOrderAmount, maOrderBillingDetailResponse, guideCreditChangeDetail);
        //更新欠款审核表
        this.updateOrderArrearsAudit(maOrderAmount.getOrderNumber(), maOrderAmount.getDate());
        return receiptNumberList;
    }

    @Override
    public Long querySellerIdByOrderNumber(String orderNumber) {
        return this.maOrderDAO.querySellerIdByOrderNumber(orderNumber);
    }

    @Override
    public OrderBillingDetails createMaOrderBillingDetails(OrderBillingDetails orderBillingDetails, Double preDeposit, Double cash, Double posMoney, Double otherMoney, String posNumber, String payTime) {
        orderBillingDetails.setCreateTime(Calendar.getInstance().getTime());
        //计算并设置产品券折扣
        orderBillingDetails.setProductCouponDiscount(0D);
        //计算并设置乐币折扣
        orderBillingDetails.setLebiQuantity(0);
        //计算并设置优惠券减少金额
        orderBillingDetails.setCashCouponDiscount(0D);
        //设置顾客预存款
        orderBillingDetails.setCusPreDeposit(0D);
        //设置导购信用额度
        orderBillingDetails.setEmpCreditMoney(0D);
        //设置门店信用额度
        orderBillingDetails.setStoreCreditMoney(0D);
        //设置门店现金返利
        orderBillingDetails.setStoreSubvention(0D);
        orderBillingDetails.setOnlinePayType(OnlinePayType.NO);

        //计算订单金额小计以及应付款
        Double orderAmountSubtotal;
        Double amountPayable = 0D;
        orderAmountSubtotal = orderBillingDetails.getTotalGoodsPrice()
                + OrderUtils.replaceNullWithZero(orderBillingDetails.getFreight())
                - OrderUtils.replaceNullWithZero(orderBillingDetails.getProductCouponDiscount())
                - OrderUtils.replaceNullWithZero(orderBillingDetails.getMemberDiscount())
                - OrderUtils.replaceNullWithZero(orderBillingDetails.getPromotionDiscount())
                - OrderUtils.replaceNullWithZero(orderBillingDetails.getLebiCashDiscount())
                - OrderUtils.replaceNullWithZero(orderBillingDetails.getCashCouponDiscount());
        orderBillingDetails.setOrderAmountSubtotal(orderAmountSubtotal);

        amountPayable = orderBillingDetails.getOrderAmountSubtotal()
                - OrderUtils.replaceNullWithZero(orderBillingDetails.getStPreDeposit())
                - OrderUtils.replaceNullWithZero(orderBillingDetails.getEmpCreditMoney());
        orderBillingDetails.setAmountPayable(amountPayable);
        if (amountPayable < 0D) {
            throw new OrderPayableAmountException("订单应付款金额异常(<0)");
        }
        orderBillingDetails.setArrearage(0D);
        orderBillingDetails.setIsPayUp(true);

        //设置门店预存款
        Double stPreDeposit = 0D;
        if (null != preDeposit) {
            stPreDeposit = preDeposit;
            orderBillingDetails.setStoreCash(0D);
            orderBillingDetails.setStoreOtherMoney(0D);
            orderBillingDetails.setStorePosMoney(0D);
            orderBillingDetails.setStorePosNumber(null);
        } else {
            orderBillingDetails.setStoreCash(null == cash ? 0D : cash);
            orderBillingDetails.setStoreOtherMoney(null == otherMoney ? 0D : otherMoney);
            orderBillingDetails.setStorePosMoney(null == posMoney ? 0D : posMoney);
            orderBillingDetails.setStorePosNumber(null == posNumber ? null : posNumber);
            orderBillingDetails.setStPreDeposit(0D);
        }
        orderBillingDetails.setStPreDeposit(stPreDeposit);
        orderBillingDetails.setPayUpTime(new Date());
        if (StringUtils.isNotBlank(payTime)) {
            orderBillingDetails.setManageReceiptTime(DateUtil.parseToDate(payTime,"yyyy-MM-dd"));
        }
        return orderBillingDetails;
    }


    @Override
    public Map<Object, Object> createMaOrderBillingPaymentDetails(OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails, AppStore store, AppCustomer customer, Long creatorId) {
        Map<Object, Object> map = new HashMap<>();

        List<OrderBillingPaymentDetails> billingPaymentDetails = new ArrayList<>();
        List<RechargeReceiptInfo> rechargeReceiptInfoList = new ArrayList<>();
        List<RechargeOrder> rechargeOrderList = new ArrayList<>();
        if (null != orderBaseInfo && null != orderBillingDetails) {
            //门店预存款
            if (null != orderBillingDetails.getStPreDeposit() && orderBillingDetails.getStPreDeposit() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.ST_PREPAY, orderBillingDetails.getStPreDeposit(),
                        PaymentSubjectType.SELLER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);

                RechargeReceiptInfo rechargeReceiptInfo = this.createRechargeReceiptInfo(orderBillingDetails.getStPreDeposit(), orderBaseInfo.getOrderNumber(), OrderBillingPaymentType.ST_PREPAY, details.getReceiptNumber());
                rechargeReceiptInfoList.add(rechargeReceiptInfo);
                RechargeOrder rechargeOrder = this.createRechargeOrder(orderBillingDetails.getStPreDeposit(), orderBaseInfo.getOrderNumber(), store.getStoreId(), OrderBillingPaymentType.ST_PREPAY, creatorId, customer.getCusId());
                rechargeOrderList.add(rechargeOrder);
            }
            //门店现金
            if (null != orderBillingDetails.getStoreCash() && orderBillingDetails.getStoreCash() != 0D) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.CASH, orderBillingDetails.getStoreCash(),
                        PaymentSubjectType.SELLER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);
                if (orderBillingDetails.getStoreCash() > 0) {
                    RechargeReceiptInfo rechargeReceiptInfo = this.createRechargeReceiptInfo(orderBillingDetails.getStoreCash(), orderBaseInfo.getOrderNumber(), OrderBillingPaymentType.CASH, details.getReceiptNumber());
                    rechargeReceiptInfoList.add(rechargeReceiptInfo);
                    RechargeOrder rechargeOrder = this.createRechargeOrder(orderBillingDetails.getStoreCash(), orderBaseInfo.getOrderNumber(), store.getStoreId(), OrderBillingPaymentType.CASH, creatorId, customer.getCusId());
                    rechargeOrderList.add(rechargeOrder);
                }
            }
            //门店POS
            if (null != orderBillingDetails.getStorePosMoney() && orderBillingDetails.getStorePosMoney() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.POS, orderBillingDetails.getStorePosMoney(),
                        PaymentSubjectType.SELLER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);
                if (null != orderBillingDetails.getStoreCash() && orderBillingDetails.getStoreCash() < 0) {
                    Double addCashAndPos = CountUtil.add(orderBillingDetails.getStoreCash(), orderBillingDetails.getStorePosMoney());
                    RechargeReceiptInfo rechargeReceiptInfo = this.createRechargeReceiptInfo(addCashAndPos, orderBaseInfo.getOrderNumber(), OrderBillingPaymentType.POS, details.getReceiptNumber());
                    rechargeReceiptInfoList.add(rechargeReceiptInfo);
                    RechargeOrder rechargeOrder = this.createRechargeOrder(addCashAndPos, orderBaseInfo.getOrderNumber(), store.getStoreId(), OrderBillingPaymentType.POS, creatorId, customer.getCusId());
                    rechargeOrderList.add(rechargeOrder);
                } else {
                    RechargeReceiptInfo rechargeReceiptInfo = this.createRechargeReceiptInfo(orderBillingDetails.getStorePosMoney(), orderBaseInfo.getOrderNumber(), OrderBillingPaymentType.POS, details.getReceiptNumber());
                    rechargeReceiptInfoList.add(rechargeReceiptInfo);
                    RechargeOrder rechargeOrder = this.createRechargeOrder(orderBillingDetails.getStorePosMoney(), orderBaseInfo.getOrderNumber(), store.getStoreId(), OrderBillingPaymentType.POS, creatorId, customer.getCusId());
                    rechargeOrderList.add(rechargeOrder);
                }
            }
            //门店其他
            if (null != orderBillingDetails.getStoreOtherMoney() && orderBillingDetails.getStoreOtherMoney() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.OTHER, orderBillingDetails.getStoreOtherMoney(),
                        PaymentSubjectType.SELLER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);

                RechargeReceiptInfo rechargeReceiptInfo = this.createRechargeReceiptInfo(orderBillingDetails.getStoreOtherMoney(), orderBaseInfo.getOrderNumber(), OrderBillingPaymentType.OTHER, details.getReceiptNumber());
                rechargeReceiptInfoList.add(rechargeReceiptInfo);
                RechargeOrder rechargeOrder = this.createRechargeOrder(orderBillingDetails.getStoreOtherMoney(), orderBaseInfo.getOrderNumber(), store.getStoreId(), OrderBillingPaymentType.OTHER, creatorId, customer.getCusId());
                rechargeOrderList.add(rechargeOrder);
            }
        }
        map.put("billingPaymentDetails", billingPaymentDetails);
        map.put("rechargeReceiptInfoList", rechargeReceiptInfoList);
        map.put("rechargeOrderList", rechargeOrderList);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMaOrderBusiness(Integer identityType, Long userId, OrderBillingDetails orderBillingDetails, OrderBaseInfo orderBaseInfo,
                                      List<OrderGoodsInfo> orderGoodsInfoList, List<OrderBillingPaymentDetails> paymentDetails, String ipAddress,
                                      OrderLogisticsInfo orderLogisticsInfo, Long operatorId, List<RechargeReceiptInfo> rechargeReceiptInfoList,
                                      List<RechargeOrder> rechargeOrderList, List<PromotionSimpleInfo> promotionSimpleInfoList) throws UnsupportedEncodingException {
        //******* 检查库存和与账单支付金额是否充足,如果充足就扣减相应的数量
        this.deductionsStPreDeposit(identityType, userId, orderBillingDetails, orderBaseInfo.getOrderNumber(), ipAddress);
        //******* 持久化订单相关实体信息  *******
        this.saveAndHandleMaOrderRelevantInfo(orderBaseInfo, orderGoodsInfoList, orderBillingDetails, paymentDetails, orderLogisticsInfo, operatorId, rechargeReceiptInfoList, rechargeOrderList, promotionSimpleInfoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductionsStPreDeposit(Integer identityType, Long userId, OrderBillingDetails billingDetails, String orderNumber, String ipAddress) throws LockStorePreDepositException {
        //扣减门店预存款
        if (null != billingDetails.getStPreDeposit() && billingDetails.getStPreDeposit() > 0) {
            for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                StorePreDeposit preDeposit = storeService.findStorePreDepositByEmpId(userId);
                if (null != preDeposit) {
                    if (preDeposit.getBalance() < billingDetails.getStPreDeposit()) {
                        throw new LockStorePreDepositException("导购所属门店预存款余额不足!");
                    }
                    int affectLine = storeService.lockStoreDepositByUserIdAndStoreDeposit(
                            userId, billingDetails.getStPreDeposit(), preDeposit.getLastUpdateTime());
                    if (affectLine > 0) {
                        StPreDepositLogDO log = new StPreDepositLogDO();
                        log.setStoreId(preDeposit.getStoreId());
                        log.setChangeMoney(CountUtil.sub(0, billingDetails.getStPreDeposit()));
                        log.setBalance(CountUtil.sub(preDeposit.getBalance(), billingDetails.getStPreDeposit()));
                        log.setCreateTime(LocalDateTime.now());
                        log.setOrderNumber(orderNumber);
                        log.setOperatorId(userId);
                        log.setOperatorType(AppIdentityType.getAppIdentityTypeByValue(identityType));
                        log.setOperatorIp(ipAddress);
                        log.setChangeType(StorePreDepositChangeType.PLACE_ORDER);
                        log.setChangeTypeDesc(StorePreDepositChangeType.PLACE_ORDER.getDescription());
                        storeService.addStPreDepositLog(log);
                        break;
                    } else {
                        if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                            throw new SystemBusyException("系统繁忙，请稍后再试!");
                        }
                    }
                } else {
                    throw new LockStorePreDepositException("没有找到该导购所在门店的预存款信息!");
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAndHandleMaOrderRelevantInfo(OrderBaseInfo orderBaseInfo, List<OrderGoodsInfo> orderGoodsInfoList,
                                                 OrderBillingDetails orderBillingDetails, List<OrderBillingPaymentDetails> paymentDetails,
                                                 OrderLogisticsInfo orderLogisticsInfo, Long operatorId, List<RechargeReceiptInfo> rechargeReceiptInfoList,
                                                 List<RechargeOrder> rechargeOrderList ,List<PromotionSimpleInfo> promotionSimpleInfos) throws UnsupportedEncodingException {
        if (null != orderBaseInfo) {
            Map<Long, Double> map = actService.returnGcActIdAndJXDiscunt(promotionSimpleInfos);

            AppCustomer customer = new AppCustomer();
            Long cusId = orderBaseInfo.getCustomerId();
            customer = customerService.findById(cusId);
            if (null != orderBillingDetails && orderBillingDetails.getIsPayUp()) {
                //修改顾客上一次下单时间
                customer.setLastConsumptionTime(new Date());

                // 更新顾客归属门店及导购
                updateCustomerAttachedStoreAndSeller(orderBaseInfo, customer);

                //更新顾客信息
                if (null != customer.getCusId()) {
                    customerService.update(customer);
                }

            }
            /* ******************* 保存订单相关一系列信息 ******************* */
            //保存订单基础信息
            orderService.saveOrderBaseInfo(orderBaseInfo);

            if (null != orderBaseInfo.getId()) {
                //保存订单商品信息
                if (null != orderGoodsInfoList && orderGoodsInfoList.size() > 0) {
                    for (OrderGoodsInfo goodsInfo : orderGoodsInfoList) {
                        goodsInfo.setOid(orderBaseInfo.getId());
                        orderService.saveOrderGoodsInfo(goodsInfo);

                        if (null != goodsInfo.getId()) {
                            if (null != goodsInfo.getOrderQuantity() && goodsInfo.getOrderQuantity() > 0) {
                                for (int i = 1; i <= goodsInfo.getOrderQuantity(); i++) {
                                    //创建产品券信息
                                    CustomerProductCoupon customerProductCoupon = new CustomerProductCoupon();
                                    customerProductCoupon.setCustomerId(customer.getCusId());
                                    customerProductCoupon.setGoodsId(goodsInfo.getGid());
                                    customerProductCoupon.setQuantity(1);
                                    customerProductCoupon.setGetType(CouponGetType.BUY);
                                    customerProductCoupon.setGetTime(orderBaseInfo.getCreateTime());

                                    Calendar c = Calendar.getInstance();
                                    customerProductCoupon.setEffectiveStartTime(c.getTime());

                                    c.add(Calendar.MONTH, 6);

                                    customerProductCoupon.setEffectiveEndTime(c.getTime());
                                    customerProductCoupon.setIsUsed(Boolean.FALSE);
                                    customerProductCoupon.setUseTime(null);
                                    customerProductCoupon.setUseOrderNumber(null);
                                    customerProductCoupon.setGetOrderNumber(orderBaseInfo.getOrderNumber());
                                    customerProductCoupon.setBuyPrice(goodsInfo.getReturnPrice());
                                    customerProductCoupon.setStoreId(orderBaseInfo.getStoreId());
                                    customerProductCoupon.setSellerId(orderBaseInfo.getSalesConsultId());
                                    customerProductCoupon.setStatus(Boolean.TRUE);
                                    customerProductCoupon.setDisableTime(null);
                                    customerProductCoupon.setGoodsLineId(goodsInfo.getId());
                                    customerProductCoupon.setGoodsSign(goodsInfo.getGoodsSign());
                                    if (null == goodsInfo.getPromotionId()){
                                        customerProductCoupon.setSettlementPrice(goodsInfo.getSettlementPrice());
                                    }else {
                                        Long actId = Long.valueOf(goodsInfo.getPromotionId());
                                        if (map.containsKey(actId)) {
                                            Double gcDiscount = map.get(actId);
                                            customerProductCoupon.setSettlementPrice(goodsInfo.getSettlementPrice() - gcDiscount);
                                        } else {
                                            customerProductCoupon.setSettlementPrice(goodsInfo.getSettlementPrice());
                                        }
                                    }
                                    customerProductCoupon.setWholesalePrice(goodsInfo.getWholesalePrice());

                                    //保存产品券信息
                                    productCouponService.addCustomerProductCoupon(customerProductCoupon);

                                    if (null != customerProductCoupon.getId()) {
                                        //保存顾客产品券变更日志
                                        CustomerProductCouponChangeLog customerProductCouponChangeLog = new CustomerProductCouponChangeLog();
                                        customerProductCouponChangeLog.setCusId(orderBaseInfo.getCustomerId());
                                        customerProductCouponChangeLog.setUseTime(orderBaseInfo.getCreateTime());
                                        customerProductCouponChangeLog.setCouponId(customerProductCoupon.getId());
                                        customerProductCouponChangeLog.setReferenceNumber(orderBaseInfo.getOrderNumber());
                                        customerProductCouponChangeLog.setChangeType(CustomerProductCouponChangeType.BUY_COUPON);
                                        customerProductCouponChangeLog.setChangeTypeDesc(CustomerProductCouponChangeType.BUY_COUPON.getDescription());
                                        customerProductCouponChangeLog.setOperatorId(operatorId);
                                        customerProductCouponChangeLog.setOperatorIp(null);
                                        customerProductCouponChangeLog.setRemark(null);

                                        productCouponService.addCustomerProductCouponChangeLog(customerProductCouponChangeLog);
                                    } else {
                                        throw new OrderSaveException("顾客产品券主键生成失败!");
                                    }

                                }
                            }
                        } else {
                            throw new OrderSaveException("商品主键生成失败!");
                        }
                    }
                }

                //保存订单物流信息
                if (null != orderLogisticsInfo) {
                    orderLogisticsInfo.setOid(orderBaseInfo.getId());
                    orderService.saveOrderLogisticsInfo(orderLogisticsInfo);
                }
                //保存订单账单信息
                if (null != orderBillingDetails) {
                    orderBillingDetails.setOid(orderBaseInfo.getId());
                    orderService.saveOrderBillingDetails(orderBillingDetails);
                }
                //保存订单账单支付明细信息
                if (null != paymentDetails && paymentDetails.size() > AppConstant.INTEGER_ZERO) {
                    for (OrderBillingPaymentDetails paymentDetail : paymentDetails) {
                        paymentDetail.setOrderId(orderBaseInfo.getId());
                        orderService.saveOrderBillingPaymentDetail(paymentDetail);
                    }
                }
            } else {
                throw new OrderSaveException("订单主键生成失败!");
            }

            //保存传EBS信息
            if (null != rechargeReceiptInfoList && rechargeReceiptInfoList.size() > 0) {
                for (RechargeReceiptInfo rechargeReceiptInfo : rechargeReceiptInfoList) {
                    rechargeService.saveRechargeReceiptInfo(rechargeReceiptInfo);
                }
            }
            if (null != rechargeOrderList && rechargeOrderList.size() > 0) {
                for (RechargeOrder rechargeOrder : rechargeOrderList) {
                    rechargeService.saveRechargeOrder(rechargeOrder);
                }
            }
        }
    }

    private void updateCustomerAttachedStoreAndSeller(OrderBaseInfo baseInfo, AppCustomer customer) {
        if (baseInfo.getCreatorIdentityType() == AppIdentityType.SELLER) {
            AppStore originalStore = storeService.findStoreByUserIdAndIdentityType(baseInfo.getCustomerId(),
                    AppIdentityType.CUSTOMER.getValue());
            if (originalStore.getIsDefault()) {
                AppStore newStore = storeService.findStoreByUserIdAndIdentityType(baseInfo.getCreatorId(),
                        AppIdentityType.SELLER.getValue());
                customer.setStoreId(newStore.getStoreId());
                customer.setSalesConsultId(baseInfo.getCreatorId());
                customer.setBindingTime(new Date());
            }

        }
    }

    @Override
    public OrderBaseInfo createMaOrderBaseInfo(AppCustomer appCustomer, City city, AppStore appStore, AppEmployee appEmployee,
                                               Double preDepositMoney, String remarks, String preDepositRemarks, Double totalMoney, String orderNumber, String salesNumber) {
        OrderBaseInfo orderBaseInfo = new OrderBaseInfo();
        //城市id
        orderBaseInfo.setCityId(city.getCityId());
        //城市名称
        orderBaseInfo.setCityName(city.getName());
        //设置订单创建时间
        Calendar calendar = Calendar.getInstance();
        //创建时间
        orderBaseInfo.setCreateTime(calendar.getTime());
        //计算失效时间
        calendar.setTime(calendar.getTime());
        calendar.add(Calendar.MONTH, 6);
        //设置失效时间
        orderBaseInfo.setEffectiveEndTime(calendar.getTime());
        //订单号
        orderBaseInfo.setOrderNumber(orderNumber);

        //下单人id
        orderBaseInfo.setCreatorId(appEmployee.getEmpId());
        //下单人姓名
        orderBaseInfo.setCreatorName(appEmployee.getName());
        //下单人电话
        orderBaseInfo.setCreatorPhone(appEmployee.getMobile());
        //顾客id
        orderBaseInfo.setCustomerId(appCustomer.getCusId());
        //顾客姓名
        orderBaseInfo.setCustomerName(appCustomer.getName());
        //顾客电话
        orderBaseInfo.setCustomerPhone(appCustomer.getMobile());
        //顾客类型
        orderBaseInfo.setCustomerType(appCustomer.getCustomerType());
        //下单人身份类型
        orderBaseInfo.setCreatorIdentityType(appEmployee.getIdentityType());
        //导购id
        orderBaseInfo.setSalesConsultId(appEmployee.getEmpId());
        //导购姓名
        orderBaseInfo.setSalesConsultName(appEmployee.getName());
        //导购电话
        orderBaseInfo.setSalesConsultPhone(appEmployee.getMobile());
        //订单状态
        orderBaseInfo.setStatus(AppOrderStatus.FINISHED);
        //门店组织id
        orderBaseInfo.setStoreOrgId(appStore.getStoreOrgId());
        //门店id
        orderBaseInfo.setStoreId(appStore.getStoreId());
        //分公司id
        orderBaseInfo.setSobId(appStore.getSobId());
        //门店编码
        orderBaseInfo.setStoreCode(appStore.getStoreCode());
        //门店组织全编码
        orderBaseInfo.setStoreStructureCode(appStore.getStoreStructureCode());
        //后台买券销售纸质单号
        orderBaseInfo.setSalesNumber(salesNumber);
        //订单类型
        orderBaseInfo.setOrderType(AppOrderType.COUPON);
        //订单下单主体类型
        orderBaseInfo.setOrderSubjectType(AppOrderSubjectType.STORE);
        //配送方式
        orderBaseInfo.setDeliveryType(AppDeliveryType.PRODUCT_COUPON);
        orderBaseInfo.setIsEvaluated(null);
        orderBaseInfo.setPickUpCode(null);
        orderBaseInfo.setDeliveryStatus(null);
        orderBaseInfo.setEffectiveEndTime(null);
        orderBaseInfo.setAuditNo(null);
        if (null == preDepositMoney) {
            orderBaseInfo.setRemark(remarks);
            orderBaseInfo.setTotalGoodsPrice(totalMoney);
        } else {
            orderBaseInfo.setRemark(preDepositRemarks);
            orderBaseInfo.setTotalGoodsPrice(preDepositMoney);
        }
        return orderBaseInfo;
    }

    /**
     * 查询当天所有待付款订单
     */
    @Override
    public void findScanningUnpaidOrder() {
        Date date = new Date();
        //获取当前时间5小时前时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 5);
        String findDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
        //获取所有待付款订单
        List<OrderBaseInfo> orderBaseInfoList = maOrderDAO.scanningUnpaidOrder(findDate);
        if (null != orderBaseInfoList && orderBaseInfoList.size() > 0) {
            for (OrderBaseInfo orderBaseInfo : orderBaseInfoList) {
                if (date.after(orderBaseInfo.getEffectiveEndTime())) {
                    this.scanningUnpaidOrder(orderBaseInfo);
                } else {
                    System.out.println(new Date() + "：未查询到待付款超时订单，订单号：");
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Async
    public String scanningUnpaidOrder(OrderBaseInfo orderBaseInfo) {
        Date date = new Date();
        //获取错误信息
        TimingTaskErrorMessageDO timingTaskErrorMessageDO = null;
        try {
            //获取退单号
            String returnNumber = OrderUtils.getReturnNumber();
            System.out.println(new Date() + "：开始处理待付款超时订单，订单号：" + orderBaseInfo.getOrderNumber());
            //创建退单头
            ReturnOrderBaseInfo returnOrderBaseInfo = new ReturnOrderBaseInfo();
            //获取订单账目明细
            OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderBaseInfo.getOrderNumber());


            //获取错误信息
            timingTaskErrorMessageDO = timingTaskErrorMessageDAO.findTimingTaskErrorMessageByOrderNumber(orderBaseInfo.getOrderNumber());

            //记录退单头信息
            returnOrderBaseInfo.setOrderId(orderBaseInfo.getId());
            returnOrderBaseInfo.setOrderNo(orderBaseInfo.getOrderNumber());
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
            List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderBaseInfo.getOrderNumber());

            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                returnPrice += (orderGoodsInfo.getOrderQuantity() * orderGoodsInfo.getReturnPrice());
            }
            returnOrderBaseInfo.setReturnPrice(returnPrice);
            returnOrderBaseInfo.setRemarksInfo(null);
            returnOrderBaseInfo.setCreatorIdentityType(AppIdentityType.ADMINISTRATOR);
            returnOrderBaseInfo.setCreatorName("SYSTEM");
            returnOrderBaseInfo.setCreatorPhone("SYSTEM");
            returnOrderBaseInfo.setCustomerId(orderBaseInfo.getCustomerId());
            returnOrderBaseInfo.setCustomerName(orderBaseInfo.getCustomerName());
            returnOrderBaseInfo.setReasonInfo("超过待付款时间");
            returnOrderBaseInfo.setOrderType(orderBaseInfo.getOrderType());
            returnOrderBaseInfo.setReturnStatus(AppReturnOrderStatus.FINISHED);
            //保存退单头信息
            returnOrderService.saveReturnOrderBaseInfo(returnOrderBaseInfo);
            //获取退单头id
            Long returnOrderId = returnOrderBaseInfo.getRoid();


            List<ReturnOrderGoodsInfo> returnOrderGoodsInfos = new ArrayList<>(orderGoodsInfoList.size());
            for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                //创建退货商品实体类
                ReturnOrderGoodsInfo returnGoodsInfo = new ReturnOrderGoodsInfo();
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
                returnGoodsInfo.setGoodsLineType(orderGoodsInfo.getGoodsLineType());
                returnGoodsInfo.setGoodsSign(orderGoodsInfo.getGoodsSign());
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
                        Integer affectLine = 0;
                        if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.DECORATE_MANAGER)) {
                            //退还城市库存量
                            affectLine = cityService.updateCityInventoryByEmployeeIdAndGoodsIdAndInventoryAndVersion(orderBaseInfo.getCreatorId(), orderGoodsInfo.getGid(), orderGoodsInfo.getOrderQuantity(), cityInventory.getLastUpdateTime());
                        } else {
                            //退还城市库存量
                            affectLine = cityService.updateCityInventoryByEmployeeIdAndGoodsIdAndInventoryAndVersion(orderBaseInfo.getSalesConsultId(), orderGoodsInfo.getGid(), orderGoodsInfo.getOrderQuantity(), cityInventory.getLastUpdateTime());
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
                            cityInventoryAvailableQtyChangeLog.setChangeType(null);
                            cityInventoryAvailableQtyChangeLog.setChangeTypeDesc("超过待付款时间");
                            cityInventoryAvailableQtyChangeLog.setReferenceNumber(orderBaseInfo.getOrderNumber());
                            //保存记录
                            cityService.addCityInventoryAvailableQtyChangeLog(cityInventoryAvailableQtyChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                if (null == timingTaskErrorMessageDO) {
                                    timingTaskErrorMessageDO = new TimingTaskErrorMessageDO();
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还城市库存量失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.saveTimingTaskErrorMessage(timingTaskErrorMessageDO);
                                } else {
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还城市库存量失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.updateTimingTaskErrorMessageByOrderNo(timingTaskErrorMessageDO);
                                }
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                } else if ("门店自提".equals(orderBaseInfo.getDeliveryType().getDescription())) {
                    OrderLogisticsInfo orderLogisticsInfo = appOrderService.getOrderLogistice(orderBaseInfo.getOrderNumber());
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
                            storeInventoryAvailableQtyChangeLog.setChangeType(StoreInventoryAvailableQtyChangeType.SELF_TAKE_ORDER_CANCEL_AUTO);
                            storeInventoryAvailableQtyChangeLog.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.SELF_TAKE_ORDER_CANCEL_AUTO.getDescription());
                            storeInventoryAvailableQtyChangeLog.setReferenceNumber(orderBaseInfo.getOrderNumber());
                            //保存记录
                            appStoreService.addStoreInventoryAvailableQtyChangeLog(storeInventoryAvailableQtyChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                if (null == timingTaskErrorMessageDO) {
                                    timingTaskErrorMessageDO = new TimingTaskErrorMessageDO();
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还门店可用量失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.saveTimingTaskErrorMessage(timingTaskErrorMessageDO);
                                } else {
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还门店可用量失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.updateTimingTaskErrorMessageByOrderNo(timingTaskErrorMessageDO);
                                }
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
            if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.CUSTOMER)) {
                //返回乐币
                if (orderBillingDetails.getLebiQuantity() != null && orderBillingDetails.getLebiQuantity() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取顾客当前乐币数量
                        CustomerLeBi customerLeBi = appCustomerService.findCustomerLebiByCustomerId(orderBaseInfo.getCustomerId());
                        //返还乐币后顾客乐币数量
                        Integer lebiTotal = (customerLeBi.getQuantity() + orderBillingDetails.getLebiQuantity());
                        //更改顾客乐币数量
                        Integer affectLine = leBiVariationLogService.updateLeBiQtyByUserId(lebiTotal, customerLeBi.getLastUpdateTime(), orderBaseInfo.getCustomerId());
                        if (affectLine > 0) {
                            //记录乐币日志
                            CustomerLeBiVariationLog leBiVariationLog = new CustomerLeBiVariationLog();
                            leBiVariationLog.setCusId(orderBaseInfo.getCustomerId());
                            leBiVariationLog.setVariationQuantity(orderBillingDetails.getLebiQuantity());
                            leBiVariationLog.setAfterVariationQuantity(lebiTotal);
                            leBiVariationLog.setVariationTime(date);
                            leBiVariationLog.setLeBiVariationType(LeBiVariationType.CANCEL_ORDER);
                            leBiVariationLog.setVariationTypeDesc("超过待付款时间");
                            leBiVariationLog.setOrderNum(orderBaseInfo.getOrderNumber());
                            //保存日志
                            leBiVariationLogService.addCustomerLeBiVariationLog(leBiVariationLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                if (null == timingTaskErrorMessageDO) {
                                    timingTaskErrorMessageDO = new TimingTaskErrorMessageDO();
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还顾客乐币数量失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.saveTimingTaskErrorMessage(timingTaskErrorMessageDO);
                                } else {
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还顾客乐币数量失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.updateTimingTaskErrorMessageByOrderNo(timingTaskErrorMessageDO);
                                }
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
                //返回顾客预存款
                if (orderBillingDetails.getCusPreDeposit() != null && orderBillingDetails.getCusPreDeposit() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取顾客预存款
                        CustomerPreDeposit customerPreDeposit = appCustomerService.findByCusId(orderBaseInfo.getCustomerId());
                        //返还预存款后顾客预存款金额
                        Double cusPreDeposit = (customerPreDeposit.getBalance() + orderBillingDetails.getCusPreDeposit());
                        //更改顾客预存款金额
                        Integer affectLine = appCustomerService.updateDepositByUserIdAndVersion(orderBaseInfo.getCustomerId(), orderBillingDetails.getCusPreDeposit(), customerPreDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录预存款日志
                            CusPreDepositLogDO cusPreDepositLogDO = new CusPreDepositLogDO();
                            cusPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            cusPreDepositLogDO.setChangeMoney(orderBillingDetails.getCusPreDeposit());
                            cusPreDepositLogDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                            cusPreDepositLogDO.setChangeType(CustomerPreDepositChangeType.CANCEL_ORDER);
                            cusPreDepositLogDO.setChangeTypeDesc("超过待付款时间");
                            cusPreDepositLogDO.setCusId(orderBaseInfo.getCustomerId());
                            cusPreDepositLogDO.setOperatorId(null);
                            cusPreDepositLogDO.setOperatorType(AppIdentityType.ADMINISTRATOR);
                            cusPreDepositLogDO.setBalance(cusPreDeposit);
                            cusPreDepositLogDO.setDetailReason("超过待付款时间");
                            cusPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            cusPreDepositLogDO.setMerchantOrderNumber(null);
                            //保存日志
                            appCustomerService.addCusPreDepositLog(cusPreDepositLogDO);

                            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                            returnOrderBillingDetail.setCreateTime(new Date());
                            returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderBillingDetail.setReturnNo(returnNumber);
                            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.CUS_PREPAY);
                            returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getCusPreDeposit());
                            returnOrderBillingDetail.setIntoAmountTime(new Date());
                            returnOrderBillingDetail.setReplyCode(null);
                            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                if (null == timingTaskErrorMessageDO) {
                                    timingTaskErrorMessageDO = new TimingTaskErrorMessageDO();
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还顾客预存款失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.saveTimingTaskErrorMessage(timingTaskErrorMessageDO);
                                } else {
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还顾客预存款失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.updateTimingTaskErrorMessageByOrderNo(timingTaskErrorMessageDO);
                                }
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
                            stPreDepositLogDO.setRemarks("超过待付款时间");
                            stPreDepositLogDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                            stPreDepositLogDO.setChangeType(StorePreDepositChangeType.CANCEL_ORDER);
                            stPreDepositLogDO.setChangeTypeDesc(StorePreDepositChangeType.CANCEL_ORDER.getDescription());
                            stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                            stPreDepositLogDO.setOperatorId(null);
                            stPreDepositLogDO.setOperatorType(AppIdentityType.ADMINISTRATOR);
                            stPreDepositLogDO.setBalance(stPreDeposit);
                            stPreDepositLogDO.setDetailReason("超过待付款时间");
                            stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            //保存日志
                            storePreDepositLogService.save(stPreDepositLogDO);

                            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                            returnOrderBillingDetail.setCreateTime(new Date());
                            returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderBillingDetail.setReturnNo(returnNumber);
                            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.ST_PREPAY);
                            returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getStPreDeposit());
                            returnOrderBillingDetail.setIntoAmountTime(new Date());
                            returnOrderBillingDetail.setReplyCode(null);
                            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                if (null == timingTaskErrorMessageDO) {
                                    timingTaskErrorMessageDO = new TimingTaskErrorMessageDO();
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还门店预存款失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.saveTimingTaskErrorMessage(timingTaskErrorMessageDO);
                                } else {
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还门店预存款失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.updateTimingTaskErrorMessageByOrderNo(timingTaskErrorMessageDO);
                                }
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
                        Integer affectLine = appEmployeeService.unlockGuideCreditByUserIdAndGuideCreditAndVersion(orderBaseInfo.getSalesConsultId(), orderBillingDetails.getEmpCreditMoney(), empCreditMoney.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录导购信用金变更日志
                            EmpCreditMoneyChangeLog empCreditMoneyChangeLog = new EmpCreditMoneyChangeLog();
                            empCreditMoneyChangeLog.setEmpId(orderBaseInfo.getSalesConsultId());
                            empCreditMoneyChangeLog.setCreateTime(date);
                            empCreditMoneyChangeLog.setCreditLimitAvailableChangeAmount(orderBillingDetails.getEmpCreditMoney());
                            empCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                            empCreditMoneyChangeLog.setReferenceNumber(orderBaseInfo.getOrderNumber());
                            empCreditMoneyChangeLog.setChangeType(EmpCreditMoneyChangeType.CANCEL_ORDER);
                            empCreditMoneyChangeLog.setChangeTypeDesc("超过待付款时间");
                            empCreditMoneyChangeLog.setOperatorId(null);
                            empCreditMoneyChangeLog.setOperatorType(AppIdentityType.ADMINISTRATOR);
                            //保存日志
                            appEmployeeService.addEmpCreditMoneyChangeLog(empCreditMoneyChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                if (null == timingTaskErrorMessageDO) {
                                    timingTaskErrorMessageDO = new TimingTaskErrorMessageDO();
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还导购信用金失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.saveTimingTaskErrorMessage(timingTaskErrorMessageDO);
                                } else {
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还导购信用金失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.updateTimingTaskErrorMessageByOrderNo(timingTaskErrorMessageDO);
                                }
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
            }
            if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.DECORATE_MANAGER)) {
                //返回门店预存款
                if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
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
                            stPreDepositLogDO.setRemarks("超过待付款时间");
                            stPreDepositLogDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                            stPreDepositLogDO.setChangeType(StorePreDepositChangeType.CANCEL_ORDER);
                            stPreDepositLogDO.setChangeTypeDesc(StorePreDepositChangeType.CANCEL_ORDER.getDescription());
                            stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                            stPreDepositLogDO.setOperatorId(null);
                            stPreDepositLogDO.setOperatorType(AppIdentityType.ADMINISTRATOR);
                            stPreDepositLogDO.setBalance(stPreDeposit);
                            stPreDepositLogDO.setDetailReason("超过待付款时间");
                            stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            //保存日志
                            storePreDepositLogService.save(stPreDepositLogDO);

                            ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                            returnOrderBillingDetail.setCreateTime(new Date());
                            returnOrderBillingDetail.setRoid(returnOrderBaseInfo.getRoid());
                            returnOrderBillingDetail.setReturnNo(returnNumber);
                            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.ST_PREPAY);
                            returnOrderBillingDetail.setReturnMoney(orderBillingDetails.getStPreDeposit());
                            returnOrderBillingDetail.setIntoAmountTime(new Date());
                            returnOrderBillingDetail.setReplyCode(null);
                            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                            returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                if (null == timingTaskErrorMessageDO) {
                                    timingTaskErrorMessageDO = new TimingTaskErrorMessageDO();
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还门店预存款失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.saveTimingTaskErrorMessage(timingTaskErrorMessageDO);
                                } else {
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还门店预存款失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.updateTimingTaskErrorMessageByOrderNo(timingTaskErrorMessageDO);
                                }
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
                        Integer affectLine = appStoreService.updateStoreCreditByUserIdAndVersion(orderBaseInfo.getCreatorId(), orderBillingDetails.getStoreCreditMoney(), storeCreditMoney.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店信用金变更日志
                            StoreCreditMoneyChangeLog storeCreditMoneyChangeLog = new StoreCreditMoneyChangeLog();
                            storeCreditMoneyChangeLog.setStoreId(storeCreditMoney.getStoreId());
                            storeCreditMoneyChangeLog.setCreateTime(date);
                            storeCreditMoneyChangeLog.setChangeAmount(orderBillingDetails.getStoreCreditMoney());
                            storeCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(creditMoney);
                            storeCreditMoneyChangeLog.setReferenceNumber(orderBaseInfo.getOrderNumber());
                            storeCreditMoneyChangeLog.setChangeType(StoreCreditMoneyChangeType.CANCEL_ORDER);
                            storeCreditMoneyChangeLog.setChangeTypeDesc("超过待付款时间");
                            storeCreditMoneyChangeLog.setOperatorId(null);
                            storeCreditMoneyChangeLog.setOperatorType(AppIdentityType.ADMINISTRATOR);
                            storeCreditMoneyChangeLog.setRemark("超过待付款时间");
                            //保存日志
                            appStoreService.addStoreCreditMoneyChangeLog(storeCreditMoneyChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                if (null == timingTaskErrorMessageDO) {
                                    timingTaskErrorMessageDO = new TimingTaskErrorMessageDO();
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还门店信用金失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.saveTimingTaskErrorMessage(timingTaskErrorMessageDO);
                                } else {
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还门店信用金失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.updateTimingTaskErrorMessageByOrderNo(timingTaskErrorMessageDO);
                                }
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
                //返回门店现金返利（装饰公司）
                if (orderBillingDetails.getStoreSubvention() != null && orderBillingDetails.getStoreSubvention() > 0) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取门店现金返利
                        StoreSubvention storeSubvention = appStoreService.findStoreSubventionByEmpId(orderBaseInfo.getCreatorId());
                        //返还后门店现金返利余额
                        Double subvention = (storeSubvention.getBalance() + orderBillingDetails.getStoreSubvention());
                        //修改门店现金返利
                        Integer affectLine = appStoreService.updateStoreSubventionByUserIdAndVersion(orderBillingDetails.getStoreSubvention(), orderBaseInfo.getCreatorId(), storeSubvention.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录门店现金返利变更日志
                            StoreSubventionChangeLog storeSubventionChangeLog = new StoreSubventionChangeLog();
                            storeSubventionChangeLog.setStoreId(storeSubvention.getStoreId());
                            storeSubventionChangeLog.setCreateTime(date);
                            storeSubventionChangeLog.setChangeAmount(orderBillingDetails.getStoreSubvention());
                            storeSubventionChangeLog.setBalance(subvention);
                            storeSubventionChangeLog.setReferenceNumber(orderBaseInfo.getOrderNumber());
                            storeSubventionChangeLog.setChangeType(StoreSubventionChangeType.CANCEL_ORDER);
                            storeSubventionChangeLog.setChangeTypeDesc("超过待付款时间");
                            storeSubventionChangeLog.setOperatorId(null);
                            storeSubventionChangeLog.setOperatorType(AppIdentityType.ADMINISTRATOR);
                            storeSubventionChangeLog.setRemark("超过待付款时间");
                            //保存日志
                            appStoreService.addStoreSubventionChangeLog(storeSubventionChangeLog);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                if (null == timingTaskErrorMessageDO) {
                                    timingTaskErrorMessageDO = new TimingTaskErrorMessageDO();
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还门店现金返利失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.saveTimingTaskErrorMessage(timingTaskErrorMessageDO);
                                } else {
                                    timingTaskErrorMessageDO.setMessage("系统繁忙，退还门店现金返利失败，请稍后再试!");
                                    timingTaskErrorMessageDO.setRecordTime(date);
                                    timingTaskErrorMessageDAO.updateTimingTaskErrorMessageByOrderNo(timingTaskErrorMessageDO);
                                }
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
                    changeLog.setCusId(orderBaseInfo.getCustomerId());
                    changeLog.setCouponId(orderProductCoupon.getCouponId());
                    changeLog.setChangeType(CustomerProductCouponChangeType.CANCEL_ORDER);
                    changeLog.setChangeTypeDesc(CustomerProductCouponChangeType.CANCEL_ORDER.getDescription());
                    changeLog.setReferenceNumber(orderBaseInfo.getOrderNumber());
                    changeLog.setOperatorId(null);
                    changeLog.setOperatorIp(null);
                    changeLog.setOperatorType(AppIdentityType.ADMINISTRATOR);
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

                    //修改买券订单可退数量
                    if (StringUtils.isNotBlank(customerProductCoupon.getGetOrderNumber()) && (customerProductCoupon.getGetType() == CouponGetType.BUY || customerProductCoupon.getGetType() == CouponGetType.PRESENT)){
                        returnOrderDAO.updateProductOrderReturnNableQty(customerProductCoupon.getGoodsLineId());
                    }
                }
            }
            //获取订单使用现金券
            List<OrderCouponInfo> orderCashCouponList = productCouponService.findOrderCouponByCouponTypeAndOrderId(orderBaseInfo.getCreatorId(), OrderCouponType.CASH_COUPON);
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

                    customerCashCouponChangeLog.setCusId(orderBaseInfo.getCustomerId());
                    customerCashCouponChangeLog.setUseTime(date);
                    customerCashCouponChangeLog.setCouponId(orderCashCoupon.getCouponId());
                    customerCashCouponChangeLog.setReferenceNumber(orderBaseInfo.getOrderNumber());
                    customerCashCouponChangeLog.setChangeType(CustomerCashCouponChangeType.CANCEL_ORDER);
                    customerCashCouponChangeLog.setChangeTypeDesc("超过待付款时间");
                    customerCashCouponChangeLog.setOperatorId(null);
                    customerCashCouponChangeLog.setOperatorType(AppIdentityType.ADMINISTRATOR);
                    customerCashCouponChangeLog.setRemark("超过待付款时间");
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
            AppStore appStore = appStoreService.findStoreByUserIdAndIdentityType(orderBaseInfo.getCreatorId(), orderBaseInfo.getCreatorIdentityType().getValue());

            if (AssertUtil.isNotEmpty(appStore) && appStore.getStoreType().equals(StoreType.FX) || appStore.getStoreType().equals(StoreType.JM)) {
                commonService.deductionOrderJxPriceDifferenceRefund(returnOrderBaseInfo, orderBaseInfo, returnOrderGoodsInfos);
            }

            if (!orderBaseInfo.getStatus().equals(AppOrderStatus.UNPAID)) {
                //*******************************记录订单生命周期**************************
                OrderLifecycle orderLifecycle = new OrderLifecycle();
                orderLifecycle.setOid(orderBaseInfo.getId());
                orderLifecycle.setOrderNumber(orderBaseInfo.getOrderNumber());
                orderLifecycle.setOperation(OrderLifecycleType.CANCEL_ORDER);
                orderLifecycle.setPostStatus(AppOrderStatus.CANCELED);
                orderLifecycle.setOperationTime(new Date());
                returnOrderDAO.saveOrderLifecycle(orderLifecycle);
                //********************************保存退单生命周期信息***********************
                ReturnOrderLifecycle returnOrderLifecycle = new ReturnOrderLifecycle();
                returnOrderLifecycle.setRoid(returnOrderBaseInfo.getRoid());
                returnOrderLifecycle.setReturnNo(returnOrderBaseInfo.getReturnNo());
                returnOrderLifecycle.setOperation(OrderLifecycleType.CANCEL_ORDER);
                returnOrderLifecycle.setPostStatus(AppReturnOrderStatus.FINISHED);
                returnOrderLifecycle.setOperationTime(new Date());
                returnOrderDAO.saveReturnOrderLifecycle(returnOrderLifecycle);
            }

            //修改订单状态为已取消
            appOrderService.updateOrderStatusAndDeliveryStatusByOrderNo(AppOrderStatus.CANCELED, null, orderBaseInfo.getOrderNumber());
            return returnNumber;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("异常错误，待付款超时订单处理失败，订单号：" + orderBaseInfo.getOrderNumber());
            if (null == timingTaskErrorMessageDO) {
                timingTaskErrorMessageDO = new TimingTaskErrorMessageDO();
                timingTaskErrorMessageDO.setMessage(e.getMessage());
                timingTaskErrorMessageDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                timingTaskErrorMessageDO.setRecordTime(date);
                timingTaskErrorMessageDAO.saveTimingTaskErrorMessage(timingTaskErrorMessageDO);
            } else {
                timingTaskErrorMessageDO.setMessage("系统繁忙，退还门店现金返利失败，请稍后再试!");
                timingTaskErrorMessageDO.setRecordTime(date);
                timingTaskErrorMessageDAO.updateTimingTaskErrorMessageByOrderNo(timingTaskErrorMessageDO);
            }
            throw new RuntimeException();
        }

    }


    @Override
    public List<MaPaymentData> findPaymentDataByOrderNo(String orderNumber) {
        return maOrderDAO.findPaymentDataByOrderNo(orderNumber);
    }

    @Override
    public OrderLogisticsInfo createMaOrderLogisticsInfo(AppStore store, String orderNumber) {
        OrderLogisticsInfo orderLogisticsInfo = new OrderLogisticsInfo();
        orderLogisticsInfo.setOrdNo(orderNumber);
        orderLogisticsInfo.setDeliveryType(AppDeliveryType.PRODUCT_COUPON);
        orderLogisticsInfo.setBookingStoreCode(store.getStoreCode());
        orderLogisticsInfo.setBookingStoreName(store.getStoreName());
        orderLogisticsInfo.setBookingStoreAddress(store.getDetailedAddress());
        return orderLogisticsInfo;
    }

    public RechargeReceiptInfo createRechargeReceiptInfo(Double money, String orderNumber, OrderBillingPaymentType orderBillingPaymentType, String receiptNumber) {
        Date date = new Date();
        RechargeReceiptInfo createRechargeReceiptInfo = new RechargeReceiptInfo();
        createRechargeReceiptInfo.setRechargeNo(orderNumber);
        createRechargeReceiptInfo.setCreateTime(date);
        createRechargeReceiptInfo.setPayTime(date);
        createRechargeReceiptInfo.setPayType(orderBillingPaymentType);
        createRechargeReceiptInfo.setPayTypeDesc(orderBillingPaymentType.getDescription());
        createRechargeReceiptInfo.setPaymentSubjectType(PaymentSubjectType.STORE);
        createRechargeReceiptInfo.setPaymentSubjectTypeDesc(PaymentSubjectType.STORE.getDescription());
        createRechargeReceiptInfo.setRechargeAccountType(RechargeAccountType.PRODUCT_COUPON);
        createRechargeReceiptInfo.setPaymentSubjectTypeDesc(RechargeAccountType.PRODUCT_COUPON.getDescription());
        createRechargeReceiptInfo.setChargeType(null);
        createRechargeReceiptInfo.setChargeTypeDesc(null);
        createRechargeReceiptInfo.setAmount(money);
        createRechargeReceiptInfo.setReplyCode(null);
        createRechargeReceiptInfo.setReceiptNumber(receiptNumber);

        return createRechargeReceiptInfo;
    }

    public RechargeOrder createRechargeOrder(Double money, String orderNumber, Long storeId, OrderBillingPaymentType orderBillingPaymentType, Long creatorId, Long customerId) {
        Date date = new Date();
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setRechargeNo(orderNumber);
        rechargeOrder.setStatus(AppRechargeOrderStatus.PAID);
        rechargeOrder.setCreateTime(date);
        rechargeOrder.setCreatorIdentityType(AppIdentityType.ADMINISTRATOR);
        rechargeOrder.setCreatorId(creatorId);
        rechargeOrder.setRechargeAccountType(RechargeAccountType.PRODUCT_COUPON);
        rechargeOrder.setRechargeAccountTypeDesc(RechargeAccountType.PRODUCT_COUPON.getDescription());
        rechargeOrder.setCustomerId(customerId);
        rechargeOrder.setStoreId(storeId);
        rechargeOrder.setAmount(money);
        rechargeOrder.setPayType(orderBillingPaymentType);
        rechargeOrder.setPayTypeDesc(orderBillingPaymentType.getDescription());
        rechargeOrder.setPaymentSubjectType(PaymentSubjectType.STORE);
        rechargeOrder.setPaymentSubjectTypeDesc(PaymentSubjectType.STORE.getDescription());
        rechargeOrder.setPayUpTime(date);
        return rechargeOrder;
    }


    @Override
    public PageInfo<FitOrderVO> findFitOrderVOPageInfo(Integer page, Integer size, List<Long> storeIds) {
        if (null != page && null != size && AssertUtil.isNotEmpty(storeIds)) {
            PageHelper.startPage(page, size);
            List<FitOrderVO> maOrderVOList = maOrderDAO.findFitOrderVOPageInfo(storeIds);
            return new PageInfo<>(maOrderVOList);
        }
        return null;
    }


    @Override
    public PageInfo<FitOrderVO> findFitOrderListByScreen(Integer page, Integer size, Long cityId, Long storeId, List<Long> storeIds) {
        if (null != page && null != size && AssertUtil.isNotEmpty(storeIds)) {
            PageHelper.startPage(page, size);
            List<FitOrderVO> maOrderVOList = maOrderDAO.findFitOrderListByScreen(cityId, storeId, storeIds);
            return new PageInfo<>(maOrderVOList);
        }
        return null;
    }

    @Override
    public PageInfo<FitOrderVO> findFitOrderListByInfo(Integer page, Integer size, String info, List<Long> storeIds) {
        if (null != page && null != size && AssertUtil.isNotEmpty(storeIds)) {
            PageHelper.startPage(page, size);
            List<FitOrderVO> maOrderVOList = maOrderDAO.findFitOrderListByInfo(info, storeIds);
            return new PageInfo<>(maOrderVOList);
        }
        return null;
    }

    @Override
    public List<FitOrderVO> findFitOrderByCondition(MaCompanyOrderVORequest maCompanyOrderVORequest) {
        List<FitOrderVO> maOrderVOList = maOrderDAO.findFitOrderByCondition(maCompanyOrderVORequest);
        return maOrderVOList;
    }


    @Override
    public DetailFitOrderVO findFitOrderByOrderNumber(String ordNumber) {
        DetailFitOrderVO detailFitOrderVO = maOrderDAO.findFitOrderByOrderNumber(ordNumber);
        return detailFitOrderVO;
    }

    @Override
    public CreateOrderGoodsSupport createMaOrderGoodsInfo(List<MaGoodsSimpleInfo> goodsList, Long userId, Integer identityType, Long customerId,
                                                        List<ProductCouponSimpleInfo> productCouponList, String orderNumber) throws UnsupportedEncodingException {
        List<OrderGoodsInfo> orderGoodsInfoList = new ArrayList<>();
        //新建一个map,用来存放最终要检核库存的商品和商品数量
        Map<Long, Integer> inventoryCheckMap = new HashMap<>();
        //定义订单商品零售总价
        Double goodsTotalPrice = 0D;
        //定义订单会员折扣
        Double memberDiscount = 0D;
        //定义订单促销折扣
        Double promotionDiscount = 0D;
        //处理订单本品商品信息
        //获取当单顾客信息
        AppCustomer customer = new AppCustomer();
        if (identityType == AppIdentityType.CUSTOMER.getValue()) {
            customer = customerService.findById(userId);
        } else if (identityType == AppIdentityType.SELLER.getValue()) {
            customer = customerService.findById(customerId);
        }
        if (null != goodsList && goodsList.size() > 0) {
            //获取本品id集合
//            Set<Long> goodsIdSet = new HashSet<>();
            Set<String> goodsSkuSet = new HashSet<>();
            for (MaGoodsSimpleInfo goods : goodsList) {
                goodsSkuSet.add(goods.getSku());
            }
            //根据本品id集合查询商品信息
            List<OrderGoodsVO> goodsVOList = this.findMaOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsSkus(
                    userId, identityType, goodsSkuSet);
            //定义当前有唯一价格的本品id集合
//            Set<Long> hasPriceGoodsIdSet = new HashSet<>();
            Set<String> hasPriceGoodsSkuSet = new HashSet<>();
            //循环处理查询到的商品信息
            for (OrderGoodsVO goodsVO : goodsVOList) {
                if (hasPriceGoodsSkuSet.contains(goodsVO.getSku())) {
                    throw new GoodsMultipartPriceException("商品 '" + goodsVO.getSkuName() + "'在当前门店下存在多个价格!");
                } else {
                    hasPriceGoodsSkuSet.add(goodsVO.getSku());
                }
                for (MaGoodsSimpleInfo info : goodsList) {
                    if (info.getSku().equals(goodsVO.getSku())) {
                        if (null == info.getQty() || info.getQty().equals(0)) {
                            throw new GoodsQtyErrorException("商品 '" + goodsVO.getSkuName() + "'数量出现异常(0或不存在)!");
                        }
                        goodsVO.setQty(info.getQty());
                    }
                }
                //加总商品零售价总额
                goodsTotalPrice += goodsVO.getRetailPrice() * goodsVO.getQty();
                //将本品数量加入库存检核map
                if (inventoryCheckMap.containsKey(goodsVO.getGid())) {
                    inventoryCheckMap.put(goodsVO.getGid(), inventoryCheckMap.get(goodsVO.getGid()) + goodsVO.getQty());
                } else {
                    inventoryCheckMap.put(goodsVO.getGid(), goodsVO.getQty());
                }
                //设置本品会员折扣
                if (identityType == AppIdentityType.DECORATE_MANAGER.getValue()) {
                    memberDiscount += (goodsVO.getRetailPrice() - goodsVO.getVipPrice()) * goodsVO.getQty();
                } else {
                    if (null == customer) {
                        throw new OrderCustomerException("订单顾客信息异常!");
                    }
                    if (customer.getCustomerType() == AppCustomerType.MEMBER) {
                        memberDiscount += (goodsVO.getRetailPrice() - goodsVO.getVipPrice()) * goodsVO.getQty();
                    } else {
                        memberDiscount += 0D;
                    }
                }
                OrderGoodsInfo goodsInfo = new OrderGoodsInfo();
                goodsInfo.setOrderNumber(orderNumber);
                goodsInfo.setGoodsLineType(AppGoodsLineType.GOODS);
                goodsInfo.setRetailPrice(goodsVO.getRetailPrice());
                goodsInfo.setVIPPrice(goodsVO.getVipPrice());
                goodsInfo.setWholesalePrice(goodsVO.getWholesalePrice());
                goodsInfo.setIsPriceShare(Boolean.FALSE);
                goodsInfo.setPromotionSharePrice(0D);
                goodsInfo.setLbSharePrice(0D);
                goodsInfo.setCashCouponSharePrice(0D);
                goodsInfo.setCashReturnSharePrice(0D);
                goodsInfo.setIsReturnable(Boolean.TRUE);
                if (identityType == AppIdentityType.DECORATE_MANAGER.getValue()) {
                    goodsInfo.setSettlementPrice(goodsVO.getVipPrice());
                    goodsInfo.setReturnPrice(goodsVO.getVipPrice());
                } else {
                    if (null == customer) {
                        throw new OrderCustomerException("订单顾客信息异常!");
                    }
                    if (customer.getCustomerType() == AppCustomerType.MEMBER) {
                        goodsInfo.setSettlementPrice(goodsVO.getVipPrice());
                        goodsInfo.setReturnPrice(goodsVO.getVipPrice());
                    } else {
                        goodsInfo.setSettlementPrice(goodsVO.getRetailPrice());
                        goodsInfo.setReturnPrice(goodsVO.getRetailPrice());
                    }
                }
                goodsInfo.setGid(goodsVO.getGid());
                goodsInfo.setSku(goodsVO.getSku());
                goodsInfo.setSkuName(goodsVO.getSkuName());
                goodsInfo.setOrderQuantity(goodsVO.getQty());
                goodsInfo.setShippingQuantity(0);
                goodsInfo.setReturnableQuantity(goodsVO.getQty());
                goodsInfo.setReturnQuantity(0);
                goodsInfo.setReturnPriority(1);
                goodsInfo.setPriceItemId(goodsVO.getPriceItemId());
                goodsInfo.setCompanyFlag(goodsVO.getCompanyFlag());
                goodsInfo.setCoverImageUri(goodsVO.getCoverImageUri());
                orderGoodsInfoList.add(goodsInfo);
            }

            if (goodsSkuSet.size() != hasPriceGoodsSkuSet.size()) {
                StringBuilder skus = new StringBuilder();
                for (String sku : goodsSkuSet) {
                    if (!hasPriceGoodsSkuSet.contains(sku)) {
                        skus.append(sku).append(",");
                    }
                }
                throw new GoodsNoPriceException("sku为 '" + skus + "'的商品在当前门店下没有找到价格!");
            }
        }
        //将本品信息零存入本品商品列表
        List<OrderGoodsInfo> pureOrderGoodsInfo = new ArrayList<>();
        pureOrderGoodsInfo.addAll(orderGoodsInfoList);

        //处理订单产品券商品信息
        List<OrderGoodsInfo> productCouponGoodsList = new ArrayList<>();
        if (null != productCouponList && productCouponList.size() > 0) {
            //获取本品id集合
            Set<Long> couponGoodsIdSet = new HashSet<>();
            for (ProductCouponSimpleInfo productCouponGoods : productCouponList) {
                couponGoodsIdSet.add(productCouponGoods.getId());
            }
            //根据本品id集合查询商品信息
            List<OrderGoodsVO> couponGoodsList = goodsService.findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(
                    userId, identityType, couponGoodsIdSet);
            //定义当前有唯一价格的本品id集合
            Set<Long> hasPriceCouponGoodsIdSet = new HashSet<>();

            for (OrderGoodsVO couponGoods : couponGoodsList) {
                if (hasPriceCouponGoodsIdSet.contains(couponGoods.getGid())) {
                    throw new GoodsMultipartPriceException("产品券商品 '" + couponGoods.getSkuName() + "'在当前门店下存在多个价格!");
                } else {
                    hasPriceCouponGoodsIdSet.add(couponGoods.getGid());
                }
                for (ProductCouponSimpleInfo info : productCouponList) {
                    if (null == info.getQty() || info.getQty().equals(0)) {
                        throw new GoodsQtyErrorException("产品券商品 '" + couponGoods.getSkuName() + "'数量出现异常(0或不存在)!");
                    }
                    if (info.getId().equals(couponGoods.getGid())) {
                        couponGoods.setQty(info.getQty());
                    }
                }
                //加总商品零售价总额
                goodsTotalPrice += couponGoods.getRetailPrice() * couponGoods.getQty();
                //将产品券数量加入库存检核map
                if (inventoryCheckMap.containsKey(couponGoods.getGid())) {
                    inventoryCheckMap.put(couponGoods.getGid(), inventoryCheckMap.get(couponGoods.getGid()) + couponGoods.getQty());
                } else {
                    inventoryCheckMap.put(couponGoods.getGid(), couponGoods.getQty());
                }
                //设置产品券商品会员折扣
                if (null == customer) {
                    throw new OrderCustomerException("订单顾客信息异常!");
                }
                if (customer.getCustomerType() == AppCustomerType.MEMBER) {
                    memberDiscount += (couponGoods.getRetailPrice() - couponGoods.getVipPrice()) * couponGoods.getQty();
                } else {
                    memberDiscount += 0D;
                }
                OrderGoodsInfo couponGoodsInfo = new OrderGoodsInfo();
                couponGoodsInfo.setOrderNumber(orderNumber);
                couponGoodsInfo.setGoodsLineType(AppGoodsLineType.PRODUCT_COUPON);
                couponGoodsInfo.setRetailPrice(couponGoods.getRetailPrice());
                couponGoodsInfo.setVIPPrice(couponGoods.getVipPrice());
                couponGoodsInfo.setWholesalePrice(couponGoods.getWholesalePrice());
                couponGoodsInfo.setIsPriceShare(Boolean.FALSE);
                couponGoodsInfo.setPromotionSharePrice(0D);
                couponGoodsInfo.setLbSharePrice(0D);
                couponGoodsInfo.setCashReturnSharePrice(0D);
                couponGoodsInfo.setCashCouponSharePrice(0D);
                couponGoodsInfo.setReturnPrice(0D);
                couponGoodsInfo.setIsReturnable(Boolean.TRUE);
                if (customer.getCustomerType() == AppCustomerType.MEMBER) {
                    couponGoodsInfo.setSettlementPrice(couponGoods.getVipPrice());
                } else {
                    couponGoodsInfo.setSettlementPrice(couponGoods.getRetailPrice());
                }
                couponGoodsInfo.setGid(couponGoods.getGid());
                couponGoodsInfo.setSku(couponGoods.getSku());
                couponGoodsInfo.setSkuName(couponGoods.getSkuName());
                couponGoodsInfo.setOrderQuantity(couponGoods.getQty());
                couponGoodsInfo.setShippingQuantity(0);
                couponGoodsInfo.setReturnableQuantity(couponGoods.getQty());
                couponGoodsInfo.setReturnQuantity(0);
                couponGoodsInfo.setReturnPriority(1);
                couponGoodsInfo.setPriceItemId(couponGoods.getPriceItemId());
                couponGoodsInfo.setCompanyFlag(couponGoods.getCompanyFlag());
                couponGoodsInfo.setCoverImageUri(couponGoods.getCoverImageUri());
                productCouponGoodsList.add(couponGoodsInfo);
            }
            if (couponGoodsIdSet.size() != hasPriceCouponGoodsIdSet.size()) {
                //StringBuilder ids = new StringBuilder();
                List<Long> noPriceGoodsIdList = new ArrayList<>();
                for (Long id : couponGoodsIdSet) {
                    if (!hasPriceCouponGoodsIdSet.contains(id)) {
                        noPriceGoodsIdList.add(id);
                    }
                }
                List<String> noPriceGoodsSkuNameList = goodsService.getGoodsSkuNameListByGoodsIdList(noPriceGoodsIdList);
               /* for (Long id : noPriceGoodsIdList) {
                    noPriceGoodsSkuNameList.add(productCouponList.stream().filter(p -> p.getId().equals(id)).collect(Collectors.toList()).get(0).getSkuName());
                }*/

                throw new GoodsNoPriceException("产品券商品:" + noPriceGoodsSkuNameList.toString() + "在当前门店下没有找到价格!");
            }
        }
        if (productCouponGoodsList.size() > 0) {
            orderGoodsInfoList.addAll(productCouponGoodsList);
        }
        CreateOrderGoodsSupport support = new CreateOrderGoodsSupport();
        support.setGoodsTotalPrice(goodsTotalPrice);
        support.setInventoryCheckMap(inventoryCheckMap);
        support.setMemberDiscount(CountUtil.HALF_UP_SCALE_2(memberDiscount));
        support.setOrderGoodsInfoList(orderGoodsInfoList);
        support.setProductCouponGoodsList(productCouponGoodsList);
        support.setPromotionDiscount(promotionDiscount);
        support.setPureOrderGoodsInfo(pureOrderGoodsInfo);
        return support;
    }

    @Override
    public List<OrderGoodsVO> findMaOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsSkus(Long userId, Integer identityType, Set<String> goodsSkuSet) {
        if (null != userId && null != identityType && null != goodsSkuSet && goodsSkuSet.size() > 0) {
                return maOrderDAO.findMaOrderGoodsVOListByEmpIdAndGoodsSkus(userId, goodsSkuSet);
        }
        return null;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearOrderGoodsInMaterialList(Long userId, Integer identityType, List<MaGoodsSimpleInfo> goodsList,
                                              List<ProductCouponSimpleInfo> productCouponList) {
        if (null != userId && null != identityType) {
            if (null != goodsList && goodsList.size() > 0) {
//                Set<Long> goodsIds = new HashSet<>();
                Set<String> goodsSkus = new HashSet<>();
                for (MaGoodsSimpleInfo goods : goodsList) {
                    goodsSkus.add(goods.getSku());
                }
                this.deleteMaterialListByUserIdAndIdentityTypeAndGoodsSkus(
                        userId, AppIdentityType.getAppIdentityTypeByValue(identityType), goodsSkus);
            }
            if (null != productCouponList) {
                Set<Long> couponGoodsIds = new HashSet<>();
                for (ProductCouponSimpleInfo couponGoods : productCouponList) {
                    couponGoodsIds.add(couponGoods.getId());
                }
                this.deleteMaterialListProductCouponGoodsByUserIdAndIdentityTypeAndGoodsIds(
                        userId, AppIdentityType.getAppIdentityTypeByValue(identityType), couponGoodsIds);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMaterialListByUserIdAndIdentityTypeAndGoodsSkus(Long userId, AppIdentityType identityType, Set<String> goodsSkus) {
        if (null != userId && null != identityType && null != goodsSkus && goodsSkus.size() > 0) {
            materialListDAO.deleteMaMaterialListByUserIdAndIdentityTypeAndGoodsSkus(userId, identityType, goodsSkus);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMaterialListProductCouponGoodsByUserIdAndIdentityTypeAndGoodsIds(Long userId, AppIdentityType identityType,
                                                                                       Set<Long> couponGoodsIds) {
        if (null != userId && null != identityType && null != couponGoodsIds && couponGoodsIds.size() > 0) {
            materialListDAO.deleteMaterialListProductCouponGoodsByUserIdAndIdentityTypeAndGoodsIds(userId, identityType, couponGoodsIds);
        }

    }
}
