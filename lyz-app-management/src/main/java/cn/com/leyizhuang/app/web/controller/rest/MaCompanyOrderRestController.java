package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.request.management.MaCompanyOrderVORequest;
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
 * 后台装饰公司订单管理
 * Created by caiyu on 2017/12/16.
 */
@RestController
@RequestMapping(value = MaCompanyOrderRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaCompanyOrderRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/company/order";

    private final Logger logger = LoggerFactory.getLogger(MaCompanyOrderRestController.class);

    @Resource
    private MaOrderService maOrderService;

    /**
     * 后台分页查询装饰公司所有订单
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords 不知
     * @return 订单列表
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<MaOrderVO> restOrderPageGird(Integer offset, Integer size, String keywords) {
        logger.warn("restOrderPageGird 后台分页查询装饰公司所有订单 ,入参offsetL:{}, size:{}, kewords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            List<MaOrderVO> maOrderVOList = this.maOrderService.findCompanyOrderAll();
            PageInfo<MaOrderVO> maOrderVOPageInfo = new PageInfo<>(maOrderVOList);
            List<MaOrderVO> orderVOList = maOrderVOPageInfo.getList();
            logger.warn("restOrderPageGird ,后台分页查询装饰公司所有订单成功", orderVOList.size());
            return new GridDataVO<MaOrderVO>().transform(orderVOList, maOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restOrderPageGird EXCEPTION,发生未知错误，后台分页查询装饰公司所有订单失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 根据订单号查询装饰公司订单
     *
     * @param offset      当前页
     * @param size        每页条数
     * @param keywords    不知
     * @param orderNumber 订单号
     * @return
     */
    @GetMapping(value = "/page/byOrderNumber/{orderNumber}")
    public GridDataVO<MaOrderVO> findOrderByOrderNumber(Integer offset, Integer size, String keywords, @PathVariable(value = "orderNumber") String orderNumber) {
        logger.warn("findOrderByOrderNumber 根据订单号查询装饰公司订单 ,入参 offsetL:{}, size:{}, kewords:{}, orderNumber:{}", offset, size, keywords, orderNumber);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            List<MaOrderVO> maOrderVOList = this.maOrderService.findCompanyOrderByOrderNumber(orderNumber);
            PageInfo<MaOrderVO> maOrderVOPageInfo = new PageInfo<>(maOrderVOList);
            List<MaOrderVO> orderVOList = maOrderVOPageInfo.getList();
            logger.warn("findOrderByOrderNumber ,根据订单号查询装饰公司订单成功", orderVOList.size());
            return new GridDataVO<MaOrderVO>().transform(orderVOList, maOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("findOrderByOrderNumber EXCEPTION,发生未知错误，根据订单号查询装饰公司订单失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 多条件分页查询装饰公司订单列表
     *
     * @param offset       当前页
     * @param size         每页条数
     * @param keywords     不知
     * @param maCompanyOrderVORequest  多条件查询请求参数类
     * @return 订单列表
     */
    @GetMapping(value = "/page/test")
    public GridDataVO<MaOrderVO> findOrderByCondition(Integer offset, Integer size, String keywords, MaCompanyOrderVORequest maCompanyOrderVORequest) {
        logger.warn("findOrderByCondition 多条件分页查询装饰公司订单列表 ,入参 offsetL:{}, size:{}, kewords:{}, maOrderVORequest:{}",offset, size, keywords, maCompanyOrderVORequest);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageHelper.startPage(page, size);
            List<MaOrderVO> maOrderVOList = this.maOrderService.findCompanyOrderByCondition(maCompanyOrderVORequest);
            PageInfo<MaOrderVO> maOrderVOPageInfo = new PageInfo<>(maOrderVOList);
            List<MaOrderVO> orderVOList = maOrderVOPageInfo.getList();
            logger.warn("getOrderByStoreIdAndCityIdAndDeliveryType ,多条件分页查询装饰公司订单列表成功", orderVOList.size());
            return new GridDataVO<MaOrderVO>().transform(orderVOList, maOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getOrderByStoreIdAndCityIdAndDeliveryType EXCEPTION,发生未知错误，多条件分页查询装饰公司订单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }
}
