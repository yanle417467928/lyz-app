package cn.com.leyizhuang.app.web.controller.settlement;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.ApplicationConstant;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.SelfTakeStore;
import cn.com.leyizhuang.app.foundation.pojo.response.SelfTakeStoreResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.service.AppCustomerService;
import cn.com.leyizhuang.app.foundation.service.AppOrderService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * 结算页面控制器
 *
 * @author Richard
 * Created on 2017-11-22 13:14
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
            //商品id数组转化成list
            List<Long> goodsIdList = Arrays.asList(goodsIds);
            String[] forbiddenSelfTakeCompany = ApplicationConstant.FORBIDDEN_SELF_TAKE_COMPANY_FLAG.split("|");
            List<String> forbiddenSelfTakeCompanyList = Arrays.asList(forbiddenSelfTakeCompany);
            List<String> companyFlag = goodsService.findCompanyFlagListById(goodsIdList);
            SelfTakeStoreResponse response = new SelfTakeStoreResponse();
            List<SelfTakeStore> selfTakeStoreList = new ArrayList<>();
            if (null != companyFlag && !companyFlag.isEmpty()) {
                for (String flag : companyFlag) {
                    if (forbiddenSelfTakeCompanyList.contains(flag)) {
                        response.setIsSelfTakePermitted(false);
                        resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response);
                        logger.info("getCityDeliveryTime CALLED,获取可选自提门店信息成功，出参 resultDTO:{}", resultDTO);
                        return resultDTO;
                    }
                }
                AppStore store = storeService.findStoreByUserIdAndIdentityType(userId, identityType);
                if (!store.getIsSelfDelivery()) {
                    response.setIsSelfTakePermitted(false);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response);
                    logger.info("getCityDeliveryTime CALLED,获取可选自提门店信息成功，出参 resultDTO:{}", resultDTO);
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
                    logger.info("getCityDeliveryTime CALLED,获取可选自提门店信息成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                //默认门店顾客下单
                if (identityType.equals(AppIdentityType.CUSTOMER.getValue()) && store.getIsDefault()) {
                    AppCustomer customer = customerService.findById(userId);
                    response.setIsSelfTakePermitted(true);
                    selfTakeStoreList = storeService.findSelfTakePermittedStoreByCityId(customer.getCityId());
                    response.setStoreList(selfTakeStoreList);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response);
                    logger.info("getCityDeliveryTime CALLED,获取可选自提门店信息成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }

            }
            response.setIsSelfTakePermitted(false);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, response);
            logger.info("getCityDeliveryTime CALLED,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取可选自提门店信息失败",
                    null);
            logger.warn("getCityDeliveryTime EXCEPTION,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
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
            for (GoodsSimpleInfo info : simpleInfos) {
                if (!goodsQuantity.containsKey(info.getId())) {
                    goodsQuantity.put(info.getId(), info.getNum());
                } else {
                    goodsQuantity.put(info.getId(), info.getNum() + goodsQuantity.get(info.getId()));
                }
            }
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
            logger.info("chooseSelfTakeStore OUT,选择自提门店失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,选择自提门店失败",
                    null);
            logger.warn("getCityDeliveryTime EXCEPTION,获取可选自提门店信息失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }
}
