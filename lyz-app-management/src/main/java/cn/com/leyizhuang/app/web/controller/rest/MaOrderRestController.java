package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.EmpCreditMoneyChangeType;
import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.app.core.constant.remote.webservice.ebs.ChargeObjType;
import cn.com.leyizhuang.app.core.exception.*;
import cn.com.leyizhuang.app.core.utils.IpUtil;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import cn.com.leyizhuang.app.foundation.pojo.management.guide.GuideCreditChangeDetail;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderAmount;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderTempInfo;
import cn.com.leyizhuang.app.foundation.pojo.city.City;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaActGoodsMapping;
import cn.com.leyizhuang.app.foundation.pojo.order.*;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeOrder;
import cn.com.leyizhuang.app.foundation.pojo.recharge.RechargeReceiptInfo;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs.RechargeReceiptInf;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCompanyOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.PromotionSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.user.AppCustomer;
import cn.com.leyizhuang.app.foundation.pojo.user.AppEmployee;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.foundation.vo.MaOrderVO;
import cn.com.leyizhuang.app.foundation.vo.management.guide.GuideCreditChangeDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.order.MaAgencyAndArrearsOrderVO;
import cn.com.leyizhuang.app.foundation.vo.management.order.MaOrderBillingDetailResponse;
import cn.com.leyizhuang.app.foundation.vo.management.order.MaOrderDeliveryInfoResponse;
import cn.com.leyizhuang.app.foundation.vo.management.order.MaSelfTakeOrderVO;
import cn.com.leyizhuang.app.remote.queue.MaSinkSender;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 后台门店订单管理
 * Created by caiyu on 2017/12/16.
 */
@RestController
@RequestMapping(value = MaOrderRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaOrderRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/order";

    private final Logger logger = LoggerFactory.getLogger(MaOrderRestController.class);

    @Resource
    private AppEmployeeService appEmployeeService;
    @Resource
    private AppStoreService appStoreService;
    @Resource
    private CityService cityService;
    @Resource
    private AppCustomerService appCustomerService;
    @Resource
    private MaOrderService maOrderService;
    @Resource
    private CommonService commonService;
    @Resource
    private AppActDutchService dutchService;
    @Resource
    private AppActService actService;
    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;

    @Resource
    private MaSinkSender maSinkSender;

    @Resource
    private UserService userService;


    /**
     * 后台分页查询所有订单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords 不知
     * @return 订单列表
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<MaOrderVO> restOrderPageGird(Integer offset, Integer size, String keywords) {
        logger.warn("restOrderPageGird 后台分页获取所有订单列表 ,入参offsetL:{}, size:{}, kewords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            List<MaOrderVO> maOrderVOList = this.maOrderService.findMaOrderVOAll();
            PageInfo<MaOrderVO> maOrderVOPageInfo = new PageInfo<>(maOrderVOList);
            List<MaOrderVO> orderVOList = maOrderVOPageInfo.getList();
            logger.warn("restOrderPageGird ,后台分页获取所有订单列表成功", orderVOList.size());
            return new GridDataVO<MaOrderVO>().transform(orderVOList, maOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restOrderPageGird EXCEPTION,发生未知错误，后台分页获取所有订单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 分页查询城市订单列表
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords 不知
     * @param cityId   城市id
     * @return 订单列表
     */
    @GetMapping(value = "/page/cityGrid/{cityId}")
    public GridDataVO<MaOrderVO> getOrderByCityId(Integer offset, Integer size, String keywords, @PathVariable(value = "cityId") Long cityId) {
        logger.warn("getOrderByCityId 分页查询城市订单列表 ,入参 offsetL:{}, size:{}, kewords:{}, cityId:{}", offset, size, keywords, cityId);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            List<MaOrderVO> maOrderVOList = this.maOrderService.findMaOrderVOByCityId(cityId);
            PageInfo<MaOrderVO> maOrderVOPageInfo = new PageInfo<>(maOrderVOList);
            List<MaOrderVO> orderVOList = maOrderVOPageInfo.getList();
            logger.warn("getOrderByCityId ,分页查询城市订单列表成功", orderVOList.size());
            return new GridDataVO<MaOrderVO>().transform(orderVOList, maOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getOrderByCityId EXCEPTION,发生未知错误，分页查询城市订单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 根据订单号查询订单
     *
     * @param offset      当前页
     * @param size        每页条数
     * @param keywords    不知
     * @param orderNumber 订单号
     * @return
     */
    @GetMapping(value = "/page/byOrderNumber/{orderNumber}")
    public GridDataVO<MaOrderVO> findOrderByOrderNumber(Integer offset, Integer size, String keywords, @PathVariable(value = "orderNumber") String orderNumber) {
        logger.warn("findOrderByOrderNumber 根据订单号查询订单 ,入参 offsetL:{}, size:{}, kewords:{}, orderNumber:{}", offset, size, keywords, orderNumber);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            List<MaOrderVO> maOrderVOList = this.maOrderService.findMaOrderVOByOrderNumber(orderNumber);
            PageInfo<MaOrderVO> maOrderVOPageInfo = new PageInfo<>(maOrderVOList);
            List<MaOrderVO> orderVOList = maOrderVOPageInfo.getList();
            logger.warn("findOrderByOrderNumber ,根据订单号查询订单成功", orderVOList.size());
            return new GridDataVO<MaOrderVO>().transform(orderVOList, maOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findOrderByOrderNumber EXCEPTION,发生未知错误，根据订单号查询订单失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 多条件分页查询订单列表
     *
     * @param offset           当前页
     * @param size             每页条数
     * @param keywords         不知
     * @param maOrderVORequest 多条件查询请求参数类
     * @return 订单列表
     */
    @GetMapping(value = "/page/condition")
    public GridDataVO<MaOrderVO> findOrderByCondition(Integer offset, Integer size, String keywords, MaOrderVORequest maOrderVORequest, @RequestParam(value = "deliveryType") String deliveryType) {
        logger.warn("findOrderByCondition 多条件分页查询订单列表 ,入参 offsetL:{}, size:{}, kewords:{}, maOrderVORequest:{}", offset, size, keywords, maOrderVORequest);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            if ("-1".equals(deliveryType)) {
                maOrderVORequest.setAppDeliveryType(null);
            } else if ("SELF_TAKE".equals(deliveryType)) {
                maOrderVORequest.setAppDeliveryType(AppDeliveryType.SELF_TAKE);
            } else if ("HOUSE_DELIVERY".equals(deliveryType)) {
                maOrderVORequest.setAppDeliveryType(AppDeliveryType.HOUSE_DELIVERY);
            } else if ("PRODUCT_COUPON".equals(deliveryType)) {
                maOrderVORequest.setAppDeliveryType(AppDeliveryType.PRODUCT_COUPON);
            }
            List<MaOrderVO> maOrderVOList = this.maOrderService.findMaOrderVOByCondition(maOrderVORequest);
            PageInfo<MaOrderVO> maOrderVOPageInfo = new PageInfo<>(maOrderVOList);
            List<MaOrderVO> orderVOList = maOrderVOPageInfo.getList();
            logger.warn("getOrderByStoreIdAndCityIdAndDeliveryType ,根据配送方式分页查询订单列表成功", orderVOList.size());
            return new GridDataVO<MaOrderVO>().transform(orderVOList, maOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getOrderByStoreIdAndCityIdAndDeliveryType EXCEPTION,发生未知错误，根据配送方式分页查询订单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 获取待发货订单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords 不知
     * @return 订单列表
     */
    @GetMapping(value = "/pendingShipment/list")
    public GridDataVO<MaOrderVO> getPendingShipmentOrder(Integer offset, Integer size, String keywords) {
        logger.warn("getPendingShipmentOrder 获取待发货订单列表 ,入参offsetL:{}, size:{}, kewords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            List<MaOrderVO> maOrderVOList = this.maOrderService.findPendingShipmentOrder();
            PageInfo<MaOrderVO> maOrderVOPageInfo = new PageInfo<>(maOrderVOList);
            List<MaOrderVO> orderVOList = maOrderVOPageInfo.getList();
            logger.warn("getPendingShipmentOrder ,获取待发货订单列表成功", orderVOList.size());
            return new GridDataVO<MaOrderVO>().transform(orderVOList, maOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getPendingShipmentOrder EXCEPTION,发生未知错误，获取待发货订单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 多条件分页查询待发货订单列表
     *
     * @param offset                  当前页
     * @param size                    每页条数
     * @param keywords                不知
     * @param maCompanyOrderVORequest 多条件查询请求参数类
     * @return 订单列表
     */
    @GetMapping(value = "/page/pendingShipment/condition")
    public GridDataVO<MaOrderVO> findPendingShipmentOrderByCondition(Integer offset, Integer size, String keywords, MaCompanyOrderVORequest maCompanyOrderVORequest) {
        logger.warn("findPendingShipmentOrderByCondition 多条件分页查询待发货订单列表 ,入参 offsetL:{}, size:{}, kewords:{}, maCompanyOrderVORequest:{}", offset, size, keywords, maCompanyOrderVORequest);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            List<MaOrderVO> maOrderVOList = this.maOrderService.findPendingShipmentOrderByCondition(maCompanyOrderVORequest);
            PageInfo<MaOrderVO> maOrderVOPageInfo = new PageInfo<>(maOrderVOList);
            List<MaOrderVO> orderVOList = maOrderVOPageInfo.getList();
            logger.warn("findPendingShipmentOrderByCondition ,多条件分页查询待发货订单列表成功", orderVOList.size());
            return new GridDataVO<MaOrderVO>().transform(orderVOList, maOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findPendingShipmentOrderByCondition EXCEPTION,发生未知错误，多条件分页查询待发货订单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 根据订单号查询待配送订单
     *
     * @param offset      当前页
     * @param size        每页条数
     * @param keywords    不知
     * @param orderNumber 订单号
     * @return
     */
    @GetMapping(value = "/pendingShipment/byOrderNumber/{orderNumber}")
    public GridDataVO<MaOrderVO> findPendingShipmentOrderByOrderNumber(Integer offset, Integer size, String keywords, @PathVariable(value = "orderNumber") String orderNumber) {
        logger.warn("findPendingShipmentOrderByOrderNumber 根据订单号查询待配送订单 ,入参 offsetL:{}, size:{}, kewords:{}, orderNumber:{}", offset, size, keywords, orderNumber);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            List<MaOrderVO> maOrderVOList = this.maOrderService.findPendingShipmentOrderByOrderNumber(orderNumber);
            PageInfo<MaOrderVO> maOrderVOPageInfo = new PageInfo<>(maOrderVOList);
            List<MaOrderVO> orderVOList = maOrderVOPageInfo.getList();
            logger.warn("findPendingShipmentOrderByOrderNumber ,根据订单号查询待配送订单成功", orderVOList.size());
            return new GridDataVO<MaOrderVO>().transform(orderVOList, maOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findPendingShipmentOrderByOrderNumber EXCEPTION,发生未知错误，根据订单号查询待配送订单失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 后台查看门店配送单物流详情
     *
     * @param orderNumber 订单号
     * @return 物流详情
     */
    @GetMapping(value = "/delivery/{orderNumber}")
    public ResultDTO<MaOrderDeliveryInfoResponse> getDeliveryInfoByOrderNumber(@PathVariable(value = "orderNumber") String orderNumber) {
        logger.warn("getDeliveryInfoByOrderNumber 后台查看门店配送单物流详情 ,入参 orderNumber:{}", orderNumber);
        MaOrderDeliveryInfoResponse maOrderDeliveryInfoResponse = maOrderService.getDeliveryInfoByOrderNumber(orderNumber);
        if (null == maOrderDeliveryInfoResponse) {
            logger.warn("后台查看门店配送单物流详情失败：maOrderDeliveryInfoResponse == null", maOrderDeliveryInfoResponse);
            return new ResultDTO<>(CommonGlobal.COMMON_NOT_FOUND_CODE,
                    "订单物流信息不存在，请联系管理员", null);
        } else {
            maOrderDeliveryInfoResponse.setOrderDeliveryInfoDetailsList(orderDeliveryInfoDetailsService.queryListByOrderNumber(orderNumber));
            logger.warn("后台查看门店配送单物流详情成功：maOrderDeliveryInfoResponse{}", maOrderDeliveryInfoResponse);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, null, maOrderDeliveryInfoResponse);
        }
    }


    /**
     * 后台分页查询所有自提订单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords
     * @return 订单列表
     */
    @GetMapping(value = "/selfTakeOrder/page/grid")
    public GridDataVO<MaSelfTakeOrderVO> restSelfTakeOrderPageGird(Integer offset, Integer size, String keywords) {
        logger.info("restSelfTakeOrderPageGird 后台分页获取所有自提订单列表 ,入参offsetL:{}, size:{}, kewords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaSelfTakeOrderVO> maSelfTakeOrderVOPageInfo = this.maOrderService.findSelfTakeOrderList(page, size);
            List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maSelfTakeOrderVOPageInfo.getList();
            logger.info("restSelfTakeOrderPageGird ,后台分页获取所有自提订单列表成功", (maSelfTakeOrderVOList == null) ? 0 : maSelfTakeOrderVOList.size());
            return new GridDataVO<MaSelfTakeOrderVO>().transform(maSelfTakeOrderVOList, maSelfTakeOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restSelfTakeOrderPageGird EXCEPTION,发生未知错误，后台分页获取所有自提订单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 后台根据筛选条件分页查询所有待出货自提订单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords
     * @return 订单列表
     */
    @GetMapping(value = "/selfTakeOrder/page/screenGrid")
    public GridDataVO<MaSelfTakeOrderVO> restSelfTakeOrderPageGirdByScreen(Integer offset, Integer size, String keywords, @RequestParam(value = "cityId") Long cityId, @RequestParam(value = "storeId") Long storeId, @RequestParam(value = "status") Integer status, @RequestParam(value = "isPayUp") Integer isPayUp) {
        logger.info("restSelfTakeOrderPageGirdByCityId 后台根据筛选条件分页查询所有自提订单 ,入参offset:{}, size:{}, kewords:{},cityId:{},storeId:{},status:{},isPayUp:{}", offset, size, keywords, cityId, storeId, status, isPayUp);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaSelfTakeOrderVO> maSelfTakeOrderVOPageInfo = this.maOrderService.findSelfTakeOrderListByScreen(page, size, cityId, storeId, status, isPayUp);
            List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maSelfTakeOrderVOPageInfo.getList();
            logger.info("restSelfTakeOrderPageGirdByCityId ,后台根据筛选条件分页查询所有自提订单列表成功", (maSelfTakeOrderVOList == null) ? 0 : maSelfTakeOrderVOList.size());
            return new GridDataVO<MaSelfTakeOrderVO>().transform(maSelfTakeOrderVOList, maSelfTakeOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restSelfTakeOrderPageGirdByCityId EXCEPTION,发生未知错误，后台根据筛选条件分页查询所有自提订单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 后台根据条件信息分页查询自提订单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords
     * @return 订单列表
     */
    @GetMapping(value = "/selfTakeOrder/page/infoGrid")
    public GridDataVO<MaSelfTakeOrderVO> restSelfTakeOrderPageGirdByInfo(Integer offset, Integer size, String keywords, @RequestParam(value = "info") String info) {
        logger.info("restSelfTakeOrderPageGirdByInfo 后台根据条件信息分页查询自提订单 ,入参offsetL:{}, size:{}, kewords:{},info:{}", offset, size, keywords, info);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaSelfTakeOrderVO> maSelfTakeOrderVOPageInfo = this.maOrderService.findSelfTakeOrderListByInfo(page, size, info);
            List<MaSelfTakeOrderVO> maSelfTakeOrderVOList = maSelfTakeOrderVOPageInfo.getList();
            logger.info("restSelfTakeOrderPageGirdByInfo ,后台根据条件信息分页查询自提订单列表成功", (maSelfTakeOrderVOList == null) ? 0 : maSelfTakeOrderVOList.size());
            return new GridDataVO<MaSelfTakeOrderVO>().transform(maSelfTakeOrderVOList, maSelfTakeOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restSelfTakeOrderPageGirdByInfo EXCEPTION,发生未知错误，后台根据条件信息分页查询自提订单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 多条件分页查询自提订单列表
     *
     * @param offset           当前页
     * @param size             每页条数
     * @param keywords
     * @param maOrderVORequest 多条件查询请求参数类
     * @return 订单列表
     */
    @GetMapping(value = "/selfTakeOrder/page/conditionGrid")
    public GridDataVO<MaSelfTakeOrderVO> findSelfTakeOrderPageGirdByCondition(Integer offset, Integer size, String keywords, MaOrderVORequest maOrderVORequest) {
        logger.warn("findSelfTakeOrderPageGirdByCondition 多条件分页查询订单列表 ,入参 offsetL:{}, size:{}, kewords:{}, maOrderVORequest:{}", offset, size, keywords, maOrderVORequest);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaSelfTakeOrderVO> maOrderVOList = this.maOrderService.findSelfTakeOrderByCondition(page, size, maOrderVORequest);
            List<MaSelfTakeOrderVO> orderVOList = maOrderVOList.getList();
            logger.warn("findSelfTakeOrderPageGirdByCondition ,多条件分页查询订单列表成功", orderVOList.size());
            return new GridDataVO<MaSelfTakeOrderVO>().transform(orderVOList, maOrderVOList.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findSelfTakeOrderPageGirdByCondition EXCEPTION,发生未知错误，多条件分页查询订单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 后台自提单发货
     *
     * @param orderNumber
     * @return
     */
    @Transactional
    @GetMapping(value = "/orderShipping")
    public ResultDTO<Object> orderShipping(@RequestParam(value = "orderNumber") String orderNumber, @RequestParam(value = "code") String code) {
        logger.warn("orderShipping 后台自提单单发货 ,入参orderNumbe:{} code:{}", orderNumber, code);
        ResultDTO<Object> resultDTO;
        if (null == orderNumber && "".equals(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单编号不允许为空", null);
            logger.warn("orderShipping OUT,后台自提单发货失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        MaOrderTempInfo maOrderTempInfo = maOrderService.getOrderInfoByOrderNo(orderNumber);
        if ("SELLER".equals(maOrderTempInfo.getCreatorIdentityType())) {
            if (null == code && "".equals(code)) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "验证码不允许为空", null);
                logger.warn("orderShipping OUT,后台自提单发货失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
            if (!code.equals(maOrderTempInfo.getPickUpCode())) {
                resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "验证码错误", null);
                logger.warn("orderShipping OUT,后台自提单发货失败，出参 resultDTO:{}", resultDTO);
                return resultDTO;
            }
        }
        if (!"PENDING_RECEIVE".equals(maOrderTempInfo.getStatus().getValue())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单状态错误", null);
            logger.warn("orderShipping OUT,后台自提单发货失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            ShiroUser shiroUser = this.getShiroUser();
            //后台发货并返回插入接口表数据的id
            maOrderService.orderShipping(orderNumber, shiroUser, maOrderTempInfo);
            //发送门店自提单消息队列
            maSinkSender.sendStorePickUpReceivedToEBSAndRecord(orderNumber);
            logger.info("orderShipping ,后台自提单发货成功");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,
                    "后台自提单发货成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("orderShipping EXCEPTION,发生未知错误，后台自提单发货失败");
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "后台自提单发货失败", null);
        }
    }


    /**
     * 验证后台顾客下单发货验证码
     *
     * @param code
     * @param orderNumber
     * @return
     */
    @GetMapping(value = "/judgmentVerification")
    public ResultDTO<Object> judgmentVerification(@RequestParam(value = "orderNumber") String orderNumber, @RequestParam(value = "code") String code) {
        logger.warn("orderForGuide 后台验证发货验证码 ,入参orderNumbe:{},code:{}", orderNumber, code);
        ResultDTO<Object> resultDTO;
        if (null == orderNumber && "".equals(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单编号不允许为空", null);
            logger.warn("judgmentVerification OUT,后台验证发货验证码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == code && "".equals(code)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "验证码不允许为空", null);
            logger.warn("judgmentVerification OUT,后台验证发货验证码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            Boolean isCorrect = this.maOrderService.judgmentVerification(orderNumber, code);
            if (isCorrect) {
                logger.warn("judgmentVerification ,后台验证发货验证码成功,验证码正确");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,
                        "后台验证发货验证码成功,验证码正确", null);
            } else {
                logger.warn("judgmentVerification ,后台验证发货验证码成功,验证码错误");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                        "后台验证发货验证码成功,验证码错误", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("orderForGuide EXCEPTION,发生未知错误，后台验证发货验证码失败");
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "后台验证发货验证码失败", null);
        }
    }

    /**
     * 后台自提单收款
     *
     * @param
     * @param maOrderAmount
     * @return
     */
    @PostMapping(value = "/orderReceivables")
    public ResultDTO<Object> orderReceivables(MaOrderAmount maOrderAmount) {
        logger.warn("orderReceivables 后台订单收款 ,maOrderAmount:{}", maOrderAmount);
        ResultDTO<Object> resultDTO;
        if (null == maOrderAmount.getCashAmount()) {
            maOrderAmount.setCashAmount(BigDecimal.ZERO);
        }
        if (null == maOrderAmount.getOtherAmount()) {
            maOrderAmount.setOtherAmount(BigDecimal.ZERO);
        }
        if (null == maOrderAmount.getPosAmount()) {
            maOrderAmount.setPosAmount(BigDecimal.ZERO);
        }
        MaOrderTempInfo maOrderTempInfo = maOrderService.getOrderInfoByOrderNo(maOrderAmount.getOrderNumber());
        BigDecimal acount = maOrderAmount.getCashAmount().add(maOrderAmount.getOtherAmount()).add(maOrderAmount.getPosAmount());
        if (null == maOrderAmount.getAllAmount()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "总金额为空", null);
            logger.warn("orderReceivablesForCustomer OUT,后台订单收款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(maOrderAmount.getOrderNumber())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号为空", null);
            logger.warn("orderReceivablesForCustomer OUT,后台订单收款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        //获取订单账目明细
        MaOrderBillingDetailResponse maOrderBillingDetailResponse = maOrderService.getMaOrderBillingDetailByOrderNumber(maOrderAmount.getOrderNumber());
        if (0 != acount.compareTo(BigDecimal.valueOf(maOrderBillingDetailResponse.getArrearage()))) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "收款金额之和不等于总金额", null);
            logger.warn("orderReceivablesForCustomer OUT,后台订单收款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if ("CANCELED".equals(maOrderTempInfo.getStatus().getValue())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "当前订单已取消,不能收款", null);
            logger.warn("orderReceivablesForCustomer OUT,后台订单收款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (maOrderAmount.getDate().after(new Date())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "收款时间大于当前时间", null);
            logger.warn("orderReceivablesForCustomer OUT,后台订单收款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            List<String> ReceiptNumberList = this.maOrderService.orderReceivables(maOrderAmount);
            for (String receiptNumber : ReceiptNumberList) {
                this.maSinkSender.sendOrderReceipt(receiptNumber);
            }
            logger.warn("orderReceivablesForCustomer ,后台订单收款成功");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,
                    "后台订单收款成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("orderReceivablesForCustomer EXCEPTION,发生未知错误，后台订单收款失败");
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "后台订单收款失败", null);
        }
    }


    /**
     * 后台分页查询所有欠款与还款订单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords
     * @return 订单列表
     */
    @GetMapping(value = "/arrearsAndAgencyOrder/page/grid")
    public GridDataVO<MaAgencyAndArrearsOrderVO> restArrearsAndAgencyOrderPageGird(Integer offset, Integer size, String keywords) {
        logger.info("restarrearsAndAgencyOrderPageGird 后台分页查询所有欠款与还款订单 ,入参offsetL:{}, size:{}, kewords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaAgencyAndArrearsOrderVO> arrearsAndAgencyOrderVOPageInfo = this.maOrderService.findArrearsAndAgencyOrderList(page, size);
            List<MaAgencyAndArrearsOrderVO> arrearsAndAgencyOrderVOList = arrearsAndAgencyOrderVOPageInfo.getList();
            logger.info("restarrearsAndAgencyOrderPageGird ,后台分页查询所有欠款与还款订单", (arrearsAndAgencyOrderVOList == null) ? 0 : arrearsAndAgencyOrderVOList.size());
            return new GridDataVO<MaAgencyAndArrearsOrderVO>().transform(arrearsAndAgencyOrderVOList, arrearsAndAgencyOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restarrearsAndAgencyOrderPageGird EXCEPTION,发生未知错误，后台分页查询所有欠款与还款订单");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 后台根据筛选条件分页查询所有欠款与还款订单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords
     * @return 订单列表
     */
    @GetMapping(value = "/arrearsAndAgencyOrder/page/screenGrid")
    public GridDataVO<MaAgencyAndArrearsOrderVO> restArrearsAndAgencyOrderPageGirdByScreen(Integer offset, Integer size, String keywords, @RequestParam(value = "cityId") Long cityId, @RequestParam(value = "storeId") Long storeId, @RequestParam(value = "status") Integer status, @RequestParam(value = "isPayUp") Integer isPayUp) {
        logger.info("restArrearsAndAgencyOrderPageGirdByScreen 后台根据筛选条件分页查询所有欠款与还款订单 ,入参offset:{}, size:{}, kewords:{},cityId:{},storeId:{},status:{},isPayUp:{}", offset, size, keywords, cityId, storeId, status, isPayUp);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaAgencyAndArrearsOrderVO> maAgencyAndArrearsOrderVOPageInfo = this.maOrderService.findMaAgencyAndArrearsOrderListByScreen(page, size, cityId, storeId, status, isPayUp);
            List<MaAgencyAndArrearsOrderVO> maAgencyAndArrearsOrderVOList = maAgencyAndArrearsOrderVOPageInfo.getList();
            logger.info("restArrearsAndAgencyOrderPageGirdByScreen ,后台根据筛选条件分页查询所有欠款与还款订单列表成功", (maAgencyAndArrearsOrderVOList == null) ? 0 : maAgencyAndArrearsOrderVOList.size());
            return new GridDataVO<MaAgencyAndArrearsOrderVO>().transform(maAgencyAndArrearsOrderVOList, maAgencyAndArrearsOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restArrearsAndAgencyOrderPageGirdByScreen EXCEPTION,发生未知错误，后台根据筛选条件分页查询所有欠款与还款订单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 后台根据条件信息分页查询欠款与还款订单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords
     * @return 订单列表
     */
    @GetMapping(value = "/arrearsAndAgencyOrder/page/infoGrid")
    public GridDataVO<MaAgencyAndArrearsOrderVO> restArrearsAndAgencyOrderPageGirdByInfo(Integer offset, Integer size, String keywords, @RequestParam(value = "info") String info) {
        logger.info("restArrearsAndAgencyOrderPageGirdByInfo 后台根据条件信息分页查询欠款与还款订单 ,入参offsetL:{}, size:{}, kewords:{},info:{}", offset, size, keywords, info);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaAgencyAndArrearsOrderVO> maAgencyAndArrearsOrderVOPageInfo = this.maOrderService.findMaAgencyAndArrearsOrderListByInfo(page, size, info);
            List<MaAgencyAndArrearsOrderVO> maAgencyAndArrearsOrderVOList = maAgencyAndArrearsOrderVOPageInfo.getList();
            logger.info("restArrearsAndAgencyOrderPageGirdByInfo ,后台根据条件信息分页查询欠款与还款订单成功", (maAgencyAndArrearsOrderVOList == null) ? 0 : maAgencyAndArrearsOrderVOList.size());
            return new GridDataVO<MaAgencyAndArrearsOrderVO>().transform(maAgencyAndArrearsOrderVOList, maAgencyAndArrearsOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restArrearsAndAgencyOrderPageGirdByInfo EXCEPTION,发生未知错误，后台根据条件信息分页查询欠款与还款订单失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 审核订单
     *
     * @param
     * @return
     */
    @PutMapping(value = "/arrearsAndAgencyOrder/auditOrderStatus")
    public ResultDTO<Object> auditOrderStatus(@RequestParam(value = "orderNumber") String orderNumber, @RequestParam(value = "status") String status) {
        logger.info("auditOrderStatus 审核订单 ,入参orderNumber:{},status:{}", orderNumber, status);
        if (StringUtils.isBlank(status)) {
            logger.warn("订单状态为空,审核订单失败");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "审核订单失败", null);
        }
        if (StringUtils.isBlank(orderNumber)) {
            logger.warn("订单号为空,审核订单失败");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "审核订单失败", null);
        }
        if (!(ArrearsAuditStatus.AUDIT_PASSED.toString().equals(status) || ArrearsAuditStatus.AUDIT_NO.toString().equals(status))) {
            logger.warn("订单状态错误,审核订单失败");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "审核订单失败", null);
        }
        String orderStatus = maOrderService.getArrearsAuditInfo(orderNumber).getStatus().getValue();
        if (StringUtils.isBlank(orderStatus) || !(orderStatus.equals(ArrearsAuditStatus.AUDITING.toString()))) {
            logger.info("欠款审核单信息错误！ 该订单不在审核状态,orderStatus{}" + orderStatus);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "欠款审核单信息错误！",
                    null);
        }
        try {
          String receiptNumber = this.maOrderService.auditOrderStatus(orderNumber, status);
          if(null!=receiptNumber){
              //将收款记录录入拆单消息队列
              this.maSinkSender.sendOrderReceipt(receiptNumber);
          }
            logger.info("auditOrderStatus ,审核订单成功");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,
                    "审核订单成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("auditOrderStatus EXCEPTION,发生未知错误，审核订单失败");
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "审核订单失败", null);
        }
    }


    /**
     * 欠款订单还款
     *
     * @param
     * @return
     */
    @PutMapping(value = "/arrearsAndAgencyOrder/arrearsOrderRepayment")
    public ResultDTO<Object> arrearsOrderRepayment(MaOrderAmount maOrderAmount, HttpServletRequest request, @RequestParam String lastUpdateTime) {
        logger.info("arrearsOrderRepayment 欠款订单还款 ,入参maOrderAmount:{}", maOrderAmount);
        ResultDTO<Object> resultDTO;
        if (null == maOrderAmount.getCashAmount()) {
            maOrderAmount.setCashAmount(BigDecimal.ZERO);
        }
        if (null == maOrderAmount.getOtherAmount()) {
            maOrderAmount.setOtherAmount(BigDecimal.ZERO);
        }
        if (null == maOrderAmount.getPosAmount()) {
            maOrderAmount.setPosAmount(BigDecimal.ZERO);
        }
        Double repaymentAmount = maOrderService.queryRepaymentAmount(maOrderAmount.getOrderNumber());
        BigDecimal acount = maOrderAmount.getCashAmount().add(maOrderAmount.getOtherAmount()).add(maOrderAmount.getPosAmount());
        if (null == maOrderAmount.getAllAmount()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "总金额为空", null);
            logger.warn("arrearsOrderRepayment OUT,后台欠款订单还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(maOrderAmount.getOrderNumber())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号为空", null);
            logger.warn("arrearsOrderRepayment OUT,后台欠款订单还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (StringUtils.isBlank(lastUpdateTime)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "导购额度上次更新时间为空", null);
            logger.warn("arrearsOrderRepayment OUT,后台欠款订单还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (0 != acount.compareTo(BigDecimal.valueOf(repaymentAmount))) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "所有金额不等于总金额", null);
            logger.warn("arrearsOrderRepayment OUT,后台欠款订单还款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date lastUpdateTimeFormat = dateFormat.parse(lastUpdateTime);
            ShiroUser shiroUser = this.getShiroUser();
            GuideCreditChangeDetail guideCreditChangeDetail = new GuideCreditChangeDetail();
            guideCreditChangeDetail.setOperatorId(shiroUser.getId());
            guideCreditChangeDetail.setOperatorName(shiroUser.getName());
            guideCreditChangeDetail.setChangeTypeDesc(EmpCreditMoneyChangeType.ORDER_REPAYMENT.getDescription());
            guideCreditChangeDetail.setChangeType(EmpCreditMoneyChangeType.ORDER_REPAYMENT);
            guideCreditChangeDetail.setOperatorIp(IpUtil.getIpAddress(request));
            guideCreditChangeDetail.setReferenceNumber(maOrderAmount.getOrderNumber());
            List<String> receiptNumberList =this.maOrderService.arrearsOrderRepayment(maOrderAmount, guideCreditChangeDetail, lastUpdateTimeFormat);
            for(String receiptNumber:receiptNumberList){
                this.maSinkSender.sendOrderReceipt(receiptNumber);
            }
            logger.warn("arrearsOrderRepayment ,后台欠款订单还款成功");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,
                    "后台欠款订单还款成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("arrearsOrderRepayment EXCEPTION,发生未知错误，后台欠款订单还款失败");
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "后台欠款订单还款失败", null);
        }
    }


    /**
     * 保存买券信息，创建买券订单
     *
     * @param sellerId
     * @param customerId
     * @param goodsDetails
     * @param giftDetails
     * @param cashMoney
     * @param posMoney
     * @param otherMoney
     * @param posNumber
     * @param collectMoneyTime
     * @param remarks
     * @param preDepositMoney
     * @param preDepositCollectMoneyTime
     * @param preDepositRemarks
     * @param salesNumber
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/save/productCoupon")
    public ResultDTO<?> saveMaProductCoupon(HttpServletRequest request, Long sellerId, Long customerId, String goodsDetails, String giftDetails,
                                            Double cashMoney, Double posMoney, Double otherMoney, String posNumber, Double totalMoney,
                                            String collectMoneyTime, String remarks, String salesNumber, Double preDepositMoney, String preDepositCollectMoneyTime, String preDepositRemarks) throws IOException {
        logger.info("saveMaProductCoupon 保存买券信息，创建买券订单,入参 sellerId:{},customerId:{},goodsDetails:{},giftDateils:{},cashMoney:{},posMoney:{},otherMoney:{}," +
                        "posNumber:{},collectMoneyTime:{},remarks:{},preDepositMoney:{},preDepositCollectMoneyTime:{},preDepositRemarks:{},preDepositRemarks:{},salesNumber:{}", sellerId, customerId, goodsDetails, giftDetails,
                cashMoney, posMoney, otherMoney, posNumber, collectMoneyTime, remarks, preDepositMoney, preDepositCollectMoneyTime, preDepositRemarks, totalMoney, salesNumber);
        if (null == sellerId) {
            logger.warn("saveMaProductCoupon OUT,保存买券信息，创建买券订单失败,导购id不能为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "导购id不能为空！", null);
        }

        if (null == customerId) {
            logger.warn("saveMaProductCoupon OUT,保存买券信息，创建买券订单失败,顾客id不能为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "顾客id不能为空！", null);
        }
        if (StringUtils.isBlank(goodsDetails)) {
            logger.warn("saveMaProductCoupon OUT,保存买券信息，创建买券订单失败,商品信息不能为空！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "商品信息不能为空！", null);
        }
        if (StringUtils.isNotBlank(posNumber) && posNumber.length() < 6) {
            logger.warn("saveMaProductCoupon OUT,保存买券信息，创建买券订单失败,POS流水号小于6位！");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "请输入POS流水号后6位数！", null);
        }
        try {

            // 当前登录帐号
            ShiroUser shiroUser = super.getShiroUser();
            User user = userService.findByLoginName(shiroUser.getLoginName());

            ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, MaActGoodsMapping.class);
            JavaType javaType2 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, PromotionSimpleInfo.class);
            //商品信息
            List<MaActGoodsMapping> goodsList = objectMapper.readValue(goodsDetails, javaType1);
            //促销信息
            List<PromotionSimpleInfo> giftList = new ArrayList<>();
            if (StringUtils.isNotBlank(giftDetails)) {
                giftList = objectMapper.readValue(giftDetails, javaType2);
            }
            // 检查促销是否过期
            List<Long> promotionIds = new ArrayList<>();
            for (PromotionSimpleInfo promotion : giftList) {
                promotionIds.add(promotion.getPromotionId());
            }
            if (promotionIds.size() > 0) {
                Boolean outTime = actService.checkActOutTime(promotionIds);
                if (!outTime) {
                    logger.warn("saveMaProductCoupon OUT,买券订单创建失败,出参 resultDTO:{}", "存在过期促销，请重新下单！");
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "保存失败，存在过期促销，请重新下单！", null);
                }
            }
            //获取导购信息
            AppEmployee appEmployee = appEmployeeService.findById(sellerId);
            if (null == appEmployee) {
                logger.warn("saveMaProductCoupon OUT,买券订单创建失败,出参 resultDTO:{}", "未查询到导购信息！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "保存失败，未查询到导购信息！", null);
            }

            //获取顾客信息
            AppCustomer appCustomer = appCustomerService.findById(customerId);
            if (null == appCustomer) {
                logger.warn("saveMaProductCoupon OUT,买券订单创建失败,出参 resultDTO:{}", "未查询到顾客信息！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "保存失败，未查询到顾客信息！", null);
            }
            //获取城市信息
            City city = cityService.findById(appEmployee.getCityId());
            if (null == city) {
                logger.warn("saveMaProductCoupon OUT,买券订单创建失败,出参 resultDTO:{}", "未查询到城市信息！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "保存失败，未查询到城市信息！", null);
            }
            //获取门店信息
            AppStore appStore = appStoreService.findById(appEmployee.getStoreId());
            if (null == appStore) {
                logger.warn("saveMaProductCoupon OUT,买券订单创建失败,出参 resultDTO:{}", "未查询到门店信息！");
                return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "保存失败，未查询到门店信息！", null);
            }

            if ("2121".equals(city.getNumber()) && "ZY".equals(appStore.getStoreType().getValue())) {
                if (StringUtils.isBlank(salesNumber)) {
                    logger.warn("saveMaProductCoupon OUT,保存买券信息，创建买券订单失败,销售纸质单号不能为空！");
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "保存失败，销售纸质单号不能为空！", null);
                }
            }
            //***********************创建买券订单头*********************
            //生成订单号
            String orderNumber = OrderUtils.generateOrderNumber(city.getCityId());
            OrderBaseInfo orderBaseInfo = maOrderService.createMaOrderBaseInfo(appCustomer, city, appStore, appEmployee,
                    preDepositMoney, remarks, preDepositRemarks, totalMoney, orderNumber, salesNumber);

            //******************************创建买券订单商品信息******************************
            CreateOrderGoodsSupport support = commonService.createMaOrderGoodsInfo(goodsList, appCustomer, sellerId, 0, orderNumber);

            //****************** 创建订单物流信息 ******************
            OrderLogisticsInfo orderLogisticsInfo = maOrderService.createMaOrderLogisticsInfo(appStore, orderNumber);


            //****************** 处理订单账单相关信息 ***************
            OrderBillingDetails orderBillingDetails = new OrderBillingDetails();
            orderBillingDetails.setOrderNumber(orderBaseInfo.getOrderNumber());
            orderBillingDetails.setTotalGoodsPrice(support.getGoodsTotalPrice());
            orderBillingDetails.setMemberDiscount(support.getMemberDiscount());
            orderBillingDetails.setPromotionDiscount(support.getPromotionDiscount());
            String payTime = "";
            if (null != preDepositMoney) {
                cashMoney = null;
                posMoney = null;
                posNumber = null;
                otherMoney = null;
                payTime = null;
            } else {
                if (null != posMoney && posNumber == null) {
                    logger.warn("saveMaProductCoupon OUT,买券订单创建失败,出参 resultDTO:{}", "有POS收款金额，请填写POS流水号！！");
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "保存失败，有POS收款金额，请填写POS流水号！", null);
                }
                if (!StringUtils.isBlank(posNumber) && posMoney == null) {
                    logger.warn("saveMaProductCoupon OUT,买券订单创建失败,出参 resultDTO:{}", "有POS流水号，请填写POS收款金额！");
                    return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "保存失败，有POS流水号，请填写POS收款金额！", null);
                }
                preDepositMoney = null;
                payTime = collectMoneyTime;
            }
            orderBillingDetails = maOrderService.createMaOrderBillingDetails(orderBillingDetails, preDepositMoney, cashMoney, posMoney, otherMoney, posNumber, payTime);
            orderBaseInfo.setTotalGoodsPrice(orderBillingDetails.getTotalGoodsPrice());

            //****************** 处理订单账单支付明细信息 ************
            Map<Object, Object> map = maOrderService.createMaOrderBillingPaymentDetails(orderBaseInfo, orderBillingDetails, appStore, appCustomer, user.getUid());
            List<OrderBillingPaymentDetails> paymentDetails = (List<OrderBillingPaymentDetails>) map.get("billingPaymentDetails");
            List<RechargeReceiptInfo> rechargeReceiptInfoList = (List<RechargeReceiptInfo>) map.get("rechargeReceiptInfoList");
            List<RechargeOrder> rechargeOrderList = (List<RechargeOrder>) map.get("rechargeOrderList");

            //********* 开始计算分摊 促销分摊可能产生新的行记录 所以优先分摊 ****************
            List<OrderGoodsInfo> orderGoodsInfoList;
            orderGoodsInfoList = dutchService.addGoodsDetailsAndDutch(sellerId, AppIdentityType.getAppIdentityTypeByValue(0), giftList, support.getOrderGoodsInfoList());

            //******** 分摊完毕 计算退货 单价 ***************************
            orderGoodsInfoList = dutchService.countReturnPrice(orderGoodsInfoList);

            //**************** 1、检查账单支付金额是否充足,如果充足就扣减相应的数量 ***********
            //**************** 2、持久化订单相关实体信息 ****************
            maOrderService.createMaOrderBusiness(0, sellerId, orderBillingDetails, orderBaseInfo, orderGoodsInfoList, paymentDetails, null, orderLogisticsInfo, user.getUid(), rechargeReceiptInfoList, rechargeOrderList);


            //将该订单入拆单消息队列
            maSinkSender.sendRechargeReceipt(orderBaseInfo.getOrderNumber());

            logger.warn("saveMaProductCoupon OUT,买券订单创建成功", "买券订单创建成功");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS, "买券订单创建成功", null);
        } catch (LockStorePreDepositException | SystemBusyException | GoodsMultipartPriceException | GoodsNoPriceException |
                OrderPayableAmountException | DutchException e) {
            e.printStackTrace();
            logger.warn("saveMaProductCoupon EXCEPTION,买券订单创建失败,出参 resultDTO:{}", e.getMessage());
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, e.getMessage(), null);
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn("saveMaProductCoupon EXCEPTION,买券订单创建失败,出参 resultDTO:{}", "买券订单参数转换异常!");
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "买券订单参数转换异常!", null);
        } catch (OrderSaveException e) {
            e.printStackTrace();
            logger.warn("saveMaProductCoupon EXCEPTION,买券订单创建失败,出参 resultDTO:{}", "买券订单创建异常!");
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "买券订单创建异常!", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("saveMaProductCoupon EXCEPTION,买券订单创建失败,出参 resultDTO:{}", "发生未知异常,下单失败!");
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常,下单失败!", null);
        }
    }
}
