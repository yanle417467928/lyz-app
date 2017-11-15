package cn.com.leyizhuang.app.web.controller.order;

import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.request.*;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderGoodsSimpleRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderLockExpendRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.BillingSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.ProductCouponSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.CashCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderGoodsSimpleResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderGoodsSimpleResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.OrderUsableProductCouponResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单相关接口
 *
 * @author Richard
 *         Created on 2017-10-23 17:02
 **/
@RestController
@RequestMapping(value = "/app/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private AppCustomerService appCustomerService;

    @Autowired
    private AppEmployeeService appEmployeeService;

    @Autowired
    private AppStoreService appStoreService;

    @Autowired
    private CityService cityService;

    @Autowired
    private GoodsService goodsServiceImpl;

    @Autowired
    private AppOrderService appOrderService;

    @Autowired
    private ProductCouponService productCouponService;

    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> createOrder(Long userId, Integer identityType, Long customerId, String goodsInfo,
                                         String deliveryInfo, Integer leBiQuantity,
                                         @RequestParam(value = "cashCouponIds", required = false) String[] cashCouponIds,
                                         String productCouponInfo, String billingInfo) {
        logger.info("createOrder CALLED,去支付生成订单,入参 userId:{},identityType:{},customerId:{},goodsInfo:{}," +
                        " deliveryInfo:{},leBiQuantity:{},cashCouponIds:{},productCouponInfo:{},billingInfo:{}", userId, identityType, customerId, goodsInfo, deliveryInfo,
                leBiQuantity, cashCouponIds, productCouponInfo, billingInfo);
        ResultDTO<Object> resultDTO;
        if(null == userId){

        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JavaType goodsSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSimpleInfo.class);
        JavaType productCouponSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ProductCouponSimpleInfo.class);
        try {
            //转化前台提交过来的json类型参数
            List<GoodsSimpleInfo> goodsList = objectMapper.readValue(goodsInfo, goodsSimpleInfo);
            DeliverySimpleInfo deliverySimpleInfo = objectMapper.readValue(deliveryInfo, DeliverySimpleInfo.class);
            List<ProductCouponSimpleInfo> productCouponList = objectMapper.readValue(productCouponInfo, productCouponSimpleInfo);
            BillingSimpleInfo billing = objectMapper.readValue(billingInfo, BillingSimpleInfo.class);


            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,null,null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

        //先核对页面订单信息
        //锁定库存
        //掉用支付接口
        //减扣库存
    }

    /**
     * 用户确认订单计算商品价格明细
     * @param goodsSimpleRequest 用户下料清单里的商品信息DTO对象
     * @return
     * @author Jerry
     */
    @PostMapping(value = "/enter", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> enterOrder(@RequestBody OrderGoodsSimpleRequest goodsSimpleRequest) {

        logger.info("enterOrder CALLED,用户确认订单计算商品价格明细，入参 goodsSimpleRequest:{}", goodsSimpleRequest);

        ResultDTO resultDTO;
        if (goodsSimpleRequest.getGoodsList().isEmpty()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "找不到对象！", null);
            logger.info("enterOrder OUT,用户确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsSimpleRequest.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("enterOrder OUT,用户确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsSimpleRequest.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("enterOrder OUT,用户确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Long userId = goodsSimpleRequest.getUserId();
        Integer identityType = goodsSimpleRequest.getIdentityType();
        List<GoodsSimpleInfo> goodsList = goodsSimpleRequest.getGoodsList();

        try {
            int totalQty = 0;
            Double totalPrice = 0.00;
            Double memberDiscount = 0.00;
            //订单优惠在以后的促销活动表中获取数据
            Double orderDiscount = 0.00;
            //运费暂时还没出算法
            Double freight = 0.00;
            Double upstairsFee = 0.00;
            Double totalOrderAmount = 0.00;
            List<Long> goodsIds = new ArrayList<Long>();
            List<OrderGoodsSimpleResponse> goodsInfo = null;
            List<OrderUsableProductCouponResponse> productCouponResponseList = null;
            List<CashCouponResponse> cashCouponResponseList = null;
            Map<String, Object> goodsSettlement = new HashMap<>();


            if (identityType == 6) {
                AppCustomer customer = appCustomerService.findById(userId);
                Long cityId = customer.getCityId();
                for (int i = 0; i < goodsList.size(); i++) {
                    if (!goodsList.get(i).getIsGift()) {
                        goodsIds.add(goodsList.get(i).getId());
                    }
                    //获取商品总数
                    totalQty = totalQty + goodsList.get(i).getNum();
                }
                goodsInfo = goodsServiceImpl.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIds);
                int goodsTotalQty = 0;
                for (int i = 0; i < goodsInfo.size(); i++) {
                    for (int j = 0; j < goodsList.size(); j++) {
                        OrderGoodsSimpleResponse info = goodsInfo.get(i);
                        GoodsSimpleInfo simpleInfo = goodsList.get(j);
                        if (info.getId().equals(simpleInfo.getId())) {
                            //如果是赠品则标识设置为赠品
                            if (simpleInfo.getIsGift()) {
                                goodsTotalQty = info.getGoodsQty() + simpleInfo.getNum();
                                info.setHasGift(Boolean.TRUE);
                                info.setGoodsQty(goodsTotalQty);
                            } else {
                                //先获取本品数量
                                info.setGoodsQty(simpleInfo.getNum());

                                //可以算出总金额
                                totalPrice = CountUtil.add(totalPrice, CountUtil.mul(info.getRetailPrice(), simpleInfo.getNum()));
                                if (null != customer.getSalesConsultId()) {
                                    memberDiscount = CountUtil.mul(CountUtil.sub(info.getRetailPrice(), info.getVipPrice()), goodsList.get(j).getNum());
                                }
                            }
                            //判断库存
                            Boolean isHaveInventory = appOrderService.existGoodsCityInventory(cityId, info.getId(), info.getGoodsQty());
                            if (!isHaveInventory) {
                                String msg = goodsInfo.get(i).getGoodsName().concat("城市库存不足！");
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, msg, null);
                                logger.info("enterOrder OUT,顾客确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
                                return resultDTO;
                            }
                        }
                    }
                }
                //计算订单金额小计
                //TODO 根据促销减去订单折扣
                totalOrderAmount = CountUtil.add(CountUtil.sub(totalPrice, memberDiscount, totalPrice / 100), totalPrice / 1000);
                //计算顾客乐币
                CustomerLeBi leBi = appCustomerService.findLeBiByUserIdAndGoodsMoney(userId, totalOrderAmount);

                //TODO... 根据促销减去产品券商品。
                productCouponResponseList = productCouponService.findProductCouponByCustomerIdAndGoodsId(userId, goodsIds);
                cashCouponResponseList = appCustomerService.findCashCouponByCustomerId(userId);

                //查询顾客预存款
                Double preDeposit = appCustomerService.findPreDepositBalanceByUserIdAndIdentityType(userId, identityType);

                goodsSettlement.put("totalQty", totalQty);
                goodsSettlement.put("totalPrice", totalPrice);
                goodsSettlement.put("totalGoodsInfo", goodsInfo);
                goodsSettlement.put("memberDiscount", memberDiscount);
                // TODO 会员折扣在创建促销表后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("orderDiscount", memberDiscount * 10);
                // TODO 运费再出算法后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("freight", memberDiscount / 10);
                goodsSettlement.put("upstairsFee", 100.00);
                goodsSettlement.put("totalOrderAmount", totalOrderAmount);
                goodsSettlement.put("lebi", leBi);
                goodsSettlement.put("productCouponList", productCouponResponseList);
                goodsSettlement.put("cashCouponList", cashCouponResponseList);
                goodsSettlement.put("preDeposit", preDeposit);
            }
            if (identityType == 0) {
                if (null == goodsSimpleRequest.getCustomerId()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "导购代下单客户不能为空", null);
                    logger.info("enterOrder OUT,用户确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                Long customerId = goodsSimpleRequest.getCustomerId();
                AppCustomer customer = appCustomerService.findById(customerId);
                Long storeId = customer.getStoreId();
                for (int i = 0; i < goodsList.size(); i++) {
                    if (!goodsList.get(i).getIsGift()) {
                        goodsIds.add(goodsList.get(i).getId());
                    }
                    //获取商品总数
                    totalQty = totalQty + goodsList.get(i).getNum();
                }
                goodsInfo = goodsServiceImpl.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);
                int goodsTotalQty = 0;
                for (int i = 0; i < goodsInfo.size(); i++) {
                    for (int j = 0; j < goodsList.size(); j++) {
                        OrderGoodsSimpleResponse info = goodsInfo.get(i);
                        GoodsSimpleInfo simpleInfo = goodsList.get(j);
                        if (info.getId().equals(simpleInfo.getId())) {
                            //如果是赠品则标识设置为包含赠品
                            if (simpleInfo.getIsGift()) {
                                goodsTotalQty = info.getGoodsQty() + simpleInfo.getNum();
                                info.setHasGift(Boolean.TRUE);
                                info.setGoodsQty(goodsTotalQty);
                            } else {
                                //先获取本品数量
                                info.setGoodsQty(simpleInfo.getNum());
                                //可以算出总金额
                                totalPrice = CountUtil.add(totalPrice, CountUtil.mul(info.getRetailPrice(), simpleInfo.getNum()));
                                memberDiscount = CountUtil.mul(CountUtil.sub(info.getRetailPrice(), info.getVipPrice()), goodsList.get(j).getNum());
                            }
                            //判断库存
                            Boolean isHaveInventory = appOrderService.existGoodsStoreInventory(storeId, info.getId(), info.getGoodsQty());
                            if (!isHaveInventory) {
                                String msg = goodsInfo.get(i).getGoodsName().concat("门店库存不足！");
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, msg, null);
                                logger.info("enterOrder OUT,导购确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
                                return resultDTO;
                            }
                        }
                    }
                }
                //计算订单金额小计
                //TODO 根据促销减去订单折扣
                totalOrderAmount = CountUtil.add(CountUtil.sub(totalPrice, memberDiscount, totalPrice / 100), totalPrice / 1000);
                //计算顾客乐币
                CustomerLeBi leBi = appCustomerService.findLeBiByUserIdAndGoodsMoney(customerId, totalOrderAmount);

                //TODO... 根据促销减去产品券商品。
                //现金券还需要传入订单金额判断是否满减
                productCouponResponseList = productCouponService.findProductCouponByCustomerIdAndGoodsId(customerId, goodsIds);
                cashCouponResponseList = appCustomerService.findCashCouponByCustomerId(customerId);

                //查询导购预存款和信用金
                SellerCreditMoney sellerCreditMoney = appEmployeeService.findCreditMoneyBalanceByUserIdAndIdentityType(userId, identityType);
                Double creditMoney = sellerCreditMoney.getAvailableBalance();
                //导购门店预存款
                Double storePreDeposit = appStoreService.findPreDepositBalanceByUserId(userId);


                goodsSettlement.put("totalQty", totalQty);
                goodsSettlement.put("totalPrice", totalPrice);
                goodsSettlement.put("totalGoodsInfo", goodsInfo);
                goodsSettlement.put("memberDiscount", memberDiscount);
                // TODO 会员折扣在创建促销表后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("orderDiscount", memberDiscount * 10);
                // TODO 运费再出算法后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("freight", memberDiscount / 10);
                goodsSettlement.put("upstairsFee", 100.00);
                goodsSettlement.put("totalOrderAmount", totalOrderAmount);
                goodsSettlement.put("lebi", leBi);
                goodsSettlement.put("productCouponList", productCouponResponseList);
                goodsSettlement.put("cashCouponList", cashCouponResponseList);
                goodsSettlement.put("creditMoney", creditMoney);
                goodsSettlement.put("storePreDeposit", storePreDeposit);
            }

            if (identityType == 2) {
                AppEmployee employee = appEmployeeService.findById(userId);
                Long storeId = employee.getStoreId();
                for (int i = 0; i < goodsList.size(); i++) {
                    if (!goodsList.get(i).getIsGift()) {
                        goodsIds.add(goodsList.get(i).getId());
                    }
                    //获取商品总数
                    totalQty = totalQty + goodsList.get(i).getNum();
                }
                goodsInfo = goodsServiceImpl.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);
                int goodsTotalQty = 0;
                for (int i = 0; i < goodsInfo.size(); i++) {
                    for (int j = 0; j < goodsList.size(); j++) {
                        OrderGoodsSimpleResponse info = goodsInfo.get(i);
                        GoodsSimpleInfo simpleInfo = goodsList.get(j);
                        if (info.getId().equals(simpleInfo.getId())) {
                            //如果是赠品则标识设置为赠品
                            if (simpleInfo.getIsGift()) {
                                goodsTotalQty = info.getGoodsQty() + simpleInfo.getNum();
                                info.setHasGift(Boolean.TRUE);
                                info.setGoodsQty(goodsTotalQty);
                            } else {
                                //先获取本品数量
                                info.setGoodsQty(simpleInfo.getNum());
                                //可以算出总金额
                                totalPrice = CountUtil.add(totalPrice, CountUtil.mul(info.getRetailPrice(), simpleInfo.getNum()));
                            }
                        }
                    }
                    //判断库存
                    Boolean isHaveInventory = appOrderService.existGoodsStoreInventory(storeId, goodsInfo.get(i).getId(), goodsTotalQty);
                    if (!isHaveInventory) {
                        String msg = goodsInfo.get(i).getGoodsName().concat("门店库存不足！");
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, msg, null);
                        logger.info("enterOrder OUT,项目经理确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
                //计算订单金额小计
                totalOrderAmount = CountUtil.add(CountUtil.sub(totalPrice, totalPrice / 100), totalPrice / 1000);

                //获取门店预存款，信用金，现金返利。
                Double storePreDeposit = appStoreService.findPreDepositBalanceByUserId(userId);
                Double storeCreditMoney = appStoreService.findCreditMoneyBalanceByUserId(userId);
                Double storeSubvention = appStoreService.findSubventionBalanceByUserId(userId);

                goodsSettlement.put("totalQty", totalQty);
                goodsSettlement.put("totalPrice", totalPrice);
                goodsSettlement.put("totalGoodsInfo", goodsInfo);
                // TODO 会员折扣在创建促销表后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("orderDiscount", totalPrice / 100);
                // TODO 运费再出算法后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("freight", totalPrice / 1000);
                goodsSettlement.put("upstairsFee", 100.00);
                goodsSettlement.put("totalOrderAmount", totalOrderAmount);
                goodsSettlement.put("storePreDeposit", storePreDeposit);
                goodsSettlement.put("storeCreditMoney", storeCreditMoney);
                goodsSettlement.put("storeSubvention", storeSubvention);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    goodsSettlement.size() > 0 ? goodsSettlement : null);
            logger.info("getGoodsMoney OUT,用户确认订单计算商品价格明细成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,用户确认订单计算商品价格明细失败!", null);
            logger.warn("enterOrder EXCEPTION,用户确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }


    /**
     * 通过现金券来重新计算确认订单
     *
     * @param usedCashCouponReq 使用现金券变更金额所需判断参数
     * @return
     */
    @PostMapping(value = "/reEnter/ccp", produces = "application/json;charset=UTF-8")
    public ResultDTO reEnterOrderByCashCoupon(@RequestBody UsedCashCouponReq usedCashCouponReq) {

        logger.info("reEnterOrderByCashCoupon CALLED,通过现金券来重新计算确认订单，入参 usedCashCouponReq:{}", usedCashCouponReq);

        ResultDTO resultDTO;
        if (usedCashCouponReq.getCashCouponsList().isEmpty()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "所选现金券不能为空", null);
            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == usedCashCouponReq.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == usedCashCouponReq.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == usedCashCouponReq.getOrderDiscount() || null == usedCashCouponReq.getTotalOrderAmount()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "计算订单所需参数不足", null);
            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Long userId = usedCashCouponReq.getUserId();
        Integer identityType = usedCashCouponReq.getIdentityType();
        List<GoodsIdQtyParam> cashCouponsList = usedCashCouponReq.getCashCouponsList();
//        Double totalPrice = usedCashCouponReq.getTotalPrice();
        Double cashCouponDiscount = 0.00;
        Double totalOrderAmount = CountUtil.add(usedCashCouponReq.getOrderDiscount(), usedCashCouponReq.getTotalOrderAmount());
        try {
            if (identityType == 6) {
                cashCouponDiscount = calculationCashCouponsDiscount(cashCouponsList, userId);
                if (cashCouponDiscount > totalOrderAmount) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "现金券抵扣金额超出订单金额", null);
                    logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (identityType == 0) {
                if (null == usedCashCouponReq.getCustomerId()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "代下单顾客id不能为空", null);
                    logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                Long cusId = usedCashCouponReq.getCustomerId();
                cashCouponDiscount = calculationCashCouponsDiscount(cashCouponsList, cusId);
                if (cashCouponDiscount > totalOrderAmount) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "现金券抵扣金额超出订单金额", null);
                    logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }

            }
            totalOrderAmount = CountUtil.sub(totalOrderAmount, cashCouponDiscount);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, totalOrderAmount);
            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知错误，通过现金券来重新计算确认订单失败", null);
            logger.warn("reEnterOrderByCashCoupon EXCEPTION,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 通过产品券来重新计算确认订单
     *
     * @param usedProductCouponReq 使用产品券变更金额所需判断参数
     * @return
     */
    @PostMapping(value = "/reEnter/pcp", produces = "application/json;charset=UTF-8")
    public ResultDTO reEnterOrderByProductCoupon(@RequestBody UsedProductCouponReq usedProductCouponReq) {
        logger.info("reEnterOrderByProductCoupon CALLED,通过产品券来重新计算确认订单，入参 usedCashCouponReq:{}", usedProductCouponReq);

        ResultDTO resultDTO;
        if (usedProductCouponReq.getProductCouponsList().isEmpty()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "所选现金券不能为空", null);
            logger.info("reEnterOrderByProductCoupon OUT,通过产品券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == usedProductCouponReq.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("reEnterOrderByProductCoupon OUT,通过产品券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == usedProductCouponReq.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("reEnterOrderByProductCoupon OUT,通过产品券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == usedProductCouponReq.getMemberDiscount()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "计算订单所需会员折扣参数不足", null);
            logger.info("reEnterOrderByProductCoupon OUT,通过产品券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Long userId = usedProductCouponReq.getUserId();
        Integer identityType = usedProductCouponReq.getIdentityType();
        List<GoodsIdQtyParam> productCouponsList = usedProductCouponReq.getProductCouponsList();
        Map<String, Double> returnMap = new HashMap<>(2);
        try {
            if (identityType == 6) {
                returnMap = calculationProductCouponsDiscount(productCouponsList, userId, usedProductCouponReq.getMemberDiscount(),
                        usedProductCouponReq.getTotalOrderAmount());
                if (null == returnMap) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "产品券抵扣金额超出订单金额", null);
                    logger.info("reEnterOrderByCashCoupon OUT,通过产品券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (identityType == 0) {
                if (null == usedProductCouponReq.getCustomerId()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "代下单顾客id不能为空", null);
                    logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                Long cusId = usedProductCouponReq.getCustomerId();
                returnMap = calculationProductCouponsDiscount(productCouponsList, cusId, usedProductCouponReq.getMemberDiscount(),
                        usedProductCouponReq.getTotalOrderAmount());
                if (null == returnMap) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "产品券抵扣金额超出订单金额", null);
                    logger.info("reEnterOrderByCashCoupon OUT,通过产品券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, returnMap);
            logger.info("reEnterOrderByProductCoupon OUT,通过产品券来重新计算确认订单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知错误，通过产品券来重新计算确认订单失败", null);
            logger.warn("reEnterOrderByProductCoupon EXCEPTION,通过产品券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 用户锁定订单相关款项和库存
     *
     * @param lockExpendRequest
     * @return
     */
    @Transactional
    @PostMapping(value = "/lock", produces = "application/json;charset=UTF-8")
    public ResultDTO lockOrder(OrderLockExpendRequest lockExpendRequest) {

        logger.info("lockOrder CALLED,用户锁定订单相关款项和库存，入参 lockExpendRequest:{}", lockExpendRequest);

        ResultDTO resultDTO;
        if (null == lockExpendRequest) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "找不到对象！", null);
            logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        if (null == lockExpendRequest.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == lockExpendRequest.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Long userId = lockExpendRequest.getUserId();
        Integer identityType = lockExpendRequest.getIdentityType();
        try {
            if (null != lockExpendRequest.getCustomerDeposit() && identityType == 6) {

                int result = appCustomerService.lockCustomerDepositByUserIdAndDeposit(
                        userId, lockExpendRequest.getCustomerDeposit());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "客户预存款余额不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败,客户预存款余额不足，出参 resultDTO:{}", resultDTO);
                    throw new RuntimeException("客户预存款余额不足!");
                }
            }
            if (null != lockExpendRequest.getGuideCredit() && identityType == 0) {

                int result = appEmployeeService.lockGuideCreditByUserIdAndCredit(
                        userId, lockExpendRequest.getGuideCredit());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "导购信用额度不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，导购信用额度不足 出参 resultDTO:{}", resultDTO);
                    throw new RuntimeException("导购信用额度不足!");
                }
            }
            if (null != lockExpendRequest.getStoreDeposit()) {
                if (identityType == 0 || identityType == 2) {
                    int result = appStoreService.lockStoreDepositByUserIdAndStoreDeposit(
                            userId, lockExpendRequest.getStoreDeposit());
                    if (result == 0) {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户所属门店预存款余额不足!", null);
                        logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，用户所属门店预存款余额不足 出参 resultDTO:{}", resultDTO);
                        throw new RuntimeException("用户所属门店预存款余额不足!");
                    }
                }
            }
            if (null != lockExpendRequest.getStoreCredit() && identityType == 2) {

                int result = appStoreService.lockStoreCreditByUserIdAndCredit(
                        userId, lockExpendRequest.getStoreCredit());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户所属门店信用额度余额不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，用户所属门店信用额度余额不足 出参 resultDTO:{}", resultDTO);
                    throw new RuntimeException("用户所属门店信用额度余额不足!");
                }
            }
            if (null != lockExpendRequest.getStoreSubvention() && identityType == 2) {

                int result = appStoreService.lockStoreSubventionByUserIdAndSubvention(
                        userId, lockExpendRequest.getStoreSubvention());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户所属门店现金返利余额不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，用户所属门店现金返利余额不足 出参 resultDTO:{}", resultDTO);
                    throw new RuntimeException("用户所属门店现金返利余额不足!");
                }
            }
            if (null != lockExpendRequest.getLebiQty() && identityType == 6) {

                int result = appCustomerService.lockCustomerLebiByUserIdAndQty(
                        userId, lockExpendRequest.getLebiQty());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客乐币剩余数量不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，顾客乐币剩余数量不足 出参 resultDTO:{}", resultDTO);
                    throw new RuntimeException("顾客乐币剩余数量不足!");
                }
            }
            if (null != lockExpendRequest.getStoreInventory() && !lockExpendRequest.getStoreInventory().isEmpty()) {

                int result = appStoreService.lockStoreInventoryByUserIdAndIdentityTypeAndInventory(
                        userId, identityType, lockExpendRequest.getStoreInventory());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户所属门店库存不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，用户所属门店库存不足 出参 resultDTO:{}", resultDTO);
                    throw new RuntimeException("用户所属门店库存不足!");
                }
            }
            if (null != lockExpendRequest.getCityInventory() && !lockExpendRequest.getStoreInventory().isEmpty()) {

                int result = cityService.lockCityInventoryByUserIdAndIdentityTypeAndInventory(
                        userId, identityType, lockExpendRequest.getCityInventory());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市库存不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，城市库存不足 出参 resultDTO:{}", resultDTO);
                    throw new RuntimeException("城市库存不足!");
                }
            }
            if (null != lockExpendRequest.getProductCoupons() && !lockExpendRequest.getProductCoupons().isEmpty() && identityType == 6) {

                int result = appCustomerService.lockCustomerProductCouponByUserIdAndProductCoupons(
                        userId, lockExpendRequest.getProductCoupons());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客产品券数量不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，顾客产品券数量不足 出参 resultDTO:{}", resultDTO);
                    throw new RuntimeException("城市库存不足!");
                }
            }
            if (null != lockExpendRequest.getCashCoupons() && !lockExpendRequest.getCashCoupons().isEmpty() && identityType == 6) {

                int result = appCustomerService.lockCustomerCashCouponByUserIdAndCashCoupons(
                        userId, lockExpendRequest.getCashCoupons());
                if (result == 0) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客现金券数量不足!", null);
                    logger.info("lockOrder OUT,用户锁定订单相关款项和库存失败，顾客现金券数量不足 出参 resultDTO:{}", resultDTO);
                    throw new RuntimeException("顾客现金券数量不足!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, e.getMessage(), null);
            logger.warn("getGoodsListByUserIdAndIdentityType EXCEPTION,用户锁定订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;

        }
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "用户锁定订单相关款项和库存成功！", null);
        logger.info("lockOrder OUT,用户锁定订单相关款项和库存成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * 用户释放订单相关款项和库存
     *
     * @param lockExpendRequest 释放资源对象
     * @return
     */
    @Transactional
    @PostMapping(value = "/unlock", produces = "application/json;charset=UTF-8")
    public ResultDTO unlockOrder(@RequestBody OrderLockExpendRequest lockExpendRequest) {

        logger.info("unlockOrder CALLED,用户释放订单相关款项和库存，入参 lockExpendRequest:{}", lockExpendRequest);

        ResultDTO resultDTO;
        if (null == lockExpendRequest) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "找不到对象！", null);
            logger.info("unlockOrder OUT,用户释放订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        if (null == lockExpendRequest.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("unlockOrder OUT,用户释放订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == lockExpendRequest.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("unlockOrder OUT,用户释放订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Long userId = lockExpendRequest.getUserId();
        Integer identityType = lockExpendRequest.getIdentityType();
        try {
            if (null != lockExpendRequest.getCustomerDeposit() && identityType == 6) {

                appCustomerService.unlockCustomerDepositByUserIdAndDeposit(userId, lockExpendRequest.getCustomerDeposit());

            }
            if (null != lockExpendRequest.getGuideCredit() && identityType == 0) {

                appEmployeeService.unlockGuideCreditByUserIdAndCredit(userId, lockExpendRequest.getGuideCredit());

            }
            if (null != lockExpendRequest.getStoreDeposit()) {
                if (identityType == 0 || identityType == 2) {
                    appStoreService.unlockStoreDepositByUserIdAndStoreDeposit(userId, lockExpendRequest.getStoreDeposit());
                }
            }
            if (null != lockExpendRequest.getStoreCredit() && identityType == 2) {

                appStoreService.unlockStoreCreditByUserIdAndCredit(userId, lockExpendRequest.getStoreCredit());

            }
            if (null != lockExpendRequest.getStoreSubvention() && identityType == 2) {

                appStoreService.unlockStoreSubventionByUserIdAndSubvention(userId, lockExpendRequest.getStoreSubvention());

            }
            if (null != lockExpendRequest.getLebiQty() && identityType == 6) {

                appCustomerService.unlockCustomerLebiByUserIdAndQty(userId, lockExpendRequest.getLebiQty());

            }
            if (null != lockExpendRequest.getStoreInventory() && !lockExpendRequest.getStoreInventory().isEmpty()) {

                appStoreService.unlockStoreInventoryByUserIdAndIdentityTypeAndInventory(userId, identityType, lockExpendRequest.getStoreInventory());

            }
            if (null != lockExpendRequest.getCityInventory() && !lockExpendRequest.getStoreInventory().isEmpty()) {

                cityService.unlockCityInventoryByUserIdAndIdentityTypeAndInventory(userId, identityType, lockExpendRequest.getCityInventory());

            }
            if (null != lockExpendRequest.getProductCoupons() && !lockExpendRequest.getProductCoupons().isEmpty() && identityType == 6) {

                appCustomerService.unlockCustomerProductCouponByUserIdAndProductCoupons(userId, lockExpendRequest.getProductCoupons());
            }
            if (null != lockExpendRequest.getCashCoupons() && !lockExpendRequest.getCashCoupons().isEmpty() && identityType == 6) {

                appCustomerService.unlockCustomerCashCouponByUserIdAndCashCoupons(userId, lockExpendRequest.getCashCoupons());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户释放订单相关款项和库存失败", null);
            logger.warn("unlockOrder EXCEPTION,用户释放订单相关款项和库存失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "用户锁定订单相关款项和库存成功！", null);
        logger.info("unlockOrder OUT,用户锁定订单相关款项和库存成功，出参 resultDTO:{}", resultDTO);
        return resultDTO;
    }

    /**
     * 用户获取订单列表
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @return 订单列表
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getOrderList(Long userID, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("getOrderList CALLED,用户获取订单列表，入参 userID:{}, identityType:{}", userID, identityType);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getOrderList OUT,用户获取订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getOrderList OUT,用户获取订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取用户所有订单列表
            List<OrderBaseInfo> orderBaseInfoList = appOrderService.getOrderListByUserIDAndIdentityType(userID, identityType);
            //创建有个存放图片地址的list
            List<String> goodsImgList = new ArrayList<>();
            //创建一个返回对象list
            List<OrderListResponse> orderListResponses = new ArrayList<>();
            //循环遍历订单列表
            for (OrderBaseInfo orderBaseInfo : orderBaseInfoList) {
                //创建一个返回类
                OrderListResponse orderListResponse = new OrderListResponse();
                //获取订单商品
                List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderBaseInfo.getOrderNumber());
                //遍历订单商品
                for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                    goodsImgList.add(goodsServiceImpl.queryBySku(orderGoodsInfo.getSku()).getCoverImageUri());
                }
                //计算剩余过期失效时间
                Long time = ((orderBaseInfo.getEffectiveEndTime().getTime()) - (new Date().getTime()));
                //设置
                if (time > 0){
                    orderListResponse.setEndTime(time);
                }
                orderListResponse.setOrderNo(orderBaseInfo.getOrderNumber());
                orderListResponse.setStatus(orderBaseInfo.getStatus());
                orderListResponse.setDeliveryType(orderBaseInfo.getDeliveryType().getValue());
                orderListResponse.setCount(appOrderService.querySumQtyByOrderNumber(orderBaseInfo.getOrderNumber()));
                orderListResponse.setPrice(appOrderService.getAmountPayableByOrderNumber(orderBaseInfo.getOrderNumber()));
                orderListResponse.setGoodsImgList(goodsImgList);
                //添加到返回类list中
                orderListResponses.add(orderListResponse);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, orderListResponses);
            logger.info("getOrderList OUT,用户获取订单列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户获取订单列表失败", null);
            logger.warn("getOrderList EXCEPTION,用户获取订单列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    private Double calculationCashCouponsDiscount(List<GoodsIdQtyParam> cashCouponsList, Long userId) {

        Double cashCouponDiscount = 0.00;
        for (GoodsIdQtyParam aCashCouponsList : cashCouponsList) {
            CashCouponResponse cashCoupon = appCustomerService.findCashCouponByCcIdAndUserIdAndQty(
                    aCashCouponsList.getId(), userId, aCashCouponsList.getQty());
            if (null != cashCoupon) {
                cashCouponDiscount = CountUtil.add(cashCouponDiscount, CountUtil.mul(cashCoupon.getDenomination(), aCashCouponsList.getQty()));
            }
        }
        return cashCouponDiscount;
    }

    private Map<String, Double> calculationProductCouponsDiscount(List<GoodsIdQtyParam> productCouponsList, Long userId,
                                                                  Double memberDiscount, Double totalOrderAmount) {
        Map<String, Double> returnMap;
        Double productCouponDiscount = 0.00;
        Double totalGoodsPrice = 0.00;
        for (GoodsIdQtyParam aProductCouponsList : productCouponsList) {
            GoodsPrice goodsPrice = goodsServiceImpl.findGoodsPriceByProductCouponIdAndUserId(userId, aProductCouponsList.getId(), aProductCouponsList.getQty());
            //算出产品券抵扣了多少会员折扣
            if (goodsPrice != null) {
                productCouponDiscount = CountUtil.add(productCouponDiscount, CountUtil.mul(aProductCouponsList.getQty(), CountUtil.sub(goodsPrice.getRetailPrice(), goodsPrice.getVIPPrice())));
                //算出抵扣了的商品总价
                totalGoodsPrice = CountUtil.add(totalGoodsPrice, CountUtil.mul(aProductCouponsList.getQty(), goodsPrice.getRetailPrice()));
            }
        }
        if (productCouponDiscount > memberDiscount || productCouponDiscount > totalOrderAmount) {
            return null;
        }
        Double memDiscount = CountUtil.sub(memberDiscount, productCouponDiscount);
        Double orderAmount = CountUtil.add(productCouponDiscount, CountUtil.sub(totalOrderAmount, totalGoodsPrice));

        returnMap = new HashMap<>(2);
        returnMap.put("memberDiscount", memDiscount);
        returnMap.put("totalOrderAmount", orderAmount);
        return returnMap;
    }
}
