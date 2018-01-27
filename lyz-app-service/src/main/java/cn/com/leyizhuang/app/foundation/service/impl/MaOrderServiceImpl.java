package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.core.remote.ebs.EbsSenderService;
import cn.com.leyizhuang.app.core.utils.DateUtil;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.MaGoodsDAO;
import cn.com.leyizhuang.app.foundation.dao.MaOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.CityInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventoryAvailableQtyChangeLog;
import cn.com.leyizhuang.app.foundation.pojo.management.city.MaCityInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.city.MaCityInventoryChange;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsShippingInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderAmount;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderBillingPaymentDetails;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderTempInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventoryChange;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderReceiveInf;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCompanyOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.BillingSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.MaOrderVO;
import cn.com.leyizhuang.app.foundation.vo.management.order.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by caiyu on 2017/12/16.
 */
@Service
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
    private AppCustomerService customerService;
    @Resource
    private AppOrderService orderService;
    @Resource
    private AppStoreService storeService;
    @Resource
    private ProductCouponService productCouponService;


    @Override
    public List<MaOrderVO> findMaOrderVOAll() {
        return maOrderDAO.findMaOrderVOAll();
    }

    @Override
    public List<MaOrderVO> findMaOrderVOByCityId(Long cityId) {
        return maOrderDAO.findMaOrderVOByCityId(cityId);
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
    public List<MaOrderVO> findMaOrderVOByCondition(MaOrderVORequest maOrderVORequest) {
        return maOrderDAO.findMaOrderVOByCondition(maOrderVORequest);
    }

    @Override
    public List<MaOrderVO> findCompanyOrderAll() {
        return maOrderDAO.findCompanyOrderAll();
    }

    @Override
    public List<MaOrderVO> findCompanyOrderByOrderNumber(String orderNumber) {
        return maOrderDAO.findCompanyOrderByOrderNumber(orderNumber);
    }

    @Override
    public List<MaOrderVO> findCompanyOrderByCondition(MaCompanyOrderVORequest maCompanyOrderVORequest) {
        return maOrderDAO.findCompanyOrderByCondition(maCompanyOrderVORequest);
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
    public MaOrderDeliveryInfoResponse getDeliveryInfoByOrderNumber(String orderNmber) {
        return maOrderDAO.getDeliveryInfoByOrderNumber(orderNmber);
    }

    @Override
    public PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderList(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findSelfTakeOrderList();
        return new PageInfo<>(maSelfTakeOrderVOList);
    }

    @Override
    public PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderListByScreen(Integer page, Integer size, Long cityId, Long storeId, Integer status, Integer isPayUp) {
        PageHelper.startPage(page, size);
        if (storeId != -1) {
            cityId = null;
        }
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findSelfTakeOrderListByScreen(cityId, storeId, status, isPayUp);
        return new PageInfo<>(maSelfTakeOrderVOList);
    }


    @Override
    public PageInfo<MaSelfTakeOrderVO> findSelfTakeOrderListByInfo(Integer page, Integer size, String info) {
        PageHelper.startPage(page, size);
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findSelfTakeOrderListByInfo(info);
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
    public void updateOrderStatus(String orderNo) {
        this.maOrderDAO.updateOrderStatus(orderNo);
    }

    @Override
    public void updateorderReceivablesStatus(String orderNo) {
        this.maOrderDAO.updateorderReceivablesStatus(orderNo);
    }


    @Override
    public List<MaOrderGoodsInfo> findOrderGoodsList(String orderNo) {
        return this.maOrderDAO.findOrderGoodsList(orderNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderShipping(String orderNumber, ShiroUser shiroUser, MaOrderTempInfo maOrderTempInfo) {
        Date date = new Date();
        if (null == maOrderTempInfo || null == maOrderTempInfo.getCityId()) {
            throw new RuntimeException("该订单城市ID为空,无法更新城市库存");
        }
        if (null == maOrderTempInfo || null == maOrderTempInfo.getStoreId()) {
            throw new RuntimeException("该订单门店ID为空,无法更新门店库存");
        }
        //更新订单状态
        this.updateOrderStatus(orderNumber);
        //查询该订单下的所有商品
        List<MaOrderGoodsInfo> MaOrderGoodsInfoList = this.findOrderGoodsList(orderNumber);
        //生成出货记录
        for (MaOrderGoodsInfo maOrderGoodsInfo : MaOrderGoodsInfoList) {
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
        }

        //更新城市和门店库存及生成日志信息
        for (MaOrderGoodsInfo maOrderGoodsInfo : MaOrderGoodsInfoList) {
            //查看门店下 该商品的库存
            MaStoreInventory storeInventory = maStoreInventoryService.findStoreInventoryByStoreCodeAndGoodsId(maOrderTempInfo.getStoreId(), maOrderGoodsInfo.getGid());
            if (null == storeInventory || null == storeInventory.getAvailableIty()) {
                throw new RuntimeException("该门店下没有该商品,商品id:" + maOrderGoodsInfo.getGid());
            }
            if (storeInventory.getAvailableIty() < maOrderGoodsInfo.getOrderQty()) {
                throw new RuntimeException("门店下该商品库存不足,无法发货,商品id:" + maOrderGoodsInfo.getGid());
            }
            //查看城市下 该商品的库存
            MaCityInventory maCityInventory = maCityInventoryService.findCityInventoryByCityIdAndGoodsId(maOrderTempInfo.getCityId(), maOrderGoodsInfo.getGid());
            if (null == maCityInventory || null == maCityInventory.getAvailableIty()) {
                throw new RuntimeException("该城市下没有该商品,商品id:" + maOrderGoodsInfo.getGid());
            }
            if (maCityInventory.getAvailableIty() < maOrderGoodsInfo.getOrderQty()) {
                throw new RuntimeException("城市下该商品库存不足,无法发货,商品id:" + maOrderGoodsInfo.getGid());
            }

            //更新门店库存数量
            Integer goodsQtyAfterChange = storeInventory.getAvailableIty() - maOrderGoodsInfo.getOrderQty();
            maStoreInventoryService.updateStoreInventory(maOrderTempInfo.getStoreId(), maOrderGoodsInfo.getGid(), goodsQtyAfterChange, date);

            //更新城市库存数量
            Integer cityQtyAfterChange = maCityInventory.getAvailableIty() - maOrderGoodsInfo.getOrderQty();
            maCityInventoryService.updateCityInventory(maOrderTempInfo.getCityId(), maOrderGoodsInfo.getGid(), cityQtyAfterChange, date);

            //增加门店库存变更日志
            MaStoreInventoryChange storeInventoryChange = new MaStoreInventoryChange();
            storeInventoryChange.setCityId(maOrderTempInfo.getCityId());
            storeInventoryChange.setCityName(maOrderTempInfo.getCityName());
            storeInventoryChange.setStoreId(maOrderTempInfo.getStoreId());
            storeInventoryChange.setStoreCode(maOrderTempInfo.getStoreCode());
            storeInventoryChange.setReferenceNumber(maOrderTempInfo.getOrderNumber());
            storeInventoryChange.setGid(maOrderGoodsInfo.getGid());
            storeInventoryChange.setSku(maOrderGoodsInfo.getSku());
            storeInventoryChange.setSkuName(maOrderGoodsInfo.getSkuName());
            storeInventoryChange.setChangeTime(date);
            storeInventoryChange.setAfterChangeQty(goodsQtyAfterChange);
            storeInventoryChange.setChangeQty(maOrderGoodsInfo.getOrderQty());
            storeInventoryChange.setChangeType(StoreInventoryAvailableQtyChangeType.SELF_TAKE_ORDER);
            storeInventoryChange.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.SELF_TAKE_ORDER.getDescription());
            maStoreInventoryService.addInventoryChangeLog(storeInventoryChange);

            //增加库存库存变更日志
            MaCityInventoryChange cityInventoryChange = new MaCityInventoryChange();
            cityInventoryChange.setCityId(maOrderTempInfo.getCityId());
            cityInventoryChange.setCityName(maOrderTempInfo.getCityName());
            cityInventoryChange.setReferenceNumber(maOrderTempInfo.getOrderNumber());
            cityInventoryChange.setGid(maOrderGoodsInfo.getGid());
            cityInventoryChange.setSku(maOrderGoodsInfo.getSku());
            cityInventoryChange.setSkuName(maOrderGoodsInfo.getSkuName());
            cityInventoryChange.setChangeTime(date);
            cityInventoryChange.setAfterChangeQty(cityQtyAfterChange);
            cityInventoryChange.setChangeQty(maOrderGoodsInfo.getOrderQty());
            cityInventoryChange.setChangeType(CityInventoryAvailableQtyChangeType.SELF_TAKE_ORDER);
            cityInventoryChange.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.SELF_TAKE_ORDER.getDescription());
            maCityInventoryService.addInventoryChangeLog(cityInventoryChange);
        }
        //生成ebs接口表数据
        MaOrderReceiveInf maOrderReceiveInf = new MaOrderReceiveInf();
        maOrderReceiveInf.setDeliverTypeTitle(AppDeliveryType.SELF_TAKE);
        maOrderReceiveInf.setOrderNumber(orderNumber);
        maOrderReceiveInf.setReceiveDate(date);
        maOrderReceiveInf.setSobId(maOrderTempInfo.getSobId());
        maOrderReceiveInf.setInitDate(maOrderTempInfo.getCreateTime());
        maOrderReceiveInf.setHeaderId(maOrderTempInfo.getId());
        maOrderReceiveInf.setSendTime(new Date());
        this.saveAppToEbsOrderReceiveInf(maOrderReceiveInf);

    }

    @Override
    public MaOrderReceiveInf queryOrderReceiveInf(String orderNumber){
        MaOrderReceiveInf maOrderReceiveInf  =this.maOrderDAO.queryOrderReceiveInf(orderNumber);
        return  maOrderReceiveInf;
    }

    @Override
    public void sendOrderReceiveInfAndRecord(String orderNumber){
        if(null==orderNumber){
            throw  new RuntimeException("发送接口失败，订单ID为空");
        }
        MaOrderReceiveInf maOrderReceiveInf  =this.queryOrderReceiveInf(orderNumber);
        //调用ebsSenderService接口传ebs
        this.ebsSenderService.sendOrderReceiveInfAndRecord(maOrderReceiveInf);
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderReceivables(MaOrderAmount maOrderAmount) {
        //得到订单基本信息
        MaOrderTempInfo maOrderTempInfo = this.getOrderInfoByOrderNo(maOrderAmount.getOrderNumber());
        //更新订单收款状态
        this.updateorderReceivablesStatus(maOrderAmount.getOrderNumber());
        //设置订单收款信息并存入订单账款支付明细表
        MaOrderBillingPaymentDetails maOrderBillingPaymentDetails = new MaOrderBillingPaymentDetails();
        maOrderBillingPaymentDetails.setOrdNo(maOrderAmount.getOrderNumber());
        maOrderBillingPaymentDetails.setPayTime(maOrderAmount.getDate());
        maOrderBillingPaymentDetails.setCreateTime(new Date());
        maOrderBillingPaymentDetails.setPaymentSubjectType(maOrderTempInfo.getCreatorIdentityType());
        maOrderBillingPaymentDetails.setPaymentSubjectTypeDesc(maOrderTempInfo.getCreatorIdentityType().getDescription());
        maOrderBillingPaymentDetails.setOid(maOrderTempInfo.getId());
        maOrderBillingPaymentDetails.setReceiptNumber(maOrderAmount.getSerialNumber());
        if (!maOrderAmount.getCashAmount().equals(BigDecimal.ZERO)) {
            maOrderBillingPaymentDetails.setPayType(OrderBillingPaymentType.CASH);
            maOrderBillingPaymentDetails.setPayTypeDesc(OrderBillingPaymentType.CASH.getDescription());
            maOrderBillingPaymentDetails.setAmount(maOrderAmount.getCashAmount());
            this.saveOrderBillingPaymentDetails(maOrderBillingPaymentDetails);
        }
        if (!maOrderAmount.getOtherAmount().equals(BigDecimal.ZERO)) {
            maOrderBillingPaymentDetails.setPayType(OrderBillingPaymentType.OTHER);
            maOrderBillingPaymentDetails.setPayTypeDesc(OrderBillingPaymentType.OTHER.getDescription());
            maOrderBillingPaymentDetails.setAmount(maOrderAmount.getOtherAmount());
            this.saveOrderBillingPaymentDetails(maOrderBillingPaymentDetails);
        }
        if (!maOrderAmount.getPosAmount().equals(BigDecimal.ZERO)) {
            maOrderBillingPaymentDetails.setPayType(OrderBillingPaymentType.POS);
            maOrderBillingPaymentDetails.setPayTypeDesc(OrderBillingPaymentType.POS.getDescription());
            maOrderBillingPaymentDetails.setAmount(maOrderAmount.getPosAmount());
            this.saveOrderBillingPaymentDetails(maOrderBillingPaymentDetails);
        }
    }

    @Override
    public Boolean isPayUp(String orderNumber) {
        return this.maOrderDAO.isPayUp(orderNumber);
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
    public PageInfo<MaSelfTakeOrderVO> findArrearsAndAgencyOrderList(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maOrderDAO.findArrearsAndAgencyOrderList();
        return new PageInfo<>(maSelfTakeOrderVOList);
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
        //根据应付金额判断订单账单是否已付清
        orderBillingDetails.setIsPayUp(true);

        //设置门店预存款
        Double stPreDeposit = 0D;
        if (null != preDeposit) {
            stPreDeposit = preDeposit;
            orderBillingDetails.setCash(0D);
            orderBillingDetails.setOtherMoney(0D);
            orderBillingDetails.setPosMoney(0D);
            orderBillingDetails.setPosNumber(null);
        } else {
            orderBillingDetails.setCash(null == cash ? 0D : cash);
            orderBillingDetails.setOtherMoney(null == otherMoney ? 0D : otherMoney);
            orderBillingDetails.setPosMoney(null == posMoney ? 0D : posMoney);
            orderBillingDetails.setPosNumber(null == posNumber ? null : posNumber);
            orderBillingDetails.setStPreDeposit(0D);
        }
        orderBillingDetails.setStPreDeposit(stPreDeposit);
        orderBillingDetails.setPayUpTime(DateUtil.parseDateTime(payTime));
        return orderBillingDetails;
    }


    @Override
    public List<OrderBillingPaymentDetails> createMaOrderBillingPaymentDetails(OrderBaseInfo orderBaseInfo, OrderBillingDetails orderBillingDetails) {
        List<OrderBillingPaymentDetails> billingPaymentDetails = new ArrayList<>();
        if (null != orderBaseInfo && null != orderBillingDetails) {
            //门店预存款
            if (null != orderBillingDetails.getStPreDeposit() && orderBillingDetails.getStPreDeposit() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.ST_PREPAY, orderBillingDetails.getStPreDeposit(),
                        PaymentSubjectType.SELLER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);
            }
            //门店现金
            if (null != orderBillingDetails.getCash() && orderBillingDetails.getCash() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.CASH, orderBillingDetails.getStPreDeposit(),
                        PaymentSubjectType.SELLER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);
            }
            //门店POS
            if (null != orderBillingDetails.getPosMoney() && orderBillingDetails.getPosMoney() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.POS, orderBillingDetails.getStPreDeposit(),
                        PaymentSubjectType.SELLER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);
            }
            //门店其他
            if (null != orderBillingDetails.getOtherMoney() && orderBillingDetails.getOtherMoney() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.OTHER, orderBillingDetails.getStPreDeposit(),
                        PaymentSubjectType.SELLER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);
            }
        }
        return billingPaymentDetails;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMaOrderBusiness(Integer identityType, Long userId, OrderBillingDetails orderBillingDetails, OrderBaseInfo orderBaseInfo,
                                    List<OrderGoodsInfo> orderGoodsInfoList, List<OrderBillingPaymentDetails> paymentDetails, String ipAddress) {
        //******* 检查库存和与账单支付金额是否充足,如果充足就扣减相应的数量
        this.deductionsStPreDeposit(identityType, userId, orderBillingDetails, orderBaseInfo.getOrderNumber(), ipAddress);
        //******* 持久化订单相关实体信息  *******
        this.saveAndHandleMaOrderRelevantInfo(orderBaseInfo, orderGoodsInfoList, orderBillingDetails, paymentDetails);
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
                        log.setChangeMoney(billingDetails.getStPreDeposit());
                        log.setBalance(preDeposit.getBalance() - billingDetails.getStPreDeposit());
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
    public void saveAndHandleMaOrderRelevantInfo(OrderBaseInfo orderBaseInfo,List<OrderGoodsInfo> orderGoodsInfoList,
                                               OrderBillingDetails orderBillingDetails, List<OrderBillingPaymentDetails> paymentDetails) {
        if (null != orderBaseInfo) {
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
                        //创建产品券信息
                        CustomerProductCoupon customerProductCoupon = new CustomerProductCoupon();
                        customerProductCoupon.setCustomerId(customer.getCusId());
                        customerProductCoupon.setGoodsId(goodsInfo.getGid());
                        customerProductCoupon.setQuantity(goodsInfo.getOrderQuantity());
                        customerProductCoupon.setGetType(CouponGetType.BUY);
                        customerProductCoupon.setGetTime(orderBaseInfo.getCreateTime());
                        customerProductCoupon.setEffectiveStartTime(orderBaseInfo.getCreateTime());
                        customerProductCoupon.setEffectiveEndTime(null);
                        customerProductCoupon.setIsUsed(Boolean.FALSE);
                        customerProductCoupon.setUseTime(null);
                        customerProductCoupon.setUseOrderNumber(null);
                        customerProductCoupon.setGetOrderNumber(orderBaseInfo.getOrderNumber());
                        if (customer.getCustomerType().equals(AppCustomerType.MEMBER)) {
                            customerProductCoupon.setBuyPrice(goodsInfo.getVIPPrice());
                        }else {
                            customerProductCoupon.setBuyPrice(goodsInfo.getRetailPrice());
                        }
                        customerProductCoupon.setStoreId(orderBaseInfo.getStoreId());
                        customerProductCoupon.setSellerId(orderBaseInfo.getSalesConsultId());
                        customerProductCoupon.setStatus(Boolean.TRUE);
                        customerProductCoupon.setDisableTime(null);
                        customerProductCoupon.setGoodsId(goodsInfo.getId());
                        //保存产品券信息
                        productCouponService.addCustomerProductCoupon(customerProductCoupon);
                    }
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
    public OrderBaseInfo createMaOrderBaseInfo(AppCustomer appCustomer,City city,AppStore appStore,AppEmployee appEmployee,
                                               Double preDepositMoney,String remarks,String preDepositRemarks , Double totalMoney, String orderNumber){
        OrderBaseInfo orderBaseInfo = new OrderBaseInfo();
        //城市id
        orderBaseInfo.setCityId(city.getCityId());
        //城市名称
        orderBaseInfo.setCityName(city.getName());
        //设置订单创建时间
        Calendar calendar = Calendar.getInstance();
        //订单号
        orderBaseInfo.setOrderNumber(orderNumber);
        //创建时间
        orderBaseInfo.setCreateTime(calendar.getTime());
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
        if (null == preDepositMoney){
            orderBaseInfo.setRemark(remarks);
            orderBaseInfo.setTotalGoodsPrice(totalMoney);
        } else{
            orderBaseInfo.setRemark(preDepositRemarks);
            orderBaseInfo.setTotalGoodsPrice(preDepositMoney);
        }
        return orderBaseInfo;
    }

}
