package cn.com.leyizhuang.app.web.controller.order;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.core.utils.IpUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.pojo.OrderEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.*;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.*;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.OrderGoodsVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.CountUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private GoodsService goodsService;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private ProductCouponService productCouponService;

    @Resource
    private CommonService commonService;

    @Resource
    private CityService cityService;

    @Resource
    private AppActService actService;

    @Resource
    private AppActDutchService dutchService;

    @Resource
    private OrderEvaluationService orderEvaluationService;

    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> createOrder(OrderCreateParam orderParam, HttpServletRequest request) {
        logger.info("createOrder CALLED,去支付生成订单,入参:{}", JSON.toJSONString(orderParam));
        System.out.println(JSON.toJSONString(orderParam));
        ResultDTO<Object> resultDTO;
        //获取客户端ip地址
        String ipAddress = IpUtils.getIpAddress(request);
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

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            JavaType goodsSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSimpleInfo.class);
            JavaType cashCouponSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
            JavaType productCouponSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ProductCouponSimpleInfo.class);
            JavaType promotionSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, PromotionSimpleInfo.class);

            //************** 转化前台提交过来的json类型参数 ***************

            //商品信息
            List<GoodsSimpleInfo> goodsList = objectMapper.readValue(orderParam.getGoodsInfo(), goodsSimpleInfo);
            //配送信息
            DeliverySimpleInfo deliverySimpleInfo = objectMapper.readValue(orderParam.getDeliveryInfo(), DeliverySimpleInfo.class);
            if (StringUtils.isBlank(deliverySimpleInfo.getDeliveryType())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送方式不允许为空!", "");
                logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //优惠券信息
            List<Long> cashCouponList = objectMapper.readValue(orderParam.getCashCouponIds(),cashCouponSimpleInfo);
            //产品券信息
            List<ProductCouponSimpleInfo> productCouponList = objectMapper.readValue(orderParam.getProductCouponInfo(), productCouponSimpleInfo);
            //促销信息
            List<PromotionSimpleInfo> promotionSimpleInfoList = objectMapper.readValue(orderParam.getPromotionInfo(), promotionSimpleInfo);
            //账单信息
            BillingSimpleInfo billing = objectMapper.readValue(orderParam.getBillingInfo(), BillingSimpleInfo.class);


            //*********************** 开始创建订单 **************************

            //***** 创建订单基础信息 *****
            OrderBaseInfo orderBaseInfo = appOrderService.createOrderBaseInfo(orderParam.getCityId(), orderParam.getUserId(),
                    orderParam.getIdentityType(), orderParam.getCustomerId(), deliverySimpleInfo.getDeliveryType(), orderParam.getRemark());
            orderBaseInfo.setOrderType(AppOrderType.SHIPMENT);


            //***** 创建订单物流信息 *****
            OrderLogisticsInfo orderLogisticsInfo = appOrderService.createOrderLogisticInfo(deliverySimpleInfo);
            orderLogisticsInfo.setOrdNo(orderBaseInfo.getOrderNumber());

            //***** 创建订单商品信息 *****
            List<OrderGoodsInfo> orderGoodsInfoList = new ArrayList<>();
            //定义订单商品零售总价
            Double goodsTotalPrice = 0D;
            //定义订单会员折扣
            Double memberDiscount = 0D;
            //定义订单促销折扣
            Double promotionDiscount = 0D;
            //获取商品id集合
            Set<Long> goodsIdSet = new HashSet<>();
            for (GoodsSimpleInfo goods : goodsList) {
                goodsIdSet.add(goods.getId());
            }
            //新建一个map,用来存放最终要检核库存的商品和商品数量
            Map<Long, Integer> inventoryCheckMap = new HashMap<>();
            List<OrderGoodsVO> goodsVOList = goodsService.findOrderGoodsVOListByUserIdAndIdentityTypeAndGoodsIds(
                    orderParam.getUserId(), orderParam.getIdentityType(), goodsIdSet);
            //设置商品数量
            Set<Long> hasPriceGoodsIdSet = new HashSet<>();
            AppStore store = appStoreService.findStoreByUserIdAndIdentityType(orderParam.getUserId(), orderParam.getIdentityType());
            AppCustomer customer = new AppCustomer();
            if (orderParam.getIdentityType() == AppIdentityType.CUSTOMER.getValue()) {
                customer = appCustomerService.findById(orderParam.getUserId());
            } else if (orderParam.getIdentityType() == AppIdentityType.SELLER.getValue()) {
                customer = appCustomerService.findById(orderParam.getCustomerId());
            }
            for (OrderGoodsVO goodsVO : goodsVOList) {
                if (hasPriceGoodsIdSet.contains(goodsVO.getGid())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品 '" + goodsVO.getSkuName() + "'在当前门店下存在多个价格!",
                            null);
                    logger.warn("createOrder OUT,订单创建失败,出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                    //throw new RuntimeException("商品 '" + goodsVO.getSkuName() + "'在当前门店下存在多个价格!");
                } else {
                    hasPriceGoodsIdSet.add(goodsVO.getGid());
                }
                for (GoodsSimpleInfo info : goodsList) {
                    if (info.getId().equals(goodsVO.getGid())) {
                        goodsVO.setQty(info.getQty());
                    }
                }
                //加总商品零售价总额
                goodsTotalPrice += goodsVO.getRetailPrice() * goodsVO.getQty();
                if (inventoryCheckMap.containsKey(goodsVO.getGid())) {
                    inventoryCheckMap.put(goodsVO.getGid(), inventoryCheckMap.get(goodsVO.getGid()) + goodsVO.getQty());
                } else {
                    inventoryCheckMap.put(goodsVO.getGid(), goodsVO.getQty());
                }
                if (orderParam.getIdentityType() == AppIdentityType.DECORATE_MANAGER.getValue()) {
                    memberDiscount += (goodsVO.getRetailPrice() - goodsVO.getVipPrice()) * goodsVO.getQty();
                } else {
                    if (null == customer) {
                        throw new OrderCustomerException("订单顾客信息异常!");
                    }
                    if (customer.getCustomerType() == AppCustomerType.MEMBER) {
                        memberDiscount += (goodsVO.getRetailPrice() - goodsVO.getVipPrice()) * goodsVO.getQty();
                    } else {
                        memberDiscount = 0D;
                    }
                }

                OrderGoodsInfo goodsInfo = new OrderGoodsInfo();
                goodsInfo.setOrderNumber(orderBaseInfo.getOrderNumber());
                goodsInfo.setGoodsLineType(AppGoodsLineType.GOODS);
                goodsInfo.setRetailPrice(goodsVO.getRetailPrice());
                goodsInfo.setVIPPrice(goodsVO.getVipPrice());
                goodsInfo.setWholesalePrice(goodsVO.getWholesalePrice());
                goodsInfo.setIsPriceShare(Boolean.FALSE);
                goodsInfo.setSharePrice(0D);
                goodsInfo.setIsReturnable(Boolean.TRUE);
                if (orderParam.getIdentityType() == AppIdentityType.DECORATE_MANAGER.getValue()) {
                    goodsInfo.setReturnPrice(goodsVO.getVipPrice());
                } else {
                    if (null == customer) {
                        throw new OrderCustomerException("订单顾客信息异常!");
                    }
                    if (customer.getCustomerType() == AppCustomerType.MEMBER) {
                        goodsInfo.setReturnPrice(goodsVO.getVipPrice());
                    } else {
                        goodsInfo.setReturnPrice(goodsVO.getRetailPrice());
                    }
                }
                goodsInfo.setSku(goodsVO.getSku());
                goodsInfo.setSkuName(goodsVO.getSkuName());
                goodsInfo.setOrderQuantity(goodsVO.getQty());
                goodsInfo.setShippingQuantity(0);
                goodsInfo.setReturnableQuantity(goodsVO.getQty());
                goodsInfo.setReturnQuantity(0);
                goodsInfo.setReturnPriority(1);
                goodsInfo.setPriceItemId(goodsVO.getPrice_item_id());
                goodsInfo.setCompanyFlag(goodsVO.getCompanyFlag());
                orderGoodsInfoList.add(goodsInfo);
            }
            if (goodsIdSet.size() != hasPriceGoodsIdSet.size()) {
                StringBuilder ids = new StringBuilder();
                for (Long id : goodsIdSet) {
                    if (!hasPriceGoodsIdSet.contains(id)) {
                        ids.append(id).append(",");
                    }
                }
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "id为 '" + ids + "'的商品在当前门店下没有找到价格!",
                        null);
                logger.warn("createOrder OUT,订单创建失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
                //throw new RuntimeException("id为 '" + ids + "'的商品在当前门店下没有找到价格!");
            }

            //********* 处理订单账务相关信息 *********
            OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
            orderBillingDetails.setOrderNumber(orderBaseInfo.getOrderNumber());
            orderBillingDetails.setIsOwnerReceiving(orderLogisticsInfo.getIsOwnerReceiving());
            orderBillingDetails.setCreateTime(Calendar.getInstance().getTime());
            orderBillingDetails.setTotalGoodsPrice(goodsTotalPrice);
            orderBillingDetails.setMemberDiscount(memberDiscount);
            orderBillingDetails.setPromotionDiscount(promotionDiscount);
            orderBillingDetails.setIsOwnerReceiving(orderLogisticsInfo.getIsOwnerReceiving());
            orderBillingDetails = appOrderService.createOrderBillingDetails(orderBillingDetails, orderParam.getUserId(), orderParam.getIdentityType(),
                    billing, cashCouponList);
            orderBaseInfo.setTotalGoodsPrice(orderBillingDetails.getTotalGoodsPrice());

            //根据应付金额判断订单账单是否已付清
            if (orderBillingDetails.getArrearage() <= AppApplicationConstant.PAY_UP_LIMIT) {
                orderBillingDetails.setIsPayUp(true);
            } else {
                orderBillingDetails.setIsPayUp(false);
            }

            //******** 处理账单支付明细信息 *********
            List<OrderBillingPaymentDetails> paymentDetails = new ArrayList<>();
            //******* 检查库存和与账单支付金额是否充足,如果充足就扣减相应的数量
            commonService.reduceInventoryAndMoney(deliverySimpleInfo, inventoryCheckMap, orderParam.getCityId(), orderParam.getIdentityType(),
                    orderParam.getUserId(), orderParam.getCustomerId(), cashCouponList, orderBillingDetails, orderBaseInfo.getOrderNumber(), ipAddress);

            //******* 持久化订单相关实体信息  *******
            commonService.saveAndHandleOrderRelevantInfo(orderBaseInfo, orderLogisticsInfo, orderGoodsInfoList, orderBillingDetails, paymentDetails);

            // *******分摊**********
            dutchService.addGoodsDetailsAndDutch(Long.valueOf(orderParam.getUserId()), AppIdentityType.getAppIdentityTypeByValue(orderParam.getIdentityType()), promotionSimpleInfoList, orderGoodsInfoList);

            if (orderBillingDetails.getIsPayUp()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CreateOrderResponse(orderBaseInfo.getOrderNumber(), Double.parseDouble(CountUtil.retainTwoDecimalPlaces(orderBillingDetails.getAmountPayable())), true));
                logger.info("createOrder OUT,订单创建成功,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CreateOrderResponse(orderBaseInfo.getOrderNumber(), Double.parseDouble(CountUtil.retainTwoDecimalPlaces(orderBillingDetails.getAmountPayable())), false));
                logger.info("createOrder OUT,订单创建成功,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

        } catch (LockStoreInventoryException | LockStorePreDepositException | LockCityInventoryException | LockCustomerCashCouponException |
                LockCustomerLebiException | LockCustomerPreDepositException | LockEmpCreditMoneyException | LockStoreCreditMoneyException |
                LockStoreSubventionException | SystemBusyException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, e.getMessage(), null);
            logger.warn("createOrder OUT,订单创建失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (IOException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单参数转换异常!", null);
            logger.warn("createOrder EXCEPTION,订单创建失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (OrderSaveException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单创建异常!", null);
            logger.warn("createOrder EXCEPTION,订单创建失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常,下单失败!", null);
            logger.warn("createOrder EXCEPTION,订单创建失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
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

        ResultDTO<Object> resultDTO;
        //可以传的都可以单独存在，不能判断
//        if (AssertUtil.isEmpty(goodsSimpleRequest.getProductCouponList()) || AssertUtil.isEmpty(goodsSimpleRequest.getGoodsList())) {
//            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单商品信息不可为空！", null);
//            logger.info("enterOrder OUT,用户确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
//            return resultDTO;
//        }
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
        List<GoodsIdQtyParam> goodsList = goodsSimpleRequest.getGoodsList();
        List<PromotionSimpleInfo> giftList = goodsSimpleRequest.getGiftList();
        List<GoodsIdQtyParam> couponList = goodsSimpleRequest.getProductCouponList();
        try {
            int goodsQty = 0;
            int giftQty = 0;
            int couponQty = 0;
            Double totalPrice = 0.00;
            Double memberDiscount = 0.00;
            //订单优惠在以后的促销活动表中获取数据
            Double orderDiscount = 0.00;
            //运费暂时还没出算法
            Double freight = 0.00;
            Double totalOrderAmount = 0.00;
            List<Long> goodsIds = new ArrayList<Long>();
            List<Long> giftIds = new ArrayList<Long>();
            List<Long> couponIds = new ArrayList<Long>();
            List<GoodsIdQtyParam> giftsList = new ArrayList<>();
//            List<GoodsIdQtyParam> couponList = new ArrayList<>();
            List<OrderGoodsSimpleResponse> goodsInfo = null;
            List<OrderGoodsSimpleResponse> giftsInfo = null;
            List<OrderGoodsSimpleResponse> productCouponInfo = null;
            List<CashCouponResponse> cashCouponResponseList = null;
            Map<String, Object> goodsSettlement = new HashMap<>();


            if (identityType == 6) {
                AppCustomer customer = appCustomerService.findById(userId);
                Long cityId = customer.getCityId();
                if (AssertUtil.isNotEmpty(goodsList)) {
                    for (GoodsIdQtyParam aGoodsList : goodsList) {
                        goodsIds.add(aGoodsList.getId());
                        goodsQty = goodsQty + aGoodsList.getQty();
                    }
                }
                if (AssertUtil.isNotEmpty(giftList)) {
                    for (PromotionSimpleInfo promotionSimpleInfo : giftList) {
                        if (null != promotionSimpleInfo.getPresentInfo()) {
                            giftsList.addAll(promotionSimpleInfo.getPresentInfo());
                            for (GoodsIdQtyParam goodsIdQtyParam : promotionSimpleInfo.getPresentInfo()) {
                                giftIds.add(goodsIdQtyParam.getId());
                                giftQty = giftQty + goodsIdQtyParam.getQty();
                            }
                        }
                    }
                }
                if (AssertUtil.isNotEmpty(couponList)) {
                    for (GoodsIdQtyParam couponSimpleInfo : couponList) {
//                        OrderGoodsSimpleResponse couponInfo = goodsService.findGoodsByCustomerIdAndSku(userId,couponSimpleInfo.getSku());
//                        couponInfo.setGoodsQty(couponSimpleInfo.getQty());
//                        couponInfo.setGoodsLineType(AppGoodsLineType.PRODUCT_COUPON.getValue());
//                        productCouponInfo.add(couponInfo);
//                        GoodsIdQtyParam param = new GoodsIdQtyParam();
//                        param.setId(couponInfo.getId());
//                        param.setQty(couponSimpleInfo.getQty());
//                        couponList.add(param);
                        couponIds.add(couponSimpleInfo.getId());
                        couponQty = couponQty + couponSimpleInfo.getQty();
                    }
                }
                //获取商品信息
                goodsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIds);
                //获取赠品信息
                giftsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, giftIds);
                //获取产品券信息
                productCouponInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, couponIds);
                //加本品标识
                if (AssertUtil.isNotEmpty(goodsInfo)) {
                    for (OrderGoodsSimpleResponse simpleResponse : goodsInfo) {
                        for (GoodsIdQtyParam goodsIdQtyParam : goodsList) {
                            if (simpleResponse.getId().equals(goodsIdQtyParam.getId())) {
                                simpleResponse.setGoodsQty(goodsIdQtyParam.getQty());
                                break;
                            }
                        }
                        simpleResponse.setGoodsLineType(AppGoodsLineType.GOODS.getValue());
                        //算总金额
                        totalPrice = CountUtil.add(totalPrice, CountUtil.mul(simpleResponse.getRetailPrice(), simpleResponse.getGoodsQty()));
                        //算会员折扣(先判断是否是会员还是零售会员)
                        if (null != customer.getSalesConsultId()) {
                            memberDiscount = CountUtil.add(memberDiscount, CountUtil.mul(simpleResponse.getVipPrice(), simpleResponse.getGoodsQty()));
                        }
                    }
                }
                //赠品的数量和标识
                if (AssertUtil.isNotEmpty(giftsInfo)) {
                    for (OrderGoodsSimpleResponse aGiftInfo : giftsInfo) {
                        for (GoodsIdQtyParam goodsIdQtyParam : giftsList) {
                            if (aGiftInfo.getId().equals(goodsIdQtyParam.getId())) {
                                aGiftInfo.setGoodsQty(goodsIdQtyParam.getQty());
                                break;
                            }
                        }
                        aGiftInfo.setGoodsLineType(AppGoodsLineType.PRESENT.getValue());
                    }
                    //合并商品和赠品集合
                    goodsInfo.addAll(giftsInfo);
                }
                //产品券加标识
                if (AssertUtil.isNotEmpty(productCouponInfo)) {
                    for (OrderGoodsSimpleResponse orderGoodsSimpleResponse : productCouponInfo) {
                        for (GoodsIdQtyParam goodsIdQtyParam : couponList) {
                            if (orderGoodsSimpleResponse.getId().equals(goodsIdQtyParam.getId())) {
                                orderGoodsSimpleResponse.setGoodsQty(goodsIdQtyParam.getQty());
                                break;
                            }
                        }
                        orderGoodsSimpleResponse.setGoodsLineType(AppGoodsLineType.PRODUCT_COUPON.getValue());
                    }
                    //合并商品和赠品集合
                    if (AssertUtil.isNotEmpty(goodsInfo)) {
                        goodsInfo.addAll(productCouponInfo);
                    } else {
                        goodsInfo = productCouponInfo;
                    }
                }

                //判断库存的特殊处理
                Long gid = appOrderService.existOrderGoodsInventory(cityId, goodsList, giftsList, couponList);
                if (gid != null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品ID:" + gid + ";商品库存不足！", null);
                    logger.info("getGoodsMoneyOfWorker OUT,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                //计算订单金额小计
                //TODO 根据促销减去订单折扣, 加运费
                //********* 计算促销立减金额 *************
                List<PromotionDiscountListResponse> discountListResponseList = actService.countDiscount(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), goodsInfo);
                for (PromotionDiscountListResponse discountResponse : discountListResponseList) {
                    orderDiscount = CountUtil.add(orderDiscount, discountResponse.getDiscountPrice());
                }

                totalOrderAmount = CountUtil.sub(totalPrice, memberDiscount, orderDiscount);
                if (totalOrderAmount != null && totalOrderAmount < 1000) {
                    freight = 30.00;
                }
                totalOrderAmount = CountUtil.add(totalOrderAmount, freight);
                //计算顾客乐币
                Map<String, Object> leBi = appCustomerService.findLeBiByUserIdAndGoodsMoney(userId, totalOrderAmount);
                //计算可用的优惠券
                cashCouponResponseList = appCustomerService.findCashCouponUseableByCustomerId(userId, totalOrderAmount);
                //查询顾客预存款
                Double preDeposit = appCustomerService.findPreDepositBalanceByUserIdAndIdentityType(userId, identityType);

                goodsSettlement.put("totalQty", goodsQty + giftQty);
                goodsSettlement.put("totalPrice", totalPrice);
                goodsSettlement.put("totalGoodsInfo", goodsInfo);
                goodsSettlement.put("memberDiscount", memberDiscount);
                // TODO 会员折扣在创建促销表后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("orderDiscount", orderDiscount);
                // TODO 运费再出算法后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("freight", freight);
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
                if (AssertUtil.isNotEmpty(goodsList)) {
                    for (GoodsIdQtyParam aGoodsList : goodsList) {
                        goodsIds.add(aGoodsList.getId());
                        goodsQty = goodsQty + aGoodsList.getQty();
                    }
                }
                if (AssertUtil.isNotEmpty(giftList)) {
                    for (PromotionSimpleInfo promotionSimpleInfo : giftList) {
                        if (null != promotionSimpleInfo.getPresentInfo()) {
                            giftsList.addAll(promotionSimpleInfo.getPresentInfo());
                            for (GoodsIdQtyParam goodsIdQtyParam : promotionSimpleInfo.getPresentInfo()) {
                                giftIds.add(goodsIdQtyParam.getId());
                                giftQty = giftQty + goodsIdQtyParam.getQty();
                            }
                        }
                    }
                }
                if (AssertUtil.isNotEmpty(couponList)) {
                    for (GoodsIdQtyParam couponSimpleInfo : couponList) {
//                        OrderGoodsSimpleResponse couponInfo = goodsService.findGoodsByCustomerIdAndSku(userId,couponSimpleInfo.getSku());
//                        couponInfo.setGoodsQty(couponSimpleInfo.getQty());
//                        couponInfo.setGoodsLineType(AppGoodsLineType.PRODUCT_COUPON.getValue());
//                        productCouponInfo.add(couponInfo);
//                        GoodsIdQtyParam param = new GoodsIdQtyParam();
//                        param.setId(couponInfo.getId());
//                        param.setQty(couponSimpleInfo.getQty());
//                        couponList.add(param);
                        couponIds.add(couponSimpleInfo.getId());
                        couponQty = couponQty + couponSimpleInfo.getQty();
                    }
                }
                goodsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);
                giftsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, giftIds);
                //获取产品券信息
                productCouponInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, couponIds);
                //加本品标识
                if (AssertUtil.isNotEmpty(goodsInfo)) {
                    for (OrderGoodsSimpleResponse simpleResponse : goodsInfo) {
                        for (GoodsIdQtyParam goodsIdQtyParam : goodsList) {
                            if (simpleResponse.getId().equals(goodsIdQtyParam.getId())) {
                                simpleResponse.setGoodsQty(goodsIdQtyParam.getQty());
                                break;
                            }
                        }
                        simpleResponse.setGoodsLineType(AppGoodsLineType.GOODS.getValue());
                        //算总金额
                        totalPrice = CountUtil.add(totalPrice, CountUtil.mul(simpleResponse.getRetailPrice(), simpleResponse.getGoodsQty()));
                        //算会员折扣(先判断是否是会员还是零售会员)
                        memberDiscount = CountUtil.add(memberDiscount, CountUtil.mul(simpleResponse.getVipPrice(), simpleResponse.getGoodsQty()));
                    }
                }
                //赠品的数量和标识
                if (AssertUtil.isNotEmpty(giftsInfo)) {
                    for (OrderGoodsSimpleResponse aGiftInfo : giftsInfo) {
                        for (GoodsIdQtyParam goodsIdQtyParam : giftsList) {
                            if (aGiftInfo.getId().equals(goodsIdQtyParam.getId())) {
                                aGiftInfo.setGoodsQty(goodsIdQtyParam.getQty());
                                break;
                            }
                        }
                        aGiftInfo.setGoodsLineType(AppGoodsLineType.PRESENT.getValue());
                    }
                    //合并商品和赠品集合
                    goodsInfo.addAll(giftsInfo);
                }
                //产品券加标识
                if (AssertUtil.isNotEmpty(productCouponInfo)) {
                    for (OrderGoodsSimpleResponse orderGoodsSimpleResponse : productCouponInfo) {
                        for (GoodsIdQtyParam goodsIdQtyParam : couponList) {
                            if (orderGoodsSimpleResponse.getId().equals(goodsIdQtyParam.getId())) {
                                orderGoodsSimpleResponse.setGoodsQty(goodsIdQtyParam.getQty());
                                break;
                            }
                        }
                        orderGoodsSimpleResponse.setGoodsLineType(AppGoodsLineType.PRODUCT_COUPON.getValue());
                    }
                    //合并商品和赠品集合
                    if (AssertUtil.isNotEmpty(goodsInfo)) {
                        goodsInfo.addAll(productCouponInfo);
                    } else {
                        goodsInfo = productCouponInfo;
                    }
                }
                //判断库存
                Long gid = appOrderService.existOrderGoodsInventory(customer.getCityId(), goodsList, giftsList, couponList);
                if (gid != null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品ID:" + gid + ";商品库存不足！", null);
                    logger.info("getGoodsMoneyOfWorker OUT,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                //计算订单金额小计
                //TODO 根据促销减去订单折扣
                //********* 计算促销立减金额 *************
                List<PromotionDiscountListResponse> discountListResponseList = actService.countDiscount(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), goodsInfo);
                for (PromotionDiscountListResponse discountResponse : discountListResponseList) {
                    orderDiscount = CountUtil.add(orderDiscount, discountResponse.getDiscountPrice());
                }

                totalOrderAmount = CountUtil.sub(totalPrice, memberDiscount, orderDiscount);
                if (totalOrderAmount != null && totalOrderAmount < 1000) {
                    freight = 30.00;
                }
                totalOrderAmount = CountUtil.add(totalOrderAmount, freight);
                //计算顾客乐币
                Map<String, Object> leBi = appCustomerService.findLeBiByUserIdAndGoodsMoney(customerId, totalOrderAmount);
                //现金券还需要传入订单金额判断是否满减
                cashCouponResponseList = appCustomerService.findCashCouponUseableByCustomerId(customerId, totalOrderAmount);
                //查询导购预存款和信用金
                SellerCreditMoneyResponse sellerCreditMoneyResponse = appEmployeeService.findCreditMoneyBalanceByUserIdAndIdentityType(userId, identityType);
                Double creditMoney = sellerCreditMoneyResponse.getAvailableBalance();
                //导购门店预存款
                Double storePreDeposit = appStoreService.findPreDepositBalanceByUserId(userId);

                goodsSettlement.put("totalQty", goodsQty + giftQty);
                goodsSettlement.put("totalPrice", totalPrice);
                goodsSettlement.put("totalGoodsInfo", goodsInfo);
                goodsSettlement.put("memberDiscount", memberDiscount);
                // TODO 会员折扣在创建促销表后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("orderDiscount", orderDiscount);
                // TODO 运费再出算法后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("freight", freight);
                goodsSettlement.put("totalOrderAmount", totalOrderAmount);
                goodsSettlement.put("leBi", leBi);
                goodsSettlement.put("cashCouponList", cashCouponResponseList);
                goodsSettlement.put("creditMoney", creditMoney);
                goodsSettlement.put("storePreDeposit", storePreDeposit);
            }

            if (identityType == 2) {
                AppEmployee employee = appEmployeeService.findById(userId);
                Long storeId = employee.getStoreId();
                if (AssertUtil.isNotEmpty(goodsList)) {
                    for (GoodsIdQtyParam aGoodsList : goodsList) {
                        goodsIds.add(aGoodsList.getId());
                        goodsQty = goodsQty + aGoodsList.getQty();
                    }
                }
                if (AssertUtil.isNotEmpty(giftList)) {
                    for (PromotionSimpleInfo promotionSimpleInfo : giftList) {
                        if (null != promotionSimpleInfo.getPresentInfo()) {
                            giftsList.addAll(promotionSimpleInfo.getPresentInfo());
                            for (GoodsIdQtyParam goodsIdQtyParam : promotionSimpleInfo.getPresentInfo()) {
                                giftIds.add(goodsIdQtyParam.getId());
                                giftQty = giftQty + goodsIdQtyParam.getQty();
                            }
                        }
                    }
                }
                goodsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);

                giftsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, giftIds);
                //加本品标识
                if (AssertUtil.isNotEmpty(goodsInfo)) {
                    for (OrderGoodsSimpleResponse simpleResponse : goodsInfo) {
                        for (GoodsIdQtyParam goodsIdQtyParam : goodsList) {
                            if (simpleResponse.getId().equals(goodsIdQtyParam.getId())) {
                                simpleResponse.setGoodsQty(goodsIdQtyParam.getQty());
                                break;
                            }
                        }
                        simpleResponse.setGoodsLineType(AppGoodsLineType.GOODS.getValue());
                        //算总金额
                        totalPrice = CountUtil.add(totalPrice, CountUtil.mul(simpleResponse.getRetailPrice(), simpleResponse.getGoodsQty()));
                    }
                }
                //赠品的数量和标识
                if (AssertUtil.isNotEmpty(giftsInfo)) {
                    for (OrderGoodsSimpleResponse aGiftInfo : giftsInfo) {
                        for (GoodsIdQtyParam goodsIdQtyParam : giftsList) {
                            if (aGiftInfo.getId().equals(goodsIdQtyParam.getId())) {
                                aGiftInfo.setGoodsQty(goodsIdQtyParam.getQty());
                                break;
                            }
                        }
                        aGiftInfo.setGoodsLineType(AppGoodsLineType.PRESENT.getValue());
                    }
                    //合并商品和赠品集合
                    goodsInfo.addAll(giftsInfo);
                }
                //判断库存
                Long gid = appOrderService.existOrderGoodsInventory(employee.getCityId(), goodsList, giftsList, null);
                if (gid != null) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品ID:" + gid + ";商品库存不足！", null);
                    logger.info("getGoodsMoneyOfWorker OUT,确认商品计算工人订单总金额失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                //计算订单金额小计
                //********* 计算促销立减金额 *************
                List<PromotionDiscountListResponse> discountListResponseList = actService.countDiscount(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), goodsInfo);
                if (AssertUtil.isNotEmpty(discountListResponseList)) {
                    for (PromotionDiscountListResponse discountResponse : discountListResponseList) {
                        orderDiscount = CountUtil.add(orderDiscount, discountResponse.getDiscountPrice());
                    }
                }
                totalOrderAmount = CountUtil.sub(totalPrice, memberDiscount, orderDiscount);
                if (totalOrderAmount != null && totalOrderAmount < 1000) {
                    freight = 30.00;
                }
                totalOrderAmount = CountUtil.add(totalOrderAmount, freight);

                //获取门店预存款，信用金，现金返利。
                Double storePreDeposit = appStoreService.findPreDepositBalanceByUserId(userId);
                Double storeCreditMoney = appStoreService.findCreditMoneyBalanceByUserId(userId);
                Double storeSubvention = appStoreService.findSubventionBalanceByUserId(userId);

                goodsSettlement.put("totalQty", goodsQty + giftQty);
                goodsSettlement.put("totalPrice", totalPrice);
                goodsSettlement.put("totalGoodsInfo", goodsInfo);
                // TODO 会员折扣在创建促销表后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("orderDiscount", orderDiscount);
                // TODO 运费再出算法后折算（以下算法无任何意义，作数据填充）
                goodsSettlement.put("freight", freight);
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
        if (null == usedCouponRequest.getCouponsList() && usedCouponRequest.getCouponsList().isEmpty()) {
            returnMap.put("leBi", usedCouponRequest.getLeBi());
            returnMap.put("totalOrderAmount", totalOrderAmount);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, returnMap);
            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单成功，出参 resultDTO:{}", resultDTO);
        }
        List<GoodsIdQtyParam> cashCouponsList = usedCouponRequest.getCouponsList();
        Map<String, Object> leBi = null;
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
        if (null == usedCouponRequest || usedCouponRequest.getCouponsList().isEmpty()) {
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
     * 用户获取订单列表
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @return 订单列表
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getOrderList(Long userID, Integer identityType, Integer showStatus) {
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
            List<OrderBaseInfo> orderBaseInfoList = appOrderService.getOrderListByUserIDAndIdentityType(userID, identityType, showStatus);
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
                    goodsImgList.add(goodsService.queryBySku(orderGoodsInfo.getSku()).getCoverImageUri());
                }
                if ("待付款".equals(orderBaseInfo.getStatus().getDescription())) {
                    //计算剩余过期失效时间
                    Long time = ((orderBaseInfo.getEffectiveEndTime().getTime()) - (System.currentTimeMillis()));
                    //设置
                    if (time > 0) {
                        orderListResponse.setEndTime(time);
                    }
                }
                orderListResponse.setOrderNo(orderBaseInfo.getOrderNumber());
                orderListResponse.setStatus(orderBaseInfo.getStatus().getDescription());
                orderListResponse.setIsEvaluated(orderBaseInfo.getIsEvaluated());
                orderListResponse.setDeliveryType(orderBaseInfo.getDeliveryType().getDescription());
                orderListResponse.setCount(appOrderService.querySumQtyByOrderNumber(orderBaseInfo.getOrderNumber()));
                orderListResponse.setPrice(appOrderService.getAmountPayableByOrderNumber(orderBaseInfo.getOrderNumber()));
                orderListResponse.setGoodsImgList(goodsImgList);
                if (identityType == 0) {
                    CustomerSimpleInfo customer = new CustomerSimpleInfo();
                    customer.setCustomerId(orderBaseInfo.getCustomerId());
                    customer.setCustomerName(orderBaseInfo.getCustomerName());
                    customer.setCustomerPhone(orderBaseInfo.getCustomerPhone());
                    orderListResponse.setCustomer(customer);
                }
                //添加到返回类list中
                orderListResponses.add(orderListResponse);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, orderListResponses);
            logger.info("getOrderList OUT,用户获取订单列表成功，出参 resultDTO:{}", orderListResponses.size());
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
                    goodsImgList.add(goodsService.queryBySku(orderGoodsInfo.getSku()).getCoverImageUri());
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
                orderListResponse.setStatus(orderBaseInfo.getStatus().getDescription());
                orderListResponse.setDeliveryType(orderBaseInfo.getDeliveryType().getDescription());
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
            OrderBillingDetails billingDetails = appOrderService.getOrderBillingDetail(orderNumber);
            OrderEvaluation orderEvaluation = orderEvaluationService.queryOrderEvaluationListByOrderNumber(orderNumber);
            if (null != orderBaseInfo) {
                //获取订单收货/自提门店地址
                OrderLogisticsInfo orderLogisticsInfo = appOrderService.getOrderLogistice(orderNumber);
                //创建返回对象
                OrderDetailsResponse orderDetailsResponse = new OrderDetailsResponse();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //设值
                orderDetailsResponse.setOrderNumber(orderNumber);
                orderDetailsResponse.setCreateTime(sdf.format(orderBaseInfo.getCreateTime()));
                orderDetailsResponse.setStatus(orderBaseInfo.getStatus().getDescription());
                orderDetailsResponse.setPayType(null == billingDetails.getOnlinePayType() ?
                        OnlinePayType.NO.getDescription() : billingDetails.getOnlinePayType().getDescription());
                orderDetailsResponse.setDeliveryType(orderBaseInfo.getDeliveryType().getDescription());
                if (orderEvaluation != null){
                    orderDetailsResponse.setLogisticsStar(orderEvaluation.getLogisticsStar());
                    orderDetailsResponse.setProductStar(orderEvaluation.getProductStar());
                    orderDetailsResponse.setServiceStars(orderEvaluation.getServiceStars());
                }
                orderDetailsResponse.setIsEvaluated(orderBaseInfo.getIsEvaluated());
                if (identityType == 0) {
                    CustomerSimpleInfo customer = new CustomerSimpleInfo();
                    customer.setCustomerId(orderBaseInfo.getCustomerId());
                    customer.setCustomerName(orderBaseInfo.getCustomerName());
                    customer.setCustomerPhone(orderBaseInfo.getCustomerPhone());
                    orderDetailsResponse.setCustomer(customer);
                }
                //根据不同的配送方式进行设值
                if ("门店自提".equals(orderBaseInfo.getDeliveryType().getValue())) {
                    orderDetailsResponse.setBookingStoreName(orderLogisticsInfo.getBookingStoreName());
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
                    customerBillingDetailResponse.setCouponDiscount(orderBillingDetails.getCashCouponDiscount());
                    customerBillingDetailResponse.setFreight(orderBillingDetails.getFreight());
                    customerBillingDetailResponse.setLeBiCashDiscount(orderBillingDetails.getLebiCashDiscount());
                    customerBillingDetailResponse.setMemberDiscount(orderBillingDetails.getMemberDiscount());
                    customerBillingDetailResponse.setPreDeposit(orderBillingDetails.getCusPreDeposit());
                    customerBillingDetailResponse.setProductCouponDiscount(orderBillingDetails.getProductCouponDiscount());
                    customerBillingDetailResponse.setPromotionDiscount(orderBillingDetails.getPromotionDiscount());
                    customerBillingDetailResponse.setTotalPrice(orderBaseInfo.getTotalGoodsPrice());

                    orderDetailsResponse.setCustomerBillingDetailResponse(customerBillingDetailResponse);
                } else if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.DECORATE_MANAGER)) {
                    //经理
                    ManagerBillingDetailResponse managerBillingDetailResponse = new ManagerBillingDetailResponse();
                    managerBillingDetailResponse.setAmountPayable(orderBillingDetails.getAmountPayable());
                    managerBillingDetailResponse.setCouponDiscount(orderBillingDetails.getCashCouponDiscount());
                    managerBillingDetailResponse.setFreight(orderBillingDetails.getFreight());
                    managerBillingDetailResponse.setMemberDiscount(orderBillingDetails.getMemberDiscount());
                    managerBillingDetailResponse.setSubvention(orderBillingDetails.getStoreSubvention());
                    managerBillingDetailResponse.setProductCouponDiscount(orderBillingDetails.getProductCouponDiscount());
                    managerBillingDetailResponse.setPreDeposit(orderBillingDetails.getStPreDeposit());
                    managerBillingDetailResponse.setCreditMoney(orderBillingDetails.getStoreCreditMoney());
                    managerBillingDetailResponse.setPromotionDiscount(orderBillingDetails.getPromotionDiscount());
                    managerBillingDetailResponse.setTotalPrice(orderBaseInfo.getTotalGoodsPrice());

                    orderDetailsResponse.setManagerBillingDetailResponse(managerBillingDetailResponse);
                } else {
                    //导购
                    SellerBillingDetailResponse sellerBillingDetailResponse = new SellerBillingDetailResponse();
                    sellerBillingDetailResponse.setAmountPayable(orderBillingDetails.getAmountPayable());
                    sellerBillingDetailResponse.setCouponDiscount(orderBillingDetails.getCashCouponDiscount());
                    sellerBillingDetailResponse.setCreditMoney(orderBillingDetails.getEmpCreditMoney());
                    sellerBillingDetailResponse.setFreight(orderBillingDetails.getFreight());
                    sellerBillingDetailResponse.setMemberDiscount(orderBillingDetails.getMemberDiscount());
                    sellerBillingDetailResponse.setPreDeposit(orderBillingDetails.getStPreDeposit());
                    sellerBillingDetailResponse.setProductCouponDiscount(orderBillingDetails.getProductCouponDiscount());
                    sellerBillingDetailResponse.setPromotionDiscount(orderBillingDetails.getPromotionDiscount());
                    sellerBillingDetailResponse.setTotalPrice(orderBaseInfo.getTotalGoodsPrice());

                    orderDetailsResponse.setSellerBillingDetailResponse(sellerBillingDetailResponse);
                }
                orderDetailsResponse.setGoodsList(appOrderService.getOrderGoodsList(orderNumber));

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
            GoodsPrice goodsPrice = goodsService.findGoodsPriceByProductCouponIdAndUserId(userId, aProductCouponsList.getId(), aProductCouponsList.getQty());
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
