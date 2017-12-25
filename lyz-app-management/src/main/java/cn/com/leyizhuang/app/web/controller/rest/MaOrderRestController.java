package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaOrderVORequest;
import cn.com.leyizhuang.app.foundation.service.MaOrderService;
import cn.com.leyizhuang.app.foundation.vo.MaOrderVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
     * @param offset       当前页
     * @param size         每页条数
     * @param keywords     不知
     * @param maOrderVORequest  多条件查询请求参数类
     * @return 订单列表
     */
    @GetMapping(value = "/page/test")
    public GridDataVO<MaOrderVO> findOrderByCondition(Integer offset, Integer size, String keywords, MaOrderVORequest maOrderVORequest,@RequestParam(value = "deliveryType") String deliveryType) {
        logger.warn("findOrderByCondition 多条件分页查询订单列表 ,入参 offsetL:{}, size:{}, kewords:{}, maOrderVORequest:{}",offset, size, keywords, maOrderVORequest);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            if ("-1".equals(deliveryType)){
                maOrderVORequest.setAppDeliveryType(null);
            }else if ("SELF_TAKE".equals(deliveryType)){
                maOrderVORequest.setAppDeliveryType(AppDeliveryType.SELF_TAKE);
            }else if ("HOUSE_DELIVERY".equals(deliveryType)){
                maOrderVORequest.setAppDeliveryType(AppDeliveryType.HOUSE_DELIVERY);
            }else if ("PRODUCT_COUPON".equals(deliveryType)){
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
     * @param offset    当前页
     * @param size  每页条数
     * @param keywords  不知
     * @return  订单列表
     */
    @GetMapping(value = "/pendingShipment/list")
    public GridDataVO<MaOrderVO> getPendingShipmentOrder(Integer offset, Integer size, String keywords){
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
}
