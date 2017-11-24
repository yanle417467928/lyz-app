package cn.com.leyizhuang.app.web.controller.order;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingDetails;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderLogisticsInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.*;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.BillingSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.DeliverySimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.ProductCouponSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单相关接口
 *
 * @author Richard
 * Created on 2017-10-23 17:02
 **/
@RestController
@RequestMapping(value = "/app/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private AppCustomerService appCustomerService;

    @Resource
    private AppEmployeeService appEmployeeService;

    @Resource
    private AppStoreService appStoreService;

    @Resource
    private CityService cityService;

    @Resource
    private GoodsService goodsServiceImpl;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private ProductCouponService productCouponService;


    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> createOrder(OrderCreateParam orderParam) {
        logger.info("createOrder CALLED,去支付生成订单,入参 cityId:{},userId:{},identityType:{},customerId:{},goodsInfo:{}," +
                        " deliveryInfo:{},leBiQuantity:{},cashCouponIds:{},productCouponInfo:{},billingInfo:{}", orderParam.getCityId(),
                orderParam.getUserId(), orderParam.getIdentityType(), orderParam.getCustomerId(), orderParam.getGoodsInfo(),
                orderParam.getDeliveryInfo(), orderParam.getLeBiQuantity(), orderParam.getProductCouponInfo(), orderParam.getBillingInfo());
        ResultDTO<Object> resultDTO;
        if (null == orderParam.getCityId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市id不允许为空!", "");
            logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderParam.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不允许为空!", "");
            logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == orderParam.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份类型不允许为空!", "");
            logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderParam.getGoodsInfo())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品信息不允许为空!", "");
            logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderParam.getDeliveryInfo())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送信息不允许为空!", "");
            logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        //判断创单人身份是否合法
        if (!(orderParam.getIdentityType() == 0 || orderParam.getIdentityType() == 6 || orderParam.getIdentityType() == 2)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "创单人身份不合法!", "");
            logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JavaType goodsSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSimpleInfo.class);
        JavaType productCouponSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ProductCouponSimpleInfo.class);
        try {
            //转化前台提交过来的json类型参数
            List<GoodsSimpleInfo> goodsList = objectMapper.readValue(orderParam.getGoodsInfo(), goodsSimpleInfo);
            DeliverySimpleInfo deliverySimpleInfo = objectMapper.readValue(orderParam.getDeliveryInfo(), DeliverySimpleInfo.class);
            List<ProductCouponSimpleInfo> productCouponList = objectMapper.readValue(orderParam.getProductCouponInfo(), productCouponSimpleInfo);
            BillingSimpleInfo billing = objectMapper.readValue(orderParam.getBillingInfo(), BillingSimpleInfo.class);
            OrderBaseInfo tempOrder = new OrderBaseInfo();

            //*********************** 开始创建订单 **************************

            //设置订单创建时间
            Calendar calendar = Calendar.getInstance();
            tempOrder.setCreateTime(calendar.getTime());
            //设置订单过期时间
            calendar.add(Calendar.MINUTE, ApplicationConstant.ORDER_EFFECTIVE_MINUTE);
            tempOrder.setEffectiveEndTime(calendar.getTime());
            //设置订单状态
            tempOrder.setStatus(AppOrderStatus.UNPAID);
            //设置订单类型 买券、出货
            tempOrder.setOrderType(AppOrderType.SHIPMENT);
            //生成并设置订单号
            String orderNumber = OrderUtils.generateOrderNumber(orderParam.getCityId());
            tempOrder.setOrderNumber(orderNumber);
            //设置订单配送方式
            if (deliverySimpleInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.HOUSE_DELIVERY.getValue())) {
                tempOrder.setDeliveryType(AppDeliveryType.HOUSE_DELIVERY);
            } else if (deliverySimpleInfo.getDeliveryType().equalsIgnoreCase(AppDeliveryType.SELF_TAKE.getValue())) {
                tempOrder.setDeliveryType(AppDeliveryType.SELF_TAKE);
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送方式不合法!", "");
                logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //设置下单人所在门店信息
            AppStore userStore = appStoreService.findStoreByUserIdAndIdentityType(orderParam.getUserId(), orderParam.getIdentityType());
            tempOrder.setStoreId(userStore.getStoreId());
            tempOrder.setStoreCode(userStore.getStoreCode());
            tempOrder.setStoreStructureCode(userStore.getStoreStructureCode());

            switch (orderParam.getIdentityType()) {
                //导购代下单
                case 0:
                    tempOrder.setOrderSubjectType(AppOrderSubjectType.STORE);
                    tempOrder.setCreatorIdentityType(AppIdentityType.SELLER);
                    AppEmployee employee = appEmployeeService.findById(orderParam.getUserId());
                    tempOrder.setSalesConsultId(employee.getEmpId());
                    tempOrder.setSalesConsultName(employee.getName());
                    tempOrder.setSalesConsultPhone(employee.getMobile());
                    AppCustomer customer = appCustomerService.findById(orderParam.getCustomerId());
                    tempOrder.setCustomerId(customer.getCusId());
                    tempOrder.setCustomerName(customer.getName());
                    tempOrder.setCustomerPhone(customer.getMobile());
                    break;
                //顾客下单
                case 6:
                    tempOrder.setOrderSubjectType(AppOrderSubjectType.STORE);
                    tempOrder.setCreatorIdentityType(AppIdentityType.CUSTOMER);
                    AppCustomer appCustomer  = appCustomerService.findById(orderParam.getUserId());
                    if (null != appCustomer.getSalesConsultId()){
                        AppEmployee seller = appEmployeeService.findById(appCustomer.getSalesConsultId());
                        tempOrder.setSalesConsultId(seller.getEmpId());
                        tempOrder.setSalesConsultName(seller.getName());
                        tempOrder.setSalesConsultPhone(seller.getMobile());
                    }

                    break;
                //装饰公司经理下单
                case 2:
                    tempOrder.setOrderSubjectType(AppOrderSubjectType.FIT);
                    tempOrder.setCreatorIdentityType(AppIdentityType.DECORATE_MANAGER);
                    break;
                default:
                    break;
            }


            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
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
     *
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
            Double totalOrderAmount = 0.00;
            List<Long> goodsIds = new ArrayList<Long>();
            List<Long> giftIds = new ArrayList<>();
            List<OrderGoodsSimpleResponse> goodsInfo = null;
            List<OrderGoodsSimpleResponse> giftsInfo = null;
            List<CashCouponResponse> cashCouponResponseList = null;
            Map<String, Object> goodsSettlement = new HashMap<>();


            if (identityType == 6) {
                AppCustomer customer = appCustomerService.findById(userId);
                Long cityId = customer.getCityId();
                for (int i = 0; i < goodsList.size(); i++) {
                    if (!goodsList.get(i).getIsGift()) {
                        goodsIds.add(goodsList.get(i).getId());
                    } else {
                        giftIds.add(goodsList.get(i).getId());
                    }
                    //获取商品总数
                    totalQty = totalQty + goodsList.get(i).getNum();
                }
                //获取商品信息
                goodsInfo = goodsServiceImpl.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIds);
                //获取赠品信息
                giftsInfo = goodsServiceImpl.findGoodsListByCustomerIdAndGoodsIdList(userId, giftIds);
                //正品的数量和标识这里需要判断库存的特殊处理（所以不能和赠品的集合加在一起循环）
                int goodsTotalQty = 0;
                for (int i = 0; i < goodsInfo.size(); i++) {
                    for (int j = 0; j < goodsList.size(); j++) {
                        OrderGoodsSimpleResponse info = goodsInfo.get(i);
                        GoodsSimpleInfo simpleInfo = goodsList.get(j);
                        if (info.getId().equals(simpleInfo.getId())) {
                            //如果是赠品则加上赠品数量后面判断库存
                            if (simpleInfo.getIsGift()) {
                                goodsTotalQty = info.getGoodsQty() + simpleInfo.getNum();
                            } else {
                                info.setIsGift(Boolean.FALSE);
                                //先获取本品数量
                                info.setGoodsQty(simpleInfo.getNum());
                                goodsTotalQty = simpleInfo.getNum();
                                //可以算出总金额
                                totalPrice = CountUtil.add(totalPrice, CountUtil.mul(info.getRetailPrice(), simpleInfo.getNum()));
                                if (null != customer.getSalesConsultId()) {
                                    memberDiscount = CountUtil.mul(CountUtil.sub(info.getRetailPrice(), info.getVipPrice()), goodsList.get(j).getNum());
                                }
                            }
                            //判断库存
                            Boolean isHaveInventory = appOrderService.existGoodsCityInventory(cityId, info.getId(), goodsTotalQty);
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
                //TODO 根据促销减去订单折扣, 加运费
                totalOrderAmount = CountUtil.add(CountUtil.sub(totalPrice, memberDiscount, totalPrice / 100), totalPrice / 1000);
                //计算顾客乐币
                CustomerLeBi leBi = appCustomerService.findLeBiByUserIdAndGoodsMoney(userId, totalOrderAmount);
                //计算可用的优惠券
                cashCouponResponseList = appCustomerService.findCashCouponUseableByCustomerId(userId, totalOrderAmount);
                //查询顾客预存款
                Double preDeposit = appCustomerService.findPreDepositBalanceByUserIdAndIdentityType(userId, identityType);
                //赠品的数量和标识
                if (giftsInfo != null) {
                    for (int i = 0; i < giftsInfo.size(); i++) {
                        for (int j = 0; j < goodsList.size(); j++) {
                            if (giftsInfo.get(i).getId().equals(goodsList.get(j).getId())) {
                                if (goodsList.get(j).getIsGift()) {
                                    giftsInfo.get(i).setGoodsQty(goodsList.get(j).getNum());
                                    giftsInfo.get(i).setIsGift(Boolean.TRUE);
                                }
                            }
                        }
                    }
                    //合并商品和赠品集合
                    goodsInfo.addAll(giftsInfo);
                }
                goodsSettlement.put("totalQty", totalQty);
                goodsSettlement.put("totalPrice", totalPrice);
                goodsSettlement.put("totalGoodsInfo", goodsInfo);
                goodsSettlement.put("memberDiscount", memberDiscount);
                // TODO 会员折扣在创建促销表后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("orderDiscount", memberDiscount * 10);
                // TODO 运费再出算法后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("freight", memberDiscount / 10);
                goodsSettlement.put("totalOrderAmount", totalOrderAmount);
                goodsSettlement.put("leBi", leBi);
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
                    } else {
                        giftIds.add(goodsList.get(i).getId());
                    }
                    //获取商品总数
                    totalQty = totalQty + goodsList.get(i).getNum();
                }
                goodsInfo = goodsServiceImpl.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);
                giftsInfo = goodsServiceImpl.findGoodsListByEmployeeIdAndGoodsIdList(userId, giftIds);

                int goodsTotalQty = 0;
                for (int i = 0; i < goodsInfo.size(); i++) {
                    for (int j = 0; j < goodsList.size(); j++) {
                        OrderGoodsSimpleResponse info = goodsInfo.get(i);
                        GoodsSimpleInfo simpleInfo = goodsList.get(j);
                        if (info.getId().equals(simpleInfo.getId())) {
                            //如果是赠品则加上赠品数量后面判断库存
                            if (simpleInfo.getIsGift()) {
                                goodsTotalQty = info.getGoodsQty() + simpleInfo.getNum();
                            } else {
                                info.setIsGift(Boolean.FALSE);
                                //先获取本品数量
                                info.setGoodsQty(simpleInfo.getNum());
                                goodsTotalQty = simpleInfo.getNum();
                                //可以算出总金额
                                totalPrice = CountUtil.add(totalPrice, CountUtil.mul(info.getRetailPrice(), simpleInfo.getNum()));
                                memberDiscount = CountUtil.mul(CountUtil.sub(info.getRetailPrice(), info.getVipPrice()), goodsList.get(j).getNum());
                            }
                            //判断库存
                            Boolean isHaveInventory = appOrderService.existGoodsStoreInventory(storeId, info.getId(), goodsTotalQty);
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
                //现金券还需要传入订单金额判断是否满减
                cashCouponResponseList = appCustomerService.findCashCouponUseableByCustomerId(customerId, totalOrderAmount);
                //查询导购预存款和信用金
                SellerCreditMoney sellerCreditMoney = appEmployeeService.findCreditMoneyBalanceByUserIdAndIdentityType(userId, identityType);
                Double creditMoney = sellerCreditMoney.getAvailableBalance();
                //导购门店预存款
                Double storePreDeposit = appStoreService.findPreDepositBalanceByUserId(userId);

                //赠品的数量和标识
                if (giftsInfo != null) {
                    for (int i = 0; i < giftsInfo.size(); i++) {
                        for (int j = 0; j < goodsList.size(); j++) {
                            if (giftsInfo.get(i).getId().equals(goodsList.get(j).getId())) {
                                if (goodsList.get(j).getIsGift()) {
                                    giftsInfo.get(i).setGoodsQty(goodsList.get(j).getNum());
                                    giftsInfo.get(i).setIsGift(Boolean.TRUE);
                                }
                            }
                        }
                    }
                    goodsInfo.addAll(giftsInfo);
                }
                goodsSettlement.put("totalQty", totalQty);
                goodsSettlement.put("totalPrice", totalPrice);
                goodsSettlement.put("totalGoodsInfo", goodsInfo);
                goodsSettlement.put("memberDiscount", memberDiscount);
                // TODO 会员折扣在创建促销表后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("orderDiscount", memberDiscount * 10);
                // TODO 运费再出算法后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("freight", memberDiscount / 10);
                goodsSettlement.put("totalOrderAmount", totalOrderAmount);
                goodsSettlement.put("leBi", leBi);
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
                    } else {
                        giftIds.add(goodsList.get(i).getId());
                    }
                    //获取商品总数
                    totalQty = totalQty + goodsList.get(i).getNum();
                }
                goodsInfo = goodsServiceImpl.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);

                giftsInfo = goodsServiceImpl.findGoodsListByEmployeeIdAndGoodsIdList(userId, giftIds);

                int goodsTotalQty = 0;
                for (int i = 0; i < goodsInfo.size(); i++) {
                    for (int j = 0; j < goodsList.size(); j++) {
                        OrderGoodsSimpleResponse info = goodsInfo.get(i);
                        GoodsSimpleInfo simpleInfo = goodsList.get(j);
                        if (info.getId().equals(simpleInfo.getId())) {
                            //如果是赠品则加上赠品数量后面判断库存
                            if (simpleInfo.getIsGift()) {
                                goodsTotalQty = info.getGoodsQty() + simpleInfo.getNum();
                            } else {
                                info.setIsGift(Boolean.FALSE);
                                //先获取本品数量
                                info.setGoodsQty(simpleInfo.getNum());
                                goodsTotalQty = simpleInfo.getNum();
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


                //赠品的数量和标识
                if (giftsInfo != null) {
                    for (int i = 0; i < giftsInfo.size(); i++) {
                        for (int j = 0; j < goodsList.size(); j++) {
                            if (giftsInfo.get(i).getId().equals(goodsList.get(j).getId())) {
                                if (goodsList.get(j).getIsGift()) {
                                    giftsInfo.get(i).setGoodsQty(goodsList.get(j).getNum());
                                    giftsInfo.get(i).setIsGift(Boolean.TRUE);
                                }
                            }
                        }
                    }
                    goodsInfo.addAll(giftsInfo);
                }
                goodsSettlement.put("totalQty", totalQty);
                goodsSettlement.put("totalPrice", totalPrice);
                goodsSettlement.put("totalGoodsInfo", goodsInfo);
                // TODO 会员折扣在创建促销表后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("orderDiscount", totalPrice / 100);
                // TODO 运费再出算法后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("freight", totalPrice / 1000);
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
     * @param usedCouponRequest 使用现金券变更金额所需判断参数
     * @return
     */
    @PostMapping(value = "/reEnter/ccp", produces = "application/json;charset=UTF-8")
    public ResultDTO reEnterOrderByCashCoupon(@RequestBody UsedCouponRequest usedCouponRequest) {

        logger.info("reEnterOrderByCashCoupon CALLED,通过现金券来重新计算确认订单，入参 usedCouponRequest:{}", usedCouponRequest);

        ResultDTO resultDTO;
        if (null == usedCouponRequest.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == usedCouponRequest.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == usedCouponRequest.getTotalOrderAmount()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单小计不能为空", null);
            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == usedCouponRequest.getLeBi()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客乐币不能为空", null);
            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        //计算张数
        int index = 0;
        //优惠券折扣
        Double cashCouponDiscount = 0.00;
        //返回数据的容器
        Map<String, Object> returnMap = new HashMap(3);
        Long userId = usedCouponRequest.getUserId();
        Integer identityType = usedCouponRequest.getIdentityType();
        Double totalOrderAmount = usedCouponRequest.getTotalOrderAmount();
        //如果顾客没有选券，直接返回传入的数值不必再计算
        if (usedCouponRequest.getCouponsList().isEmpty()) {
            returnMap.put("leBi", usedCouponRequest.getLeBi());
            returnMap.put("totalOrderAmount", totalOrderAmount);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, returnMap);
            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单成功，出参 resultDTO:{}", resultDTO);
        }
        List<GoodsIdQtyParam> cashCouponsList = usedCouponRequest.getCouponsList();
        CustomerLeBi leBi = null;
        try {
            //只有顾客和导购身份可进来
            if (identityType == 6 || identityType == 0) {
                if (identityType == 0) {
                    //导购代下单顾客身份不能空
                    if (null == usedCouponRequest.getCustomerId()) {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "代下单顾客id不能为空", null);
                        logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                    userId = usedCouponRequest.getCustomerId();
                }
                //遍历产品券列表
                for (GoodsIdQtyParam aCashCouponsList : cashCouponsList) {
                    //根据券ID 去查产品券
                    CashCouponResponse cashCoupon = appCustomerService.findCashCouponByCcIdAndUserIdAndQty(
                            aCashCouponsList.getId(), userId, aCashCouponsList.getQty());
                    if (null != cashCoupon) {
                        //如果当前小计满足第一张券的满减条件就减去优惠券的折扣,循环判断
                        if (totalOrderAmount >= cashCoupon.getCondition()) {
                            Double couponDiscount = CountUtil.mul(cashCoupon.getDenomination(), aCashCouponsList.getQty());
                            cashCouponDiscount = CountUtil.add(cashCouponDiscount, couponDiscount);
                            totalOrderAmount = CountUtil.sub(totalOrderAmount, couponDiscount);
                            index++;
                        } else {
                            //直到如有使用过多的券，返回最多可使用index张券
                            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "您最多使用" + index + "张优惠券", null);
                            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
                            return resultDTO;
                        }
                    } else {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未找到产品券或者该产品券已过期！", null);
                        logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
                //计算顾客乐币
                leBi = appCustomerService.findLeBiByUserIdAndGoodsMoney(userId, totalOrderAmount);
            }
            returnMap.put("leBi", leBi);
            returnMap.put("totalOrderAmount", totalOrderAmount);
            returnMap.put("couponDiscount", cashCouponDiscount);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, returnMap);
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
     * @param usedCouponRequest 使用产品券变更金额所需判断参数
     * @return
     */
    @Deprecated
    @PostMapping(value = "/reEnter/pcp", produces = "application/json;charset=UTF-8")
    public ResultDTO reEnterOrderByProductCoupon(@RequestBody UsedCouponRequest usedCouponRequest) {
        logger.info("reEnterOrderByProductCoupon CALLED,通过产品券来重新计算确认订单，入参 usedCashCouponReq:{}", usedCouponRequest);

        ResultDTO resultDTO;
        if (usedCouponRequest.getCouponsList().isEmpty()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "所选现金券不能为空", null);
            logger.info("reEnterOrderByProductCoupon OUT,通过产品券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == usedCouponRequest.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("reEnterOrderByProductCoupon OUT,通过产品券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == usedCouponRequest.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("reEnterOrderByProductCoupon OUT,通过产品券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Long userId = usedCouponRequest.getUserId();
        Integer identityType = usedCouponRequest.getIdentityType();
        List<GoodsIdQtyParam> productCouponsList = usedCouponRequest.getCouponsList();
        Double productCouponDiscount = 0.00;
        try {
            if (identityType == 6) {
                productCouponDiscount = calculationProductCouponsDiscount(productCouponsList, userId);
            }
            if (identityType == 0) {
                if (null == usedCouponRequest.getCustomerId()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "代下单顾客id不能为空", null);
                    logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                Long cusId = usedCouponRequest.getCustomerId();
                productCouponDiscount = calculationProductCouponsDiscount(productCouponsList, cusId);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, productCouponDiscount);
            logger.info("reEnterOrderByProductCoupon OUT,通过产品券来重新计算确认订单成功，出参 resultDTO:{}", productCouponDiscount);
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
            //创建一个返回对象list
            List<OrderListResponse> orderListResponses = new ArrayList<>();
            //循环遍历订单列表
            for (OrderBaseInfo orderBaseInfo : orderBaseInfoList) {
                //创建有个存放图片地址的list
                List<String> goodsImgList = new ArrayList<>();
                //创建一个返回类
                OrderListResponse orderListResponse = new OrderListResponse();
                //获取订单商品
                List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderBaseInfo.getOrderNumber());
                //遍历订单商品
                for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                    goodsImgList.add(goodsServiceImpl.queryBySku(orderGoodsInfo.getSku()).getCoverImageUri());
                }
                if ("待付款".equals(orderBaseInfo.getStatus())) {
                    //计算剩余过期失效时间
                    Long time = ((orderBaseInfo.getEffectiveEndTime().getTime()) - (System.currentTimeMillis()));
                    //设置
                    if (time > 0) {
                        orderListResponse.setEndTime(time);
                    }
                }
                orderListResponse.setOrderNo(orderBaseInfo.getOrderNumber());
                orderListResponse.setStatus(orderBaseInfo.getStatus().getValue());
                orderListResponse.setDeliveryType(orderBaseInfo.getDeliveryType().getValue());
                orderListResponse.setDeliveryType(orderBaseInfo.getDeliveryType().getDescription());
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

    /**
     * 模糊查询订单
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @param condition    查询条件
     * @return 订单列表
     */
    @PostMapping(value = "/search", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getFuzzyQuery(Long userID, Integer identityType, String condition) {
        ResultDTO<Object> resultDTO;
        logger.info("getFuzzyQuery CALLED,模糊查询订单列表，入参 userID:{}, identityType:{}, condition:{}", userID, identityType, condition);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getFuzzyQuery OUT,模糊查询订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getFuzzyQuery OUT,模糊查询订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(condition)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "查询条件不能为空！", null);
            logger.info("getFuzzyQuery OUT,模糊查询订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取符合条件所有订单列表
            List<OrderBaseInfo> orderBaseInfoList = appOrderService.getFuzzyQuery(userID, identityType, condition);
            //创建一个返回对象list
            List<OrderListResponse> orderListResponses = new ArrayList<>();
            //循环遍历订单列表
            for (OrderBaseInfo orderBaseInfo : orderBaseInfoList) {
                //创建有个存放图片地址的list
                List<String> goodsImgList = new ArrayList<>();
                //创建一个返回类
                OrderListResponse orderListResponse = new OrderListResponse();
                //获取订单商品
                List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderBaseInfo.getOrderNumber());
                //遍历订单商品
                for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                    goodsImgList.add(goodsServiceImpl.queryBySku(orderGoodsInfo.getSku()).getCoverImageUri());
                }
                if ("待付款".equals(orderBaseInfo.getStatus())) {
                    //计算剩余过期失效时间
                    Long time = ((orderBaseInfo.getEffectiveEndTime().getTime()) - (System.currentTimeMillis()));
                    //设置
                    if (time > 0) {
                        orderListResponse.setEndTime(time);
                    }
                }
                orderListResponse.setOrderNo(orderBaseInfo.getOrderNumber());
                orderListResponse.setStatus(orderBaseInfo.getStatus().getValue());
                orderListResponse.setDeliveryType(orderBaseInfo.getDeliveryType().getValue());
                orderListResponse.setCount(appOrderService.querySumQtyByOrderNumber(orderBaseInfo.getOrderNumber()));
                orderListResponse.setPrice(appOrderService.getAmountPayableByOrderNumber(orderBaseInfo.getOrderNumber()));
                orderListResponse.setGoodsImgList(goodsImgList);
                //添加到返回类list中
                orderListResponses.add(orderListResponse);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, orderListResponses);
            logger.info("getFuzzyQuery OUT,模糊查询订单列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，模糊查询订单列表失败", null);
            logger.warn("getFuzzyQuery EXCEPTION,模糊查询订单列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取订单详情
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @param orderNumber  订单号
     * @return 订单详情
     */
    @PostMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getOrderDetail(Long userID, Integer identityType, String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("getOrderDetail CALLED,用户获取订单详情，入参 userID:{}, identityType:{}, identityType:{}", userID, identityType, orderNumber);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getOrderDetail OUT,用户获取订单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getOrderDetail OUT,用户获取订单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("getOrderDetail OUT,用户获取订单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取订单详情
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderDetail(orderNumber);
            if (null != orderBaseInfo) {
                //获取订单收货/自提门店地址
                OrderLogisticsInfo orderLogisticsInfo = appOrderService.getOrderLogistice(orderNumber);
                //创建返回对象
                OrderDetailsResponse orderDetailsResponse = new OrderDetailsResponse();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //设值
                orderDetailsResponse.setOrderNumber(orderNumber);
                orderDetailsResponse.setCreateTime(sdf.format(orderBaseInfo.getCreateTime()));
                orderDetailsResponse.setStatus(orderBaseInfo.getStatus().getValue());
                orderDetailsResponse.setPayType(orderBaseInfo.getOnlinePayType().getDescription());
                orderDetailsResponse.setDeliveryType(orderBaseInfo.getDeliveryType().getDescription());
                //根据不同的配送方式进行设值
                if ("门店自提".equals(orderBaseInfo.getDeliveryType().getValue())) {
                    orderDetailsResponse.setBookingStoreName(orderLogisticsInfo.getBookingStoreName());
                    orderDetailsResponse.setBookingTime(orderLogisticsInfo.getBookingTime());
                    AppStore appStore = appStoreService.findByStoreCode(orderLogisticsInfo.getBookingStoreCode());
                    orderDetailsResponse.setBookingStorePhone(appStore.getPhone());
                    orderDetailsResponse.setStoreDetailedAddress(appStore.getDetailedAddress());
                } else {
                    orderDetailsResponse.setDeliveryTime(orderLogisticsInfo.getDeliveryTime());
                    orderDetailsResponse.setReceiver(orderLogisticsInfo.getReceiver());
                    orderDetailsResponse.setReceiverPhone(orderLogisticsInfo.getReceiverPhone());
                    orderDetailsResponse.setShippingAddress(orderLogisticsInfo.getShippingAddress());
                }
                //获取订单账目明细
                OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderNumber);
                //根据不同的身份返回对应的账目明细
                if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.CUSTOMER)) {
                    //会员
                    CustomerBillingDetailResponse customerBillingDetailResponse = new CustomerBillingDetailResponse();
                    customerBillingDetailResponse.setAmountPayable(orderBillingDetails.getAmountPayable());
                    customerBillingDetailResponse.setCouponDiscount(orderBillingDetails.getCouponDiscount());
                    customerBillingDetailResponse.setFreight(orderBillingDetails.getFreight());
                    customerBillingDetailResponse.setLeBiCashDiscount(orderBillingDetails.getLeBiCashDiscount());
                    customerBillingDetailResponse.setMemberDiscount(orderBillingDetails.getMemberDiscount());
                    customerBillingDetailResponse.setPreDeposit(orderBillingDetails.getPreDeposit());
                    customerBillingDetailResponse.setProductCouponDiscount(orderBillingDetails.getProductCouponDiscount());
                    customerBillingDetailResponse.setPromotionDiscount(orderBillingDetails.getPromotionDiscount());
                    customerBillingDetailResponse.setTotalPrice(orderBaseInfo.getTotalGoodsPrice());

                    orderDetailsResponse.setCustomerBillingDetailResponse(customerBillingDetailResponse);
                } else if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.DECORATE_MANAGER)) {
                    //经理
                    ManagerBillingDetailResponse managerBillingDetailResponse = new ManagerBillingDetailResponse();
                    managerBillingDetailResponse.setAmountPayable(orderBillingDetails.getAmountPayable());
                    managerBillingDetailResponse.setCouponDiscount(orderBillingDetails.getCouponDiscount());
                    managerBillingDetailResponse.setFreight(orderBillingDetails.getFreight());
                    managerBillingDetailResponse.setMemberDiscount(orderBillingDetails.getMemberDiscount());
                    managerBillingDetailResponse.setSubvention(orderBillingDetails.getSubvention());
                    managerBillingDetailResponse.setProductCouponDiscount(orderBillingDetails.getProductCouponDiscount());
                    managerBillingDetailResponse.setPreDeposit(orderBillingDetails.getPreDeposit());
                    managerBillingDetailResponse.setCreditMoney(orderBillingDetails.getCreditMoney());
                    managerBillingDetailResponse.setPromotionDiscount(orderBillingDetails.getPromotionDiscount());
                    managerBillingDetailResponse.setTotalPrice(orderBaseInfo.getTotalGoodsPrice());

                    orderDetailsResponse.setManagerBillingDetailResponse(managerBillingDetailResponse);
                } else {
                    //导购
                    SellerBillingDetailResponse sellerBillingDetailResponse = new SellerBillingDetailResponse();
                    sellerBillingDetailResponse.setAmountPayable(orderBillingDetails.getAmountPayable());
                    sellerBillingDetailResponse.setCouponDiscount(orderBillingDetails.getCouponDiscount());
                    sellerBillingDetailResponse.setCreditMoney(orderBillingDetails.getCreditMoney());
                    sellerBillingDetailResponse.setFreight(orderBillingDetails.getFreight());
                    sellerBillingDetailResponse.setMemberDiscount(orderBillingDetails.getMemberDiscount());
                    sellerBillingDetailResponse.setPreDeposit(orderBillingDetails.getPreDeposit());
                    sellerBillingDetailResponse.setProductCouponDiscount(orderBillingDetails.getProductCouponDiscount());
                    sellerBillingDetailResponse.setPromotionDiscount(orderBillingDetails.getPromotionDiscount());
                    sellerBillingDetailResponse.setTotalPrice(orderBaseInfo.getTotalGoodsPrice());

                    orderDetailsResponse.setSellerBillingDetailResponse(sellerBillingDetailResponse);
                }
                orderDetailsResponse.setGoodsList(appOrderService.getOrderGoodsDetails(orderNumber));

                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, orderDetailsResponse);
                logger.info("getOrderDetail OUT,用户获取订单详情成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此订单！", null);
            logger.info("getOrderDetail OUT,用户获取订单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户获取订单详情失败", null);
            logger.warn("getOrderDetail EXCEPTION,用户获取订单详情失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    @Deprecated
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

    private Double calculationProductCouponsDiscount(List<GoodsIdQtyParam> productCouponsList, Long userId) {
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
        //选择了产品券影响了会员折扣，说有在优惠折扣中减去会员折扣
        return CountUtil.sub(totalGoodsPrice, productCouponDiscount);
    }
}
