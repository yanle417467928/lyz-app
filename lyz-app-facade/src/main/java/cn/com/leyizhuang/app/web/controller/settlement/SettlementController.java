package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.core.constant.AppConstant;
import cn.com.leyizhuang.app.core.constant.AppGoodsLineType;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.GiftListResponseGoods;
import cn.com.leyizhuang.app.foundation.pojo.response.SelfTakeStore;
import cn.com.leyizhuang.app.foundation.pojo.response.SelfTakeStoreResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 结算页面控制器
 *
 * @author Richard
 *         Created on 2017-11-22 13:14
 **/
@RestController
@RequestMapping(value = "/app/settlement")
public class SettlementController {

    private static final Logger logger = LoggerFactory.getLogger(SettlementController.class);

    @Resource
    private GoodsService goodsService;

    @Resource
    private AppStoreService storeService;

    @Resource
    private AppCustomerService customerService;

    @Resource
    private AppOrderService orderService;

    @Autowired
    private GoodsPriceService goodsPriceService;


    /**
     * 获取下单时可用自提门店信息
     *
     * @param userId       用户id
     * @param identityType 身份类型
     * @param goodsIds     商品id
     * @return 可选自提门店信息
     */
    @RequestMapping(value = "/get/selfTakeStore/available", method = RequestMethod.POST)
    public ResultDTO<Object> getSelfTakeStoreAvailable(Long userId, Integer identityType, @RequestParam(value = "goodsIds",
            required = false) Long[] goodsIds) {
        logger.info("getSelfTakeStoreAvailable CALLED,获取可选自提门店信息，入参 userId:{},identityType:{},goodsIds:{}",
                userId, identityType, goodsIds);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户ID不允许为空", null);
            logger.warn("getSelfTakeStoreAvailable OUT,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType || identityType == AppIdentityType.DECORATE_MANAGER.getValue()
                || identityType == AppIdentityType.DECORATE_EMPLOYEE.getValue()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该身份类型不允许门店自提,", null);
            logger.warn("getSelfTakeStoreAvailable OUT,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == goodsIds) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品ID不允许为空", null);
            logger.warn("getSelfTakeStoreAvailable OUT,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } else if (goodsIds.length == 0) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品ID不允许为空", null);
            logger.warn("getSelfTakeStoreAvailable OUT,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            AppStore store = storeService.findStoreByUserIdAndIdentityType(userId, identityType);
            Integer hour = getHour();
            if (identityType == AppIdentityType.SELLER.getValue() && (hour < 6 || hour > 19) && "ZY".equals(store.getStoreType().getValue()) && "2121".equals(store.getCityCode())) {
                if ("FZY009".equals(store.getStoreCode()) || "HLC004".equals(store.getStoreCode()) || "ML001".equals(store.getStoreCode()) || "QCMJ008".equals(store.getStoreCode()) ||
                        "SB010".equals(store.getStoreCode()) || "YC002".equals(store.getStoreCode()) || "ZC002".equals(store.getStoreCode()) || "RC005".equals(store.getStoreCode()) ||
                        "FZM007".equals(store.getStoreCode()) || "SH001".equals(store.getStoreCode()) || "YJ001".equals(store.getStoreCode()) || "HS001".equals(store.getStoreCode()) ||
                        "XC001".equals(store.getStoreCode())){
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该时段此门店不支持门店自提！", null);
                    logger.warn("chooseSelfTakeStore OUT,选择自提门店失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }


            //商品id数组转化成list
            List<Long> goodsIdList = Arrays.asList(goodsIds);
            String[] forbiddenSelfTakeCompany = AppConstant.FORBIDDEN_SELF_TAKE_COMPANY_FLAG.split("\\|");
            List<String> forbiddenSelfTakeCompanyList = Arrays.asList(forbiddenSelfTakeCompany);
            List<String> companyFlag = goodsService.findCompanyFlagListById(goodsIdList);
            SelfTakeStoreResponse response = new SelfTakeStoreResponse();
            List<SelfTakeStore> selfTakeStoreList = new ArrayList<>();

            //判断商品是否有专供商品
            List<GiftListResponseGoods> goodsZGList = this.goodsPriceService.findGoodsPriceListByGoodsIdsAndUserId(goodsIdList, userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
            //有专供商品只能选择送货上门
            if (null != goodsZGList && goodsZGList.size() > 0) {
                response.setIsSelfTakePermitted(false);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response);
                logger.info("getSelfTakeStoreAvailable CALLED,获取可选自提门店信息成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (null != companyFlag && !companyFlag.isEmpty()) {
                for (String flag : companyFlag) {
                    if (forbiddenSelfTakeCompanyList.contains(flag)) {

                        /** 提示 哪些商品不能配送 2018-04-13 15：52**/
                        List<String> goodsNames = goodsService.findGoodsByCompanyFlagAndIds(goodsIdList,companyFlag);
                        StringBuffer msg = new StringBuffer();

                        for (int i =0 ;i< goodsNames.size(); i++){
                            msg.append("“"+goodsNames.get(i)+"”");

                            if (i == 2){
                                msg.append("等");
                                break;
                            }
                        }
                        msg.append("属于不能自提商品！");


                        response.setIsSelfTakePermitted(false);
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, msg.toString(), response);
                        logger.info("getSelfTakeStoreAvailable CALLED,获取可选自提门店信息成功，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
                if (!store.getIsSelfDelivery()) {
                    response.setIsSelfTakePermitted(false);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response);
                    logger.info("getSelfTakeStoreAvailable CALLED,获取可选自提门店信息成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                //导购下单，或非默认门店顾客下单，带出其所在门店信息
                if (identityType.equals(AppIdentityType.SELLER.getValue()) ||
                        (identityType.equals(AppIdentityType.CUSTOMER.getValue()) && !store.getIsDefault())) {
                    response.setIsSelfTakePermitted(true);
                    SelfTakeStore selfTakeStore = new SelfTakeStore();
                    selfTakeStore.setStoreId(store.getStoreId());
                    selfTakeStore.setStoreName(store.getStoreName());
                    selfTakeStore.setDetailedAddress(store.getDetailedAddress());
                    selfTakeStoreList.add(selfTakeStore);
                    response.setStoreList(selfTakeStoreList);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response);
                    logger.info("getSelfTakeStoreAvailable CALLED,获取可选自提门店信息成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                //默认门店顾客下单
                if (identityType.equals(AppIdentityType.CUSTOMER.getValue()) && store.getIsDefault()) {
                    AppCustomer customer = customerService.findById(userId);
                    response.setIsSelfTakePermitted(true);
                    selfTakeStoreList = storeService.findSelfTakePermittedStoreByCityId(customer.getCityId());
                    response.setStoreList(selfTakeStoreList);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response);
                    logger.info("getSelfTakeStoreAvailable CALLED,获取可选自提门店信息成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }

            }
            response.setIsSelfTakePermitted(false);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response);
            logger.info("getSelfTakeStoreAvailable CALLED,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取可选自提门店信息失败",
                    null);
            logger.warn("getSelfTakeStoreAvailable EXCEPTION,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }


    /**
     * 客户选择自提门店时检核
     * 该门店库存是否充足
     *
     * @param userId       用户id
     * @param identityType 身份类型
     * @param storeId      门店id
     * @param goodsList    商品信息列表
     * @return 门店库存是否充足
     */
    @RequestMapping(value = "/selfTakeStore/choose", method = RequestMethod.POST)
    public ResultDTO<Object> chooseSelfTakeStore(Long userId, Integer identityType, Long storeId, String goodsList) {
        logger.info("chooseSelfTakeStore CALLED,选择自提门店,入参 userId:{},identityType:{},storeId:{},goodsList:{}",
                userId, identityType, storeId, goodsList);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户ID不允许为空", null);
            logger.warn("chooseSelfTakeStore OUT,选择自提门店失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "身份类型不允许为空", null);
            logger.warn("chooseSelfTakeStore OUT,选择自提门店失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == storeId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店id不允许为空", null);
            logger.warn("chooseSelfTakeStore OUT,选择自提门店失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!(null != goodsList && goodsList.length() > 0)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品ID不允许为空", null);
            logger.warn("chooseSelfTakeStore OUT,选择自提门店失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsSimpleInfo.class);
            List<GoodsSimpleInfo> simpleInfos = objectMapper.readValue(goodsList, javaType1);
            Map<Long, Integer> goodsQuantity = new HashMap<>();
            AppStore store = this.storeService.findById(storeId);
            for (GoodsSimpleInfo info : simpleInfos) {
                if (!goodsQuantity.containsKey(info.getId())) {
                    goodsQuantity.put(info.getId(), info.getQty());
                } else {
                    goodsQuantity.put(info.getId(), info.getQty() + goodsQuantity.get(info.getId()));
                }

                /**************/
                //2018-04-03 generation 加盟门店自提不能用产品卷
                if (null != store && StoreType.JM == store.getStoreType() && info.getGoodsLineType().equals(AppGoodsLineType.PRODUCT_COUPON.toString())) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "包含不支持自提的商品!", null);
                    logger.info("chooseSelfTakeStore OUT,选择自提门店失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                /**************/
            }

            /**************/
            //2018-04-03 generation 加盟门店自提不用判断库存

            if(null != store && StoreType.JM == store.getStoreType()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
                logger.info("chooseSelfTakeStore OUT,选择自提门店成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            /**************/


            for (Map.Entry<Long, Integer> entry : goodsQuantity.entrySet()) {
                GoodsDO goodsDO = goodsService.findGoodsById(entry.getKey());
                Boolean enoughInvFlag = orderService.existGoodsStoreInventory(storeId, entry.getKey(), entry.getValue());
                if (!enoughInvFlag) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该门店 '" + goodsDO.getSkuName()
                            + "' 库存不足，无法门店自提!", null);
                    logger.info("chooseSelfTakeStore OUT,选择自提门店失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("chooseSelfTakeStore OUT,选择自提门店成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,选择自提门店失败",
                    null);
            logger.warn("chooseSelfTakeStore EXCEPTION,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }

    /**
     * 得到现在小时
     */
    public static Integer getHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String hour;
        hour = dateString.substring(11, 13);
        return Integer.parseInt(hour);
    }
}
