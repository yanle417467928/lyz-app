package cn.com.leyizhuang.app.web.controller.user;

import cn.com.leyizhuang.app.core.constant.AppCustomerLightStatus;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderGoodsSimpleRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 个人心中接口
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/9/27.
 * Time: 13:59.
 */

@RestController
@RequestMapping(value = "/app/user")
public class UserHomePageController {

    private static final Logger logger = LoggerFactory.getLogger(UserHomePageController.class);

    @Resource
    private AppCustomerService customerService;

    @Resource
    private AppEmployeeService employeeService;

    @Resource
    private DeliveryAddressService deliveryAddressService;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private CityService cityService;

    @Resource
    private AppStoreService appStoreService;

    @Resource
    private GoodsService goodsServiceImpl;
    /**
     * 个人主页的信息
     *
     * @param userId       用户Id
     * @param identityType 用户类型
     * @return
     */
    @PostMapping(value = "/homepage", produces = "application/json;charset=UTF-8")
    public ResultDTO getPersonalHomepage(Long userId, Integer identityType) {

        logger.info("personalHomepage CALLED,获取个人主页，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (userId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("personalHomepage OUT,获取个人主页失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空",
                    null);
            logger.info("personalHomepage OUT,获取个人主页失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if (identityType == 6) {
                CustomerHomePageResponse customerHomePageResponse = customerService.findCustomerInfoByUserId(userId);
                String parseLight = AppCustomerLightStatus.valueOf(customerHomePageResponse.getLight()).getValue();
                customerHomePageResponse.setLight(parseLight);
                if (null != customerHomePageResponse.getLastSignTime() && DateUtils.isSameDay(customerHomePageResponse.getLastSignTime(), new Date())) {
                    customerHomePageResponse.setCanSign(Boolean.FALSE);
                } else {
                    customerHomePageResponse.setCanSign(Boolean.TRUE);
                }
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, customerHomePageResponse);
                logger.info("personalHomepage OUT,获取个人主页成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                EmployeeHomePageResponse employeeHomePageResponse = employeeService.findEmployeeInfoByUserIdAndIdentityType(userId, identityType);
                // TODO 配送员还需要查询配送订单数量
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, employeeHomePageResponse);
                logger.info("personalHomepage OUT,获取个人主页成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取个人主页失败", null);
            logger.info("personalHomepage OUT,获取个人主页失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 导购获取我的顾客列表接口
     *
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/customer/list", produces = "application/json;charset=UTF-8")
    public ResultDTO getCustomersList(Long userId, Integer identityType) {

        logger.info("getCustomersList CALLED,获取我的顾客列表，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (userId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getCustomersList OUT,获取我的顾客列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getCustomersList OUT,获取我的顾客列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<CustomerListResponse> appCustomerList = customerService.findListByUserIdAndIdentityType(userId, identityType);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (appCustomerList != null && appCustomerList.size() > 0) ? appCustomerList : null);
            logger.info("getCustomersList OUT,获取我的顾客列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取我的顾客列表失败", null);
            logger.warn("getCustomersList EXCEPTION,获取我的顾客列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 导购搜索我的顾客
     *
     * @param keywords
     * @param userId
     * @param identityType
     * @return
     */
    @PostMapping(value = "/search/customer", produces = "application/json;charset=UTF-8")
    public ResultDTO searchCustomerList(String keywords, Long userId, Integer identityType) {

        logger.info("searchCustomerList CALLED,搜索我的顾客，入参 keywords {},userId{},identityType{}", keywords, userId, identityType);
        ResultDTO<Object> resultDTO;
        if (StringUtils.isBlank(keywords)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "搜索关键词不能为空！", null);
            logger.info("searchCustomerList OUT,搜索我的顾客失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id{userId}不能为空！", null);
            logger.info("searchCustomerList OUT,搜索我的顾客失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getCustomersList OUT,搜索我的顾客失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<CustomerListResponse> appCustomerList = customerService.searchByUserIdAndKeywordsAndIdentityType(userId, keywords, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (appCustomerList != null && appCustomerList.size() > 0) ? appCustomerList : null);
            logger.info("searchCustomerList OUT,搜索我的顾客成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，搜索我的顾客失败", null);
            logger.warn("searchCustomerList EXCEPTION,搜索我的顾客失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 装饰公司经理获取工人列表
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/decorateEmployee/list", produces = "application/json;charset=UTF-8")
    public ResultDTO getDecorateEmployeeList(Long userId, Integer identityType) {

        logger.info("getDecorateEmployeeList CALLED,获取我的员工列表，入参 userId {},identityType{}", userId, identityType);

        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getDecorateEmployeeList OUT,获取我的员工列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("getDecorateEmployeeList OUT,获取我的员工列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<EmployeeListResponse> appEmployeeList = employeeService.findDecorateEmployeeListByUserIdAndIdentityType(userId, identityType);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (appEmployeeList != null && appEmployeeList.size() > 0) ? appEmployeeList : null);
            logger.info("getDecorateEmployeeList OUT,获取我的员工列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取我的员工列表失败", null);
            logger.warn("getDecorateEmployeeList EXCEPTION,获取我的员工列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取用户默认收货地址
     *
     * @param userId       用户id
     * @param identityType 用户身份类型
     * @return 用户默认收货地址
     */
    @PostMapping(value = "/deliveryAddress/default", produces = "application/json;charset=UTF-8")
    public ResultDTO getUserDefaultDeliveryAddress(Long userId, Integer identityType) {
        logger.info("getUserDefaultDeliveryAddress CALLED,获取用户默认收货地址，入参 userId {},identityType", userId, identityType);
        ResultDTO resultDTO;
        try {
            if (null == userId) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
                logger.info("getDeliveryAddress OUT,获取用户默认收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == identityType) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "identityType不能为空！", null);
                logger.info("getDeliveryAddress OUT,获取用户默认收货地址失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            DeliveryAddressResponse deliveryAddressResponse = deliveryAddressService.
                    getDefaultDeliveryAddressByUserIdAndIdentityType(userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
            if (null == deliveryAddressResponse) {
                deliveryAddressResponse = deliveryAddressService.getTopDeliveryAddressByUserIdAndIdentityType
                        (userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, deliveryAddressResponse);
            logger.info("getDeliveryAddress OUT,获取用户默认收货地址成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取用户默认收货地址失败!", null);
            logger.warn("addDeliveryAddress EXCEPTION,获取用户默认收货地址失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 顾客选择门店自提
     * @param simpleRequest
     * @return
     */
    @PostMapping(value = "/deliveryType/selfTake", produces = "application/json;charset=UTF-8")
    public ResultDTO getUserDeliveryTypeBySelfTake(@RequestBody OrderGoodsSimpleRequest simpleRequest) {

        logger.info("getUserDeliveryTypeBySelfTake CALLED,顾客选择门店自提，入参 simpleRequest:{}", simpleRequest);

        ResultDTO resultDTO;
        try {
            if (simpleRequest.getGoodsList().isEmpty()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "找不到对象！", null);
                logger.info("getUserDeliveryTypeBySelfTake OUT,顾客选择门店自提失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == simpleRequest.getUserId()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
                logger.info("getUserDeliveryTypeBySelfTake OUT,顾客选择门店自提失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (null == simpleRequest.getIdentityType()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
                logger.info("getUserDeliveryTypeBySelfTake OUT,顾客选择门店自提失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if(simpleRequest.getIdentityType() == 0 && null == simpleRequest.getCustomerId()){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "代下单客户身份不能为空", null);
                logger.info("getUserDeliveryTypeBySelfTake OUT,顾客选择门店自提失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            Long userId = simpleRequest.getUserId();
            Integer identityType = simpleRequest.getIdentityType();
            List<GoodsSimpleInfo> goodsList = simpleRequest.getGoodsList();
            List<StoreResponse> storeList = null;
            List<OrderGoodsSimpleResponse> goodsInfo;
            List<Long> goodsIds = new ArrayList<Long>();

            if (identityType == 6) {
                AppCustomer customer = customerService.findById(userId);
                Long storeId = customer.getStoreId();

                if (null != storeId) {

                    for (int i = 0; i < goodsList.size(); i++) {
                        if (!goodsList.get(i).getIsGift()) {
                            goodsIds.add(goodsList.get(i).getId());
                        }
                    }
                    goodsInfo = goodsServiceImpl.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIds);
                    int goodsTotalQty = 0;
                    for (OrderGoodsSimpleResponse aGoodsInfo : goodsInfo) {
                        for (GoodsSimpleInfo aGoodsList : goodsList) {
                            if (aGoodsInfo.getId().equals(aGoodsList.getId())) {
                                //如果是赠品则标识设置为赠品
                                if (aGoodsList.getIsGift()) {
                                    goodsTotalQty = aGoodsInfo.getGoodsQty() + aGoodsList.getNum();
                                    aGoodsInfo.setIsGift(Boolean.TRUE);
                                    aGoodsInfo.setGoodsQty(goodsTotalQty);
                                } else {
                                    //先获取本品数量
                                    aGoodsInfo.setGoodsQty(aGoodsList.getNum());
                                    goodsTotalQty = aGoodsList.getNum();
                                }
                                //判断是否是乐意装产品不可门店自提
                                String brandName = "乐意装";
                                Boolean isLyzProduct = goodsServiceImpl.existGoodsBrandByGoodsIdAndBrandName(aGoodsInfo.getId(), brandName);
                                if (isLyzProduct) {
                                    String msg = aGoodsInfo.getGoodsName().concat("是乐意装产品不可自提！");
                                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, msg, null);
                                    logger.info("getUserDeliveryTypeBySelfTake OUT,顾客选择门店自提失败，出参 resultDTO:{}", resultDTO);
                                    return resultDTO;
                                }
                                //判断库存
                                Boolean isHaveInventory = appOrderService.existGoodsStoreInventory(storeId, aGoodsInfo.getId(), goodsTotalQty);
                                if (!isHaveInventory) {
                                    String msg = aGoodsInfo.getGoodsName().concat("门店库存不足！");
                                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, msg, null);
                                    logger.info("getUserDeliveryTypeBySelfTake OUT,顾客选择门店自提失败，出参 resultDTO:{}", resultDTO);
                                    return resultDTO;
                                }
                            }
                        }
                    }
                    AppStore store = appStoreService.findById(storeId);

                    StoreResponse storeResponse = StoreResponse.transform(store);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, storeResponse);
                    logger.info("getUserDeliveryTypeBySelfTake OUT,顾客选择门店自提成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }else {
                    Long cityId = customer.getCityId();
                    if (null != cityId) {
                        storeList = StoreResponse.transform(appStoreService.findStoreListByCityId(cityId));
                    }
                }
            }else {
                AppEmployee employee = employeeService.findById(userId);
                Long storeId = employee.getStoreId();

                if (null != storeId) {
                    for (int i = 0; i < goodsList.size(); i++) {
                        if (!goodsList.get(i).getIsGift()) {
                            goodsIds.add(goodsList.get(i).getId());
                        }
                    }
                    goodsInfo = goodsServiceImpl.findGoodsListByCustomerIdAndGoodsIdList(userId, goodsIds);
                    int goodsTotalQty = 0;
                    for (OrderGoodsSimpleResponse aGoodsInfo : goodsInfo) {
                        for (GoodsSimpleInfo aGoodsList : goodsList) {
                            if (aGoodsInfo.getId().equals(aGoodsList.getId())) {
                                //如果是赠品则标识设置为赠品
                                if (aGoodsList.getIsGift()) {
                                    goodsTotalQty = aGoodsInfo.getGoodsQty() + aGoodsList.getNum();
                                    aGoodsInfo.setIsGift(aGoodsList.getIsGift());
                                    aGoodsInfo.setGoodsQty(goodsTotalQty);
                                } else {
                                    //先获取本品数量
                                    aGoodsInfo.setGoodsQty(aGoodsList.getNum());
                                    goodsTotalQty = aGoodsList.getNum();
                                }
                                //判断是否是乐意装产品不可门店自提
                                String brandName = "乐意装";
                                Boolean isLyzProduct = goodsServiceImpl.existGoodsBrandByGoodsIdAndBrandName(aGoodsInfo.getId(), brandName);
                                if (isLyzProduct) {
                                    String msg = aGoodsInfo.getGoodsName().concat("是乐意装产品不可自提！");
                                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, msg, null);
                                    logger.info("getUserDeliveryTypeBySelfTake OUT,员工选择门店自提失败，出参 resultDTO:{}", resultDTO);
                                    return resultDTO;
                                }
                                //判断库存
                                Boolean isHaveInventory = appOrderService.existGoodsStoreInventory(storeId, aGoodsInfo.getId(), goodsTotalQty);
                                if (!isHaveInventory) {
                                    String msg = aGoodsInfo.getGoodsName().concat("的门店库存不足！");
                                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, msg, null);
                                    logger.info("getUserDeliveryTypeBySelfTake OUT,员工选择门店自提失败，出参 resultDTO:{}", resultDTO);
                                    return resultDTO;
                                }
                            }
                        }
                    }
                    AppStore store = appStoreService.findById(storeId);

                    StoreResponse storeResponse = StoreResponse.transform(store);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, storeResponse);
                    logger.info("getUserDeliveryTypeBySelfTake OUT,顾客选择门店自提成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }else {
                    Long cityId = employee.getCityId();
                    storeList = StoreResponse.transform(appStoreService.findStoreListByCityId(cityId));
                }
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    storeList.size() > 0 ? storeList : null);
            logger.info("getUserDeliveryTypeBySelfTake OUT,顾客选择门店自提成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,顾客选择门店自提失败!", null);
            logger.warn("getUserDeliveryTypeBySelfTake EXCEPTION,顾客选择门店自提失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}
