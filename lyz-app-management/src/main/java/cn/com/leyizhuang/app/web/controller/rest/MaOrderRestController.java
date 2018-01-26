package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.remote.ebs.EbsSenderService;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderAmount;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaOrderTempInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.webservice.ebs.MaOrderReceiveInf;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCompanyOrderVORequest;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaOrderVORequest;
import cn.com.leyizhuang.app.foundation.service.MaOrderService;
import cn.com.leyizhuang.app.foundation.service.OrderDeliveryInfoDetailsService;
import cn.com.leyizhuang.app.foundation.vo.MaOrderVO;
import cn.com.leyizhuang.app.foundation.vo.management.order.MaOrderDeliveryInfoResponse;
import cn.com.leyizhuang.app.foundation.vo.management.order.MaSelfTakeOrderVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

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
    private MaOrderService maOrderService;

    @Resource
    private OrderDeliveryInfoDetailsService orderDeliveryInfoDetailsService;


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
    public GridDataVO<MaSelfTakeOrderVO> restSelfTakeOrderPageGirdByScreen(Integer offset, Integer size, String keywords, @RequestParam(value = "cityId") Long cityId, @RequestParam(value = "storeId") Long storeId,@RequestParam(value = "status") Integer status,@RequestParam(value = "isPayUp") Integer isPayUp) {
        logger.info("restSelfTakeOrderPageGirdByCityId 后台根据筛选条件分页查询所有自提订单 ,入参offset:{}, size:{}, kewords:{},cityId:{},storeId:{},status:{},isPayUp:{}", offset, size, keywords, cityId,storeId,status,isPayUp);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaSelfTakeOrderVO> maSelfTakeOrderVOPageInfo = this.maOrderService.findSelfTakeOrderListByScreen(page, size, cityId,storeId,status,isPayUp);
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
    @GetMapping(value = "/orderShipping")
    public ResultDTO<Object> orderShipping(@RequestParam(value = "orderNumber") String orderNumber,@RequestParam(value = "code") String code) {
        logger.warn("orderShipping 后台自提单单发货 ,入参orderNumbe:{} code:{}",orderNumber,code);
        ResultDTO<Object> resultDTO;
        MaOrderTempInfo maOrderTempInfo = maOrderService.getOrderInfoByOrderNo(orderNumber);
        if (null == orderNumber&&"".equals(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单编号不允许为空", null);
            logger.warn("orderShipping OUT,后台自提单发货失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if("SELLER".equals(maOrderTempInfo.getCreatorIdentityType())){
            if (null == code&&"".equals(code)) {
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
        try {
            ShiroUser shiroUser =this.getShiroUser();
            this.maOrderService.orderShipping(orderNumber,shiroUser,maOrderTempInfo);
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
    public ResultDTO<Object> judgmentVerification(@RequestParam(value = "orderNumber") String orderNumber,@RequestParam(value = "code") String code) {
        logger.warn("orderForGuide 后台验证发货验证码 ,入参orderNumbe:{},code:{}",orderNumber,code);
        ResultDTO<Object> resultDTO;
        if (null == orderNumber&&"".equals(orderNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单编号不允许为空", null);
            logger.warn("judgmentVerification OUT,后台验证发货验证码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null == code&&"".equals(code)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "验证码不允许为空", null);
            logger.warn("judgmentVerification OUT,后台验证发货验证码失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
           Boolean isCorrect = this.maOrderService.judgmentVerification(orderNumber,code);
           if(isCorrect){
               logger.warn("judgmentVerification ,后台验证发货验证码成功,验证码正确");
               return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,
                       "后台验证发货验证码成功,验证码正确", null);
           }else{
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
     * 后台订单收款
     *
     * @param
     * @param maOrderAmount
     * @return
     */
    @PostMapping(value = "/orderReceivables")
    public ResultDTO<Object> orderReceivables( MaOrderAmount maOrderAmount) {
        logger.warn("orderReceivables 后台订单收款 ,maOrderAmount:{}",maOrderAmount);
        ResultDTO<Object> resultDTO;
        if(null==maOrderAmount.getCashAmount()){
            maOrderAmount.setCashAmount(BigDecimal.ZERO);
        }
        if(null==maOrderAmount.getOtherAmount()){
            maOrderAmount.setOtherAmount(BigDecimal.ZERO);
        }
        if(null==maOrderAmount.getPosAmount()){
            maOrderAmount.setPosAmount(BigDecimal.ZERO);
        }
        BigDecimal acount = maOrderAmount.getCashAmount().add(maOrderAmount.getOtherAmount()).add(maOrderAmount.getPosAmount());
        if (null==maOrderAmount.getAllAmount()) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "总金额为空", null);
            logger.warn("orderReceivablesForCustomer OUT,后台订单收款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (null==maOrderAmount.getOrderNumber()||"".equals(maOrderAmount.getOrderNumber())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单号为空", null);
            logger.warn("orderReceivablesForCustomer OUT,后台订单收款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        if (!acount.equals(maOrderAmount.getAllAmount())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_ERROR_PARAM_CODE, "所有金额不等于总金额", null);
            logger.warn("orderReceivablesForCustomer OUT,后台订单收款失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            this.maOrderService.orderReceivables(maOrderAmount);
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
}
