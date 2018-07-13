package cn.com.leyizhuang.app.web.controller.order;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.core.utils.IpUtils;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.*;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.deliveryFeeRule.DeliveryFeeRule;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.request.*;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.*;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.SellDetailsSender;
import cn.com.leyizhuang.app.remote.queue.SinkSender;
import cn.com.leyizhuang.app.remote.webservice.ICallWms;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import cn.com.leyizhuang.common.util.CountUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    @Resource
    private TransactionalSupportService transactionalSupportService;

    @Resource
    private AppCashCouponDutchService cashCouponDutchService;

    @Resource
    private ICallWms iCallWms;

    @Resource
    private SinkSender sinkSender;

    @Resource
    private SellDetailsSender sellDetailsSender;

    @Resource
    private CashCouponService cashCouponService;

    @Resource
    private AppLeBiDutchService leBiDutchService;

    @Resource
    private AppCashReturnDutchService cashReturnDutchService;

    @Resource
    private DeliveryFeeRuleService deliveryFeeRuleService;

    @Autowired
    private GoodsPriceService goodsPriceService;

    @Resource
    private MaOrderService maOrderService;
    @Resource
    private ReturnOrderService returnOrderService;

    @Resource
    private AppEmployeeService employeeService;

    /**
     * 创建订单方法
     *
     * @param orderParam 前台提交的订单相关参数
     * @param request    request对象
     * @return 订单创建结果
     */
    @ApiOperation(value = "创建订单", notes = "创建订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cityId", value = "下单人城市id", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "userId", value = "下单人id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "identityType", value = "下单人身份类型", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "customerId", value = "顾客id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "remark", value = "备注信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "goodsInfo", value = "商品信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "productCouponInfo", value = "产品券商品信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "promotionInfo", value = "促销信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "deliveryInfo", value = "配送信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "billingInfo", value = "账单信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "auditNo", value = "物料审核单号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "salesNumber", value = "纸质单号", required = false, dataType = "String")
    })
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
        Long userId = orderParam.getUserId();
        if (null == orderParam.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不允许为空!", "");
            logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Integer identityType = orderParam.getIdentityType();
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

            //*************************** 转化前台提交过来的json类型参数 ************************

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
            List<Long> cashCouponList = new ArrayList<>();
            if (StringUtils.isNotBlank(orderParam.getCashCouponIds())) {
                cashCouponList = objectMapper.readValue(orderParam.getCashCouponIds(), cashCouponSimpleInfo);
            }
            //产品券信息
            List<ProductCouponSimpleInfo> productCouponList = new ArrayList<>();
            if (StringUtils.isNotBlank(orderParam.getProductCouponInfo())) {
                productCouponList = objectMapper.readValue(orderParam.getProductCouponInfo(), productCouponSimpleInfo);
            }
            //促销信息
            List<PromotionSimpleInfo> promotionSimpleInfoList = new ArrayList<>();
            if (StringUtils.isNotBlank(orderParam.getPromotionInfo())) {
                promotionSimpleInfoList = objectMapper.readValue(orderParam.getPromotionInfo(), promotionSimpleInfo);
            }
            // 检查促销是否过期
            List<Long> promotionIds = new ArrayList<>();
            for (PromotionSimpleInfo promotion : promotionSimpleInfoList) {
                promotionIds.add(promotion.getPromotionId());
            }
            if (promotionIds.size() > 0) {
                Boolean outTime = actService.checkActOutTime(promotionIds);
                if (!outTime) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "存在过期促销，请重新下单！", "");
                    logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }

            //账单信息
            BillingSimpleInfo billing = objectMapper.readValue(orderParam.getBillingInfo(), BillingSimpleInfo.class);


            //如果是导购下单并且是四川直营门店，判断销售纸质单号是否为空
            if (orderParam.getIdentityType() == AppIdentityType.SELLER.getValue()) {
                AppEmployee employee = appEmployeeService.findById(orderParam.getUserId());
                City city = cityService.findById(orderParam.getCityId());
                AppStore appStore = appStoreService.findById(employee.getStoreId());
                if ("ZY".equals(appStore.getStoreType().getValue()) && ("FZY009".equals(appStore.getStoreCode()) || "HLC004".equals(appStore.getStoreCode()) || "ML001".equals(appStore.getStoreCode()) || "QCMJ008".equals(appStore.getStoreCode()) ||
                        "SB010".equals(appStore.getStoreCode()) || "YC002".equals(appStore.getStoreCode()) || "ZC002".equals(appStore.getStoreCode()) || "RC005".equals(appStore.getStoreCode()) ||
                        "FZM007".equals(appStore.getStoreCode()) || "SH001".equals(appStore.getStoreCode()) || "YJ001".equals(appStore.getStoreCode()) || "HS001".equals(appStore.getStoreCode()) ||
                        "XC001".equals(appStore.getStoreCode()))) {
                    if (StringUtils.isBlank(orderParam.getSalesNumber())) {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "四川直营门店销售纸质单号不能为空！", "");
                        logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
            }

            //**********************************开始创建订单 **************************
            //******************* 根据商品确定订单表单号为 XN 或者 XNFW ********************
//            List<Long> allGoodsList = new ArrayList<>();
//            goodsList.forEach(g -> allGoodsList.add(g.getId()));
//            productCouponList.forEach(p -> allGoodsList.add(p.getId()));
//            String orderNumberType = appOrderService.returnType(allGoodsList,orderParam.getUserId(),identityType);

            //******************* 创建订单基础信息 *****************
            OrderBaseInfo orderBaseInfo = appOrderService.createOrderBaseInfo(orderParam.getCityId(), orderParam.getUserId(),
                    orderParam.getIdentityType(), orderParam.getCustomerId(), deliverySimpleInfo.getDeliveryType(), orderParam.getRemark(), orderParam.getSalesNumber());
//            String oldOrderNumber = orderBaseInfo.getOrderNumber();
//            oldOrderNumber = oldOrderNumber.replace("XN",orderNumberType);
//            orderBaseInfo.setOrderNumber(oldOrderNumber);

            //****************** 创建订单物流信息 ******************
            OrderLogisticsInfo orderLogisticsInfo = appOrderService.createOrderLogisticInfo(deliverySimpleInfo);
            orderLogisticsInfo.setOrdNo(orderBaseInfo.getOrderNumber());

            //****************** 创建订单商品信息 ******************
            CreateOrderGoodsSupport support = commonService.createOrderGoodsInfo(goodsList, orderParam.getUserId(), orderParam.getIdentityType(),
                    orderParam.getCustomerId(), productCouponList, orderBaseInfo.getOrderNumber());

            //****************** 创建订单券信息 *********************
            List<OrderCouponInfo> orderCouponInfoList = new ArrayList<>();

            //****************** 创建订单优惠券信息 *****************
            List<OrderCouponInfo> orderCashCouponInfoList = commonService.createOrderCashCouponInfo(orderBaseInfo, cashCouponList);
            if (null != orderCashCouponInfoList && orderCashCouponInfoList.size() > 0) {
                orderCouponInfoList.addAll(orderCashCouponInfoList);
            }
            //****************** 创建订单产品券信息 *****************
            List<OrderCouponInfo> orderProductCouponInfoList = commonService.createOrderProductCouponInfo(orderBaseInfo, support.getProductCouponGoodsList());
            if (null != orderProductCouponInfoList && orderProductCouponInfoList.size() > 0) {
                orderCouponInfoList.addAll(orderProductCouponInfoList);
            }

            //****************** 处理订单账单相关信息 ***************
            OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
            orderBillingDetails.setOrderNumber(orderBaseInfo.getOrderNumber());
            orderBillingDetails.setIsOwnerReceiving(orderLogisticsInfo.getIsOwnerReceiving());
            orderBillingDetails.setTotalGoodsPrice(support.getGoodsTotalPrice());
            orderBillingDetails.setMemberDiscount(support.getMemberDiscount());
            orderBillingDetails.setPromotionDiscount(billing.getOrderDiscount());

            orderBillingDetails = appOrderService.createOrderBillingDetails(orderBillingDetails, orderParam.getUserId(), orderParam.getIdentityType(),
                    billing, cashCouponList, support.getProductCouponGoodsList());

            orderBaseInfo.setTotalGoodsPrice(orderBillingDetails.getTotalGoodsPrice());

            //****************** 处理订单账单支付明细信息 ************
            List<OrderBillingPaymentDetails> paymentDetails = commonService.createOrderBillingPaymentDetails(orderBaseInfo, orderBillingDetails);

            /********* 开始计算分摊 促销分摊可能产生新的行记录 所以优先分摊 ******************/
            List<OrderGoodsInfo> orderGoodsInfoList;
            orderGoodsInfoList = dutchService.addGoodsDetailsAndDutch(orderParam.getUserId(), AppIdentityType.getAppIdentityTypeByValue(orderParam.getIdentityType()), promotionSimpleInfoList, support.getPureOrderGoodsInfo(), orderParam.getCustomerId());

            //******** 分摊现乐币 策略：每个商品 按单价占比 分摊 *********************
            // 乐币暂时不分摊
//            Integer leBiQty = billing.getLeBiQuantity();
//            orderGoodsInfoList = leBiDutchService.LeBiDutch(leBiQty, orderGoodsInfoList);

            //******** 分摊现现金返利 策略：每个商品 按单价占比 分摊 *********************
            Double cashReturnAmount = billing.getStoreSubvention();
            orderGoodsInfoList = cashReturnDutchService.cashReturnDutch(cashReturnAmount, orderGoodsInfoList);

            //******** 分摊现金券 策略：使用范围商品 按单价占比 分摊 *********************
            orderGoodsInfoList = cashCouponDutchService.cashCouponDutch(cashCouponList, orderGoodsInfoList);

            //******** 分摊完毕 计算退货 单价 ***************************
            orderGoodsInfoList = dutchService.countReturnPrice(orderGoodsInfoList, cashReturnAmount, CountUtil.div(billing.getLeBiQuantity(), 10D), billing.getOrderDiscount());
            //orderGoodsInfoList = dutchService.countReturnPrice(orderGoodsInfoList);

            //将产品券商品加入 分摊完毕的商品列表中
            orderGoodsInfoList.addAll(support.getProductCouponGoodsList());
            support.setOrderGoodsInfoList(orderGoodsInfoList);

            //****************** 创建订单经销差价返还明细 ***********
            List<OrderJxPriceDifferenceReturnDetails> jxPriceDifferenceReturnDetailsList = commonService.createOrderJxPriceDifferenceReturnDetails(orderBaseInfo, support.getOrderGoodsInfoList(), promotionSimpleInfoList, orderProductCouponInfoList );
            if (null != jxPriceDifferenceReturnDetailsList && jxPriceDifferenceReturnDetailsList.size() > 0) {
                orderBillingDetails.setJxPriceDifferenceAmount(jxPriceDifferenceReturnDetailsList.stream().mapToDouble(OrderJxPriceDifferenceReturnDetails::getAmount).sum());
            }

            //**************** 创建要检核库存的商品和商品数量的Map ***********
            Map<Long, Integer> inventoryCheckMap = commonService.createInventoryCheckMap(orderGoodsInfoList);
            support.setInventoryCheckMap(inventoryCheckMap);

            //添加商品专供标志
            orderGoodsInfoList = this.commonService.addGoodsSign(orderGoodsInfoList, orderBaseInfo);

            //**************** 1、检查库存和与账单支付金额是否充足,如果充足就扣减相应的数量 ***********
            //**************** 2、持久化订单相关实体信息 ****************
            transactionalSupportService.createOrderBusiness(deliverySimpleInfo, support.getInventoryCheckMap(), orderParam.getCityId(), orderParam.getIdentityType(),
                    orderParam.getUserId(), orderParam.getCustomerId(), cashCouponList, orderProductCouponInfoList, orderBillingDetails, orderBaseInfo,
                    orderLogisticsInfo, orderGoodsInfoList, orderCouponInfoList, paymentDetails, jxPriceDifferenceReturnDetailsList, ipAddress, promotionSimpleInfoList);

            //****** 清空当单购物车商品 ******
            commonService.clearOrderGoodsInMaterialList(orderParam.getUserId(), orderParam.getIdentityType(), goodsList, productCouponList);

            if (orderBillingDetails.getAmountPayable() <= AppConstant.PAY_UP_LIMIT) {
                //如果预存款或信用金已支付完成直接发送到WMS出货单
                if (orderBaseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                    iCallWms.sendToWmsRequisitionOrderAndGoods(orderBaseInfo.getOrderNumber());
                }
                //将该订单入拆单消息队列
                sinkSender.sendOrder(orderBaseInfo.getOrderNumber());
                //添加订单生命周期
                appOrderService.addOrderLifecycle(OrderLifecycleType.PAYED, orderBaseInfo.getOrderNumber());

                // 激活订单赠送的产品券
                // productCouponService.activateCusProductCoupon(orderBaseInfo.getOrderNumber());

                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CreateOrderResponse(orderBaseInfo.getOrderNumber(), Double.parseDouble(CountUtil.retainTwoDecimalPlaces(orderBillingDetails.getAmountPayable())), true, false));
                logger.info("createOrder OUT,订单创建成功,出参 resultDTO:{}", resultDTO);
            } else {
                //判断是否可选择货到付款
                Boolean isCashDelivery = this.commonService.checkCashDelivery(orderGoodsInfoList, userId, AppIdentityType.getAppIdentityTypeByValue(identityType), orderBaseInfo.getDeliveryType());
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CreateOrderResponse(orderBaseInfo.getOrderNumber(), Double.parseDouble(CountUtil.retainTwoDecimalPlaces(orderBillingDetails.getAmountPayable())), false, isCashDelivery));
                logger.info("createOrder OUT,订单创建成功,出参 resultDTO:{}", resultDTO);
            }
            return resultDTO;
        } catch (LockStoreInventoryException | LockStorePreDepositException | LockCityInventoryException | LockCustomerCashCouponException |
                LockCustomerLebiException | LockCustomerPreDepositException | LockEmpCreditMoneyException | LockStoreCreditMoneyException |
                LockStoreSubventionException | SystemBusyException | LockCustomerProductCouponException | GoodsMultipartPriceException | GoodsNoPriceException |
                OrderPayableAmountException | DutchException | OrderCreditMoneyException | OrderDiscountException | GoodsQtyErrorException e) {
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
        String orderType = goodsSimpleRequest.getOrderType();
        try {
            int goodsQty = 0;
            int giftQty = 0;
            int couponQty = 0;
            Double totalPrice = 0.00;
            Double memberDiscount = 0.00;
            Double orderDiscount = 0.00;
            Double proCouponDiscount = 0D;
            //运费暂时还没出算法
            Double freight = 0.00;
            Double totalOrderAmount = 0.00;
            List<Long> goodsIds = new ArrayList<Long>();
            List<Long> giftIds = new ArrayList<Long>();
            List<Long> couponIds = new ArrayList<Long>();
            List<GoodsIdQtyParam> giftsList = new ArrayList<>();
            List<OrderGoodsSimpleResponse> goodsInfo = null;
            List<OrderGoodsSimpleResponse> giftsInfo = null;
            List<OrderGoodsSimpleResponse> productCouponInfo = null;
            List<CashCouponResponse> cashCouponResponseList = null;
            Map<String, Object> goodsSettlement = new HashMap<>();
            Long cityId = 0L;
            AppStore appStore = null;
            AppCustomer customer = new AppCustomer();
            boolean isShowSalesNumber = false;
            if (identityType == 6) {
                customer = appCustomerService.findById(userId);
                if (null == customer) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据发生异常!请联系客服", null);
                    logger.info("enterOrder OUT,用户确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                cityId = customer.getCityId();
                appStore = appStoreService.findById(customer.getStoreId());
            } else if (identityType == 0) {
                if (null == goodsSimpleRequest.getCustomerId()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "导购代下单客户不能为空", null);
                    logger.info("enterOrder OUT,用户确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                Long customerId = goodsSimpleRequest.getCustomerId();
                customer = appCustomerService.findById(customerId);
                if (null == customer) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "数据发生异常!请联系客服", null);
                    logger.info("enterOrder OUT,用户确认订单计算商品价格明细失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                //是否显示纸质销售单号
                AppEmployee appEmployee = appEmployeeService.findById(userId);
                cityId = appEmployee.getCityId();
                appStore = appStoreService.findById(appEmployee.getStoreId());
                //如果是四川直营门店导购返回门店编码
                if ("ZY".equals(appStore.getStoreType().getValue()) && ("FZY009".equals(appStore.getStoreCode()) || "HLC004".equals(appStore.getStoreCode()) || "ML001".equals(appStore.getStoreCode()) || "QCMJ008".equals(appStore.getStoreCode()) ||
                        "SB010".equals(appStore.getStoreCode()) || "YC002".equals(appStore.getStoreCode()) || "ZC002".equals(appStore.getStoreCode()) || "RC005".equals(appStore.getStoreCode()) ||
                        "FZM007".equals(appStore.getStoreCode()) || "SH001".equals(appStore.getStoreCode()) || "YJ001".equals(appStore.getStoreCode()) || "HS001".equals(appStore.getStoreCode()) ||
                        "XC001".equals(appStore.getStoreCode()))) {
                    isShowSalesNumber = true;
                }
            } else if (identityType == 2) {
                AppEmployee employee = appEmployeeService.findById(userId);
                cityId = employee.getCityId();
            }

            //取出所有本品的id和计算本品数量
            if (AssertUtil.isNotEmpty(goodsList)) {
                for (GoodsIdQtyParam aGoodsList : goodsList) {
                    goodsIds.add(aGoodsList.getId());
                    goodsQty = goodsQty + aGoodsList.getQty();
                }
            }
            //取出所有赠品的id和计算赠品数量
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
            //取出所有产品券商品的id和计算产品券商品数量
            if (AssertUtil.isNotEmpty(couponList)) {
                for (GoodsIdQtyParam couponSimpleInfo : couponList) {
                    couponIds.add(couponSimpleInfo.getId());
                    couponQty = couponQty + couponSimpleInfo.getQty();
                }
            }
            if (identityType == 6) {
                //获取商品信息
                goodsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIds);
                //获取赠品信息
                giftsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, giftIds);
                //获取产品券信息
                productCouponInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, couponIds);
            } else {
                //获取商品信息
                goodsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);
                //获取赠品信息
                giftsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, giftIds);
                //获取产品券信息
                productCouponInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, couponIds);
            }
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
                    if (identityType == 2 || null != customer.getCustomerType() && customer.getCustomerType().equals(AppCustomerType.MEMBER)) {
                        memberDiscount = CountUtil.add(memberDiscount, CountUtil.mul(CountUtil.sub(simpleResponse.getRetailPrice(),
                                simpleResponse.getVipPrice()), simpleResponse.getGoodsQty()));
                    }
                }
            }
            // 本品集合 用来计算立减促销
            List<OrderGoodsSimpleResponse> bGoodsList = goodsInfo;

            //赠品的数量和标识
            if (AssertUtil.isNotEmpty(giftsInfo)) {
                for (GoodsIdQtyParam goodsIdQtyParam : giftsList) {
                    for (OrderGoodsSimpleResponse aGiftInfo : giftsInfo) {
                        if (aGiftInfo.getId().equals(goodsIdQtyParam.getId())) {
                            aGiftInfo.setGoodsQty(aGiftInfo.getGoodsQty() + goodsIdQtyParam.getQty());
                            aGiftInfo.setRetailPrice(0D);
                            break;
                        }
                        aGiftInfo.setGoodsLineType(AppGoodsLineType.PRESENT.getValue());
                    }
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
                    //算产品券总金额
                    proCouponDiscount = CountUtil.add(proCouponDiscount, CountUtil.mul(orderGoodsSimpleResponse.getRetailPrice(), orderGoodsSimpleResponse.getGoodsQty()));
                }
                //合并商品和赠品集合
                if (AssertUtil.isNotEmpty(goodsInfo)) {
                    goodsInfo.addAll(productCouponInfo);
                } else {
                    goodsInfo = productCouponInfo;
                }
            }

            //计算订单金额小计
            //********* 计算促销立减金额 *************
            List<PromotionDiscountListResponse> discountListResponseList = null;
            //买卷获取买卷促销
            if (null == orderType || !orderType.equals("COUPON")) {
                discountListResponseList = actService.countDiscount(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), bGoodsList, customer.getCusId(), "GOODS");
            } else {
                discountListResponseList = actService.countDiscount(userId, AppIdentityType.getAppIdentityTypeByValue(identityType), bGoodsList, customer.getCusId(), "COUPON");
            }
            for (PromotionDiscountListResponse discountResponse : discountListResponseList) {
                orderDiscount = CountUtil.add(orderDiscount, discountResponse.getDiscountPrice());
                PromotionSimpleInfo promotionSimpleInfo = new PromotionSimpleInfo();
                promotionSimpleInfo.setPromotionId(discountResponse.getPromotionId());
                promotionSimpleInfo.setDiscount(discountResponse.getDiscountPrice());
                promotionSimpleInfo.setEnjoyTimes(discountResponse.getEnjoyTimes());
                giftList.add(promotionSimpleInfo);
            }

            totalOrderAmount = CountUtil.sub(totalPrice, memberDiscount, orderDiscount);


            if (identityType == 6) {
                //计算顾客乐币
                Map<String, Object> leBi = appCustomerService.findLeBiByUserIdAndGoodsMoney(userId, totalOrderAmount);
                //计算可用的优惠券
                cashCouponResponseList = appCustomerService.findCashCouponUseableByCustomerId(userId, totalOrderAmount);
                //查询顾客预存款
                Double preDeposit = appCustomerService.findPreDepositBalanceByUserIdAndIdentityType(userId, identityType);

                goodsSettlement.put("leBi", leBi);
                goodsSettlement.put("cashCouponList", cashCouponResponseList);
                goodsSettlement.put("preDeposit", preDeposit);
            } else if (identityType == 0) {
                //现金券还需要传入订单金额判断是否满减
                cashCouponResponseList = appCustomerService.findCashCouponUseableByCustomerId(customer.getCusId(), totalOrderAmount);
                //查询导购预存款和信用金
                SellerCreditMoneyResponse sellerCreditMoneyResponse = appEmployeeService.findCreditMoneyBalanceByUserIdAndIdentityType(userId, identityType);
                Double creditMoney = null != sellerCreditMoneyResponse ? sellerCreditMoneyResponse.getAvailableBalance() : 0D;
                //导购门店预存款
                Double storePreDeposit = appStoreService.findPreDepositBalanceByUserId(userId);

                goodsSettlement.put("cashCouponList", cashCouponResponseList);
                goodsSettlement.put("creditMoney", creditMoney);
                goodsSettlement.put("storePreDeposit", storePreDeposit);
            } else if (identityType == 2) {
                //获取装饰公司门店预存款，信用金，现金返利。
                Double storePreDeposit = appStoreService.findPreDepositBalanceByUserId(userId);
                Double storeCreditMoney = appStoreService.findCreditMoneyBalanceByUserId(userId);
                Double storeSubvention = appStoreService.findSubventionBalanceByUserId(userId);

                goodsSettlement.put("storePreDeposit", storePreDeposit);
                goodsSettlement.put("storeCreditMoney", storeCreditMoney);
                goodsSettlement.put("storeSubvention", storeSubvention);
            }

            //由于运费不抵扣乐币及优惠券,避免分摊出现负,运费放最后计算
            // 运费计算
            //2018-04-01 generation 产品卷金额加进运费计算 买卷不计算运费
            if (null == orderType || !orderType.equals("COUPON")) {
                freight = deliveryFeeRuleService.countDeliveryFee(identityType, cityId, CountUtil.add(totalOrderAmount, proCouponDiscount), goodsInfo);
            }
            totalOrderAmount = CountUtil.add(totalOrderAmount, freight);
            ArrayList<Long> allGoods = new ArrayList<>();
            allGoods.addAll(goodsIds);
            allGoods.addAll(giftIds);
            allGoods.addAll(couponIds);


//            goodsSettlement.put("totalQty", goodsQty + giftQty + couponQty);
            goodsSettlement.put("totalQty", AssertUtil.getArrayList(allGoods).size());
            goodsSettlement.put("totalPrice", CountUtil.add(totalPrice, proCouponDiscount));
            goodsSettlement.put("totalGoodsInfo", goodsInfo);
            goodsSettlement.put("orderDiscount", orderDiscount);
            goodsSettlement.put("proCouponDiscount", proCouponDiscount);
            goodsSettlement.put("memberDiscount", memberDiscount);
            goodsSettlement.put("freight", freight);
            goodsSettlement.put("totalOrderAmount", totalOrderAmount);
            goodsSettlement.put("promotionInfo", giftList);
            goodsSettlement.put("isShowNumber", isShowSalesNumber);
            List<AppDeliveryType> deliveryTypeList = new ArrayList<>();
            goodsIds.addAll(giftIds);
            goodsIds.addAll(couponIds);

            //买卷不判断库存
            if (null == orderType || !orderType.equals("COUPON")) {
                //判断商品是否有专供商品
                List<GiftListResponseGoods> goodsZGList = this.goodsPriceService.findGoodsPriceListByGoodsIdsAndUserId(goodsIds, userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
                //有专供商品只能选择送货上门
                if (null != goodsZGList && goodsZGList.size() > 0) {
                    deliveryTypeList.add(AppDeliveryType.HOUSE_DELIVERY);
                } else {
                    if (AppDeliveryType.SELF_TAKE.equals(goodsSimpleRequest.getSysDeliveryType())) {
                        deliveryTypeList.add(AppDeliveryType.SELF_TAKE);
                    } else {
                        deliveryTypeList.add(AppDeliveryType.SELF_TAKE);
                        deliveryTypeList.add(AppDeliveryType.HOUSE_DELIVERY);
                    }
                }
                goodsSettlement.put("deliveryTypeList", deliveryTypeList);
                //非门店自提,为城市库存充足及门店库存充足
                if (!AppDeliveryType.SELF_TAKE.equals(goodsSimpleRequest.getSysDeliveryType())) {

                    //2018-04-01 generation 修改 提示所有城市库存不足的商品
                    //判断库存的特殊处理
                    List<Long> goodsIdList = appOrderService.existOrderGoodsInventory(cityId, goodsList, giftsList, couponList);
                    if (goodsIdList != null && goodsIdList.size() > 0) {
                        String message = "商品 ";
                        for (Long gid : goodsIdList) {
                            GoodsDO goodsDO = goodsService.queryById(gid);
                            message += "“";
                            message += goodsDO.getSkuName();
                            message += "” ";
                        }
                        message += "仓库库存不足，请更改购买数量!";
                        //如果这里发现库存不足还是要返回去商品列表
//                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该商品:" + goodsDO.getSkuName() + "商品库存不足！", goodsSettlement);
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, message, goodsSettlement);
                        logger.info("enterOrder OUT,用户确认订单计算商品价格明细，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
            }
            Boolean isSpecialSelfTake = false;
            /**************/
            //2018-04-03 generation 加盟门店自提为特殊自提单
            if (null != appStore && StoreType.JM == appStore.getStoreType()) {
                isSpecialSelfTake = true;
            }
            goodsSettlement.put("isSpecialSelfTake", isSpecialSelfTake);
            /**************/

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    goodsSettlement.size() > 0 ? goodsSettlement : null);
            logger.info("enterOrder OUT,用户确认订单计算商品价格明细成功，出参 resultDTO:{}", resultDTO);
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
        if (AssertUtil.isEmpty(usedCouponRequest.getCouponsList())) {
            returnMap.put("leBi", usedCouponRequest.getLeBi());
            returnMap.put("totalOrderAmount", totalOrderAmount);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, returnMap);
            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        List<GoodsIdQtyParam> cashCouponsList = usedCouponRequest.getCouponsList();

        // 本品
        List<GoodsIdQtyParam> goodsInfoList = usedCouponRequest.getGoodsList();

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

                // 顾客信息
                AppCustomer customer = appCustomerService.findById(usedCouponRequest.getCustomerId());

                // 订单满足用券条件的金额
                Double meetAmount = totalOrderAmount;
                //遍历产品券列表
                for (GoodsIdQtyParam aCashCouponsList : cashCouponsList) {
                    //根据券ID 去查产品券
//                    CashCouponResponse cashCoupon = appCustomerService.findCashCouponByCcIdAndUserIdAndQty(
//                            aCashCouponsList.getId(), userId, aCashCouponsList.getQty());
                    CustomerCashCoupon cashCoupon = cashCouponService.findCustomerCashCouponById(aCashCouponsList.getId());

                    if (null != cashCoupon) {
                        // 是否关闭
                        if (!cashCoupon.getStatus()) {
                            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "部分优惠券已经关闭使用", null);
                            logger.info("部分优惠券已经关闭使用", resultDTO);
                            return resultDTO;
                        }

                        // 判断是否过期
                        if (cashCoupon.getEffectiveEndTime().before(new Date())) {
                            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "部分优惠券已经过期", null);
                            logger.info("部分优惠券已经过期", resultDTO);
                            return resultDTO;
                        }

                        AppCashCouponType cashCouponType = cashCoupon.getType();

                        if (cashCouponType.equals(AppCashCouponType.GENERAL)) {
                            // 通用现金券

//                            meetAmount = totalOrderAmount;
                        } else if (cashCouponType.equals(AppCashCouponType.COMPANY)) {
                            // 指定公司券
                            List<Long> goodsIds = new ArrayList<>();
                            for (GoodsIdQtyParam aGoodsList : goodsInfoList) {
                                goodsIds.add(aGoodsList.getId());
                            }
                            List<OrderGoodsSimpleResponse> goodsInfo = null;
                            if (identityType == 6) {
                                //获取商品信息
                                goodsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIds);
                            } else {
                                //获取商品信息
                                goodsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);
                            }

                            if (goodsInfo == null || goodsInfo.size() == 0) {
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "本品信息不对", null);
                                logger.info("本品信息不对", resultDTO);
                                return resultDTO;
                            }

                            // 获取指定公司
                            List<String> companys = cashCouponService.queryCompanysByCcid(cashCoupon.getCcid());

                            for (OrderGoodsSimpleResponse goods : goodsInfo) {
                                if (companys.contains(goods.getCompanyFlag())) {
                                    for (GoodsIdQtyParam aGoodsList : goodsInfoList) {
                                        if (goods.getId().equals(aGoodsList.getId())) {
                                            if (customer.getCustomerType().equals(AppCustomerType.RETAIL)) {
                                                meetAmount = CountUtil.add(meetAmount, CountUtil.mul(goods.getRetailPrice(), aGoodsList.getQty()));
                                            } else {
                                                meetAmount = CountUtil.add(meetAmount, CountUtil.mul(goods.getVipPrice(), aGoodsList.getQty()));
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        } else if (cashCouponType.equals(AppCashCouponType.BRAND)) {
                            // 品牌现金券

                            List<Long> goodsIds = new ArrayList<>();
                            for (GoodsIdQtyParam aGoodsList : goodsInfoList) {
                                goodsIds.add(aGoodsList.getId());
                            }
                            List<OrderGoodsSimpleResponse> goodsInfo = null;
                            if (identityType == 6) {
                                //获取商品信息
                                goodsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIds);
                            } else {
                                //获取商品信息
                                goodsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);
                            }

                            if (goodsInfo == null || goodsInfo.size() == 0) {
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "本品信息不对", null);
                                logger.info("本品信息不对", resultDTO);
                                return resultDTO;
                            }

                            // 获取指定品牌
                            List<Long> brandIds = cashCouponService.queryBrandIdsByCcid(cashCoupon.getCcid());

                            for (OrderGoodsSimpleResponse goods : goodsInfo) {
                                if (brandIds.contains(goods.getBrandId())) {
                                    for (GoodsIdQtyParam aGoodsList : goodsInfoList) {
                                        if (goods.getId().equals(aGoodsList.getId())) {
                                            if (customer.getCustomerType().equals(AppCustomerType.RETAIL)) {
                                                meetAmount = CountUtil.add(meetAmount, CountUtil.mul(goods.getRetailPrice(), aGoodsList.getQty()));
                                            } else {
                                                meetAmount = CountUtil.add(meetAmount, CountUtil.mul(goods.getVipPrice(), aGoodsList.getQty()));
                                            }
                                            break;
                                        }
                                    }
                                }
                            }


                        } else if (cashCouponType.equals(AppCashCouponType.GOODS)) {
                            // 指定商品现金券

                            List<Long> goodsIds = new ArrayList<>();
                            for (GoodsIdQtyParam aGoodsList : goodsInfoList) {
                                goodsIds.add(aGoodsList.getId());
                            }
                            List<OrderGoodsSimpleResponse> goodsInfo = null;
                            if (identityType == 6) {
                                //获取商品信息
                                goodsInfo = goodsService.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIds);
                            } else {
                                //获取商品信息
                                goodsInfo = goodsService.findGoodsListByEmployeeIdAndGoodsIdList(userId, goodsIds);
                            }

                            if (goodsInfo == null || goodsInfo.size() == 0) {
                                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "本品信息不对", null);
                                logger.info("本品信息不对", resultDTO);
                                return resultDTO;
                            }

                            List<Long> goodsIdList = cashCouponService.queryGoodsIdsByCcid(cashCoupon.getCcid());

                            for (OrderGoodsSimpleResponse goods : goodsInfo) {
                                if (goodsIdList.contains(goods.getId())) {
                                    for (GoodsIdQtyParam aGoodsList : goodsInfoList) {
                                        if (goods.getId().equals(aGoodsList.getId())) {
                                            if (customer.getCustomerType().equals(AppCustomerType.RETAIL)) {
                                                meetAmount = CountUtil.add(meetAmount, CountUtil.mul(goods.getRetailPrice(), aGoodsList.getQty()));
                                            } else {
                                                meetAmount = CountUtil.add(meetAmount, CountUtil.mul(goods.getVipPrice(), aGoodsList.getQty()));
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        //如果当前小计满足第一张券的满减条件就减去优惠券的折扣,循环判断
                        if (meetAmount >= cashCoupon.getCondition()) {
//                            Double couponDiscount = CountUtil.mul(cashCoupon.getDenomination(), aCashCouponsList.getQty());
                            cashCouponDiscount = CountUtil.add(cashCouponDiscount, cashCoupon.getDenomination());
                            totalOrderAmount = CountUtil.sub(totalOrderAmount, cashCoupon.getDenomination());
                            meetAmount = CountUtil.sub(meetAmount, cashCoupon.getCondition(), cashCoupon.getDenomination());
                            index++;
                        } else {
                            //直到如有使用过多的券，返回最多可使用index张券
                            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "您最多使用" + index + "张优惠券", null);
                            logger.info("reEnterOrderByCashCoupon OUT,通过现金券来重新计算确认订单失败，出参 resultDTO:{}", resultDTO);
                            return resultDTO;
                        }
                    } else {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未找到优惠券或者该优惠券已过期！", null);
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
     * 用户获取待评价订单列表
     *
     * @param userId       用户id
     * @param identityType 用户类型
     * @return 订单列表
     */
    @PostMapping(value = "/list/pending/evaluation", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getPendingEvaluationOrderList(Long userId, Integer identityType, Integer page, Integer size) {
        ResultDTO<Object> resultDTO;
        logger.info("getPendingEvaluationOrderList CALLED,用户获取待评价订单列表，入参 userID:{}, identityType:{}", userId, identityType);
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getPendingEvaluationOrderList OUT,用户获取待评价订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getPendingEvaluationOrderList OUT,用户获取待评价订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getPendingEvaluationOrderList OUT,用户获取待评价订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getPendingEvaluationOrderList OUT,用户获取待评价订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取用户待评价订单列表
            if (AppIdentityType.getAppIdentityTypeByValue(identityType) != AppIdentityType.CUSTOMER
                    && AppIdentityType.getAppIdentityTypeByValue(identityType) != AppIdentityType.DECORATE_MANAGER) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户没有权限评价!", null);
                logger.info("getPendingEvaluationOrderList OUT,用户获取待评价订单列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            PageInfo<OrderListResponse> responseOrderList = appOrderService.getPendingEvaluationOrderListByUserIDAndIdentityType(userId,
                    identityType, page, size);
            for (OrderListResponse response : responseOrderList.getList()) {
                if ("PRODUCT_COUPON".equals(response.getDeliveryType()) || "SELF_TAKE".equals(response.getDeliveryType())) {
                    AppStore appStore = appStoreService.findById(response.getStoreId());
                    response.setShippingAddress(appStore.getDetailedAddress());
                }
                response.setCount(response.getGoodsImgList().size());
            }
            CustomerSignDetailResponse response = new CustomerSignDetailResponse();
            response.setCount(responseOrderList.getTotal());
            response.setNumsPerPage(responseOrderList.getPageSize());
            response.setTotalPage(responseOrderList.getPages());
            response.setCurrentPage(responseOrderList.getPageNum());
            response.setData(responseOrderList.getList());
            responseOrderList.setTotal(responseOrderList.getList().size());
            double pages = (Math.ceil((double) responseOrderList.getTotal() / (double) responseOrderList.getPageSize()));
            responseOrderList.setPages((int) pages);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<OrderListResponse>().transform(responseOrderList.getList(), responseOrderList));
            logger.info("getPendingEvaluationOrderList OUT,用户获取待评价订单列表成功，出参 resultDTO:{}", responseOrderList);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户获取待评价订单列表失败", null);
            logger.warn("getPendingEvaluationOrderList EXCEPTION,用户获取待评价订单列表失败，出参 resultDTO:{}", resultDTO);
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
    /*@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getOrderList(Long userID, Integer identityType, Integer showStatus, Integer page, Integer size) {
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
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getOrderList OUT,用户获取订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getOrderList OUT,用户获取订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取用户所有订单列表
            PageInfo<OrderBaseInfo> orderBaseInfoLists = appOrderService.getOrderListByUserIDAndIdentityType(userID, identityType, showStatus, page, size);
            List<OrderBaseInfo> orderBaseInfoList = orderBaseInfoLists.getList();
            //创建一个返回对象list
            List<OrderListResponse> orderListResponses = new ArrayList<>();
            if (null != orderBaseInfoList && orderBaseInfoList.size() > 0) {
                //循环遍历订单列表
                for (OrderBaseInfo orderBaseInfo : orderBaseInfoList) {
                    //创建有个存放图片地址的list
                    List<String> goodsImgList = new ArrayList<>();
                    //创建一个返回类
                    OrderListResponse orderListResponse = new OrderListResponse();
                    //获取订单商品
                    List<OrderGoodsInfo> orderGoodsInfoList = appOrderService.getOrderGoodsInfoByOrderNumber(orderBaseInfo.getOrderNumber());
                    //商品总数
                    int totalGoods = 0;
                    //遍历订单商品
                    for (OrderGoodsInfo orderGoodsInfo : orderGoodsInfoList) {
                        goodsImgList.add(orderGoodsInfo.getCoverImageUri());
                        totalGoods += orderGoodsInfo.getOrderQuantity();
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
                    orderListResponse.setStatus(orderBaseInfo.getStatus().getValue());
                    orderListResponse.setStatusDesc(orderBaseInfo.getStatus().getDescription());
                    orderListResponse.setIsEvaluated(orderBaseInfo.getIsEvaluated());
                    orderListResponse.setDeliveryType(orderBaseInfo.getDeliveryType().getDescription());
                    //获取订单物流相关信息
                    OrderLogisticsInfo orderLogisticsInfo = appOrderService.getOrderLogistice(orderBaseInfo.getOrderNumber());
                    if (null != orderLogisticsInfo) {
                        if ("HOUSE_DELIVERY".equals(orderBaseInfo.getDeliveryType().getValue())) {
                            orderListResponse.setShippingAddress(StringUtils.isBlank(orderLogisticsInfo.getShippingAddress()) ? null : orderLogisticsInfo.getShippingAddress());
                        } else {
                            orderListResponse.setShippingAddress(StringUtils.isBlank(orderLogisticsInfo.getBookingStoreName()) ? null : orderLogisticsInfo.getBookingStoreName());
                        }
                    }
                    orderListResponse.setCount(totalGoods);
                    OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderBaseInfo.getOrderNumber());
                    orderListResponse.setPrice(orderBillingDetails.getTotalGoodsPrice());
                    orderListResponse.setAmountPayable(orderBillingDetails.getAmountPayable());
                    orderListResponse.setGoodsImgList(goodsImgList);
                    if (identityType == 0) {
                        orderListResponse.setCustomerId(orderBaseInfo.getCustomerId());
                        orderListResponse.setCustomerName(orderBaseInfo.getCustomerName());
                        orderListResponse.setCustomerPhone(orderBaseInfo.getCustomerPhone());
                    }
                    //添加到返回类list中
                    orderListResponses.add(orderListResponse);
                }
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    new GridDataVO<OrderListResponse>().transform(orderListResponses, orderBaseInfoLists));
            logger.info("getOrderList OUT,用户获取订单列表成功，出参 resultDTO:{}", orderListResponses);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户获取订单列表失败", null);
            logger.warn("getOrderList EXCEPTION,用户获取订单列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }*/


    /**
     * 用户获取订单列表
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @return 订单列表
     */
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getOrderList(Long userID, Integer identityType, Integer showStatus, Integer page, Integer size) {
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
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getOrderList OUT,用户获取订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getOrderList OUT,用户获取订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取用户所有订单列表
            PageInfo<OrderPageInfoVO> orderListResponsePageInfo = appOrderService.getOrderListPageInfoByUserIdAndIdentityType(userID, identityType, showStatus, page, size);

            //创建一个返回对象list
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    new GridDataVO<OrderPageInfoVO>().transform(orderListResponsePageInfo));
            logger.info("getOrderList OUT,用户获取订单列表成功，出参 resultDTO:{}", orderListResponsePageInfo);
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
     * 客户经理查看自己支付的订单
     *
     * @param userId
     * @param identityType
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/list/sellerManager/payFor", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getPayForOrderList(Long userId, Integer identityType, Integer page, Integer size) {
        ResultDTO<Object> resultDTO;
        logger.info("getPayForOrderList CALLED,客户经理查看自己支付的订单，入参 userID:{}, identityType:{}", userId, identityType);
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getPayForOrderList OUT,客户经理查看自己支付的订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType != 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不正确！", null);
            logger.info("getPayForOrderList OUT,客户经理查看自己支付的订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getPayForOrderList OUT,客户经理查看自己支付的订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getPayForOrderList OUT,客户经理查看自己支付的订单失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取装饰经理支付订单订单列表
            PageInfo<OrderPageInfoVO> orderListResponsePageInfo = appOrderService.findSellerManagerPayForOrderList(userId, page, size);

            //创建一个返回对象list
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    new GridDataVO<OrderPageInfoVO>().transform(orderListResponsePageInfo));
            logger.info("getPayForOrderList OUT,客户经理查看自己支付的订单成功，出参 resultDTO:{}", orderListResponsePageInfo);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，客户经理查看自己支付的订单失败", null);
            logger.warn("getPayForOrderList EXCEPTION,客户经理查看自己支付的订单失败，出参 resultDTO:{}", resultDTO);
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
                //获取订单物流相关信息
                OrderLogisticsInfo orderLogisticsInfo = appOrderService.getOrderLogistice(orderBaseInfo.getOrderNumber());
                if ("HOUSE_DELIVERY".equals(orderBaseInfo.getDeliveryType().getValue())) {
                    orderListResponse.setShippingAddress(StringUtils.isBlank(orderLogisticsInfo.getShippingAddress()) ? null : orderLogisticsInfo.getShippingAddress());
                } else {
                    orderListResponse.setShippingAddress(StringUtils.isBlank(orderLogisticsInfo.getBookingStoreName()) ? null : orderLogisticsInfo.getBookingStoreName());
                }
                orderListResponse.setOrderNo(orderBaseInfo.getOrderNumber());
                orderListResponse.setStatus(orderBaseInfo.getStatus().getValue());
                orderListResponse.setStatusDesc(orderBaseInfo.getStatus().getDescription());
                orderListResponse.setDeliveryType(orderBaseInfo.getDeliveryType().getDescription());
                orderListResponse.setCount(appOrderService.querySumQtyByOrderNumber(orderBaseInfo.getOrderNumber()));
                OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderBaseInfo.getOrderNumber());
                orderListResponse.setPrice(orderBillingDetails.getTotalGoodsPrice());
                orderListResponse.setAmountPayable(orderBillingDetails.getAmountPayable());
                orderListResponse.setGoodsImgList(goodsImgList);

                if (identityType == 0) {
                    orderListResponse.setCustomerId(orderBaseInfo.getCustomerId());
                    orderListResponse.setCustomerName(orderBaseInfo.getCustomerName());
                    orderListResponse.setCustomerPhone(orderBaseInfo.getCustomerPhone());
                }

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
        logger.info("getOrderDetail CALLED,用户获取订单详情，入参 userID:{}, identityType:{}, orderNumber:{}", userID, identityType, orderNumber);
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
                orderDetailsResponse.setRemark(StringUtils.isBlank(orderBaseInfo.getRemark()) ? "" : orderBaseInfo.getRemark());
                orderDetailsResponse.setCreateTime(sdf.format(orderBaseInfo.getCreateTime()));
                orderDetailsResponse.setStatus(orderBaseInfo.getStatus());
                orderDetailsResponse.setStatusDesc(orderBaseInfo.getStatus().getDescription());
                orderDetailsResponse.setPickUpCode(orderBaseInfo.getPickUpCode());
                if (AppIdentityType.SELLER.equals(AppIdentityType.getAppIdentityTypeByValue(identityType))) {
                    orderDetailsResponse.setCustomerName(orderBaseInfo.getCustomerName());
                    orderDetailsResponse.setCustomerPhone(orderBaseInfo.getCustomerPhone());
                    orderDetailsResponse.setIsOwnerReceiving(orderLogisticsInfo.getIsOwnerReceiving());
                }

                orderDetailsResponse.setPayType(null == billingDetails.getOnlinePayType() ?
                        OnlinePayType.NO.getDescription() : billingDetails.getOnlinePayType().getDescription());
                orderDetailsResponse.setDeliveryType(orderBaseInfo.getDeliveryType().getDescription());
                if (orderEvaluation != null) {
                    orderDetailsResponse.setLogisticsStar(orderEvaluation.getLogisticsStar());
                    orderDetailsResponse.setProductStar(orderEvaluation.getProductStar());
                    orderDetailsResponse.setServiceStars(orderEvaluation.getServiceStars());
                }
                orderDetailsResponse.setIsEvaluated(orderBaseInfo.getIsEvaluated());
                if (identityType == 0) {
                    orderDetailsResponse.setCustomer(new CustomerSimpleInfo(orderBaseInfo.getCustomerId(),
                            orderBaseInfo.getCustomerName(),
                            orderBaseInfo.getCustomerPhone()));
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
                    orderDetailsResponse.setEstateInfo(orderLogisticsInfo.getEstateInfo());
                }
                //获取订单账目明细
                OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderNumber);
                //根据不同的身份返回对应的账目明细
                if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.CUSTOMER)) {
                    //会员
                    CustomerBillingDetailResponse customerBillingDetailResponse = new CustomerBillingDetailResponse();
                    customerBillingDetailResponse.setAmountPayable(orderBillingDetails.getAmountPayable() == null ? 0 : orderBillingDetails.getAmountPayable());
                    customerBillingDetailResponse.setCouponDiscount(orderBillingDetails.getCashCouponDiscount() == null ? 0 : orderBillingDetails.getCashCouponDiscount());
                    customerBillingDetailResponse.setFreight(orderBillingDetails.getFreight() == null ? 0 : orderBillingDetails.getFreight());
                    customerBillingDetailResponse.setLeBiCashDiscount(orderBillingDetails.getLebiCashDiscount() == null ? 0 : orderBillingDetails.getLebiCashDiscount());
                    customerBillingDetailResponse.setMemberDiscount(orderBillingDetails.getMemberDiscount() == null ? 0 : orderBillingDetails.getMemberDiscount());
                    customerBillingDetailResponse.setPreDeposit(orderBillingDetails.getCusPreDeposit() == null ? 0 : orderBillingDetails.getCusPreDeposit());
                    customerBillingDetailResponse.setProductCouponDiscount(orderBillingDetails.getProductCouponDiscount() == null ? 0 : orderBillingDetails.getProductCouponDiscount());
                    customerBillingDetailResponse.setPromotionDiscount(orderBillingDetails.getPromotionDiscount() == null ? 0 : orderBillingDetails.getPromotionDiscount());
                    customerBillingDetailResponse.setTotalPrice(orderBaseInfo.getTotalGoodsPrice() == null ? 0 : orderBaseInfo.getTotalGoodsPrice());
                    PayhelperOrder payhelperOrder = this.appOrderService.findPayhelperOrderByOrdNo(orderNumber);
                    if (null != payhelperOrder){
                        customerBillingDetailResponse.setPayForAnotherMoney(null == payhelperOrder.getPayhelperAmount() ? 0 : payhelperOrder.getPayhelperAmount());
                    }
                    orderDetailsResponse.setCustomerBillingDetailResponse(customerBillingDetailResponse);
                } else if (AppIdentityType.getAppIdentityTypeByValue(identityType).equals(AppIdentityType.DECORATE_MANAGER)) {
                    //经理
                    ManagerBillingDetailResponse managerBillingDetailResponse = new ManagerBillingDetailResponse();
                    managerBillingDetailResponse.setAmountPayable(orderBillingDetails.getAmountPayable() == null ? 0 : orderBillingDetails.getAmountPayable());
                    managerBillingDetailResponse.setCouponDiscount(orderBillingDetails.getCashCouponDiscount() == null ? 0 : orderBillingDetails.getCashCouponDiscount());
                    managerBillingDetailResponse.setFreight(orderBillingDetails.getFreight() == null ? 0 : orderBillingDetails.getFreight());
                    managerBillingDetailResponse.setMemberDiscount(orderBillingDetails.getMemberDiscount() == null ? 0 : orderBillingDetails.getMemberDiscount());
                    managerBillingDetailResponse.setSubvention(orderBillingDetails.getStoreSubvention() == null ? 0 : orderBillingDetails.getStoreSubvention());
                    managerBillingDetailResponse.setProductCouponDiscount(orderBillingDetails.getProductCouponDiscount() == null ? 0 : orderBillingDetails.getProductCouponDiscount());
                    managerBillingDetailResponse.setPreDeposit(orderBillingDetails.getStPreDeposit() == null ? 0 : orderBillingDetails.getStPreDeposit());
                    managerBillingDetailResponse.setCreditMoney(orderBillingDetails.getStoreCreditMoney() == null ? 0 : orderBillingDetails.getStoreCreditMoney());
                    managerBillingDetailResponse.setPromotionDiscount(orderBillingDetails.getPromotionDiscount() == null ? 0 : orderBillingDetails.getPromotionDiscount());
                    managerBillingDetailResponse.setTotalPrice(orderBaseInfo.getTotalGoodsPrice() == null ? 0 : orderBaseInfo.getTotalGoodsPrice());
                    PayhelperOrder payhelperOrder = this.appOrderService.findPayhelperOrderByOrdNo(orderNumber);
                    if (null != payhelperOrder){
                        managerBillingDetailResponse.setPayForAnotherMoney(null == payhelperOrder.getPayhelperAmount() ? 0 : payhelperOrder.getPayhelperAmount());
                    }
                    orderDetailsResponse.setManagerBillingDetailResponse(managerBillingDetailResponse);
                } else {
                    //导购
                    SellerBillingDetailResponse sellerBillingDetailResponse = new SellerBillingDetailResponse();
                    sellerBillingDetailResponse.setAmountPayable(orderBillingDetails.getAmountPayable() == null ? 0 : orderBillingDetails.getAmountPayable());
                    sellerBillingDetailResponse.setCouponDiscount(orderBillingDetails.getCashCouponDiscount() == null ? 0 : orderBillingDetails.getCashCouponDiscount());
                    sellerBillingDetailResponse.setCreditMoney(orderBillingDetails.getEmpCreditMoney() == null ? 0 : orderBillingDetails.getEmpCreditMoney());
                    sellerBillingDetailResponse.setFreight(orderBillingDetails.getFreight() == null ? 0 : orderBillingDetails.getFreight());
                    sellerBillingDetailResponse.setMemberDiscount(orderBillingDetails.getMemberDiscount() == null ? 0 : orderBillingDetails.getMemberDiscount());
                    sellerBillingDetailResponse.setPreDeposit(orderBillingDetails.getStPreDeposit() == null ? 0 : orderBillingDetails.getStPreDeposit());
                    sellerBillingDetailResponse.setProductCouponDiscount(orderBillingDetails.getProductCouponDiscount() == null ? 0 : orderBillingDetails.getProductCouponDiscount());
                    sellerBillingDetailResponse.setPromotionDiscount(orderBillingDetails.getPromotionDiscount() == null ? 0 : orderBillingDetails.getPromotionDiscount());
                    sellerBillingDetailResponse.setTotalPrice(orderBaseInfo.getTotalGoodsPrice() == null ? 0 : orderBaseInfo.getTotalGoodsPrice());

                    //2018-04-02 generation 导购订单详情加查看代收款
                    sellerBillingDetailResponse.setCollectionAmount(null == orderBillingDetails.getCollectionAmount() ? 0D : orderBillingDetails.getCollectionAmount());

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

    /**
     * 获取各状态订单数量
     *
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/AppOrderQuantity", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getAppOrderQuantity(Long userId, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("getAppOrderQuantity CALLED,获取App各状态订单数量，入参 userID:{}, identityType:{}", userId, identityType);
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getAppOrderQuantity OUT,获取App各状态订单数量失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getAppOrderQuantity OUT,获取App各状态订单数量失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            Map<String, Integer> quantity;
            if (0 == identityType || 2 == identityType) {
                //导购 装饰经理获取各状态订单数量
                quantity = appOrderService.getAppOrderQuantityByEmpId(userId);
            } else if (6 == identityType) {
                //顾客获取各状态订单数量
                quantity = appOrderService.getAppOrderQuantityByCusId(userId);
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
                logger.info("getAppOrderQuantity OUT,获取App各状态订单数量失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, quantity);
            logger.info("getAppOrderQuantity OUT,获取App各状态订单数量成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取App各状态订单数量失败", null);
            logger.warn("getAppOrderQuantity EXCEPTION,获取App各状态订单数量失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 处理货到付款的订单业务
     * @descripe
     * @author GenerationRoad
     * @date 2018/3/9
     */
    @PostMapping(value = "/handle/cashDelivery", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> handleOrderRelevantBusinessAfterOnlinePayCashDelivery(Long userId, Integer identityType, String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("handleOrderRelevantBusinessAfterOnlinePayCashDelivery CALLED,处理货到付款的订单业务，入参 userID:{}, identityType:{}, orderNumber{}", userId, identityType, orderNumber);
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("handleOrderRelevantBusinessAfterOnlinePayCashDelivery OUT,处理货到付款的订单业务失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("handleOrderRelevantBusinessAfterOnlinePayCashDelivery OUT,处理货到付款的订单业务失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!StringUtils.isNotBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单信息不允许为空！", null);
            logger.info("handleOrderRelevantBusinessAfterOnlinePayCashDelivery OUT,处理货到付款的订单业务失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            this.commonService.handleOrderRelevantBusinessAfterOnlinePayCashDelivery(orderNumber, OnlinePayType.CASH_DELIVERY);
            //发送订单到拆单消息队列
            sinkSender.sendOrder(orderNumber);

            //发送订单到WMS
            OrderBaseInfo baseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            if (baseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                iCallWms.sendToWmsRequisitionOrderAndGoods(orderNumber);
            }

            // 激活订单赠送的产品券
            // productCouponService.activateCusProductCoupon(orderNumber);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("handleOrderRelevantBusinessAfterOnlinePayCashDelivery OUT,处理货到付款的订单业务成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，处理货到付款的订单业务失败", null);
            logger.warn("handleOrderRelevantBusinessAfterOnlinePayCashDelivery EXCEPTION,处理货到付款的订单业务失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 待付款订单检查是否超时
     *
     * @param userId
     * @param identityType
     * @param orderNumber
     * @return
     */
    @PostMapping(value = "/verify/timeout", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> verifyTimeout(Long userId, Integer identityType, String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("verifyTimeout CALLED,待付款订单检查是否超时，入参 userID:{}, identityType:{}, orderNumber{}", userId, identityType, orderNumber);

        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("verifyTimeout OUT,待付款订单检查是否超时失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("verifyTimeout OUT,待付款订单检查是否超时失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!StringUtils.isNotBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单编号不允许为空！", null);
            logger.info("verifyTimeout OUT,待付款订单检查是否超时失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Date date = new Date();
        try {
            OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            if (null == orderBaseInfo) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到此订单信息！", null);
                logger.info("verifyTimeout OUT,待付款订单检查是否超时失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (date.after(orderBaseInfo.getEffectiveEndTime())) {
                maOrderService.scanningUnpaidOrder(orderBaseInfo);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此订单已失效，请重新下单！", null);
                logger.info("verifyTimeout OUT,此订单已失效，请重新下单！，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                logger.info("verifyTimeout OUT，此订单待付款时间未超时，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，去支付失败", null);
            logger.warn("verifyTimeout EXCEPTION,发生未知异常，待付款订单检查是否超时失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 支付 金额为0的订单
     *
     * @param userId
     * @param identityType
     * @param orderNumber
     * @return
     */
    @PostMapping(value = "/pay/zero/order", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> payZeroOrder(Long userId, Integer identityType, String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("payZeroOrder CALLED,待付款订单，入参 userID:{}, identityType:{}, orderNumber{}", userId, identityType, orderNumber);

        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("payZeroOrder OUT,待付款订单，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("payZeroOrder OUT,待付款订单，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!StringUtils.isNotBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单编号不允许为空！", null);
            logger.info("payZeroOrder OUT,待付款订单，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        OrderBaseInfo orderBaseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
        if (null == orderBaseInfo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到此订单信息！", null);
            logger.info("payZeroOrder OUT,待付款订单，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        /** 校验订单状态 以及金额是否为 0 **/
        AppOrderStatus status = orderBaseInfo.getStatus();
        if (!status.equals(AppOrderStatus.UNPAID)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单状态不正确，非待付款状态！", null);
            logger.info("payZeroOrder OUT,待付款订单，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        // 应付金额
        Double amount = appOrderService.getAmountPayableByOrderNumber(orderNumber);

        if (!amount.equals(0D)) {
            // 应付金额不为0
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单未付清，金额有误！", null);
            logger.info("payZeroOrder OUT,待付款订单，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            commonService.payZeroOrder(orderNumber);

            //发送订单到拆单消息队列
            sinkSender.sendOrder(orderNumber);

            //发送订单到WMS
            OrderBaseInfo baseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            if (baseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                iCallWms.sendToWmsRequisitionOrderAndGoods(orderNumber);
            }

            // 激活订单赠送的产品券
            // productCouponService.activateCusProductCoupon(orderBaseInfo.getOrderNumber());

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("支付0元订单成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生异常，支付失败！", null);
            logger.info("payZeroOrder OUT,待付款订单，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
    }

    /**
     * @title   创建加盟门店自提单
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/4/4
     */
    @ApiOperation(value = "创建订单", notes = "创建订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cityId", value = "下单人城市id", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "userId", value = "下单人id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "identityType", value = "下单人身份类型", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "customerId", value = "顾客id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "remark", value = "备注信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "goodsInfo", value = "商品信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "productCouponInfo", value = "产品券商品信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "promotionInfo", value = "促销信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "deliveryInfo", value = "配送信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "billingInfo", value = "账单信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "auditNo", value = "物料审核单号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "salesNumber", value = "纸质单号", required = false, dataType = "String")
    })
    @PostMapping(value = "/create/JMSelfTake", produces = "application/json;charset=UTF-8")
//    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> createJMSelfTakeOrder(OrderCreateParam orderParam, HttpServletRequest request) {
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
        Long userId = orderParam.getUserId();
        if (null == orderParam.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不允许为空!", "");
            logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Integer identityType = orderParam.getIdentityType();
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

            //*************************** 转化前台提交过来的json类型参数 ************************

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
            List<Long> cashCouponList = new ArrayList<>();
            if (StringUtils.isNotBlank(orderParam.getCashCouponIds())) {
                cashCouponList = objectMapper.readValue(orderParam.getCashCouponIds(), cashCouponSimpleInfo);
            }
            //产品券信息
            List<ProductCouponSimpleInfo> productCouponList = new ArrayList<>();
            if (StringUtils.isNotBlank(orderParam.getProductCouponInfo())) {
                productCouponList = objectMapper.readValue(orderParam.getProductCouponInfo(), productCouponSimpleInfo);
            }

            //账单信息
            BillingSimpleInfo billing = objectMapper.readValue(orderParam.getBillingInfo(), BillingSimpleInfo.class);


            //**********************************开始创建订单 **************************
            //******************* 创建订单基础信息 *****************
            OrderBaseInfo orderBaseInfo = appOrderService.createOrderBaseInfo(orderParam.getCityId(), orderParam.getUserId(),
                    orderParam.getIdentityType(), orderParam.getCustomerId(), deliverySimpleInfo.getDeliveryType(), orderParam.getRemark(), orderParam.getSalesNumber());
            orderBaseInfo.setStatus(AppOrderStatus.FINISHED);

            //****************** 创建订单物流信息 ******************
            OrderLogisticsInfo orderLogisticsInfo = appOrderService.createOrderLogisticInfo(deliverySimpleInfo);
            orderLogisticsInfo.setOrdNo(orderBaseInfo.getOrderNumber());

            //****************** 创建订单商品信息 ******************
            CreateOrderGoodsSupport support = commonService.createOrderGoodsInfo(goodsList, orderParam.getUserId(), orderParam.getIdentityType(),
                    orderParam.getCustomerId(), productCouponList, orderBaseInfo.getOrderNumber());


            //****************** 处理订单账单相关信息 ***************
            OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
            orderBillingDetails.setOrderNumber(orderBaseInfo.getOrderNumber());
            orderBillingDetails.setIsOwnerReceiving(orderLogisticsInfo.getIsOwnerReceiving());
            orderBillingDetails.setTotalGoodsPrice(support.getGoodsTotalPrice());
            orderBillingDetails.setMemberDiscount(support.getMemberDiscount());
            orderBillingDetails.setPromotionDiscount(billing.getOrderDiscount());
            orderBillingDetails = appOrderService.createOrderBillingDetails(orderBillingDetails, orderParam.getUserId(), orderParam.getIdentityType(),
                    billing, cashCouponList, support.getProductCouponGoodsList());
            orderBillingDetails.setArrearage(0D);
            orderBillingDetails.setIsPayUp(true);
            orderBillingDetails.setPayUpTime(Calendar.getInstance().getTime());

            orderBaseInfo.setTotalGoodsPrice(orderBillingDetails.getTotalGoodsPrice());

            //****************** 处理订单账单支付明细信息 ************
            List<OrderBillingPaymentDetails> paymentDetails = commonService.createOrderBillingPaymentDetails(orderBaseInfo, orderBillingDetails);

            List<OrderGoodsInfo> orderGoodsInfoList = support.getPureOrderGoodsInfo();


            //******** 分摊完毕 计算退货 单价 ***************************
            orderGoodsInfoList = dutchService.countReturnPrice(orderGoodsInfoList, null, CountUtil.div(billing.getLeBiQuantity(), 10D), billing.getOrderDiscount());
            //orderGoodsInfoList = dutchService.countReturnPrice(orderGoodsInfoList);

            support.setOrderGoodsInfoList(orderGoodsInfoList);

            //**************** 2、持久化订单相关实体信息 ****************
            this.commonService.saveOrderRelevantInfo(orderBaseInfo, orderLogisticsInfo, orderGoodsInfoList, orderBillingDetails);

            //****** 清空当单购物车商品 ******
            commonService.clearOrderGoodsInMaterialList(orderParam.getUserId(), orderParam.getIdentityType(), goodsList, productCouponList);

            //添加订单生命周期
            appOrderService.addOrderLifecycle(OrderLifecycleType.PAYED,orderBaseInfo.getOrderNumber());

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    new CreateOrderResponse(orderBaseInfo.getOrderNumber(), Double.parseDouble(CountUtil.retainTwoDecimalPlaces(orderBillingDetails.getAmountPayable())), true, false));
            logger.info("createOrder OUT,订单创建成功,出参 resultDTO:{}", resultDTO);
            return resultDTO;

        } catch (LockStoreInventoryException | LockStorePreDepositException | LockCityInventoryException | LockCustomerCashCouponException |
                LockCustomerLebiException | LockCustomerPreDepositException | LockEmpCreditMoneyException | LockStoreCreditMoneyException |
                LockStoreSubventionException | SystemBusyException | LockCustomerProductCouponException | GoodsMultipartPriceException | GoodsNoPriceException |
                OrderPayableAmountException | DutchException | OrderCreditMoneyException | OrderDiscountException e) {
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
     * @title   导购获取需代付的待付款订单列表
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/5/11
     */
    @PostMapping(value = "/payForAnother/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getPayForAnotherOrderList(Long userId, Integer identityType, String keywords, Integer page, Integer size) {
        ResultDTO<Object> resultDTO;
        logger.info("getPayForAnotherOrderList CALLED,导购获取需代付的待付款订单列表，入参 userId:{}, identityType:{}, keywords{}, page{}, size{}",
                userId, identityType, keywords, page, size);
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getPayForAnotherOrderList OUT,导购获取需代付的待付款订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || 0 != identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getPayForAnotherOrderList OUT,导购获取需代付的待付款订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getPayForAnotherOrderList OUT,导购获取需代付的待付款订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getPayForAnotherOrderList OUT,导购获取需代付的待付款订单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //导购获取需代付的待付款订单列表
            PageInfo<OrderPageInfoVO> orderListResponsePageInfo = appOrderService.getFitOrderListPageInfoByUserIdAndIdentityType(userId, identityType, keywords, page, size);

            //创建一个返回对象list
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    new GridDataVO<OrderPageInfoVO>().transform(orderListResponsePageInfo));
            logger.info("getPayForAnotherOrderList OUT,导购获取需代付的待付款订单列表成功，出参 resultDTO:{}", orderListResponsePageInfo);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，导购获取需代付的待付款订单列表失败", null);
            logger.warn("getPayForAnotherOrderList EXCEPTION,导购获取需代付的待付款订单列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @title   用户获取订单详情
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/5/14
     */
    @PostMapping(value = "/payForAnother/detail", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getPayForAnotherOrderDetail(Long userId, Integer identityType, String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("getPayForAnotherOrderDetail CALLED,用户获取订单详情，入参 userId:{}, identityType:{}, orderNumber:{}", userId, identityType, orderNumber);
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getPayForAnotherOrderDetail OUT,用户获取订单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getPayForAnotherOrderDetail OUT,用户获取订单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空！", null);
            logger.info("getPayForAnotherOrderDetail OUT,用户获取订单详情失败，出参 resultDTO:{}", resultDTO);
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
                orderDetailsResponse.setRemark(StringUtils.isBlank(orderBaseInfo.getRemark()) ? "" : orderBaseInfo.getRemark());
                orderDetailsResponse.setCreateTime(sdf.format(orderBaseInfo.getCreateTime()));
                orderDetailsResponse.setStatus(orderBaseInfo.getStatus());
                orderDetailsResponse.setStatusDesc(orderBaseInfo.getStatus().getDescription());
                orderDetailsResponse.setPickUpCode(orderBaseInfo.getPickUpCode());

                orderDetailsResponse.setCustomerName(orderBaseInfo.getCreatorName());
                orderDetailsResponse.setCustomerPhone(orderBaseInfo.getCreatorPhone());

                orderDetailsResponse.setPayType(null == billingDetails.getOnlinePayType() ?
                        OnlinePayType.NO.getDescription() : billingDetails.getOnlinePayType().getDescription());
                orderDetailsResponse.setDeliveryType(orderBaseInfo.getDeliveryType().getDescription());
                if (orderEvaluation != null) {
                    orderDetailsResponse.setLogisticsStar(orderEvaluation.getLogisticsStar());
                    orderDetailsResponse.setProductStar(orderEvaluation.getProductStar());
                    orderDetailsResponse.setServiceStars(orderEvaluation.getServiceStars());
                }
                orderDetailsResponse.setIsEvaluated(orderBaseInfo.getIsEvaluated());

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
                    orderDetailsResponse.setEstateInfo(orderLogisticsInfo.getEstateInfo());
                }
                AppStore store = appStoreService.findByStoreCode(orderBaseInfo.getStoreCode());
                if (null != store) {
                    orderDetailsResponse.setStoreName(store.getStoreName());
                } else {
                    orderDetailsResponse.setStoreName("未知");
                }
                PayhelperOrder payhelperOrder = this.appOrderService.findPayhelperOrderByOrdNo(orderNumber);
                //获取订单账目明细
                OrderBillingDetails orderBillingDetails = appOrderService.getOrderBillingDetail(orderNumber);
                if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.DECORATE_MANAGER)) {
                    ManagerBillingDetailResponse managerBillingDetailResponse = new ManagerBillingDetailResponse();
                    managerBillingDetailResponse.setAmountPayable(orderBillingDetails.getAmountPayable() == null ? 0 : orderBillingDetails.getAmountPayable());
                    managerBillingDetailResponse.setCouponDiscount(orderBillingDetails.getCashCouponDiscount() == null ? 0 : orderBillingDetails.getCashCouponDiscount());
                    managerBillingDetailResponse.setFreight(orderBillingDetails.getFreight() == null ? 0 : orderBillingDetails.getFreight());
                    managerBillingDetailResponse.setMemberDiscount(orderBillingDetails.getMemberDiscount() == null ? 0 : orderBillingDetails.getMemberDiscount());
                    managerBillingDetailResponse.setSubvention(orderBillingDetails.getStoreSubvention() == null ? 0 : orderBillingDetails.getStoreSubvention());
                    managerBillingDetailResponse.setProductCouponDiscount(orderBillingDetails.getProductCouponDiscount() == null ? 0 : orderBillingDetails.getProductCouponDiscount());
                    managerBillingDetailResponse.setPreDeposit(orderBillingDetails.getStPreDeposit() == null ? 0 : orderBillingDetails.getStPreDeposit());
                    managerBillingDetailResponse.setCreditMoney(orderBillingDetails.getStoreCreditMoney() == null ? 0 : orderBillingDetails.getStoreCreditMoney());
                    managerBillingDetailResponse.setPromotionDiscount(orderBillingDetails.getPromotionDiscount() == null ? 0 : orderBillingDetails.getPromotionDiscount());
                    managerBillingDetailResponse.setTotalPrice(orderBaseInfo.getTotalGoodsPrice() == null ? 0 : orderBaseInfo.getTotalGoodsPrice());
                    managerBillingDetailResponse.setCollectionAmount(orderBillingDetails.getCollectionAmount() == null ? 0 : orderBillingDetails.getCollectionAmount());
                    if (null != payhelperOrder) {
                        managerBillingDetailResponse.setPayForAnotherMoney(null == payhelperOrder.getPayhelperAmount() ? 0 : payhelperOrder.getPayhelperAmount());
                        managerBillingDetailResponse.setPayType(payhelperOrder.getPayType().getDescription());
                    }
                    orderDetailsResponse.setManagerBillingDetailResponse(managerBillingDetailResponse);
                }
                if (orderBaseInfo.getCreatorIdentityType().equals(AppIdentityType.CUSTOMER)) {
                    //会员
                    CustomerBillingDetailResponse customerBillingDetailResponse = new CustomerBillingDetailResponse();
                    customerBillingDetailResponse.setAmountPayable(orderBillingDetails.getAmountPayable() == null ? 0 : orderBillingDetails.getAmountPayable());
                    customerBillingDetailResponse.setCouponDiscount(orderBillingDetails.getCashCouponDiscount() == null ? 0 : orderBillingDetails.getCashCouponDiscount());
                    customerBillingDetailResponse.setFreight(orderBillingDetails.getFreight() == null ? 0 : orderBillingDetails.getFreight());
                    customerBillingDetailResponse.setLeBiCashDiscount(orderBillingDetails.getLebiCashDiscount() == null ? 0 : orderBillingDetails.getLebiCashDiscount());
                    customerBillingDetailResponse.setMemberDiscount(orderBillingDetails.getMemberDiscount() == null ? 0 : orderBillingDetails.getMemberDiscount());
                    customerBillingDetailResponse.setPreDeposit(orderBillingDetails.getCusPreDeposit() == null ? 0 : orderBillingDetails.getCusPreDeposit());
                    customerBillingDetailResponse.setProductCouponDiscount(orderBillingDetails.getProductCouponDiscount() == null ? 0 : orderBillingDetails.getProductCouponDiscount());
                    customerBillingDetailResponse.setPromotionDiscount(orderBillingDetails.getPromotionDiscount() == null ? 0 : orderBillingDetails.getPromotionDiscount());
                    customerBillingDetailResponse.setTotalPrice(orderBaseInfo.getTotalGoodsPrice() == null ? 0 : orderBaseInfo.getTotalGoodsPrice());
                    customerBillingDetailResponse.setCollectionAmount(orderBillingDetails.getCollectionAmount() == null ? 0 : orderBillingDetails.getCollectionAmount());
                    if (null != payhelperOrder) {
                        customerBillingDetailResponse.setPayForAnotherMoney(null == payhelperOrder.getPayhelperAmount() ? 0 : payhelperOrder.getPayhelperAmount());
                        customerBillingDetailResponse.setPayType(payhelperOrder.getPayType().getDescription());
                    }
                    orderDetailsResponse.setCustomerBillingDetailResponse(customerBillingDetailResponse);
                }
                orderDetailsResponse.setGoodsList(appOrderService.getOrderGoodsList(orderNumber));
                if (orderBaseInfo.getOrderSubjectType() == AppOrderSubjectType.FIT){
                    orderDetailsResponse.setIsUseEmpCredit(Boolean.FALSE);
                    orderDetailsResponse.setIsUseStorePre(Boolean.TRUE);
                } else if (orderBaseInfo.getOrderSubjectType() == AppOrderSubjectType.STORE){
                    orderDetailsResponse.setIsUseEmpCredit(Boolean.TRUE);
                    orderDetailsResponse.setIsUseStorePre(Boolean.TRUE);
                }


                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, orderDetailsResponse);
                logger.info("getPayForAnotherOrderDetail OUT,用户获取订单详情成功，出参 resultDTO:{}", resultDTO);

                return resultDTO;
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到此订单！", null);
            logger.info("getPayForAnotherOrderDetail OUT,用户获取订单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，用户获取订单详情失败", null);
            logger.warn("getPayForAnotherOrderDetail EXCEPTION,用户获取订单详情失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @title   代支付订单支付
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/5/15
     */
    @PostMapping(value = "/handle/payForAnother", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> handleOrderRelevantBusinessAfterPayForAnother(Long userId, Integer identityType, String orderNumber,
                                                                           String payType, HttpServletRequest request) {
        ResultDTO<Object> resultDTO;
        logger.info("handleOrderRelevantBusinessAfterPayForAnother CALLED,代支付订单支付，入参 userID:{}, identityType:{}, orderNumber{},payType{}",
                userId, identityType, orderNumber, payType);
        //获取客户端ip地址
        String ipAddress = IpUtils.getIpAddress(request);
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("handleOrderRelevantBusinessAfterPayForAnother OUT,代支付订单支付失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("handleOrderRelevantBusinessAfterPayForAnother OUT,代支付订单支付失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!StringUtils.isNotBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单信息不允许为空！", null);
            logger.info("handleOrderRelevantBusinessAfterPayForAnother OUT,代支付订单支付失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!StringUtils.isNotBlank(payType) && (OrderBillingPaymentType.EMP_CREDIT != OrderBillingPaymentType.getOrderBillingPaymentTypeByValue(payType)
                || OrderBillingPaymentType.ST_PREPAY != OrderBillingPaymentType.getOrderBillingPaymentTypeByValue(payType))) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "支付方式不允许为空！", null);
            logger.info("handleOrderRelevantBusinessAfterPayForAnother OUT,代支付订单支付失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            OrderBaseInfo baseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            if (AppOrderStatus.UNPAID != baseInfo.getStatus()){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单操作错误！", null);
                logger.info("handleOrderRelevantBusinessAfterPayForAnother OUT,代支付订单支付失败，订单状态错误，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (OrderBillingPaymentType.EMP_CREDIT == OrderBillingPaymentType.getOrderBillingPaymentTypeByValue(payType)) {
                if (baseInfo.getDeliveryType() == AppDeliveryType.PRODUCT_COUPON){
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "买卷订单不能使用信用额度！", null);
                    logger.info("handleOrderRelevantBusinessAfterPayForAnother OUT,代支付订单支付失败，订单状态错误，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                EmpCreditMoney empCreditMoney = employeeService.findEmpCreditMoneyByEmpId(userId);
                OrderBillingDetails billingDetails = appOrderService.getOrderBillingDetail(orderNumber);
                if (null == empCreditMoney || null == billingDetails || empCreditMoney.getCreditLimitAvailable() < billingDetails.getAmountPayable()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "信用额度不足！", null);
                    logger.info("handleOrderRelevantBusinessAfterPayForAnother OUT,代支付订单支付失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (OrderBillingPaymentType.ST_PREPAY == OrderBillingPaymentType.getOrderBillingPaymentTypeByValue(payType)) {
                OrderBillingDetails billingDetails = appOrderService.getOrderBillingDetail(orderNumber);
                StorePreDeposit preDeposit = appStoreService.findStorePreDepositByUserIdAndIdentityType(userId, identityType);
                if (null == preDeposit || null == billingDetails || preDeposit.getBalance() < billingDetails.getAmountPayable()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店预存款余额不足！", null);
                    logger.info("handleOrderRelevantBusinessAfterPayForAnother OUT,代支付订单支付失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            this.commonService.handleOrderRelevantBusinessAfterPayForAnother(orderNumber, userId, identityType, payType, ipAddress);
            //发送订单到拆单消息队列
            sinkSender.sendOrder(orderNumber);

            //发送订单到WMS
            if (baseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                iCallWms.sendToWmsRequisitionOrderAndGoods(orderNumber);
            }

            // 激活订单赠送的产品券
            productCouponService.activateCusProductCoupon(orderNumber);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("handleOrderRelevantBusinessAfterPayForAnother OUT,代支付订单支付成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，代支付订单支付失败", null);
            logger.warn("handleOrderRelevantBusinessAfterPayForAnother EXCEPTION,代支付订单支付失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    @ApiOperation(value = "创建订单", notes = "创建订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cityId", value = "下单人城市id", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "userId", value = "下单人id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "identityType", value = "下单人身份类型", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "customerId", value = "顾客id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "remark", value = "备注信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "goodsInfo", value = "商品信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "productCouponInfo", value = "产品券商品信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "promotionInfo", value = "促销信息", required = false, dataType = "String"),
            @ApiImplicitParam(name = "deliveryInfo", value = "配送信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "billingInfo", value = "账单信息", required = true, dataType = "String"),
            @ApiImplicitParam(name = "auditNo", value = "物料审核单号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "salesNumber", value = "纸质单号", required = false, dataType = "String")
    })
    @PostMapping(value = "/create/BuyCouponOrder", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> createBuyCouponOrder(OrderCreateParam orderParam, HttpServletRequest request) {
        logger.info("createBuyCouponOrder CALLED,去支付生成订单,入参:{}", JSON.toJSONString(orderParam));
        System.out.println(JSON.toJSONString(orderParam));
        ResultDTO<Object> resultDTO;
        //获取客户端ip地址
        String ipAddress = IpUtils.getIpAddress(request);
        if (null == orderParam.getCityId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市id不允许为空!", "");
            logger.warn("createBuyCouponOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Long userId = orderParam.getUserId();
        if (null == orderParam.getUserId()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不允许为空!", "");
            logger.warn("createBuyCouponOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Integer identityType = orderParam.getIdentityType();
        if (null == orderParam.getIdentityType()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份类型不允许为空!", "");
            logger.warn("createOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderParam.getGoodsInfo())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品信息不允许为空!", "");
            logger.warn("createBuyCouponOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderParam.getDeliveryInfo())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送信息不允许为空!", "");
            logger.warn("createBuyCouponOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        //判断创单人身份是否合法
        if (!(orderParam.getIdentityType() == 0 || orderParam.getIdentityType() == 6 || orderParam.getIdentityType() == 2)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "创单人身份不合法!", "");
            logger.warn("createBuyCouponOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            JavaType goodsSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSimpleInfo.class);
            JavaType cashCouponSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, Long.class);
            JavaType productCouponSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, ProductCouponSimpleInfo.class);
            JavaType promotionSimpleInfo = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, PromotionSimpleInfo.class);

            //*************************** 转化前台提交过来的json类型参数 ************************

            //商品信息
            List<GoodsSimpleInfo> goodsList = objectMapper.readValue(orderParam.getGoodsInfo(), goodsSimpleInfo);
            //配送信息
            DeliverySimpleInfo deliverySimpleInfo = objectMapper.readValue(orderParam.getDeliveryInfo(), DeliverySimpleInfo.class);
            AppStore store = appStoreService.findStoreByUserIdAndIdentityType(userId,identityType);
            deliverySimpleInfo.setBookingStoreId(store.getStoreId());
            deliverySimpleInfo.setBookingStoreCode(store.getStoreCode());
            deliverySimpleInfo.setBookingStoreName(store.getStoreName());
            deliverySimpleInfo.setBookingStoreAddress(store.getDetailedAddress());
            deliverySimpleInfo.setDeliveryType(AppDeliveryType.PRODUCT_COUPON.getValue());
            if (StringUtils.isBlank(deliverySimpleInfo.getDeliveryType())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送方式不允许为空!", "");
                logger.warn("createBuyCouponOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            //优惠券信息
            List<Long> cashCouponList = new ArrayList<>();
            if (StringUtils.isNotBlank(orderParam.getCashCouponIds())) {
                cashCouponList = objectMapper.readValue(orderParam.getCashCouponIds(), cashCouponSimpleInfo);
            }
            //产品券信息
            List<ProductCouponSimpleInfo> productCouponList = new ArrayList<>();
//            if (StringUtils.isNotBlank(orderParam.getProductCouponInfo())) {
//                productCouponList = objectMapper.readValue(orderParam.getProductCouponInfo(), productCouponSimpleInfo);
//            }
            //促销信息
            List<PromotionSimpleInfo> promotionSimpleInfoList = new ArrayList<>();
            if (StringUtils.isNotBlank(orderParam.getPromotionInfo())) {
                promotionSimpleInfoList = objectMapper.readValue(orderParam.getPromotionInfo(), promotionSimpleInfo);
            }
            // 检查促销是否过期
            List<Long> promotionIds = new ArrayList<>();
            for (PromotionSimpleInfo promotion : promotionSimpleInfoList) {
                promotionIds.add(promotion.getPromotionId());
            }
            if (promotionIds.size() > 0) {
                Boolean outTime = actService.checkActOutTime(promotionIds);
                if (!outTime) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "存在过期促销，请重新下单！", "");
                    logger.warn("createBuyCouponOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }

            //账单信息
            BillingSimpleInfo billing = objectMapper.readValue(orderParam.getBillingInfo(), BillingSimpleInfo.class);
            if (null != billing.getEmpCreditMoney() && billing.getEmpCreditMoney() > 0) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "购买产品卷不能使用信用额度！", "");
                logger.warn("createBuyCouponOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }


            //如果是导购下单并且是四川直营门店，判断销售纸质单号是否为空
            if (orderParam.getIdentityType() == AppIdentityType.SELLER.getValue()) {
                AppEmployee employee = appEmployeeService.findById(orderParam.getUserId());
                City city = cityService.findById(orderParam.getCityId());
                AppStore appStore = appStoreService.findById(employee.getStoreId());
                if ("ZY".equals(appStore.getStoreType().getValue()) && ("FZY009".equals(appStore.getStoreCode()) || "HLC004".equals(appStore.getStoreCode()) || "ML001".equals(appStore.getStoreCode()) || "QCMJ008".equals(appStore.getStoreCode()) ||
                        "SB010".equals(appStore.getStoreCode()) || "YC002".equals(appStore.getStoreCode()) || "ZC002".equals(appStore.getStoreCode()) || "RC005".equals(appStore.getStoreCode()) ||
                        "FZM007".equals(appStore.getStoreCode()) || "SH001".equals(appStore.getStoreCode()) || "YJ001".equals(appStore.getStoreCode()) || "HS001".equals(appStore.getStoreCode()) ||
                        "XC001".equals(appStore.getStoreCode()))) {
                    if (StringUtils.isBlank(orderParam.getSalesNumber())) {
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "四川直营门店销售纸质单号不能为空！", "");
                        logger.warn("createBuyCouponOrder OUT,创建订单失败,出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
            }


            //******************* 创建订单基础信息 *****************
            OrderBaseInfo orderBaseInfo = appOrderService.createOrderBaseInfo(orderParam.getCityId(), orderParam.getUserId(),
                    orderParam.getIdentityType(), orderParam.getCustomerId(), deliverySimpleInfo.getDeliveryType(), orderParam.getRemark(), orderParam.getSalesNumber());

            //****************** 创建订单物流信息 ******************
            OrderLogisticsInfo orderLogisticsInfo = appOrderService.createOrderLogisticInfo(deliverySimpleInfo);
            orderLogisticsInfo.setOrdNo(orderBaseInfo.getOrderNumber());

            //****************** 创建订单商品信息 ******************
            CreateOrderGoodsSupport support = commonService.createOrderGoodsInfo(goodsList, orderParam.getUserId(), orderParam.getIdentityType(),
                    orderParam.getCustomerId(), productCouponList, orderBaseInfo.getOrderNumber());

            //****************** 创建订单券信息 *********************
            List<OrderCouponInfo> orderCouponInfoList = new ArrayList<>();

            //****************** 创建订单优惠券信息 *****************
            List<OrderCouponInfo> orderCashCouponInfoList = commonService.createOrderCashCouponInfo(orderBaseInfo, cashCouponList);
            if (null != orderCashCouponInfoList && orderCashCouponInfoList.size() > 0) {
                orderCouponInfoList.addAll(orderCashCouponInfoList);
            }
            //****************** 创建订单产品券信息 *****************
            List<OrderCouponInfo> orderProductCouponInfoList = new ArrayList<>();

            //****************** 处理订单账单相关信息 ***************
            OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
            orderBillingDetails.setOrderNumber(orderBaseInfo.getOrderNumber());
            orderBillingDetails.setIsOwnerReceiving(orderLogisticsInfo.getIsOwnerReceiving());
            orderBillingDetails.setTotalGoodsPrice(support.getGoodsTotalPrice());
            orderBillingDetails.setMemberDiscount(support.getMemberDiscount());
            orderBillingDetails.setPromotionDiscount(billing.getOrderDiscount());

            orderBillingDetails = appOrderService.createOrderBillingDetails(orderBillingDetails, orderParam.getUserId(), orderParam.getIdentityType(),
                    billing, cashCouponList, support.getProductCouponGoodsList());

            orderBaseInfo.setTotalGoodsPrice(orderBillingDetails.getTotalGoodsPrice());

            //****************** 处理订单账单支付明细信息 ************
            List<OrderBillingPaymentDetails> paymentDetails = commonService.createOrderBillingPaymentDetails(orderBaseInfo, orderBillingDetails);

            /********* 开始计算分摊 促销分摊可能产生新的行记录 所以优先分摊 ******************/
            List<OrderGoodsInfo> orderGoodsInfoList;
            orderGoodsInfoList = dutchService.addGoodsDetailsAndDutch(orderParam.getUserId(), AppIdentityType.getAppIdentityTypeByValue(orderParam.getIdentityType()), promotionSimpleInfoList, support.getPureOrderGoodsInfo(), orderParam.getCustomerId());

            //******** 分摊现乐币 策略：每个商品 按单价占比 分摊 *********************
            // 乐币暂时不分摊
//            Integer leBiQty = billing.getLeBiQuantity();
//            orderGoodsInfoList = leBiDutchService.LeBiDutch(leBiQty, orderGoodsInfoList);

            //******** 分摊现现金返利 策略：每个商品 按单价占比 分摊 *********************
            Double cashReturnAmount = billing.getStoreSubvention();
            orderGoodsInfoList = cashReturnDutchService.cashReturnDutch(cashReturnAmount, orderGoodsInfoList);

            //******** 分摊现金券 策略：使用范围商品 按单价占比 分摊 *********************
            orderGoodsInfoList = cashCouponDutchService.cashCouponDutch(cashCouponList, orderGoodsInfoList);

            //******** 分摊完毕 计算退货 单价 ***************************
            orderGoodsInfoList = dutchService.countReturnPrice(orderGoodsInfoList, cashReturnAmount, CountUtil.div(billing.getLeBiQuantity(), 10D), billing.getOrderDiscount());
            //orderGoodsInfoList = dutchService.countReturnPrice(orderGoodsInfoList);

            //将产品券商品加入 分摊完毕的商品列表中
            orderGoodsInfoList.addAll(support.getProductCouponGoodsList());
            support.setOrderGoodsInfoList(orderGoodsInfoList);

            //****************** 创建订单经销差价返还明细 ***********
            List<OrderJxPriceDifferenceReturnDetails> jxPriceDifferenceReturnDetailsList = new ArrayList<>();
//            List<OrderJxPriceDifferenceReturnDetails> jxPriceDifferenceReturnDetailsList = commonService.createOrderJxPriceDifferenceReturnDetails(orderBaseInfo, support.getOrderGoodsInfoList(), promotionSimpleInfoList);
//            if (null != jxPriceDifferenceReturnDetailsList && jxPriceDifferenceReturnDetailsList.size() > 0) {
//                orderBillingDetails.setJxPriceDifferenceAmount(jxPriceDifferenceReturnDetailsList.stream().mapToDouble(OrderJxPriceDifferenceReturnDetails::getAmount).sum());
//            }

            //添加商品专供标志
            orderGoodsInfoList = this.commonService.addGoodsSign(orderGoodsInfoList, orderBaseInfo);

            //**************** 1、检查库存和与账单支付金额是否充足,如果充足就扣减相应的数量 ***********
            //**************** 2、持久化订单相关实体信息 ****************
            transactionalSupportService.createOrderBusiness(deliverySimpleInfo, support.getInventoryCheckMap(), orderParam.getCityId(), orderParam.getIdentityType(),
                    orderParam.getUserId(), orderParam.getCustomerId(), cashCouponList, orderProductCouponInfoList, orderBillingDetails, orderBaseInfo,
                    orderLogisticsInfo, orderGoodsInfoList, orderCouponInfoList, paymentDetails, jxPriceDifferenceReturnDetailsList, ipAddress, promotionSimpleInfoList);

            //****** 清空当单购物车商品 ******
            commonService.clearOrderGoodsInMaterialList(orderParam.getUserId(), orderParam.getIdentityType(), goodsList, productCouponList);

            if (orderBillingDetails.getAmountPayable() <= AppConstant.PAY_UP_LIMIT) {
                //如果预存款或信用金已支付完成直接发送到WMS出货单
                if (orderBaseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                    iCallWms.sendToWmsRequisitionOrderAndGoods(orderBaseInfo.getOrderNumber());
                }
                //将该订单入拆单消息队列
                sinkSender.sendOrder(orderBaseInfo.getOrderNumber());
                //添加订单生命周期
                appOrderService.addOrderLifecycle(OrderLifecycleType.PAYED, orderBaseInfo.getOrderNumber());

                // 激活订单赠送的产品券
                // productCouponService.activateCusProductCoupon(orderBaseInfo.getOrderNumber());

                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CreateOrderResponse(orderBaseInfo.getOrderNumber(), Double.parseDouble(CountUtil.retainTwoDecimalPlaces(orderBillingDetails.getAmountPayable())), true, false));
                logger.info("createBuyCouponOrder OUT,订单创建成功,出参 resultDTO:{}", resultDTO);
            } else {
                //判断是否可选择货到付款
//                Boolean isCashDelivery = this.commonService.checkCashDelivery(orderGoodsInfoList, userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
                Boolean isCashDelivery = Boolean.FALSE;
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                        new CreateOrderResponse(orderBaseInfo.getOrderNumber(), Double.parseDouble(CountUtil.retainTwoDecimalPlaces(orderBillingDetails.getAmountPayable())), false, isCashDelivery));
                logger.info("createBuyCouponOrder OUT,订单创建成功,出参 resultDTO:{}", resultDTO);
            }
            return resultDTO;
        } catch (LockStoreInventoryException | LockStorePreDepositException | LockCityInventoryException | LockCustomerCashCouponException |
                LockCustomerLebiException | LockCustomerPreDepositException | LockEmpCreditMoneyException | LockStoreCreditMoneyException |
                LockStoreSubventionException | SystemBusyException | LockCustomerProductCouponException | GoodsMultipartPriceException | GoodsNoPriceException |
                OrderPayableAmountException | DutchException | OrderCreditMoneyException | OrderDiscountException | GoodsQtyErrorException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, e.getMessage(), null);
            logger.warn("createBuyCouponOrder OUT,订单创建失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (IOException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单参数转换异常!", null);
            logger.warn("createBuyCouponOrder EXCEPTION,订单创建失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (OrderSaveException e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单创建异常!", null);
            logger.warn("createBuyCouponOrder EXCEPTION,订单创建失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常,下单失败!", null);
            logger.warn("createBuyCouponOrder EXCEPTION,订单创建失败,出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @title   处理信用额度支付的订单业务
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/6/28
     */
    @PostMapping(value = "/handle/payCredit", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> handleOrderRelevantBusinessAfterPayCredit(Long userId, Integer identityType, String orderNumber,
                                                                           String payType, HttpServletRequest request) {
        ResultDTO<Object> resultDTO;
        logger.info("handleOrderRelevantBusinessAfterPayCredit CALLED,处理信用额度支付的订单业务，入参 userID:{}, identityType:{}, orderNumber{},payType{}",
                userId, identityType, orderNumber, payType);
        //获取客户端ip地址
        String ipAddress = IpUtils.getIpAddress(request);
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("handleOrderRelevantBusinessAfterPayCredit OUT,处理信用额度支付的订单业务失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("handleOrderRelevantBusinessAfterPayCredit OUT,处理信用额度支付的订单业务失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!StringUtils.isNotBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单信息不允许为空！", null);
            logger.info("handleOrderRelevantBusinessAfterPayCredit OUT,处理信用额度支付的订单业务失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!StringUtils.isNotBlank(payType) && (OrderBillingPaymentType.EMP_CREDIT != OrderBillingPaymentType.getOrderBillingPaymentTypeByValue(payType)
                || OrderBillingPaymentType.STORE_CREDIT_MONEY != OrderBillingPaymentType.getOrderBillingPaymentTypeByValue(payType))) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "支付方式不允许为空！", null);
            logger.info("handleOrderRelevantBusinessAfterPayCredit OUT,处理信用额度支付的订单业务失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            OrderBaseInfo baseInfo = appOrderService.getOrderByOrderNumber(orderNumber);
            if (AppOrderStatus.UNPAID != baseInfo.getStatus()){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单操作错误！", null);
                logger.info("handleOrderRelevantBusinessAfterPayCredit OUT,处理信用额度支付的订单业务失败，订单状态错误，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (baseInfo.getDeliveryType() == AppDeliveryType.PRODUCT_COUPON){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "买卷订单不能使用信用额度！", null);
                logger.info("handleOrderRelevantBusinessAfterPayCredit OUT,处理信用额度支付的订单业务失败，订单状态错误，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            OrderBillingDetails billingDetails = appOrderService.getOrderBillingDetail(orderNumber);
            if (OrderBillingPaymentType.EMP_CREDIT == OrderBillingPaymentType.getOrderBillingPaymentTypeByValue(payType)) {
                EmpCreditMoney empCreditMoney = employeeService.findEmpCreditMoneyByEmpId(userId);
                if (null == empCreditMoney || null == billingDetails || empCreditMoney.getCreditLimitAvailable() < billingDetails.getAmountPayable()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "信用额度不足，请更换支付方式！", null);
                    logger.info("handleOrderRelevantBusinessAfterPayCredit OUT,处理信用额度支付的订单业务失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            if (OrderBillingPaymentType.STORE_CREDIT_MONEY == OrderBillingPaymentType.getOrderBillingPaymentTypeByValue(payType)) {
                StoreCreditMoney storeCreditMoney = this.appStoreService.findStoreCreditMoneyByEmpId(userId);
                if (null == storeCreditMoney || null == billingDetails || storeCreditMoney.getCreditLimitAvailable() < billingDetails.getAmountPayable()) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "信用金余额不足，请更换支付方式！", null);
                    logger.info("handleOrderRelevantBusinessAfterPayCredit OUT,处理信用额度支付的订单业务失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            this.commonService.handleOrderRelevantBusinessAfterPayCredit(orderNumber, userId, identityType, payType, ipAddress);
            //发送订单到拆单消息队列
            sinkSender.sendOrder(orderNumber);

            //发送订单到WMS
            if (baseInfo.getDeliveryType() == AppDeliveryType.HOUSE_DELIVERY) {
                iCallWms.sendToWmsRequisitionOrderAndGoods(orderNumber);
            }

            // 激活订单赠送的产品券
            productCouponService.activateCusProductCoupon(orderNumber);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("handleOrderRelevantBusinessAfterPayCredit OUT,处理信用额度支付的订单业务成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，处理信用额度支付的订单业务失败", null);
            logger.warn("handleOrderRelevantBusinessAfterPayCredit EXCEPTION,处理信用额度支付的订单业务失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @title   计算运费
     * @descripe
     * @param
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2018/7/12
     */
    @PostMapping(value = "/deliveryFee", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getDeliveryFee(Long userId, Integer identityType, Long cityId, String countyName, String goodsList, String totalOrderAmount) {
        ResultDTO<Object> resultDTO;
        logger.info("deliveryFee CALLED,计算运费，入参 userId:{}, identityType:{}, cityId:{}, countyName:{}, totalOrderAmount:{}, goodsList:{}",
                userId, identityType, cityId, countyName, totalOrderAmount, goodsList);
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("deliveryFee OUT,计算运费失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("deliveryFee OUT,计算运费失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == cityId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "城市不能为空",
                    null);
            logger.info("deliveryFee OUT,计算运费失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == countyName) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("deliveryFee OUT,计算运费失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!(null != goodsList && goodsList.length() > 0)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品列表", null);
            logger.warn("deliveryFee OUT,计算运费失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == totalOrderAmount) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单金额", null);
            logger.warn("deliveryFee OUT,计算运费失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        Double amount;
        try {
            amount = Double.parseDouble(totalOrderAmount);
        } catch (Exception e) {
            amount = 0D;
        }
        try {
            List<DeliveryFeeRule> ruleList = deliveryFeeRuleService.findRuleByCityIdAndCountyName(cityId, countyName);
            if (null == ruleList || ruleList.size() == 0){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "此地区还未开通配送服务，请联系客服为您开通", null);
                logger.warn("deliveryFee OUT,计算运费失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, OrderGoodsSimpleResponse.class);
            List<OrderGoodsSimpleResponse> simpleInfos = objectMapper.readValue(goodsList, javaType1);
            Double deliveryFee = this.deliveryFeeRuleService.countDeliveryFeeNew(identityType, cityId, amount, simpleInfos, countyName);
            //创建一个返回对象list
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, deliveryFee);
            logger.info("deliveryFee OUT,计算运费成功，出参 resultDTO:{}", deliveryFee);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，计算运费失败", null);
            logger.warn("deliveryFee EXCEPTION,计算运费失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

}