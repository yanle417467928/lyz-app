package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.ReturnLogisticStatus;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderDeliveryDetail;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes: 退货单物流详情服务
 * Created with IntelliJ IDEA.
 * Date: 2017/12/4.
 * Time: 14:00.
 */

public interface ReturnOrderDeliveryDetailsService {
    /**
     * 获取退单物流状态信息
     *
     * @param returnNumber 退单号
     * @return 物流信息明细
     */
    ReturnOrderDeliveryDetail getReturnLogisticStatusDetail(String returnNumber);

    /**
     * 保存退单物流状态信息
     *
     * @param returnOrderDeliveryDetail 物流信息明细
     */
    void addReturnOrderDeliveryInfoDetails(ReturnOrderDeliveryDetail returnOrderDeliveryDetail);

    /**
     * 根据退单号获取所有物流详情
     *
     * @param returnNumber
     * @return
     */
    List<ReturnOrderDeliveryDetail> queryListByReturnOrderNumber(String returnNumber);

    /**
     * 根据退单号和对应状态获取物流信息
     *
     * @param returnNo
     * @param returnLogisticStatus
     * @return
     */
    ReturnOrderDeliveryDetail getReturnOrderDeliveryDetailByReturnNoAndStatus(String returnNo, ReturnLogisticStatus returnLogisticStatus);

}
