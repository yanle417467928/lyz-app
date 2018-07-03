package cn.com.leyizhuang.app.web.controller.bill;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.response.BillHistoryListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.BillInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.CustomerLoginResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.AppEmployeeService;
import cn.com.leyizhuang.app.foundation.service.AppStoreService;
import cn.com.leyizhuang.app.foundation.service.BillInfoService;
import cn.com.leyizhuang.app.web.controller.order.OrderController;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                                                Integer page, Integer size){
        ResultDTO<BillInfoResponse> resultDTO;
        try{

            if (userId == null){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId为空！", null);
                logger.info("lookBill OUT,查看账单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (identityType == null){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "identityType为空！", null);
                logger.info("lookBill OUT,查看账单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            if (identityType != 2){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "身份类型不正确！", null);
                logger.info("lookBill OUT,查看账单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            AppEmployee appEmployee = appEmployeeService.findById(userId);

            if (appEmployee == null){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账号id不存在！", null);
                logger.info("lookBill OUT,查看账单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            Long storeId = appEmployee.getStoreId();

            AppStore store = appStoreService.findById(storeId);

            if (store == null){
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "账号门店信息有误！", null);
                logger.info("lookBill OUT,查看账单失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startTime = null;
            LocalDateTime endTime = null;
            if (StringUtils.isNotBlank(startTimeStr)){
                startTimeStr = startTimeStr.trim();
                startTimeStr = startTimeStr + "00:00:00";

                startTime = LocalDateTime.parse(startTimeStr,df);
            }

            if (StringUtils.isNotBlank(endTimeStr)){
                endTimeStr = endTimeStr.trim();
                endTimeStr = endTimeStr + "23:59:59";

                endTime = LocalDateTime.parse(endTimeStr,df);
            }

            if (page == null){
                page = 1;
            }
            if (size == null){
                size = 100;
            }

            BillInfoResponse response = billInfoService.lookBill(startTime,endTime,storeId,page,size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "查询成功", response);
            return resultDTO;
        }catch (Exception e){
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "出现未知异常", null);
            logger.warn("lookBill EXCEPTION,查看装饰公司订单出现异常，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }

    /**
     * @title   获取历史账单列表
     * @descripe
     * @param
     * @return
     * @throws
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
     * @title   获取历史账单详情
     * @descripe
     * @param
     * @return
     * @throws
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
