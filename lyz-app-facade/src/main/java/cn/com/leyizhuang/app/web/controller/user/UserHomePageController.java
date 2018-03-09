package cn.com.leyizhuang.app.web.controller.user;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.AppCustomerLightStatus;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.request.PreDepositWithdrawParam;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.SinkSender;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    private AppStoreService appStoreService;

    @Resource
    private DeliveryAddressService deliveryAddressService;

    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;

    @Resource
    private CustomerLevelService customerLevelService;

    @Resource
    private AppPreDepositWithdrawService appPreDepositWithdrawService;

    @Resource
    private SinkSender sinkSender;

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
            if (identityType == AppIdentityType.CUSTOMER.getValue()) {
                CustomerHomePageResponse customerHomePageResponse = customerService.findCustomerInfoByUserId(userId);
                if (null != customerHomePageResponse) {
                    String parseLight = AppCustomerLightStatus.valueOf(customerHomePageResponse.getLight()).getValue();
                    customerHomePageResponse.setLight(parseLight);
                    if (null != customerHomePageResponse.getLastSignTime() && DateUtils.isSameDay(customerHomePageResponse.getLastSignTime(), new Date())) {
                        customerHomePageResponse.setCanSign(Boolean.FALSE);
                    } else {
                        customerHomePageResponse.setCanSign(Boolean.TRUE);
                    }
                    Integer cashCouponQty = customerService.findCashCouponAvailQtyByCustomerId(userId);
                    customerHomePageResponse.setCashCouponQty(null == cashCouponQty ? 0 : cashCouponQty);
                    Integer productCouponQty = customerService.findProductCouponAvailQtyByCustomerId(userId);
                    customerHomePageResponse.setProductCouponQty(null == productCouponQty ? 0 : productCouponQty);
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, customerHomePageResponse);
                    logger.info("personalHomepage OUT,获取个人主页成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                } else {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有找到该用户!", null);
                    logger.info("personalHomepage OUT,获取个人主页失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }

            } else {
                AppEmployee employee = employeeService.findById(userId);
                if (null == employee) {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有找到该配送员!", null);
                    logger.info("personalHomepage OUT,获取个人主页失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
                EmployeeHomePageResponse employeeHomePageResponse = employeeService.findEmployeeInfoByUserIdAndIdentityType(userId, identityType);
                if (null != employeeHomePageResponse) {
                    // 配送员还需要查询配送订单数量
                    if (identityType == 1) {
                        int count = orderDeliveryInfoDetailsService.countAuditFinishOrderByOperatorNo(employee.getDeliveryClerkNo());
                        employeeHomePageResponse.setSendQty(count);
                    }
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, employeeHomePageResponse);
                    logger.info("personalHomepage OUT,获取个人主页成功，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                } else {
                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "没有找到该用户!", null);
                    logger.info("personalHomepage OUT,获取个人主页失败，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }

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
    public ResultDTO getCustomersList(Long userId, Integer identityType, Integer page, Integer size) {

        logger.info("getCustomersList CALLED,获取我的顾客列表，入参 userId {},identityType{},page:{},size:{}", userId, identityType, page, size);

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
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空", null);
            logger.info("getCustomersList OUT,获取我的顾客列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空", null);
            logger.info("getCustomersList OUT,获取我的顾客列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            PageInfo<AppCustomer> appCustomerList = customerService.findListByUserIdAndIdentityType(userId, identityType, page, size);

            // 计算灯号
            List<AppCustomer> customers = appCustomerList.getList();
            customers = customerLevelService.countCustomerListLightlevel(customers, userId);

            List<CustomerListResponse> customerlistresponselist = CustomerListResponse.transform(customers);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    null != appCustomerList.getList() && appCustomerList.getList().size() > 0 ? new GridDataVO<CustomerListResponse>().transform(customerlistresponselist, appCustomerList) : null);
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
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空！", null);
            logger.info("getCustomersList OUT,搜索我的顾客失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 0){
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户身份不能进行此操作！", null);
            logger.info("getCustomersList OUT,搜索我的顾客失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            AppEmployee employee = employeeService.findById(userId);
            List<FindCustomerResponse> findCustomerResponseList;
            if (keywords.matches("[0-9]{11}")){
                findCustomerResponseList = customerService.findCustomerByCusPhone(employee.getCityId(),keywords);
            }else {
                findCustomerResponseList = customerService.findCustomerByCusName(employee.getStoreId(),keywords);
            }
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (findCustomerResponseList != null && findCustomerResponseList.size() > 0) ? findCustomerResponseList : null);
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
    public ResultDTO getDecorateEmployeeList(Long userId, Integer identityType, Integer page, Integer size) {
        logger.info("getDecorateEmployeeList CALLED,获取我的员工列表，入参 userId {},identityType{},page:{},size:{}", userId, identityType, page, size);
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
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getDecorateEmployeeList OUT,获取我的员工列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getDecorateEmployeeList OUT,获取我的员工列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            PageInfo<AppEmployee> appEmployeeList = employeeService.findDecorateEmployeeListByUserIdAndIdentityType(userId, identityType, page, size);
            List<EmployeeListResponse> employeeListResponseList = EmployeeListResponse.transform(appEmployeeList.getList());
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, (null != appEmployeeList && null != appEmployeeList.getList() && appEmployeeList.getList().size() > 0) ? new GridDataVO<EmployeeListResponse>().transform(employeeListResponseList, appEmployeeList) : null);
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
     * 经理搜索工人
     *
     * @param userId       用户id
     * @param identityType 用户类型
     * @param keywords     搜索条件
     * @return 返回工人列表
     */
    @PostMapping(value = "/search/employee", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> searchDecorateEmployeeList(Long userId, Integer identityType, String keywords) {
        logger.info("searchDecorateEmployeeList CALLED,搜索我的工人，入参 userId{},identityType{},keywords {}", userId, identityType, keywords);
        ResultDTO<Object> resultDTO;
        if (StringUtils.isBlank(keywords)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "搜索关键词不能为空！", null);
            logger.info("searchDecorateEmployeeList OUT,搜索我的工人失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id{userId}不能为空！", null);
            logger.info("searchDecorateEmployeeList OUT,搜索我的工人失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份不能为空", null);
            logger.info("searchDecorateEmployeeList OUT,搜索我的工人失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<EmployeeListResponse> appEmployeeList = employeeService.searchBySalesConsultIdAndKeywords(userId, keywords, identityType);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null,
                    (appEmployeeList != null && appEmployeeList.size() > 0) ? appEmployeeList : null);
            logger.info("getDecorateEmployeeList OUT,搜索我的工人列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，搜索我的工人失败", null);
            logger.warn("searchDecorateEmployeeList EXCEPTION,搜索我的工人失败，出参 resultDTO:{}", resultDTO);
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
        logger.info("getUserDefaultDeliveryAddress CALLED,获取用户默认收货地址，入参 userId {},identityType{}", userId, identityType);
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
     * 导购获取未提货顾客列表
     *
     * @param userId       用户id
     * @param identityType 身份类型
     * @return 有未提货的顾客列表
     */
    @PostMapping(value = "/get/productCoupon/customer", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> sellerGetProductCouponCustomer(Long userId, Integer identityType, String keywords) {
        logger.info("sellerGetProductCouponCustomer CALLED,导购获取未提货客户列表,入参 userId {},identityType{}", userId, identityType);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("sellerGetProductCouponCustomer OUT,导购获取未提货客户列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "身份类型不能为空！", null);
            logger.info("sellerGetProductCouponCustomer OUT,导购获取未提货客户列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            if (identityType == 0) {
                List<ProductCouponCustomer> customerList = customerService.findProductCouponCustomerBySellerId(userId, keywords);
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, customerList);
                logger.info("sellerGetProductCouponCustomer OUT,导购获取未提货客户成功,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            } else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户身份类型不合法,获取未提货客户列表失败",
                        null);
                logger.info("sellerGetProductCouponCustomer OUT,导购获取未提货客户列表失败,出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取未提货客户列表失败", null);
            logger.warn("sellerGetProductCouponCustomer EXCEPTION,导购获取未提货客户列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 顾客预存款提现申请
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/cus/apply/withdraw", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> cusApplyWithdraw(PreDepositWithdrawParam param) {
        logger.info("顾客预存款提现申请,入参:{}", JSON.toJSONString(param));
        ResultDTO<Object> resultDTO;
        if (param == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "参数null", null);
            logger.info("顾客预存款提现申请失败", resultDTO);
            return resultDTO;
        }

        if (param.getId() == null || param.getRealName() == null || param.getRealName().equals("") || param.getRealPhone().equals("") || param.getRealPhone() == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客信息不正确", null);
            logger.info("顾客预存款提现申请失败", resultDTO);
            return resultDTO;
        }

        if (param.getAccountType() == null || param.getAccount() == null || param.getAccount().equals("")) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "提现帐号信息不正确", null);
            logger.info("顾客预存款提现申请失败", resultDTO);
            return resultDTO;
        }

        try {
            // 校验顾客可提现预存款
            CustomerPreDeposit preDeposit = customerService.findByCusId(param.getId());

            if (preDeposit == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "预存款不足", null);
                logger.info("顾客预存款提现申请失败", resultDTO);
                return resultDTO;
            }

            // 可现预存款
            Double canWithdrawAmount = preDeposit.getBalance() == null ? 0.00 : preDeposit.getBalance();
            // 需要提现预存款
            Double needWithdrawAmount = param.getAmount() == null ? 0.00 : param.getAmount();

            if (canWithdrawAmount.equals(0.00) || needWithdrawAmount > canWithdrawAmount) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "预存款不足", null);
                logger.info("顾客预存款提现申请失败", resultDTO);
                return resultDTO;
            }

            String refundNumber = appPreDepositWithdrawService.cusSave(param);
            //提现退款接口信息发送EBS
            sinkSender.sendWithdrawRefund(refundNumber);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "提现申请成功", null);
            logger.info("顾客提现申请成功,出参 resultDTO:{}", resultDTO);
            return resultDTO;

        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,提现申请失败", null);
            logger.warn("cusApplyWithdraw EXCEPTION,顾客提现申请失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }

    /**
     * 门店预存款提现
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/st/apply/withdraw", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> stApplyWithdraw(PreDepositWithdrawParam param) {
        logger.info("门店预存款提现申请,入参:{}", JSON.toJSONString(param));
        ResultDTO<Object> resultDTO;
        if (param == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "参数null", null);
            logger.info("门店预存款提现申请失败", resultDTO);
            return resultDTO;
        }

        if (param.getId() == null || param.getRealName() == null || param.getRealName().equals("") || param.getRealPhone().equals("") || param.getRealPhone() == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "提现门店信息不正确", null);
            logger.info("门店预存款提现申请失败", resultDTO);
            return resultDTO;
        }

        if (param.getAccountType() == null || param.getAccount() == null || param.getAccount().equals("")) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "提现帐号信息不正确", null);
            logger.info("门店预存款提现申请失败", resultDTO);
            return resultDTO;
        }

        try {

            // 校验顾客可提现预存款
            StorePreDeposit preDeposit = appStoreService.findStorePreDepositByEmpId(param.getId());


            // 可现预存款
            Double canWithdrawAmount = preDeposit.getBalance() == null ? 0.00 : preDeposit.getBalance();
            // 需要提现预存款
            Double needWithdrawAmount = param.getAmount() == null ? 0.00 : param.getAmount();

            if (canWithdrawAmount.equals(0.00) || needWithdrawAmount > canWithdrawAmount) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "预存款不足", null);
                logger.info("门店预存款提现申请失败", resultDTO);
                return resultDTO;
            }

            String refundNumber = appPreDepositWithdrawService.stSave(param);

            //提现退款接口信息发送EBS
            sinkSender.sendWithdrawRefund(refundNumber);

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "提现申请成功", null);
            logger.info("门店提现申请成功,出参 resultDTO:{}", resultDTO);
            return resultDTO;

        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,提现失败", null);
            logger.warn("cusApplyWithdraw EXCEPTION,门店提现申请失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }

    /**
     * 顾客申请提现列表
     */
    @PostMapping("/cus/apply/list")
    public ResultDTO cusApplyList(Integer page, Integer size, Long cusId,Integer status) {
        ResultDTO<Object> resultDTO;
        if (page == null || size == null) {
            page = 1;
            size = 10;
        }

        if (cusId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "当前帐号信息有误，请联系管理员", null);
        }

        try {
            PageInfo<CusPreDepositWithdraw> preDepositWithdrawPageInfo = appPreDepositWithdrawService.cusApplyList(page, size, cusId,status);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "获取数据成功", new GridDataVO<CusPreDepositWithdraw>().transform(preDepositWithdrawPageInfo));
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取顾客提现申请列表失败", null);
        }

        return resultDTO;
    }

    /**
     * 门店提现申请列表
     */
    @PostMapping("/st/apply/list")
    public ResultDTO stApplyList(Integer page, Integer size, Long stId ,Integer status) {
        ResultDTO<Object> resultDTO;
        if (page == null || size == null) {
            page = 1;
            size = 10;
        }

        if (stId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "当前帐号信息有误，请联系管理员", null);
        }

        try {
            PageInfo<StPreDepositWithdraw> stPreDepositWithdrawPageInfo = appPreDepositWithdrawService.stApplyList(page, size, stId,status);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "获取数据成功", new GridDataVO<StPreDepositWithdraw>().transform(stPreDepositWithdrawPageInfo));
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,获取门店提现申请列表失败", null);
        }

        return resultDTO;
    }

    /**
     * 顾客取消提现申请
     */
    @PostMapping("cus/apply/cancel")
    public ResultDTO cusApplyCancel(Long applyId, Long cusId) {
        ResultDTO<Object> resultDTO;
        if (applyId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "申请单数据有误，请联系管理员", null);
        }

        if (cusId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "当前帐号信息有误，请联系管理员", null);
        }

        try {
            String rechargeNo = appPreDepositWithdrawService.cusCancelApply(applyId, cusId);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "取消成功", null);
            sinkSender.sendRechargeReceipt(rechargeNo);
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,取消申请失败", null);
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
        return resultDTO;
    }

    @PostMapping("st/apply/cancel")
    public ResultDTO stApplyCancel(Long applyId, Long stId) {
        ResultDTO<Object> resultDTO;
        if (applyId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "申请单数据有误，请联系管理员", null);
        }

        if (stId == null) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "当前帐号信息有误，请联系管理员", null);
        }

        try {
            String rechargeNo = appPreDepositWithdrawService.stCancelApply(applyId, stId);
            sinkSender.sendRechargeReceipt(rechargeNo);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "取消成功", null);
        } catch (Exception e) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常,取消申请失败", null);
        }
        return resultDTO;
    }

}
