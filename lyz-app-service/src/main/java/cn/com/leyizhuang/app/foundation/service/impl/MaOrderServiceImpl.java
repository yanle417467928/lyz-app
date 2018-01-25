package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.CityInventoryAvailableQtyChangeType;
import cn.com.leyizhuang.app.core.constant.OrderBillingPaymentType;
import cn.com.leyizhuang.app.core.constant.StoreInventoryAvailableQtyChangeType;
import cn.com.leyizhuang.app.core.remote.ebs.EbsSenderService;
import cn.com.leyizhuang.app.foundation.dao.MaGoodsDAO;
import cn.com.leyizhuang.app.foundation.dao.MaOrderDAO;
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
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCompanyOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaOrderVORequest;
import cn.com.leyizhuang.app.foundation.service.MaCityInventoryService;
import cn.com.leyizhuang.app.foundation.service.MaOrderService;
import cn.com.leyizhuang.app.foundation.service.MaStoreInventoryService;
import cn.com.leyizhuang.app.foundation.vo.MaOrderVO;
import cn.com.leyizhuang.app.foundation.vo.management.order.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
    private EbsSenderService ebsSenderService;
    @Resource
    private MaGoodsDAO maGoodsDAO;


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
        Long id = this.saveAppToEbsOrderReceiveInf(maOrderReceiveInf);
        maOrderReceiveInf.setId(id);
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
    public Long saveAppToEbsOrderReceiveInf(MaOrderReceiveInf maOrderReceiveInf) {
        this.maOrderDAO.saveAppToEbsOrderReceiveInf(maOrderReceiveInf);
        if (null != maOrderReceiveInf) {
            return maOrderReceiveInf.getId();
        }
        return null;
    }
}
