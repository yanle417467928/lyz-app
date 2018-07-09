package cn.com.leyizhuang.app.web.controller.bill;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.request.BillPayRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.BillorderDetailsRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.BillInfoService;
import cn.com.leyizhuang.app.web.controller.order.OrderController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.CountUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12421 on 2018/6/29.
 */
@RestController
@RequestMapping(value = "/app/bill")
public class AppBillController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private BillInfoService billInfoService;

    @Resource
    private AppStoreService appStoreService;

    @Resource
    private AppEmployeeService appEmployeeService;

    @Resource
    private AppStoreService storeService;

    /**
     * 查看账单接口
     * @param userId 账号id
     * @param identityType 账号类型
     * @param startTimeStr 开始时间
     * @param endTimeStr 结束时间
     * @param page 页数
     * @param size 长度
     * @return
     */
    @PostMapping(value = "/look", produces = "application/json;charset=UTF-8")
    public ResultDTO<BillInfoResponse> lookBill(Long userId,
                                                Integer identityType,
                                                String startTimeStr,
                                                String endTimeStr,
                                                Integer page, Integer size) {
        logger.info("lookBill CALLED,查看账单接口,入参 userId:{}, identityType:{}, startTimeStr:{},endTimeStr:{},page:{},size:{}",
                userId, identityType, startTimeStr,endTimeStr,page,size);
        ResultDTO<BillInfoResponse> resultDTO;
        try {

            if (userId == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId为空！", null);
                logger.info("lookBill OUT,查看账单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (identityType == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "identityType为空！", null);
                logger.info("lookBill OUT,查看账单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (identityType != 2) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "身份类型不正确！", null);
                logger.info("lookBill OUT,查看账单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            AppEmployee appEmployee = appEmployeeService.findById(userId);

            if (appEmployee == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账号id不存在！", null);
                logger.info("lookBill OUT,查看账单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            Long storeId = appEmployee.getStoreId();

            AppStore store = appStoreService.findById(storeId);

            if (store == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账号门店信息有误！", null);
                logger.info("lookBill OUT,查看账单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startTime = null;
            LocalDateTime endTime = null;
            if (StringUtils.isNotBlank(startTimeStr)) {
                startTimeStr = startTimeStr.trim();
                startTimeStr = startTimeStr + " 00:00:00";

                startTime = LocalDateTime.parse(startTimeStr, df);
            }

            if (StringUtils.isNotBlank(endTimeStr)) {
                endTimeStr = endTimeStr.trim();
                endTimeStr = endTimeStr + " 23:59:59";

                endTime = LocalDateTime.parse(endTimeStr, df);
            }

            if (page == null) {
                page = 1;
            }
            if (size == null) {
                size = 100;
            }

            BillInfoResponse response = billInfoService.lookBill(startTime, endTime, storeId, page, size);

            if (response.getBillNo() == null){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "请联系管理员，创建账单规则！", null);
                logger.info("lookBill OUT,查看账单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "查询成功", response);
            logger.info("lookBill CALLED,查看账单接口成功 出参 未还账单条数："+ response.getNotPayOrderDetails().size());
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常", null);
            logger.warn("lookBill EXCEPTION,查看装饰公司订单出现异常，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }

    /**
     * 跳转账单支付页面
     *
     * @return
     */
    @PostMapping(value = "/pay/page", produces = "application/json;charset=UTF-8")
    public ResultDTO<BillPayPageResponse> toPayPage(BillPayRequest billPayRequest) {
        logger.info("toPayPage CALLED,查看账单接口,入参 billPayRequest:{}",billPayRequest);
        ResultDTO<BillPayPageResponse> resultDTO;
        try {

            if (billPayRequest.getUserId() == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId为空！", null);
                logger.info("toPayPage OUT,跳转账单支付页面，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (billPayRequest.getIdentityType() == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "identityType为空！", null);
                logger.info("toPayPage OUT,跳转账单支付页面，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (billPayRequest.getIdentityType() != 2) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "身份类型不正确！", null);
                logger.info("toPayPage OUT,跳转账单支付页面，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            AppEmployee appEmployee = appEmployeeService.findById(billPayRequest.getUserId());

            if (appEmployee == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账号id不存在！", null);
                logger.info("toPayPage OUT,跳转账单支付页面，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            Long storeId = appEmployee.getStoreId();
            AppStore store = appStoreService.findById(storeId);

            if (store == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账号门店信息有误！", null);
                logger.info("toPayPage OUT,跳转账单支付页面，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (StringUtils.isBlank(billPayRequest.getOrderDetails())){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "选择订单信息有误！", null);
                logger.info("toPayPage OUT,跳转账单支付页面，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<BillorderDetailsRequest> billorderDetailsRequests = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType billorderdetailsJavaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, BillorderDetailsRequest.class);
            billorderDetailsRequests = objectMapper.readValue(billPayRequest.getOrderDetails(),billorderdetailsJavaType);

            // 检验是否存在已经还过订单
            // 分离订单和退单
            List<Long> orderIds = new ArrayList<>();
            List<Long> returnIds = new ArrayList<>();
            for (BillorderDetailsRequest request : billorderDetailsRequests){
                if (request.getOrderType().equals("order")){
                    orderIds.add(request.getId());
                }else if (request.getOrderType().equals("return")){
                    returnIds.add(request.getId());
                }
            }
            List<BillRepaymentGoodsInfoResponse> paidOrderDetails = billInfoService.findPaidOrderDetailsByOids(orderIds,storeId);
            if (paidOrderDetails != null && paidOrderDetails.size() > 0){
                String  msg = "存在已经还款订单";
                for (int i = 0 ; i< paidOrderDetails.size() ;i++){
                    if (i<3){
                        msg += " "+paidOrderDetails.get(i).getOrderNo()+" ";
                    }else {
                        break;
                    }
                }

                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, msg, null);
                logger.info("toPayPage OUT,跳转账单支付页面，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }else {
                List<BillRepaymentGoodsInfoResponse> paidReturnOrderDetails = billInfoService.findPaidReturnOrderDetailsByOids(orderIds,storeId);

                if (paidReturnOrderDetails != null && paidReturnOrderDetails.size() > 0){
                    String  msg = "存在已经还款退单";
                    for (int i = 0 ; i< paidReturnOrderDetails.size() ;i++){
                        if (i<3){
                            msg += " "+paidReturnOrderDetails.get(i).getReturnNo()+" ";
                        }else {
                            break;
                        }
                    }

                    resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, msg, null);
                    logger.info("toPayPage OUT,跳转账单支付页面，出参 resultDTO:{}", resultDTO);
                    return resultDTO;
                }
            }

            Double totalPayAmount = 0D;
            Double preDepositAmount = 0D;
            Double creditMoney = 0D;

            // 当前信用金账户
            StorePreDeposit preDeposit = storeService.findStorePreDepositByStoreId(storeId);

            if (preDeposit != null){
                preDepositAmount = preDeposit.getBalance();
            }
            // 当前预存款账户
            StoreCreditMoney storeCreditMoney = storeService.findStoreCreditMoneyByEmpId(billPayRequest.getUserId());
            if (storeCreditMoney != null){
                creditMoney = storeCreditMoney.getCreditLimitAvailable();
            }
            // 应支付金额
            totalPayAmount = billInfoService.calculatePayAmount(storeId, billorderDetailsRequests);

            if (totalPayAmount < 0){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "还款金额不能小于0,请使用信用金下单后冲抵", null);
                logger.info("toPayPage OUT,跳转账单支付页面，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            BillPayPageResponse response = new BillPayPageResponse();
            response.setCurrentCreditMoney(creditMoney);
            response.setStPreDiposit(preDepositAmount);
            response.setRepaymentCreditMoney(CountUtil.add(creditMoney,totalPayAmount));
            response.setTotalPayAmount(totalPayAmount);
            response.setBillorderDetailsRequests(billorderDetailsRequests);
            response.setBillNo(billPayRequest.getBillNo());

            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "查询成功", response);
            logger.info("toPayPage OUT,账单还款跳转支付页面成功" );
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常", null);
            logger.warn("toPayPage EXCEPTION,账单还款跳转支付页面出现异常，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 支付账单
     * @param billPayRequest
     * @return
     */
    @PostMapping(value = "/pay", produces = "application/json;charset=UTF-8")
    public ResultDTO<String> payBill(BillPayRequest billPayRequest) {
        logger.info("payBill CALLED,查看账单接口,入参 billPayRequest:{}",billPayRequest);
        ResultDTO<String> resultDTO;
        try {

            if (billPayRequest.getUserId() == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId为空！", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (billPayRequest.getIdentityType() == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "identityType为空！", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (billPayRequest.getIdentityType() != 2) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "身份类型不正确！", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            AppEmployee appEmployee = appEmployeeService.findById(billPayRequest.getUserId());

            if (appEmployee == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "员工不存在！", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            Long storeId = appEmployee.getStoreId();
            AppStore store = appStoreService.findById(storeId);

            if (store == null) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账号门店信息有误！", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (StringUtils.isBlank(billPayRequest.getOrderDetails())){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "选择订单信息有误！", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<BillorderDetailsRequest> billorderDetailsRequests = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            JavaType billorderdetailsJavaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, BillorderDetailsRequest.class);
            billorderDetailsRequests = objectMapper.readValue(billPayRequest.getOrderDetails(),billorderdetailsJavaType);

            if (billorderDetailsRequests == null || billorderDetailsRequests.size() == 0){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "选择订单信息有误！", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            Double payStPreDeposit = billPayRequest.getStPreDepositAmount() == null ? 0D : billPayRequest.getStPreDepositAmount();
            if (payStPreDeposit < 0){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店预存款金额输入有误！", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            // 当前信用金账户
            StorePreDeposit preDeposit = storeService.findStorePreDepositByStoreId(storeId);
            if (payStPreDeposit > 0 && preDeposit == null){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店预存款账户不存在", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (payStPreDeposit > preDeposit.getBalance()){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店预存款账户余额不足", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            // 应支付金额
            Double totalPayAmount = billInfoService.calculatePayAmount(storeId, billorderDetailsRequests);
            BillRepaymentInfoDO repaymentInfoDO = new BillRepaymentInfoDO();
            if (totalPayAmount.equals(payStPreDeposit) || payStPreDeposit.equals(0D) || totalPayAmount < 0D){
                // 付清
                repaymentInfoDO = billInfoService.createRepayMentInfo(storeId,billPayRequest.getUserId(),"app",billorderDetailsRequests,payStPreDeposit,0D,0D,totalPayAmount,"",0D,billPayRequest.getBillNo());
            }else if (totalPayAmount > payStPreDeposit){
                // 未付清
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店预存款支付需一次性付清", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }else if (totalPayAmount < payStPreDeposit){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "门店预存款金额大于应付金额", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (totalPayAmount.equals(payStPreDeposit)){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "支付成功", repaymentInfoDO.getRepaymentNo());
                logger.info("payBill OUT,账单还款支付成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }else if (payStPreDeposit.equals(0D) && totalPayAmount > 0D){
                resultDTO = new ResultDTO<>(CommonGlobal.PAGEABLE_DEFAULT_PAGE, "请使用第三方支付", repaymentInfoDO.getRepaymentNo());
                logger.info("payBill OUT,账单还款支付成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }else if (payStPreDeposit.equals(0D) && totalPayAmount < 0D){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "支付成功", repaymentInfoDO.getRepaymentNo());
                logger.info("payBill OUT,账单还款支付成功，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }else {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账单支付预存款金额错误", null);
                logger.info("payBill OUT,支付账单，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }


        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常", null);
            logger.warn("payBill EXCEPTION,账单还款支付出现异常，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取历史账单列表
     * @descripe
     * @author GenerationRoad
     * @date 2018/6/30
     */
    @PostMapping(value = "/history/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getBillHistoryList(Long userId, Integer identityType, Integer page, Integer size) {
        ResultDTO<Object> resultDTO;
        logger.info("getBillHistoryList CALLED,获取历史账单列表，入参 userID:{}, identityType:{}, page:{}, size:{}", userId, identityType, page, size);
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("getBillHistoryList OUT,获取历史账单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("getBillHistoryList OUT,获取历史账单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getBillHistoryList OUT,获取历史账单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getBillHistoryList OUT,获取历史账单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取用户待评价订单列表
            if (identityType != AppIdentityType.DECORATE_MANAGER.getValue()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户没有权限!", null);
                logger.info("getBillHistoryList OUT,获取历史账单列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            PageInfo<BillHistoryListResponse> responseBillList = this.billInfoService.findBillHistoryListByEmpId(userId, identityType, page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<BillHistoryListResponse>().transform(responseBillList.getList(), responseBillList));
            logger.info("getBillHistoryList OUT,获取历史账单列表成功，出参 resultDTO:{}", responseBillList);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取历史账单列表失败", null);
            logger.warn("getBillHistoryList EXCEPTION,获取历史账单列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * @param
     * @return
     * @throws
     * @title 获取历史账单详情
     * @descripe
     * @author GenerationRoad
     * @date 2018/6/30
     */
    @PostMapping(value = "/history/detail", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> findBillHistoryDetail(Long userId, Integer identityType, String billNo) {
        ResultDTO<Object> resultDTO;
        logger.info("findBillHistoryDetail CALLED,获取历史账单详情，入参 userID:{}, identityType:{}, billNo:{}", userId, identityType, billNo);
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空！", null);
            logger.info("findBillHistoryDetail OUT,获取历史账单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空！", null);
            logger.info("findBillHistoryDetail OUT,获取历史账单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == billNo) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账单编号不能为空！",
                    null);
            logger.info("findBillHistoryDetail OUT,获取历史账单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            //获取用户待评价订单列表
            if (identityType != AppIdentityType.DECORATE_MANAGER.getValue()) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该用户没有权限!", null);
                logger.info("findBillHistoryDetail OUT,获取历史账单详情失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            BillInfoResponse billInfoResponse = this.billInfoService.findBillHistoryDetail(billNo);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, billInfoResponse);
            logger.info("findBillHistoryDetail OUT,获取历史账单详情成功，出参 resultDTO:{}", billInfoResponse);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取历史账单详情失败", null);
            logger.warn("findBillHistoryDetail EXCEPTION,获取历史账单详情失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
}
