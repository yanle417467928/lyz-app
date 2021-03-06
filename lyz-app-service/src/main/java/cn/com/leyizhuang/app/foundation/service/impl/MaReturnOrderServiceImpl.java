package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.SystemBusyException;
import cn.com.leyizhuang.app.core.remote.ebs.EbsSenderService;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.dao.MaReturnOrderDAO;
import cn.com.leyizhuang.app.foundation.dao.MaStoreDAO;
import cn.com.leyizhuang.app.foundation.dao.ReturnOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.management.coupon.MaCashCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.coupon.MaProductCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.MaCustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyCredit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.MaReturnGoods;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderTempInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.*;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInventoryChange;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreRealInventoryChange;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLifecycle;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBillingDetail;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderLifecycle;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.management.city.CityDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositVO;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.CountUtil;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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
    @Resource
    private AppSeparateOrderService appSeparateOrderService;
    @Resource
    private MaDecorativeCompanyCreditService maDecorativeCompanyCreditService;
    @Resource
    private ReturnOrderService returnOrderService;
    @Resource
    private AppOrderService appOrderService;
    @Resource
    private MaCityService maCityService;
    @Resource
    private ReturnOrderDAO returnOrderDAO;
    @Resource
    private AppStoreService appStoreService;
    @Resource
    private CommonService commonService;
    @Resource
    private StorePreDepositLogService storePreDepositLogService;


    @Override
    public PageInfo<MaReturnOrderInfo> findMaReturnOrderList(Integer page, Integer size, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<MaReturnOrderInfo> maReturnOrderInfoList = maReturnOrderDAO.findMaReturnOrderList(storeIds);
        return new PageInfo<>(maReturnOrderInfoList);
    }


    @Override
    public PageInfo<MaReturnOrderInfo> findMaReturnOrderListByScreen(Integer page, Integer size, Long storeId, String status, List<Long> storeIds,String beginTime,String endTime,String memberName,String memberPhone,String creatorName,String queryOrderInfo,String shippingAddress,String receiverName,String receiverPhone,String sellerName,String cityId) {
        PageHelper.startPage(page, size);
        if (storeId == -1) {
            storeId = null;
        }
        if ("-1".equals(status)) {
            status = null;
        }
        List<MaReturnOrderInfo> maReturnOrderList = maReturnOrderDAO.findMaReturnOrderListByScreen(storeId, status, storeIds,beginTime,endTime,memberName,memberPhone,creatorName,queryOrderInfo
                ,shippingAddress,receiverName,receiverPhone,sellerName,cityId);
        return new PageInfo<>(maReturnOrderList);
    }


    @Override
    public PageInfo<MaReturnOrderInfo> findMaReturnOrderPageGirdByInfo(Integer page, Integer size, String info, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<MaReturnOrderInfo> maReturnOrderInfoList = maReturnOrderDAO.findMaReturnOrderPageGirdByInfo(info, storeIds);
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
    public void updateReturnOrderStatus(String returnNumber, String status) {
        if (StringUtils.isNotBlank(returnNumber) && StringUtils.isNotBlank(status)) {
            maReturnOrderDAO.updateReturnOrderStatus(returnNumber, status);
        }
    }

    @Override
    public List<ReturnOrderGoodsInfo> findReturnOrderGoodsList(String returnNumber) {
        return maReturnOrderDAO.findReturnOrderGoodsList(returnNumber);
    }

    @Override
    public MaOrdReturnBilling findReturnOrderBillingList(Long roid) {
        return maReturnOrderDAO.findReturnOrderBillingList(roid);
    }

    @Override
    public void sendReturnOrderReceiptInfAndRecord(String returnNumber) throws RuntimeException {
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
    public HashedMap returnOrderReceive(String returnNumber, MaReturnOrderDetailInfo maReturnOrderDetailInfo, MaOrdReturnBilling maOrdReturnBillingList, ShiroUser shiroUser) throws RuntimeException {
        HashedMap maps = new HashedMap();
        Date date = new Date();
        if (null == maReturnOrderDetailInfo || null == maReturnOrderDetailInfo.getStoreId()) {
            throw new RuntimeException("该订单门店ID为空,无法更新门店库存");
        }
        //查询该订单下的所有商品
        List<ReturnOrderGoodsInfo> MaOrderGoodsInfoList = this.findReturnOrderGoodsList(returnNumber);
        //门店库存,可用量及生成日志信息
        for (ReturnOrderGoodsInfo returnOrderGoodsInfo : MaOrderGoodsInfoList) {

            //查看是否有该门店
            StoreDetailVO storeDetailVO = maStoreService.queryStoreVOById(maReturnOrderDetailInfo.getStoreId());
            if (null == storeDetailVO) {
                throw new RuntimeException("未找到该门店,门店id:" + maReturnOrderDetailInfo.getStoreId());
            }
            MaStoreInventoryChange storeInventoryChange = new MaStoreInventoryChange();
            //查看门店下 该商品的库存
            MaStoreInventory storeInventory = maStoreInventoryService.findStoreInventoryByStoreIdAndGoodsId(maReturnOrderDetailInfo.getStoreId(), returnOrderGoodsInfo.getGid());
            if (null == storeInventory) {
             /*   //新增门店库存数量及可用量
                MaStoreInventory storeInventorySave = new MaStoreInventory();

                Integer goodsQtyAfterChange = returnOrderGoodsInfo.getReturnQty();
                Integer goodsAvailableItyAfterChange = returnOrderGoodsInfo.getReturnQty();
                storeInventorySave.setAvailableIty(goodsAvailableItyAfterChange);
                storeInventorySave.setRealIty(goodsQtyAfterChange);
                storeInventorySave.setGid(returnOrderGoodsInfo.getGid());
                storeInventorySave.setCreateTime(new Date());
                storeInventorySave.setStoreCode(storeDetailVO.getStoreCode());
                storeInventorySave.setStoreId(storeDetailVO.getStoreId());
                storeInventorySave.setStoreName(storeDetailVO.getStoreName());
                storeInventorySave.setSku(returnOrderGoodsInfo.getSku());
                storeInventorySave.setStoreName(returnOrderGoodsInfo.getSkuName());

                if (null != storeDetailVO.getCityCode()) {
                    Long cityId = storeDetailVO.getCityCode().getCityId();
                    CityDetailVO cityDetailVO = maCityService.queryCityVOById(cityId);
                    storeInventorySave.setCityId(cityDetailVO.getCityId());
                    storeInventorySave.setCityName(cityDetailVO.getName());
                    storeInventorySave.setCityCode(cityDetailVO.getCode());
                }
                maStoreInventoryService.saveStoreInventory(storeInventorySave);
                //增加门店库变更日志存可用量
                storeInventoryChange.setCityId(storeInventory.getCityId());
                storeInventoryChange.setCityName(storeInventory.getCityName());
                storeInventoryChange.setStoreId(storeInventory.getStoreId());
                storeInventoryChange.setStoreCode(storeInventory.getStoreCode());
                storeInventoryChange.setReferenceNumber(returnNumber);
                storeInventoryChange.setGid(returnOrderGoodsInfo.getGid());
                storeInventoryChange.setSku(returnOrderGoodsInfo.getSku());
                storeInventoryChange.setSkuName(returnOrderGoodsInfo.getSkuName());
                storeInventoryChange.setChangeTime(date);
                storeInventoryChange.setAfterChangeQty(goodsQtyAfterChange);
                storeInventoryChange.setChangeQty(returnOrderGoodsInfo.getReturnQty());
                storeInventoryChange.setChangeType(StoreInventoryAvailableQtyChangeType.STORE_EXPORT_GOODS);
                storeInventoryChange.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.STORE_EXPORT_GOODS.getDescription());
                maStoreInventoryService.addInventoryChangeLog(storeInventoryChange);*/
                throw new RuntimeException("未找到该门店或该门店下没有该商品库存,门店id:" + maReturnOrderDetailInfo.getStoreId() + "商品id:" + returnOrderGoodsInfo.getGid());
            } else {
                for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                    //更新门店库存数量及可用量
                    Integer goodsQtyAfterChange = storeInventory.getRealIty() + returnOrderGoodsInfo.getReturnQty();
                    Integer goodsAvailableItyAfterChange = storeInventory.getAvailableIty() + returnOrderGoodsInfo.getReturnQty();
                    Integer affectLine = maStoreInventoryService.updateStoreInventoryAndAvailableIty(maReturnOrderDetailInfo.getStoreId(), returnOrderGoodsInfo.getGid(), goodsQtyAfterChange, goodsAvailableItyAfterChange, storeInventory.getLastUpdateTime());
                    //增加门店库变更日志存可用量
                    if (affectLine > 0) {
                        storeInventoryChange.setCityId(storeInventory.getCityId());
                        storeInventoryChange.setCityName(storeInventory.getCityName());
                        storeInventoryChange.setStoreId(storeInventory.getStoreId());
                        storeInventoryChange.setStoreCode(storeInventory.getStoreCode());
                        storeInventoryChange.setStoreName(storeInventory.getStoreName());
                        storeInventoryChange.setReferenceNumber(returnNumber);
                        storeInventoryChange.setGid(returnOrderGoodsInfo.getGid());
                        storeInventoryChange.setSku(returnOrderGoodsInfo.getSku());
                        storeInventoryChange.setSkuName(returnOrderGoodsInfo.getSkuName());
                        storeInventoryChange.setChangeTime(date);
                        storeInventoryChange.setAfterChangeQty(goodsAvailableItyAfterChange);
                        storeInventoryChange.setChangeQty(returnOrderGoodsInfo.getReturnQty());
                        storeInventoryChange.setChangeType(StoreInventoryAvailableQtyChangeType.SELF_TAKE_ORDER_RETURN);
                        storeInventoryChange.setChangeTypeDesc(StoreInventoryAvailableQtyChangeType.SELF_TAKE_ORDER_RETURN.getDescription());
                        maStoreInventoryService.addInventoryChangeLog(storeInventoryChange);
                        //增加门店真实库存日志
                        MaStoreRealInventoryChange maStoreInventoryChange = new MaStoreRealInventoryChange();
                        maStoreInventoryChange.setCityId(storeInventory.getCityId());
                        maStoreInventoryChange.setCityName(storeInventory.getCityName());
                        maStoreInventoryChange.setStoreId(storeInventory.getStoreId());
                        maStoreInventoryChange.setStoreCode(storeInventory.getStoreCode());
                        maStoreInventoryChange.setStoreName(storeInventory.getStoreName());
                        maStoreInventoryChange.setReferenceNumber(returnNumber);
                        maStoreInventoryChange.setGid(returnOrderGoodsInfo.getGid());
                        maStoreInventoryChange.setSku(returnOrderGoodsInfo.getSku());
                        maStoreInventoryChange.setSkuName(returnOrderGoodsInfo.getSkuName());
                        maStoreInventoryChange.setChangeTime(date);
                        maStoreInventoryChange.setAfterChangeQty(goodsQtyAfterChange);
                        maStoreInventoryChange.setChangeQty(returnOrderGoodsInfo.getReturnQty());
                        maStoreInventoryChange.setChangeType(StoreInventoryRealQtyChangeType.SELF_TAKE_ORDER_RETURN);
                        maStoreInventoryChange.setChangeTypeDesc(StoreInventoryRealQtyChangeType.SELF_TAKE_ORDER_RETURN.getDescription());
                        maStoreInventoryService.addRealInventoryChangeLog(maStoreInventoryChange);
                        break;
                    } else {
                        if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                            throw new SystemBusyException("系统繁忙，请稍后再试!");
                        }
                    }
                }
            }
            //得到订单头商品信息
            //OrderGoodsInfo orderGoodsInfo = appOrderService.getOrderGoodsInfoById(returnOrderGoodsInfo.getOrderGoodsId());
            //更改订单头商品已退数量和可退数量
            //Integer returnQuantity = orderGoodsInfo.getReturnQuantity() + returnOrderGoodsInfo.getReturnQty();
            //Integer returnableQuantity = orderGoodsInfo.getReturnableQuantity() - returnOrderGoodsInfo.getReturnQty();
            //returnOrderService.updateReturnableQuantityAndReturnQuantityById(returnQuantity, returnableQuantity, returnOrderGoodsInfo.getOrderGoodsId());

        }
        //门店退货生成ebs接口表数据
        MaOrderTempInfo maOrderTempInfo = maOrderService.getOrderInfoByOrderNo(maReturnOrderDetailInfo.getOrderNo());
        MaStoreReturnOrderAppToEbsBaseInfo maStoreReturnOrderAppToEbs = new MaStoreReturnOrderAppToEbsBaseInfo();
        maStoreReturnOrderAppToEbs.setSobId(maOrderTempInfo.getSobId());
        maStoreReturnOrderAppToEbs.setMainOrderNumber(maOrderTempInfo.getOrderNumber());
        maStoreReturnOrderAppToEbs.setReturnDate(date);
        maStoreReturnOrderAppToEbs.setReturnNumber(maReturnOrderDetailInfo.getReturnNo());
        maReturnOrderDAO.saveAppToEbsReturnOrderInf(maStoreReturnOrderAppToEbs);

        //退款
        Double cashAmount = maOrdReturnBillingList.getCash();
        Double creditMoneyAmount = maOrdReturnBillingList.getCreditMoney();
        Double onlinePayAmount = maOrdReturnBillingList.getOnlinePay();
        Double preDepositAmount = maOrdReturnBillingList.getPreDeposit();
        Double stCreditMoneyAmount = maOrdReturnBillingList.getStCreditMoney();
        Double stPreDepositAmount = maOrdReturnBillingList.getStPreDeposit();
        Long roid = maOrdReturnBillingList.getRoid();

        //退款生成ebs接口表数据
        List<MaOrdReturnBillingDetail> maOrdReturnBillingDetailList = new ArrayList<MaOrdReturnBillingDetail>();
        Date ReturnDate = new Date();

        //退现金
        if (null != cashAmount && cashAmount > 0) {
            MaOrdReturnBillingDetail maOrdReturnBillingCash = new MaOrdReturnBillingDetail();
            String refundNumber = OrderUtils.getRefundNumber();
            maOrdReturnBillingCash.setCreateTime(ReturnDate);
            maOrdReturnBillingCash.setIntoAmountTime(ReturnDate);
            maOrdReturnBillingCash.setRefundNumber(refundNumber);
            maOrdReturnBillingCash.setReturnMoney(BigDecimal.valueOf(cashAmount));
            maOrdReturnBillingCash.setReturnPayType(OrderBillingPaymentType.CASH.getValue());
            maOrdReturnBillingCash.setRoid(roid);
            maOrdReturnBillingCash.setReturnNo(returnNumber);
            maOrdReturnBillingDetailList.add(maOrdReturnBillingCash);
        }

        //退线上支付
        if (null != onlinePayAmount && onlinePayAmount > 0) {
            Calendar calendar = Calendar.getInstance();
            //获取三个月以前的时间
            calendar.add(Calendar.MONTH, -3);
            //判断是否查过三个月 如果超过三个月退预存款 否则原路退回
            if (maOrderTempInfo.getCreateTime().before(calendar.getTime())) {
                maps.put("hasReturnOnlinePay", Boolean.FALSE);
                if (AppIdentityType.CUSTOMER.equals(maReturnOrderDetailInfo.getCreatorIdentityType())) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取顾客预存款
                        CustomerPreDeposit customerPreDeposit = appCustomerService.findByCusId(maReturnOrderDetailInfo.getCustomerId());
                        //返还预存款后顾客预存款金额
                        Double cusPreDeposit = (customerPreDeposit.getBalance() + onlinePayAmount);
                        //更改顾客预存款金额
                        Integer affectLine = appCustomerService.updateDepositByUserIdAndVersion(maReturnOrderDetailInfo.getCustomerId(), maOrdReturnBillingList.getOnlinePay(), customerPreDeposit.getLastUpdateTime());
                        if (affectLine > 0) {
                            //记录预存款日志
                            CusPreDepositLogDO cusPreDepositLogDO = new CusPreDepositLogDO();
                            cusPreDepositLogDO.setCreateTime(LocalDateTime.now());
                            cusPreDepositLogDO.setChangeMoney(maOrdReturnBillingList.getOnlinePay());
                            cusPreDepositLogDO.setOrderNumber(maOrderTempInfo.getOrderNumber());
                            cusPreDepositLogDO.setChangeType(CustomerPreDepositChangeType.RETURN_ORDER);
                            cusPreDepositLogDO.setChangeTypeDesc(CustomerPreDepositChangeType.RETURN_ORDER.getDescription());
                            cusPreDepositLogDO.setCusId(maOrderTempInfo.getCustomerId());
                            cusPreDepositLogDO.setOperatorId(maReturnOrderDetailInfo.getCreatorId());
                            cusPreDepositLogDO.setOperatorType(maReturnOrderDetailInfo.getCreatorIdentityType());
                            cusPreDepositLogDO.setBalance(cusPreDeposit);
                            cusPreDepositLogDO.setDetailReason(ReturnOrderType.NORMAL_RETURN.getDescription());
                            cusPreDepositLogDO.setTransferTime(LocalDateTime.now());
                            cusPreDepositLogDO.setRemarks("退还第三方支付转预存款");
                            cusPreDepositLogDO.setMerchantOrderNumber(returnNumber);
                            //保存日志
                            appCustomerService.addCusPreDepositLog(cusPreDepositLogDO);
                            //保存订单退款详情
                            String refundNumber = OrderUtils.getRefundNumber();
                            MaOrdReturnBillingDetail maOrdReturnBillingPreDeposit = new MaOrdReturnBillingDetail();
                            maOrdReturnBillingPreDeposit.setCreateTime(calendar.getTime());
                            maOrdReturnBillingPreDeposit.setRoid(maReturnOrderDetailInfo.getRoid());
                            maOrdReturnBillingPreDeposit.setReturnNo(returnNumber);
                            maOrdReturnBillingPreDeposit.setReturnPayType(OrderBillingPaymentType.CUS_PREPAY.getValue());
                            maOrdReturnBillingPreDeposit.setReturnMoney(BigDecimal.valueOf(onlinePayAmount));
                            maOrdReturnBillingPreDeposit.setIntoAmountTime(Calendar.getInstance().getTime());
                            maOrdReturnBillingPreDeposit.setReplyCode(null);
                            maOrdReturnBillingPreDeposit.setRefundNumber(refundNumber);
                            maOrdReturnBillingPreDeposit.setReturnNo(returnNumber);
                            maOrdReturnBillingPreDeposit.setReturnSubjectId(maOrderTempInfo.getCustomerId());
                            maOrdReturnBillingDetailList.add(maOrdReturnBillingPreDeposit);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                } else if (AppIdentityType.SELLER.equals(maReturnOrderDetailInfo.getCreatorIdentityType()) ||
                        AppIdentityType.DECORATE_MANAGER.equals(maReturnOrderDetailInfo.getCreatorIdentityType())) {
                    for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                        //获取门店预存款
                        StorePreDepositVO storePreDeposit = maStoreService.queryStorePredepositByStoreId(maReturnOrderDetailInfo.getStoreId());
                        //返还预存款后门店预存款金额
                        Double stPreDeposit = (Double.parseDouble(storePreDeposit.getBalance()) + onlinePayAmount);
                        //修改门店预存款
                        Integer affectLine = maStoreService.updateStPreDepositByUserIdAndVersion(stPreDeposit, storePreDeposit.getStoreId(), storePreDeposit.getLastUpdateTime());
                        if (affectLine > 0) {

                            StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                            stPreDepositLogDO.setCreateTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            stPreDepositLogDO.setChangeMoney(onlinePayAmount);
                            stPreDepositLogDO.setRemarks("退还第三方支付转预存款");
                            stPreDepositLogDO.setOrderNumber(maReturnOrderDetailInfo.getOrderNo());
                            stPreDepositLogDO.setChangeType(StorePreDepositChangeType.RETURN_ORDER);
                            stPreDepositLogDO.setChangeTypeDesc(StorePreDepositChangeType.RETURN_ORDER.getDescription());
                            stPreDepositLogDO.setStoreId(maReturnOrderDetailInfo.getStoreId());
                            stPreDepositLogDO.setOperatorId(null);
                            stPreDepositLogDO.setOperatorType(AppIdentityType.ADMINISTRATOR);
                            stPreDepositLogDO.setBalance(stPreDeposit);
                            stPreDepositLogDO.setDetailReason(StorePreDepositChangeType.RETURN_ORDER.getDescription());
                            stPreDepositLogDO.setTransferTime(TimeTransformUtils.UDateToLocalDateTime(date));
                            //保存日志
                            maStoreService.saveStorePreDepositLog(stPreDepositLogDO);

                            MaOrdReturnBillingDetail maOrdReturnBillingStPreDeposit = new MaOrdReturnBillingDetail();
                            String refundNumber = OrderUtils.getRefundNumber();
                            maOrdReturnBillingStPreDeposit.setCreateTime(ReturnDate);
                            maOrdReturnBillingStPreDeposit.setIntoAmountTime(ReturnDate);
                            maOrdReturnBillingStPreDeposit.setRefundNumber(refundNumber);
                            maOrdReturnBillingStPreDeposit.setReturnMoney(BigDecimal.valueOf(onlinePayAmount));
                            maOrdReturnBillingStPreDeposit.setReturnPayType(OrderBillingPaymentType.ST_PREPAY.getValue());
                            maOrdReturnBillingStPreDeposit.setRoid(roid);
                            maOrdReturnBillingStPreDeposit.setReturnNo(returnNumber);
                            maOrdReturnBillingStPreDeposit.setReturnNo(returnNumber);
                            maOrdReturnBillingStPreDeposit.setReturnSubjectId(maOrderTempInfo.getStoreId());
                            maOrdReturnBillingDetailList.add(maOrdReturnBillingStPreDeposit);
                            break;
                        } else {
                            if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                                throw new SystemBusyException("系统繁忙，请稍后再试!");
                            }
                        }
                    }
                }
            } else {
                maps.put("hasReturnOnlinePay", Boolean.TRUE);
            }
        }else{
            maps.put("hasReturnOnlinePay", Boolean.FALSE);
        }
/*            //查询原订单线上支付方式
            List<MaPaymentData> paymentDataList = maOrderService.findPaymentDataByOrderNo(maReturnOrderDetailInfo.getOrderNo());
            for (MaPaymentData maPaymentData : paymentDataList) {
                MaOrdReturnBillingDetail maOrdReturnBillingDetail = new MaOrdReturnBillingDetail();
                maOrdReturnBillingDetail.setCreateTime(ReturnDate);
                maOrdReturnBillingDetail.setIntoAmountTime(ReturnDate);
                maOrdReturnBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                maOrdReturnBillingDetail.setReturnMoney(BigDecimal.valueOf(maPaymentData.getTotalFee()));
                maOrdReturnBillingDetail.setReturnPayType(maPaymentData.getOnlinePayType().toString());
                maOrdReturnBillingDetail.setRoid(roid);
                returnOrderRefundInf.setAmount(onlinePayAmount);
                returnOrderRefundInf.setRefundType(OrderBillingPaymentType.CASH);
                returnOrderRefundInf.setDescription(OrderBillingPaymentType.CASH.getDescription());
                appSeparateOrderService.saveReturnOrderRefundInf(returnOrderRefundInf);
                maOrdReturnBillingDetailList.add(maOrdReturnBillingDetail);
            }*/


        //退顾客预存款
        if (null != preDepositAmount && preDepositAmount > 0) {
            MaOrdReturnBillingDetail maOrdReturnBillingPreDeposit = new MaOrdReturnBillingDetail();
            String refundNumber = OrderUtils.getRefundNumber();
            maOrdReturnBillingPreDeposit.setCreateTime(ReturnDate);
            maOrdReturnBillingPreDeposit.setIntoAmountTime(ReturnDate);
            maOrdReturnBillingPreDeposit.setRefundNumber(refundNumber);
            maOrdReturnBillingPreDeposit.setReturnMoney(BigDecimal.valueOf(preDepositAmount));
            maOrdReturnBillingPreDeposit.setReturnPayType(OrderBillingPaymentType.CUS_PREPAY.getValue());
            maOrdReturnBillingPreDeposit.setRoid(roid);
            maOrdReturnBillingPreDeposit.setReturnNo(returnNumber);
            maOrdReturnBillingPreDeposit.setReturnSubjectId(maOrderTempInfo.getCustomerId());
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
            String refundNumber = OrderUtils.getRefundNumber();
            maOrdReturnBillingStPreDepositAmount.setCreateTime(ReturnDate);
            maOrdReturnBillingStPreDepositAmount.setIntoAmountTime(ReturnDate);
            maOrdReturnBillingStPreDepositAmount.setRefundNumber(refundNumber);
            maOrdReturnBillingStPreDepositAmount.setReturnMoney(BigDecimal.valueOf(stPreDepositAmount));
            maOrdReturnBillingStPreDepositAmount.setReturnPayType(OrderBillingPaymentType.ST_PREPAY.getValue());
            maOrdReturnBillingStPreDepositAmount.setRoid(roid);
            maOrdReturnBillingStPreDepositAmount.setReturnNo(returnNumber);
            maOrdReturnBillingStPreDepositAmount.setReturnSubjectId(maOrderTempInfo.getStoreId());
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
                    stPreDepositLogDO.setChangeTypeDesc(StorePreDepositChangeType.RETURN_ORDER.getDescription());
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
        }
        //TODO 是否退导购信用额度
        if (null != creditMoneyAmount && creditMoneyAmount > 0) {
        }
        //退装饰公司信用金
        if (null != stCreditMoneyAmount && stCreditMoneyAmount > 0) {
            MaOrdReturnBillingDetail returnOrderBillingDetail = new MaOrdReturnBillingDetail();
            returnOrderBillingDetail.setCreateTime(Calendar.getInstance().getTime());
            returnOrderBillingDetail.setRoid(roid);
            returnOrderBillingDetail.setReturnNo(returnNumber);
            returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.STORE_CREDIT_MONEY.getValue());
            returnOrderBillingDetail.setReturnMoney(BigDecimal.valueOf(stCreditMoneyAmount));
            returnOrderBillingDetail.setIntoAmountTime(Calendar.getInstance().getTime());
            returnOrderBillingDetail.setReplyCode(null);
            returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
            returnOrderBillingDetail.setReturnSubjectId(maOrderTempInfo.getStoreId());
            maOrdReturnBillingDetailList.add(returnOrderBillingDetail);
            Long storeId = maReturnOrderDetailInfo.getStoreId();
            StoreDetailVO storeVO = maStoreService.queryStoreVOById(storeId);
            if ("ZS".equals(storeVO.getStoreType())) {
                DecorativeCompanyInfo decorativeCompanyInfo = maStoreService.queryDecorativeCompanyCreditById(storeId);
                DecorativeCompanyCredit decorativeCompanyCredit = new DecorativeCompanyCredit();
                decorativeCompanyCredit.setCreditLimit(decorativeCompanyInfo.getCreditLimit());
                decorativeCompanyCredit.setStoreId(decorativeCompanyInfo.getStoreId());
                decorativeCompanyCredit.setCredit(decorativeCompanyInfo.getCredit().add(BigDecimal.valueOf(stCreditMoneyAmount)));
                StoreCreditMoneyChangeLog storeCreditMoneyChangeLog = new StoreCreditMoneyChangeLog();
                storeCreditMoneyChangeLog.setChangeAmount(stCreditMoneyAmount);
                storeCreditMoneyChangeLog.setChangeType(StoreCreditMoneyChangeType.RETURN_ORDER);
                storeCreditMoneyChangeLog.setChangeTypeDesc(StoreCreditMoneyChangeType.RETURN_ORDER.getDescription());
                storeCreditMoneyChangeLog.setCreateTime(date);
                storeCreditMoneyChangeLog.setCreditLimitAvailableAfterChange(stCreditMoneyAmount + decorativeCompanyCredit.getCredit().doubleValue());
                storeCreditMoneyChangeLog.setStoreId(storeId);
                storeCreditMoneyChangeLog.setOperatorId(shiroUser.getId());
                storeCreditMoneyChangeLog.setOperatorType(AppIdentityType.ADMINISTRATOR);
                storeCreditMoneyChangeLog.setReferenceNumber(returnNumber);
                maDecorativeCompanyCreditService.updateDecorativeCompanyCredit(decorativeCompanyCredit, storeCreditMoneyChangeLog);
            }
        }
        //退代支付导购门店预存款
        if (null != maOrdReturnBillingList.getSellerStoreDeposit() && maOrdReturnBillingList.getSellerStoreDeposit() > AppConstant.PAY_UP_LIMIT){
            for (int i = 1; i <= AppConstant.OPTIMISTIC_LOCK_RETRY_TIME; i++) {
                //获取门店预存款
                StorePreDeposit storePreDeposit = storePreDepositLogService.findStoreByUserId(maOrderTempInfo.getSalesConsultId());
                //返还预存款后门店预存款金额
                Double stPreDeposit = CountUtil.add(storePreDeposit.getBalance(), maOrdReturnBillingList.getSellerStoreDeposit());
                //修改门店预存款
                Integer affectLine = storePreDepositLogService.updateStPreDepositByStoreIdAndVersion(stPreDeposit, storePreDeposit.getStoreId(), storePreDeposit.getLastUpdateTime());
                if (affectLine > 0) {
                    //记录门店预存款变更日志
                    StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
                    stPreDepositLogDO.setCreateTime(LocalDateTime.now());
                    stPreDepositLogDO.setChangeMoney(maOrdReturnBillingList.getSellerStoreDeposit());
                    stPreDepositLogDO.setRemarks("取消订单返还门店预存款");
                    stPreDepositLogDO.setOrderNumber(maOrderTempInfo.getOrderNumber());
                    stPreDepositLogDO.setChangeType(StorePreDepositChangeType.RETURN_ORDER);
                    stPreDepositLogDO.setStoreId(storePreDeposit.getStoreId());
                    stPreDepositLogDO.setOperatorId(maReturnOrderDetailInfo.getCreatorId());
                    stPreDepositLogDO.setOperatorType(maReturnOrderDetailInfo.getCreatorIdentityType());
                    stPreDepositLogDO.setBalance(stPreDeposit);
                    stPreDepositLogDO.setDetailReason(ReturnOrderType.NORMAL_RETURN.getDescription());
                    stPreDepositLogDO.setTransferTime(LocalDateTime.now());
                    //保存日志
                    storePreDepositLogService.save(stPreDepositLogDO);

                    ReturnOrderBillingDetail returnOrderBillingDetail = new ReturnOrderBillingDetail();
                    returnOrderBillingDetail.setCreateTime(Calendar.getInstance().getTime());
                    returnOrderBillingDetail.setRoid(maReturnOrderDetailInfo.getRoid());
                    returnOrderBillingDetail.setReturnNo(maReturnOrderDetailInfo.getReturnNo());
                    returnOrderBillingDetail.setReturnPayType(OrderBillingPaymentType.SELLER_ST_PREPAY);
                    returnOrderBillingDetail.setReturnMoney(maOrdReturnBillingList.getSellerStoreDeposit());
                    returnOrderBillingDetail.setIntoAmountTime(Calendar.getInstance().getTime());
                    returnOrderBillingDetail.setReplyCode(null);
                    returnOrderBillingDetail.setRefundNumber(OrderUtils.getRefundNumber());
                    returnOrderBillingDetail.setReturnSubjectId(maOrderTempInfo.getSalesManagerStoreId());
                    returnOrderService.saveReturnOrderBillingDetail(returnOrderBillingDetail);
                    break;
                } else {
                    if (i == AppConstant.OPTIMISTIC_LOCK_RETRY_TIME) {
                        throw new SystemBusyException("系统繁忙，请稍后再试!");
                    }
                }
            }
        }


        if (maOrdReturnBillingDetailList.size() > 0) {
            this.saveReturnOrderBillingDetail(maOrdReturnBillingDetailList);
        }
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
                //更改产品卷是否已退
                productCouponService.updateProductCouponIsReturn(maProductCouponInfo.getPcid(), true);

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

                //修改买券订单可退数量
                if (StringUtils.isNotBlank(customerProductCoupon.getGetOrderNumber()) && (customerProductCoupon.getGetType() == CouponGetType.BUY || customerProductCoupon.getGetType() == CouponGetType.PRESENT)){
                    returnOrderDAO.updateProductOrderReturnNableQty(customerProductCoupon.getGoodsLineId());
                }
            }
        }

        //获取订单使用现金券
    /*    List<MaCashCouponInfo> orderCashCouponList = maCouponService.findCashCouponTypeByReturnOrderId(maReturnOrderDetailInfo.getRoid());
        if (orderCashCouponList != null && orderCashCouponList.size() > 0) {
            for (MaCashCouponInfo maCashCouponInfo : orderCashCouponList) {
                //查询现金券原信息
                CustomerCashCoupon customerCashCoupon = cashCouponService.findCusCashCouponByCouponId(maCashCouponInfo.getCcid());
                customerCashCoupon.setLastUpdateTime(new Date());
                customerCashCoupon.setIsUsed(Boolean.FALSE);
                //修改原现金券是否使用和修改时间
                cashCouponService.updateCustomerCashCoupon(customerCashCoupon);
                //TODO
                //更改现金卷是否已退
                //productCouponService.updateCashCouponIsReturn(maCashCouponInfo.getCcid(), true);

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
        }*/
        //查询主退单
        ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderDAO.queryByReturnNo(returnNumber);

        //查原订单
        OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(returnOrderBaseInfo.getOrderNo());

        //查退货商品信息
        List<ReturnOrderGoodsInfo> returnOrderGoodsInfos = returnOrderDAO.findReturnOrderGoodsInfoByOrderNumber(returnNumber);

        //********************************退经销差价退还*************************
        AppStore appStore = appStoreService.findStoreByUserIdAndIdentityType(returnOrderBaseInfo.getCreatorId(), returnOrderBaseInfo.getCreatorIdentityType().getValue());

        if (AssertUtil.isNotEmpty(appStore) && appStore.getStoreType().equals(StoreType.FX) || appStore.getStoreType().equals(StoreType.JM)) {
            commonService.deductionOrderJxPriceDifferenceRefund(returnOrderBaseInfo, orderBaseInfo, returnOrderGoodsInfos);
        }

        //更新订单状态
        this.updateReturnOrderStatus(returnNumber, AppReturnOrderStatus.FINISHED.toString());

        //**************************保存订单生命周期信息*************************************
        OrderLifecycle orderLifecycle = new OrderLifecycle();
        orderLifecycle.setOrderNumber(orderBaseInfo.getOrderNumber());
        orderLifecycle.setOperation(OrderLifecycleType.NORMAL_RETURN);
        orderLifecycle.setPostStatus(AppOrderStatus.FINISHED);
        orderLifecycle.setOperationTime(new Date());
        returnOrderDAO.saveOrderLifecycle(orderLifecycle);
        //********************************保存退单生命周期信息***********************
        ReturnOrderLifecycle returnOrderLifecycle = new ReturnOrderLifecycle();
        returnOrderLifecycle.setRoid(maReturnOrderDetailInfo.getRoid());
        returnOrderLifecycle.setReturnNo(maReturnOrderDetailInfo.getReturnNo());
        returnOrderLifecycle.setOperation(OrderLifecycleType.NORMAL_RETURN);
        returnOrderLifecycle.setPostStatus(AppReturnOrderStatus.FINISHED);
        returnOrderLifecycle.setOperationTime(new Date());
        returnOrderDAO.saveReturnOrderLifecycle(returnOrderLifecycle);

        return maps;
    }
}

