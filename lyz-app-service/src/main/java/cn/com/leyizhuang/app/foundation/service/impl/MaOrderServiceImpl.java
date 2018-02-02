package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.core.constant.*;
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
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditMoneyDetail;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderAmount;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderBillingPaymentDetails;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderTempInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventoryChange;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderReceiveInf;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.OrderBaseInf;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCompanyOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBilling;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.BillingSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.MaOrderVO;
import cn.com.leyizhuang.app.foundation.vo.management.goodscategory.MaOrderGoodsDetailResponse;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideCreditChangeDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.order.*;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        //记录销量
        statisticsSellDetailsService.addOrderSellDetails(orderNumber);
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
    public String queryAuditStatus(String orderNumber) {
        return this.maOrderDAO.queryAuditStatus(orderNumber);
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
    public PageInfo<MaAgencyAndArrearsOrderVO> findArrearsAndAgencyOrderList(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<MaAgencyAndArrearsOrderVO> arrearsAndAgencyOrderVOList = maOrderDAO.findArrearsAndAgencyOrderList();
        return new PageInfo<>(arrearsAndAgencyOrderVOList);
    }


    @Override
    public PageInfo<MaAgencyAndArrearsOrderVO> findMaAgencyAndArrearsOrderListByScreen(Integer page, Integer size, Long cityId, Long storeId, Integer status, Integer isPayUp) {
        PageHelper.startPage(page, size);
        if (storeId != -1) {
            cityId = null;
        }
        List<MaAgencyAndArrearsOrderVO> maAgencyAndArrearsOrderVOList = maOrderDAO.findMaAgencyAndArrearsOrderListByScreen(cityId, storeId, status, isPayUp);
        return new PageInfo<>(maAgencyAndArrearsOrderVOList);
    }

    @Override
    public PageInfo<MaAgencyAndArrearsOrderVO> findMaAgencyAndArrearsOrderListByInfo(Integer page, Integer size, String info) {
        PageHelper.startPage(page, size);
        List<MaAgencyAndArrearsOrderVO> maAgencyAndArrearsOrderVOList = maOrderDAO.findMaAgencyAndArrearsOrderListByInfo(info);
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
    public void auditOrderStatus(String orderNumber, String status) {
        this.maOrderDAO.auditOrderStatus(orderNumber, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void arrearsOrderRepayment(MaOrderAmount maOrderAmount, GuideCreditChangeDetailVO guideCreditChangeDetailVO, Date lastUpdateTime) {
        for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
            //更新订单支付信息
            this.orderReceivables(maOrderAmount);
            //得到导购id
            Long sellerId = this.querySellerIdByOrderNumber(maOrderAmount.getOrderNumber());
            if (null == sellerId) {
                throw new RuntimeException("该订单导购ID为空");
            }
            //得到该销售的可用额度
            GuideCreditMoney guideCreditMoney = maEmpCreditMoneyService.findGuideCreditMoneyAvailableByEmpId(sellerId);
            BigDecimal availableCreditMoney = guideCreditMoney.getCreditLimitAvailable().add(maOrderAmount.getAllAmount());
            //更改该销售的可用额度
            Date updateTime = guideCreditMoney.getLastUpdateTime();
            if (updateTime.equals(lastUpdateTime)) {
                maEmpCreditMoneyService.updateGuideCreditMoneyByRepayment(sellerId, availableCreditMoney);
                //生成信用金额变成日志
                GuideCreditMoneyDetail guideCreditMoneyDetail = new GuideCreditMoneyDetail();
                guideCreditMoneyDetail.setEmpId(sellerId);
                guideCreditMoneyDetail.setOriginalCreditLimitAvailable(guideCreditMoney.getCreditLimitAvailable());
                guideCreditMoneyDetail.setCreditLimitAvailable(availableCreditMoney);
                guideCreditChangeDetailVO.setEmpId(sellerId);
                maEmpCreditMoneyService.saveCreditMoneyChange(guideCreditMoneyDetail, guideCreditChangeDetailVO);
                break;
            } else {
                if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                    throw new SystemBusyException("系统繁忙，请稍后再试!");
                }
            }
        }
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
        //根据应付金额判断订单账单是否已付清
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
            if (null != orderBillingDetails.getStoreCash() && orderBillingDetails.getStoreCash() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.CASH, orderBillingDetails.getStPreDeposit(),
                        PaymentSubjectType.SELLER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);
            }
            //门店POS
            if (null != orderBillingDetails.getStorePosMoney() && orderBillingDetails.getStorePosMoney() > AppConstant.DOUBLE_ZERO) {
                OrderBillingPaymentDetails details = new OrderBillingPaymentDetails();
                details.generateOrderBillingPaymentDetails(OrderBillingPaymentType.POS, orderBillingDetails.getStPreDeposit(),
                        PaymentSubjectType.SELLER, orderBaseInfo.getOrderNumber(), OrderUtils.generateReceiptNumber(orderBaseInfo.getCityId()));
                billingPaymentDetails.add(details);
            }
            //门店其他
            if (null != orderBillingDetails.getStoreOtherMoney() && orderBillingDetails.getStoreOtherMoney() > AppConstant.DOUBLE_ZERO) {
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
    public void saveAndHandleMaOrderRelevantInfo(OrderBaseInfo orderBaseInfo, List<OrderGoodsInfo> orderGoodsInfoList,
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
                        if (null != goodsInfo.getOrderQuantity() && goodsInfo.getOrderQuantity() > 0) {
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
                            } else {
                                customerProductCoupon.setBuyPrice(goodsInfo.getRetailPrice());
                            }
                            customerProductCoupon.setStoreId(orderBaseInfo.getStoreId());
                            customerProductCoupon.setSellerId(orderBaseInfo.getSalesConsultId());
                            customerProductCoupon.setStatus(Boolean.TRUE);
                            customerProductCoupon.setDisableTime(null);
                            customerProductCoupon.setGoodsLineId(goodsInfo.getId());
                            //保存产品券信息
                            productCouponService.addCustomerProductCoupon(customerProductCoupon);
                        }
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
    public OrderBaseInfo createMaOrderBaseInfo(AppCustomer appCustomer, City city, AppStore appStore, AppEmployee appEmployee,
                                               Double preDepositMoney, String remarks, String preDepositRemarks, Double totalMoney, String orderNumber) {
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
        if (null == preDepositMoney) {
            orderBaseInfo.setRemark(remarks);
            orderBaseInfo.setTotalGoodsPrice(totalMoney);
        } else {
            orderBaseInfo.setRemark(preDepositRemarks);
            orderBaseInfo.setTotalGoodsPrice(preDepositMoney);
        }
        return orderBaseInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scanningUnpaidOrder() {
        Date date = new Date();
        //获取所有待付款订单
        List<OrderBaseInfo> orderBaseInfoList = maOrderDAO.scanningUnpaidOrder();
        if (null != orderBaseInfoList && orderBaseInfoList.size() > 0) {
            for (OrderBaseInfo orderBaseInfo : orderBaseInfoList) {
                if (date.after(orderBaseInfo.getEffectiveEndTime())) {
                    //获取退单号
                    String returnNumber = OrderUtils.getReturnNumber();
                    //创建退单头
                    ReturnOrderBaseInfo returnOrderBaseInfo = new ReturnOrderBaseInfo();
                    //获取订单账目明细
                    OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderBaseInfo.getOrderNumber());
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
                        returnPrice += (orderGoodsInfo.getOrderQuantity() * orderGoodsInfo.getPromotionSharePrice());
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
                    returnOrderBaseInfo.setReturnStatus(AppReturnOrderStatus.CANCELED);
                    //保存退单头信息
                    returnOrderService.saveReturnOrderBaseInfo(returnOrderBaseInfo);
                    //获取退单头id
                    Long returnOrderId = returnOrderBaseInfo.getRoid();

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
                            //获取现有城市库存量
                            CityInventory cityInventory = cityService.findCityInventoryByCityIdAndGoodsId(orderBaseInfo.getCityId(), orderGoodsInfo.getGid());
                            //退还城市库存量
                            cityService.updateCityInventoryByEmpIdAndGoodsIdAndGoodsQty(orderBaseInfo.getSalesConsultId(), orderGoodsInfo.getGid(), orderGoodsInfo.getOrderQuantity());
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
                        } else if ("门店自提".equals(orderBaseInfo.getDeliveryType().getDescription())) {
                            OrderLogisticsInfo orderLogisticsInfo = appOrderService.getOrderLogistice(orderBaseInfo.getOrderNumber());
                            //查询现有门店库存
                            StoreInventory storeInventory = appStoreService.findStoreInventoryByStoreCodeAndGoodsId(orderLogisticsInfo.getBookingStoreCode(), orderGoodsInfo.getGid());
                            //退还门店可用量
                            appStoreService.updateStoreInventoryByStoreCodeAndGoodsId(orderLogisticsInfo.getBookingStoreCode(), orderGoodsInfo.getGid(), orderGoodsInfo.getOrderQuantity());
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
                            storeInventoryAvailableQtyChangeLog.setChangeType(null);
                            storeInventoryAvailableQtyChangeLog.setChangeTypeDesc("超过待付款时间");
                            storeInventoryAvailableQtyChangeLog.setReferenceNumber(orderBaseInfo.getOrderNumber());
                            //保存记录
                            appStoreService.addStoreInventoryAvailableQtyChangeLog(storeInventoryAvailableQtyChangeLog);
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
                            //获取顾客当前乐币数量
                            CustomerLeBi customerLeBi = appCustomerService.findCustomerLebiByCustomerId(orderBaseInfo.getCustomerId());
                            //返还乐币后顾客乐币数量
                            Integer lebiTotal = (customerLeBi.getQuantity() + orderBillingDetails.getLebiQuantity());
                            //更改顾客乐币数量
                            leBiVariationLogService.updateLeBiQtyByUserId(lebiTotal, date, orderBaseInfo.getCustomerId());
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
                        }
                        //返回顾客预存款
                        if (orderBillingDetails.getCusPreDeposit() != null && orderBillingDetails.getCusPreDeposit() > 0) {
                            //获取顾客预存款
                            CustomerPreDeposit customerPreDeposit = appCustomerService.findByCusId(orderBaseInfo.getCustomerId());
                            //返还预存款后顾客预存款金额
                            Double cusPreDeposit = (customerPreDeposit.getBalance() + orderBillingDetails.getCusPreDeposit());
                            //更改顾客预存款金额
                            appCustomerService.unlockCustomerDepositByUserIdAndDeposit(orderBaseInfo.getCustomerId(), orderBillingDetails.getCusPreDeposit());
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
                        }
                    }
                    if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.SELLER)) {
                        //返回门店预存款
                        if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                            //获取门店预存款
                            StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(orderBaseInfo.getSalesConsultId());
                            //返还预存款后门店预存款金额
                            Double stPreDeposit = (storePreDeposit.getBalance() + orderBillingDetails.getStPreDeposit());
                            //修改门店预存款
                            storePreDepositLogService.updateStPreDepositByUserId(stPreDeposit, orderBaseInfo.getSalesConsultId());
                            //记录门店预存款变更日志
                            StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                            stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            stPreDepositLogDO.setChangeMoney(orderBillingDetails.getStPreDeposit());
                            stPreDepositLogDO.setRemarks("超过待付款时间");
                            stPreDepositLogDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                            stPreDepositLogDO.setChangeType(StorePreDepositChangeType.CANCEL_ORDER);
                            stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                            stPreDepositLogDO.setOperatorId(null);
                            stPreDepositLogDO.setOperatorType(AppIdentityType.ADMINISTRATOR);
                            stPreDepositLogDO.setBalance(stPreDeposit);
                            stPreDepositLogDO.setDetailReason("超过待付款时间");
                            stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            //保存日志
                            storePreDepositLogService.save(stPreDepositLogDO);
                        }
                        //返回导购信用额度
                        if (orderBillingDetails.getEmpCreditMoney() != null && orderBillingDetails.getEmpCreditMoney() > 0) {
                            //获取导购信用金
                            EmpCreditMoney empCreditMoney = appEmployeeService.findEmpCreditMoneyByEmpId(orderBaseInfo.getSalesConsultId());
                            //返还信用金后导购信用金额度
                            Double creditMoney = (empCreditMoney.getCreditLimitAvailable() + orderBillingDetails.getEmpCreditMoney());
                            //修改导购信用额度
                            appEmployeeService.unlockGuideCreditByUserIdAndCredit(orderBaseInfo.getSalesConsultId(), orderBillingDetails.getEmpCreditMoney());
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
                        }
                    }
                    if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.DECORATE_MANAGER)) {
                        //返回门店预存款
                        if (orderBillingDetails.getStPreDeposit() != null && orderBillingDetails.getStPreDeposit() > 0) {
                            //获取门店预存款
                            StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(orderBaseInfo.getCreatorId());
                            //返还预存款后门店预存款金额
                            Double stPreDeposit = (storePreDeposit.getBalance() + orderBillingDetails.getStPreDeposit());
                            //修改门店预存款
                            storePreDepositLogService.updateStPreDepositByUserId(stPreDeposit, orderBaseInfo.getCreatorId());
                            //记录门店预存款变更日志
                            StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                            stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            stPreDepositLogDO.setChangeMoney(orderBillingDetails.getStPreDeposit());
                            stPreDepositLogDO.setRemarks("超过待付款时间");
                            stPreDepositLogDO.setOrderNumber(orderBaseInfo.getOrderNumber());
                            stPreDepositLogDO.setChangeType(StorePreDepositChangeType.CANCEL_ORDER);
                            stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                            stPreDepositLogDO.setOperatorId(null);
                            stPreDepositLogDO.setOperatorType(AppIdentityType.ADMINISTRATOR);
                            stPreDepositLogDO.setBalance(stPreDeposit);
                            stPreDepositLogDO.setDetailReason("超过待付款时间");
                            stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            //保存日志
                            storePreDepositLogService.save(stPreDepositLogDO);
                        }
                        //返回门店信用金（装饰公司）
                        if (orderBillingDetails.getStoreCreditMoney() != null && orderBillingDetails.getStoreCreditMoney() > 0) {
                            //查询门店信用金
                            StoreCreditMoney storeCreditMoney = storeCreditMoneyLogService.findStoreCreditMoneyByUserId(orderBaseInfo.getCreatorId());
                            //返还后门店信用金额度
                            Double creditMoney = (storeCreditMoney.getCreditLimitAvailable() + orderBillingDetails.getStoreCreditMoney());
                            //修改门店可用信用金
                            appStoreService.unlockStoreCreditByUserIdAndCredit(orderBaseInfo.getCreatorId(), orderBillingDetails.getStoreCreditMoney());
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
                        }
                        //返回门店现金返利（装饰公司）
                        if (orderBillingDetails.getStoreSubvention() != null && orderBillingDetails.getStoreSubvention() > 0) {
                            //获取门店现金返利
                            StoreSubvention storeSubvention = appStoreService.findStoreSubventionByEmpId(orderBaseInfo.getCreatorId());
                            //返还后门店现金返利余额
                            Double subvention = (storeSubvention.getBalance() + orderBillingDetails.getStoreSubvention());
                            //修改门店现金返利
                            appStoreService.unlockStoreSubventionByUserIdAndSubvention(orderBaseInfo.getCreatorId(), orderBillingDetails.getStoreSubvention());
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
                        }
                    }
                    //修改订单状态为已取消
                    appOrderService.updateOrderStatusAndDeliveryStatusByOrderNo(AppOrderStatus.CANCELED, null, orderBaseInfo.getOrderNumber());
                }
            }
        }

    }

}
