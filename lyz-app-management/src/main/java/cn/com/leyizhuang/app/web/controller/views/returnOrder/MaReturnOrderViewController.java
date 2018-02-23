package cn.com.leyizhuang.app.web.controller.views.returnOrder;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.MaReturnGoods;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.MaReturnOrderDetailInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.MaReturnOrderLogisticInfo;
import cn.com.leyizhuang.app.foundation.service.MaOrderService;
import cn.com.leyizhuang.app.foundation.service.MaReturnOrderService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.returnOrder.MaReturnOrderDetailVO;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 后台门店订单管理
 * Created by liuhao on 2017/12/16.
 */
@Controller
@RequestMapping(value = MaReturnOrderViewController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaReturnOrderViewController {
    protected final static String PRE_URL = "/views/admin/returnOrder";
    private final Logger logger = LoggerFactory.getLogger(MaReturnOrderViewController.class);

    @Resource
    private MaReturnOrderService maReturnOrderService;

    @Resource
    private MaOrderService maOrderService;


    /**
     * 跳转退货单页面
     *
     * @return
     */
    @RequestMapping(value = "/list")
    public String returnOrderListPage() {
        return "/views/returnorder/returnOrder_page";
    }

/*    *//**
     * 查看退货单详情
     *
     * @param returnNumber 退单号
     * @return 退单信息
     */
    @GetMapping(value = "/detail/{returnNumber}", produces = "application/json;charset=UTF-8")
    public String getMaReturnOrderDetail(@PathVariable(value = "returnNumber") String returnNumber, ModelMap map) {
        logger.info("getMaReturnOrderDetail CALLED,查看退货单详情，入参 returnNumber:{}",returnNumber);
        ResultDTO<Object> resultDTO;
        if (StringUtils.isBlank(returnNumber)) {
            logger.info("getMaReturnOrderDetail OUT,查看退货单详情失败");
            return "/error/500";
        }
        try {
            //查询退单
            MaReturnOrderDetailInfo returnOrderDetailInfo = maReturnOrderService.queryMaReturnOrderByReturnNo(returnNumber);
            if (null == returnOrderDetailInfo) {
                logger.info("getMaReturnOrderDetail OUT,查看退货单详情失败");
                return "/error/500";
            }
            //获取原订单收货/自提门店地址
            MaReturnOrderLogisticInfo returnOrderLogisticInfo = maReturnOrderService.getMaReturnOrderLogisticeInfo(returnNumber);

            MaReturnOrderDetailVO maReturnOrderDetailVO = new MaReturnOrderDetailVO();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //设置基础信息
            maReturnOrderDetailVO.setCreatorName(returnOrderDetailInfo.getCreatorName());
            maReturnOrderDetailVO.setCreatorIdentityType(returnOrderDetailInfo.getCreatorIdentityType());
            maReturnOrderDetailVO.setCustomerId(returnOrderDetailInfo.getCustomerId());
            maReturnOrderDetailVO.setCustomerName(returnOrderDetailInfo.getCustomerName());
            maReturnOrderDetailVO.setReturnNumber(returnOrderDetailInfo.getReturnNo());
            maReturnOrderDetailVO.setReturnTime(sdf.format(returnOrderDetailInfo.getReturnTime()));
            maReturnOrderDetailVO.setReasonInfo(returnOrderDetailInfo.getReasonInfo());
            maReturnOrderDetailVO.setOrderType(returnOrderDetailInfo.getOrderType());
            maReturnOrderDetailVO.setOrderNumber(returnOrderDetailInfo.getOrderNo());
            maReturnOrderDetailVO.setStoreName(returnOrderDetailInfo.getStoreName());
            maReturnOrderDetailVO.setReturnStatus(null != returnOrderDetailInfo.getReturnStatus() ? returnOrderDetailInfo.getReturnStatus(): null);
            maReturnOrderDetailVO.setReturnType(null != returnOrderDetailInfo.getReturnType() ? returnOrderDetailInfo.getReturnType().getDescription() : null);
            if(null!=returnOrderLogisticInfo){
                maReturnOrderDetailVO.setShippingAddress(returnOrderLogisticInfo.getReturnFullAddress());
                //取货方式（上门取货，送货到店）
                if (AppDeliveryType.RETURN_STORE.equals(returnOrderLogisticInfo.getDeliveryType())) {
                    maReturnOrderDetailVO.setBookingStoreName(returnOrderLogisticInfo.getReturnStoreName());
                }
            }

            //退货商品信息
           List<MaReturnGoods>  maReturnGoodsList =maReturnOrderService.getMaReturnOrderGoodsDetails(returnNumber);
            for(MaReturnGoods maReturnGoods :maReturnGoodsList){
                maReturnGoods.setTotalPrice( maReturnGoods.getReturnPrice().multiply(BigDecimal.valueOf(maReturnGoods.getReturnQty())));
            }
            maReturnOrderDetailVO.setGoodsList(maReturnGoodsList);
            //退货劵信息
            maReturnOrderDetailVO.setReturnOrderProductCouponList(maReturnOrderService.getReturnOrderProductCoupon(returnNumber));
            //退款信息
            Long  returnBillingID = maReturnOrderService.findReturnOrderBillingId(returnNumber);
            maReturnOrderDetailVO.setRetrunBillingList(maReturnOrderService.getMaReturnOrderBillingDetails(returnBillingID));
            //查询退单的配送方式
            String returnOrderType =  maReturnOrderService.findReturnOrderTypeByReturnNumber(returnNumber);
            logger.info("getMaReturnOrderDetail OUT,查看退货单详情成功");
            map.addAttribute("maReturnOrderDetailVO",maReturnOrderDetailVO);
            map.addAttribute("returnOrderType",returnOrderType);
            return "/views/returnorder/returnOrder_detail";
        } catch (Exception e) {
            e.printStackTrace();
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "发生未知异常，查看退货单详情失败", null);
            logger.warn("getMaReturnOrderDetail EXCEPTION,查看退货单详情失败，出参 resultDTO:{}", resultDTO);
            logger.warn("{}", e);
            return "/error/500";
        }
    }
}
