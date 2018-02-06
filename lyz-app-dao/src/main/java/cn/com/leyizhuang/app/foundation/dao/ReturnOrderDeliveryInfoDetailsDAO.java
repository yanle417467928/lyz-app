package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderDeliveryDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes: 退单物流信息持久化接口
 * Created with IntelliJ IDEA.
 * Date: 2017/12/4.
 * Time: 11:53.
 */
@Repository
public interface ReturnOrderDeliveryInfoDetailsDAO {

    /**
     * 获取退单物流状态信息
     *
     * @param returnNo 退单号
     * @return 物流信息明细
     */
    ReturnOrderDeliveryDetail getReturnLogisticStatusDetail(String returnNo);

    /**
     * 保存退单物流状态信息
     *
     * @param returnOrderDeliveryDetail
     */
    void save(ReturnOrderDeliveryDetail returnOrderDeliveryDetail);

    /**
     * 根据退单号获取所有物流详情
     *
     * @param returnNumber
     * @return
     */
    List<ReturnOrderDeliveryDetail> queryListByReturnOrderNumber(String returnNumber);
}
