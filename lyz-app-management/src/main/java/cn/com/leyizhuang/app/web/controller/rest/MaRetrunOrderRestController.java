package cn.com.leyizhuang.app.web.controller.rest;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.utils.StringUtils;
import cn.com.leyizhuang.app.core.utils.order.OrderUtils;
import cn.com.leyizhuang.app.core.wechat.refund.MaOnlinePayRefundService;
import cn.com.leyizhuang.app.foundation.pojo.GridDataVO;
import cn.com.leyizhuang.app.foundation.pojo.management.order.MaPaymentData;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.MaOrdReturnBilling;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.MaOrdReturnBillingDetail;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.MaReturnOrderDetailInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.returnOrder.MaReturnOrderInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBaseInfo;
import cn.com.leyizhuang.app.foundation.service.*;
import cn.com.leyizhuang.app.remote.queue.MaSinkSender;
import cn.com.leyizhuang.common.core.constant.CommonGlobal;
import cn.com.leyizhuang.common.foundation.pojo.dto.ResultDTO;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 后台门店订单管理
 * Created by caiyu on 2017/12/16.
 */
@RestController
@RequestMapping(value = MaRetrunOrderRestController.PRE_URL, produces = "application/json;charset=utf-8")
public class MaRetrunOrderRestController extends BaseRestController {
    protected final static String PRE_URL = "/rest/returnOrder";

    private final Logger logger = LoggerFactory.getLogger(MaRetrunOrderRestController.class);

    @Resource
    private MaReturnOrderService maReturnOrderService;

    @Resource
    private MaSinkSender maSinkSender;
    @Resource
    private MaOnlinePayRefundService maOnlinePayRefundService;
    @Resource
    private MaOrderService maOrderService;

    /**
     * 退货单列表
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords
     * @return 订单列表
     */
    @GetMapping(value = "/page/grid")
    public GridDataVO<MaReturnOrderInfo> restReturnOrderPageGird(Integer offset, Integer size, String keywords) {
        logger.info("restReturnOrderPageGird 后台分页查询退货订单 ,入参offsetL:{}, size:{}, kewords:{}", offset, size, keywords);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaReturnOrderInfo> returnOrderInfoPageInfo = this.maReturnOrderService.findMaReturnOrderList(page, size);
            List<MaReturnOrderInfo> returnOrderList = returnOrderInfoPageInfo.getList();
            logger.info("restReturnOrderPageGird ,后台分页查询退货订单成功", (returnOrderList == null) ? 0 : returnOrderList.size());
            return new GridDataVO<MaReturnOrderInfo>().transform(returnOrderList, returnOrderInfoPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restReturnOrderPageGird EXCEPTION,发生未知错误，后台分页查询退货订单失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 后台根据筛选条件分页查询退货单列表
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords
     * @return 订单列表
     */
    @GetMapping(value = "/page/screenGrid")
    public GridDataVO<MaReturnOrderInfo> restReturnOrderPageGirdByScreen(Integer offset, Integer size, String keywords, @RequestParam(value = "storeId") Long storeId, @RequestParam(value = "status") String status) {
        logger.info("restReturnOrderPageGirdByScreen 后台根据筛选条件分页查询退货单列表 ,入参offset:{}, size:{}, kewords:{},storeId:{},status:{}", offset, size, keywords, storeId, status);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaReturnOrderInfo> ReturnOrderVOPageInfo = this.maReturnOrderService.findMaReturnOrderListByScreen(page, size, storeId, status);
            List<MaReturnOrderInfo> ReturnOrderVOList = ReturnOrderVOPageInfo.getList();
            logger.info("restReturnOrderPageGirdByScreen ,后台根据筛选条件分页查询退货单列表成功", (ReturnOrderVOList == null) ? 0 : ReturnOrderVOList.size());
            return new GridDataVO<MaReturnOrderInfo>().transform(ReturnOrderVOList, ReturnOrderVOPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restReturnOrderPageGirdByScreen EXCEPTION,发生未知错误，后台根据筛选条件分页查询退货单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }

    /**
     * 后台根据条件信息分页查询退货单列表
     *
     * @param offset   当前页
     * @param size     每页条数
     * @param keywords
     * @return 订单列表
     */
    @GetMapping(value = "/page/infoGrid")
    public GridDataVO<MaReturnOrderInfo> restMaReturnOrderPageGirdByInfo(Integer offset, Integer size, String keywords, @RequestParam(value = "info") String info) {
        logger.info("restMaReturnOrderPageGirdByInfo 后台根据条件信息分页查询退货单列表 ,入参offsetL:{}, size:{}, kewords:{},info:{}", offset, size, keywords, info);
        try {
            size = getSize(size);
            Integer page = getPage(offset, size);
            PageInfo<MaReturnOrderInfo> maReturnOrderInfoPageInfo = this.maReturnOrderService.findMaReturnOrderPageGirdByInfo(page, size, info);
            List<MaReturnOrderInfo> maReturnOrderList = maReturnOrderInfoPageInfo.getList();
            logger.info("restMaReturnOrderPageGirdByInfo ,后台根据条件信息分页查询退货单列表成功", (maReturnOrderList == null) ? 0 : maReturnOrderList.size());
            return new GridDataVO<MaReturnOrderInfo>().transform(maReturnOrderList, maReturnOrderInfoPageInfo.getTotal());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("restMaReturnOrderPageGirdByInfo EXCEPTION,发生未知错误，后台根据条件信息分页查询退货单列表失败");
            logger.warn("{}", e);
            return null;
        }
    }


    /**
     * 后台自提单收货
     *
     * @param
     * @return
     */
    @Transactional
    @PutMapping(value = "/returnOrderReceive")
    public ResultDTO<Object> returnOrderReceive(@RequestParam(value = "returnNumber") String returnNumber) {
        logger.warn("returnOrderReceive 后台到店退货单收货 ,入参orderNumbe:{}", returnNumber);
        ResultDTO<Object> resultDTO;
        if (StringUtils.isBlank(returnNumber)) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "退单号不允许为空", null);
            logger.warn("returnOrderReceive OUT,后台到店退货单收货失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        MaReturnOrderDetailInfo maReturnOrderDetailInfo = maReturnOrderService.queryMaReturnOrderByReturnNo(returnNumber);
        if (!(3 == maReturnOrderDetailInfo.getReturnStatus().getValue().intValue())) {
            resultDTO = new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE, "订单状态错误", null);
            logger.warn("returnOrderReceive OUT,后台到店退货单收货失败，出参 resultDTO:{}", resultDTO);
            return resultDTO;
        }
        try {
            ShiroUser shiroUser = this.getShiroUser();
            MaOrdReturnBilling maOrdReturnBillingList = maReturnOrderService.findReturnOrderBillingList(maReturnOrderDetailInfo.getRoid());
            if (null == maOrdReturnBillingList) {
                logger.warn("returnOrderReceive OUT,后台到店退货单收货失败,该退单支付明细为空");
                throw new RuntimeException("收货失败，该退单支付明细为空");
            }
            //后台收货并存入接口表
            maReturnOrderService.returnOrderReceive(returnNumber, maReturnOrderDetailInfo, maOrdReturnBillingList, shiroUser);
            //线上支付退款
            if (null != maOrdReturnBillingList.getOnlinePay() && maOrdReturnBillingList.getOnlinePay() > 0) {
                List<MaPaymentData> paymentDataList = maOrderService.findPaymentDataByOrderNo(maReturnOrderDetailInfo.getOrderNo());
                for (MaPaymentData maPaymentData : paymentDataList) {
                    if ("ALIPAY".equals(maPaymentData.getOnlinePayType().toString())) {
                        maOnlinePayRefundService.alipayRefundRequest(maReturnOrderDetailInfo.getCreatorId(), maReturnOrderDetailInfo.getCreatorIdentityType().getValue(), maReturnOrderDetailInfo.getOrderNo(), returnNumber, maPaymentData.getTotalFee());
                    } else if ("WE_CHAT".equals(maPaymentData.getOnlinePayType().toString())) {
                        maOnlinePayRefundService.wechatReturnMoney(maReturnOrderDetailInfo.getCreatorId(), maReturnOrderDetailInfo.getCreatorIdentityType().getValue(), maPaymentData.getTotalFee(), maReturnOrderDetailInfo.getOrderNo(), returnNumber);
                    } else if ("银联".equals(maPaymentData.getOnlinePayType().toString())) {
                        //TODO
                    }
                }
            }
            //发送门店自提单收货消息队列
            maSinkSender.sendStorePickUpReturnOrderReceiptToEBSAndRecord(returnNumber);
            logger.info("orderShipping ,后台到店退货单收货成功");
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_SUCCESS,
                    "后台到店退货单收货成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("orderShipping EXCEPTION,发生未知错误，后台到店退货单收货失败");
            logger.warn("{}", e);
            return new ResultDTO<>(CommonGlobal.COMMON_CODE_FAILURE,
                    "后台到店退货单收货失败", null);
        }
    }

}
