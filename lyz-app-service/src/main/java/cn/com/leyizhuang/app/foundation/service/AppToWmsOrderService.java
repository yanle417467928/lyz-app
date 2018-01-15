package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwCancelOrderRequest;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrder;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwReturnOrder;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:传wms单据服务
 * Created with IntelliJ IDEA.
 * Date: 2017/12/21.
 * Time: 11:43.
 */

public interface AppToWmsOrderService {

    /**
     * 保存取消订单单据
     *
     * @param atwCancelOrderRequest 取消订单单据
     */
    void saveAtwCancelOrderRequest(AtwCancelOrderRequest atwCancelOrderRequest);

    /**
     * 保存要货单单据
     *
     * @param atwRequisitionOrder 要货单单据
     */
    void saveAtwRequisitionOrder(AtwRequisitionOrder atwRequisitionOrder);

    /**
     * 保存退货单单据
     *
     * @param returnOrder 退货单单据
     */
    void saveAtwReturnOrder(AtwReturnOrder returnOrder);

    /**
     * 保存要货单商品单据
     *
     * @param atwRequisitionOrderGoods 要货单商品单据
     */
    void saveAtwRequisitionOrderGoods(AtwRequisitionOrderGoods atwRequisitionOrderGoods);

    /**
     * 修改取消订单单据
     *
     * @param atwCancelOrderRequest 取消订单单据
     */
    void modifyAtwCancelOrderRequest(AtwCancelOrderRequest atwCancelOrderRequest);

    /**
     * 修改要货单单据
     *
     * @param atwRequisitionOrder 要货单单据
     */
    void modifyAtwRequisitionOrder(AtwRequisitionOrder atwRequisitionOrder);

    /**
     * 修改退货单单据
     *
     * @param returnOrder 退货单单据
     */
    void modifyAtwReturnOrder(AtwReturnOrder returnOrder);

    /**
     * 修改要货单商品单据
     *
     * @param atwRequisitionOrderGoods 要货单商品单据
     */
    void modifyAtwRequisitionOrderGoods(AtwRequisitionOrderGoods atwRequisitionOrderGoods);

    /**
     * 查找取消订单单据
     *
     * @param orderNo 订单号
     * @return 取消订单单据
     */
    AtwCancelOrderRequest findAtwCancelOrderByOrderNo(String orderNo);

    /**
     * 查找要货单单据
     *
     * @param orderNo 订单号
     * @return 要货单单据
     */
    AtwRequisitionOrder findAtwRequisitionOrderByOrderNo(String orderNo);

    /**
     * 查找要货单商品单据
     *
     * @param orderNo 订单号
     * @return 要货单商品单据
     */
    List<AtwRequisitionOrderGoods> findAtwRequisitionOrderGoodsByOrderNo(String orderNo);

    /**
     * 查找退货单单据
     *
     * @param returnNumber 退单号
     * @return 退货单单据
     */
    AtwReturnOrder findReturnOrderByReturnOrderNo(String returnNumber);
}
