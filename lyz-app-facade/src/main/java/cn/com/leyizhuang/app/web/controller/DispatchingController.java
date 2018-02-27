package cn.com.leyizhuang.app.web.controller;

import cn.com.leyizhuang.app.core.bean.GridDataVO;
import cn.com.leyizhuang.app.core.constant.AppReturnOrderStatus;
import cn.com.leyizhuang.app.core.constant.ReturnLogisticStatus;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwReturnOrderCheckEnter;
import cn.com.leyizhuang.app.foundation.pojo.response.*;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderDeliveryDetail;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.webservice.ICallWms;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import cn.com.leyizhuang.common.util.AssertUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * 配送员控制类
 * Created by caiyu on 2017/11/21.
 */
@RestController
@RequestMapping(value = "/app/dispatching")
public class DispatchingController {
    private static final Logger logger = LoggerFactory.getLogger(DispatchingController.class);

    @Resource
    private AppEmployeeService appEmployeeService;

    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;

    @Resource
    private AppOrderService appOrderService;

    @Resource
    private ReturnOrderService returnOrderService;

    @Resource
    private AppToWmsOrderService appToWmsOrderService;

    @Resource
    private ReturnOrderDeliveryDetailsService returnOrderDeliveryDetailsService;

    @Resource
    private ICallWms iCallWms;
    /**
     * 配送员获取待配送列表
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @return 返回待配送列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getDispatchingList(Long userID, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("getDispatchingList CALLED,配送员获取待配送列表，入参 userID:{}, identityType:{}", userID, identityType);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getDispatchingList OUT,获配送员获取待配送列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("getDispatchingList OUT,获配送员获取待配送列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "非配送员不能查看待配送列表", null);
            logger.info("getDispatchingList OUT,获配送员获取待配送列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            AppEmployee appEmployee = appEmployeeService.findById(userID);
            if (null == appEmployee) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到此配送员", null);
                logger.info("getDispatchingList OUT,获配送员获取待配送列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(appEmployee.getDeliveryClerkNo())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送员编号为空", null);
                logger.info("getDispatchingList OUT,获配送员获取待配送列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<WaitDeliveryResponse> waitDeliveryResponseList = orderDeliveryInfoDetailsService.getOrderBeasInfoByOperatorNo(appEmployee.getDeliveryClerkNo());
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, waitDeliveryResponseList);
            logger.info("getDispatchingList OUT,配送员获取待配送列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获配送员获取待配送列表失败", null);
            logger.warn("getDispatchingList EXCEPTION,获配送员获取待配送列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 配送员获待取货列表
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @return 返回待取货列表
     */
    @RequestMapping(value = "/pickUp/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getWaitPickUpList(Long userID, Integer identityType) {
        ResultDTO<Object> resultDTO;
        logger.info("getWaitPickUpList CALLED,配送员获待取货列表，入参 userID:{}, identityType:{}", userID, identityType);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getWaitPickUpList OUT,配送员获待取货列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("getWaitPickUpList OUT,配送员获待取货列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "非配送员不能查看待取货列表", null);
            logger.info("getWaitPickUpList OUT,配送员获待取货列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }

        try {
            AppEmployee appEmployee = appEmployeeService.findById(userID);
            if (null == appEmployee) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到此配送员", null);
                logger.info("getWaitPickUpList OUT,配送员获待取货列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(appEmployee.getDeliveryClerkNo())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送员编号为空", null);
                logger.info("getWaitPickUpList OUT,配送员获待取货列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            List<WaitPickUpResponse> waitPickUpResponseList = orderDeliveryInfoDetailsService.getWaitPickUpListByOperatorNo(appEmployee.getDeliveryClerkNo());
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, waitPickUpResponseList);
            logger.info("getWaitPickUpList OUT,配送员获待取货列表成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，配送员获待取货列表失败", null);
            logger.warn("getWaitPickUpList EXCEPTION,配送员获待取货列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取出货单详情
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @param orderNumber  订单号
     * @return 返回详情
     */
    @RequestMapping(value = "/shipper/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getDeliveryDetails(Long userID, Integer identityType, String orderNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("getDeliveryDetails CALLED,获取出货单详情，入参 userID:{}, identityType:{}, orderNumber:{}", userID, identityType, orderNumber);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getDeliveryDetails OUT,获取出货单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("getDeliveryDetails OUT,获取出货单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号不能为空", null);
            logger.info("getDeliveryDetails OUT,获取出货单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "非配送员不能查看出货单详情", null);
            logger.info("getDeliveryDetails OUT,获取出货单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            AppEmployee appEmployee = appEmployeeService.findById(userID);
            if (null == appEmployee) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到此配送员", null);
                logger.info("getDeliveryDetails OUT,获取出货单详情失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(appEmployee.getDeliveryClerkNo())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送员编号为空", null);
                logger.info("getDeliveryDetails OUT,获取出货单详情失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            ShipperDetailResponse shipperDetailResponse = orderDeliveryInfoDetailsService.getOrderDeliveryInfoDetailsByOperatorNoAndOrderNumber(appEmployee.getDeliveryClerkNo(), orderNumber);
            shipperDetailResponse.setGoodsList(appOrderService.getOrderGoodsDetails(orderNumber));
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, shipperDetailResponse);
            logger.info("getDeliveryDetails OUT,获取出货单详情成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取出货单详情失败", null);
            logger.warn("getDeliveryDetails EXCEPTION,获取出货单详情失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 获取取货单详情
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @param returnNumber 订单号
     * @return 返回详情
     */
    @RequestMapping(value = "/pickUp/detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getPickUpDetails(Long userID, Integer identityType, String returnNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("getPickUpDetails CALLED,获取取货单详情，入参 userID:{}, identityType:{}, returnNumber:{}", userID, identityType, returnNumber);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getPickUpDetails OUT,获取取货单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("getPickUpDetails OUT,获取取货单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(returnNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "退单号不能为空", null);
            logger.info("getPickUpDetails OUT,获取取货单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "非配送员不能查看取货单详情", null);
            logger.info("getPickUpDetails OUT,获取取货单详情失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            AppEmployee appEmployee = appEmployeeService.findById(userID);
            if (null == appEmployee) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到此配送员", null);
                logger.info("getPickUpDetails OUT,获取取货单详情失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(appEmployee.getDeliveryClerkNo())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送员编号为空", null);
                logger.info("getPickUpDetails OUT,获取取货单详情失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            PickUpDetailResponse pickUpDetailResponse = orderDeliveryInfoDetailsService.getPickUpDetailByOperatorNoAndReturnNo(appEmployee.getDeliveryClerkNo(), returnNumber);
            pickUpDetailResponse.setGoodsList(orderDeliveryInfoDetailsService.getReturnGoods(returnNumber));
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, pickUpDetailResponse);
            logger.info("getPickUpDetails OUT,获取取货单详情成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，获取取货单详情失败", null);
            logger.warn("getPickUpDetails EXCEPTION,获取取货单详情失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }

    /**
     * 配送员取货确认
     *
     * @param userID       用户id
     * @param identityType 用户类型
     * @param returnNumber 订单号
     * @return 返回详情
     */
    @RequestMapping(value = "/pickUp/enter", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getPickUpEnter(Long userID, Integer identityType, String returnNumber) {
        ResultDTO<Object> resultDTO;
        logger.info("getPickUpEnter CALLED,配送员取货确认，入参 userID:{}, identityType:{}, returnNumber:{}", userID, identityType, returnNumber);
        if (null == userID) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户id不能为空", null);
            logger.info("getPickUpEnter OUT,配送员取货确认失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型不能为空", null);
            logger.info("getPickUpEnter OUT,配送员取货确认失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(returnNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "退单号不能为空", null);
            logger.info("getPickUpEnter OUT,配送员取货确认失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "非配送员不能查看取货单详情", null);
            logger.info("getPickUpEnter OUT,配送员取货确认失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            AppEmployee appEmployee = appEmployeeService.findById(userID);
            if (null == appEmployee) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到此配送员", null);
                logger.info("getPickUpEnter OUT,配送员取货确认失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (StringUtils.isBlank(appEmployee.getDeliveryClerkNo())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "配送员编号为空", null);
                logger.info("getPickUpEnter OUT,配送员取货确认失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            ReturnOrderBaseInfo returnOrderBaseInfo = returnOrderService.queryByReturnNo(returnNumber);
            if (AssertUtil.isEmpty(returnOrderBaseInfo)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查询到该退单", null);
                logger.info("getPickUpEnter OUT,配送员取货确认失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (!returnOrderBaseInfo.getReturnStatus().equals(AppReturnOrderStatus.RETURNING)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "该状态退单不可确认收货", null);
                logger.info("getPickUpEnter OUT,配送员取货确认失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            AtwReturnOrderCheckEnter checkEnter = AtwReturnOrderCheckEnter.transform(returnOrderBaseInfo);

            //发送wms
            appToWmsOrderService.saveAtwReturnOrderCheckEnter(checkEnter);
            iCallWms.sendToWmsReturnOrderCheck(returnNumber);
            //修改退单状态
            returnOrderService.updateReturnOrderStatus(returnNumber, AppReturnOrderStatus.PENDING_REFUND);
            ReturnOrderDeliveryDetail detail = new ReturnOrderDeliveryDetail();
            detail.setDescription("配送员" + appEmployee.getName() +
                    "[" + appEmployee.getDeliveryClerkNo() + "]" +
                    "已确认取货!");
            detail.setReturnLogisticStatus(ReturnLogisticStatus.PICKUP_COMPLETE);
            detail.setReturnNo(returnNumber);
            detail.setCreateTime(Calendar.getInstance().getTime());
            detail.setPickersNumber(appEmployee.getDeliveryClerkNo());
            detail.setRoid(returnOrderBaseInfo.getRoid());
            detail.setPickersId(appEmployee.getEmpId());
            /*detail.setPicture();*/
            returnOrderDeliveryDetailsService.addReturnOrderDeliveryInfoDetails(detail);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, null);
            logger.info("getPickUpEnter OUT,配送员取货确认成功，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，配送员取货确认失败", null);
            logger.warn("getPickUpEnter EXCEPTION,配送员取货确认失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }
    }
    /**
     *  配送员获取已完成单列表
     * @param userId    用户id
     * @param identityType  用户类型
     * @return  已完成单列表
     */
    @PostMapping(value = "/finish/list", produces = "application/json;charset=UTF-8")
    public ResultDTO<Object> getFinishOrderList(Long userId, Integer identityType,Integer page, Integer size) {
        logger.info("getFinishOrderList CALLED,配送员获取已完成单列表，入参 userId:{} identityType:{},page:{},size:{}", userId, identityType,page,size);
        ResultDTO<Object> resultDTO;
        if (null == userId) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "userId不能为空！", null);
            logger.info("getFinishOrderList OUT,配送员获取已完成单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == identityType && identityType != 1) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "用户类型错误！", null);
            logger.info("getFinishOrderList OUT,配送员获取已完成单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == page) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "页码不能为空",
                    null);
            logger.info("getFinishOrderList OUT,配送员获取已完成单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == size) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "单页显示条数不能为空",
                    null);
            logger.info("getFinishOrderList OUT,配送员获取已完成单列表失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            AppEmployee appEmployee = appEmployeeService.findById(userId);
            if (null == appEmployee) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "未查到此配送员", null);
                logger.info("getDispatchingList OUT,配送员获取已完成单列表失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            PageInfo<AuditFinishResponse> auditFinishResponseList = orderDeliveryInfoDetailsService.getAuditFinishOrderByOperatorNo(appEmployee.getDeliveryClerkNo(), page, size);
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, new GridDataVO<AuditFinishResponse>().transform(auditFinishResponseList));
            if(null!=auditFinishResponseList&&null!=auditFinishResponseList.getList()){
                logger.info("getFinishOrderList OUT,配送员获取已完成单列表成功，出参 resultDTO:{}", auditFinishResponseList.getList().size());
            }else{
                logger.info("getFinishOrderList OUT,配送员获取已完成单列表成功，出参 resultDTO:{}", 0);
            }
            return resultDTO;
        }catch (Exception e){
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，配送员获取已完成单列表失败", null);
            logger.warn("getFinishOrderList EXCEPTION,配送员获取已完成单列表失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return resultDTO;
        }

    }
}
