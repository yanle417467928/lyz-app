package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:传wms单据数据持久层
 * Created with IntelliJ IDEA.
 * Date: 2017/12/21.
 * Time: 11:51.
 */
@Repository
public interface AppToWmsOrderDAO {


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
    void updateAtwCancelOrderRequest(AtwCancelOrderRequest atwCancelOrderRequest);

    /**
     * 修改要货单单据
     *
     * @param atwRequisitionOrder 要货单单据
     */
    void updateAtwRequisitionOrder(AtwRequisitionOrder atwRequisitionOrder);

    /**
     * 修改要货单商品单据
     *
     * @param atwRequisitionOrderGoods 要货单商品单据
     */
    void updateAtwRequisitionOrderGoods(AtwRequisitionOrderGoods atwRequisitionOrderGoods);

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
     * 保存退货单单据
     *
     * @param returnOrder 退货单单据
     */
    void saveAtwReturnOrder(AtwReturnOrder returnOrder);

    /**
     * 修改退货单单据
     *
     * @param returnOrder 退货单单据
     */
    void modifyAtwReturnOrder(AtwReturnOrder returnOrder);

    /**
     * 查找退货单单据
     *
     * @param returnNumber 退单号
     * @return 退货单单据
     */
    AtwReturnOrder findReturnOrderByReturnOrderNo(String returnNumber);

    /**
     * 查询所有要货单
     *
     * @param keywords
     * @return
     */
    List<AtwRequisitionOrder> findRequiringOrderList(@Param("keywords") String keywords);


    List<AtwRequisitionOrder> maFindRequiringOrderList(@Param("keywords") String keywords,@Param("storeId") Long storeId,@Param("cityId") Long cityId);

    /**,
     * 根据ID查询要货单
     *
     * @param id
     * @return
     */
    AtwRequisitionOrder findAtwRequisitionOrderById(Long id);

    void saveAtwCancelReturnOrderRequest(AtwCancelReturnOrderRequest returnOrderRequest);

    AtwCancelReturnOrderRequest findAtwCancelReturnOrderByReturnNo(String returnNumber);

    void updateAtwCancelReturnOrderRequest(AtwCancelReturnOrderRequest returnOrderRequest);

    void saveAtwReturnOrderCheckEnter(AtwReturnOrderCheckEnter atwReturnOrderCheckEnter);

    AtwReturnOrderCheckEnter findAtwReturnOrderCheckEnterByReturnNo(String returnNumber);

    void updateAtwReturnOrderCheckEnterRequest(AtwReturnOrderCheckEnter atwReturnOrderCheckEnter);

    List<AtwRequisitionOrder> findFailResendWmsOrder(LocalDateTime yesterday);

    List<AtwReturnOrder> findFailResendWmsReturnOrder(LocalDateTime yesterday);
}
