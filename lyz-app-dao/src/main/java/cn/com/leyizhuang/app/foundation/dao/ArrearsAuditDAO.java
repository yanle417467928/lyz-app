package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.order.OrderBillingPaymentDetails;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearageListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerArrearsAuditResponse;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
@Repository
public interface ArrearsAuditDAO {

    List<ArrearsAuditResponse> findByUserIdAndStatus(@Param("userId") Long userId, @Param("list") List<ArrearsAuditStatus> arrearsAuditStatusList);

    List<ArrearsAuditResponse> findByUserIdAndOrderNoAndStatus(@Param("userId")Long userId, @Param("orderNo")String orderNo, @Param("list")List<ArrearsAuditStatus> arrearsAuditStatusList);

    void save(OrderArrearsAuditDO orderArrearsAuditDO);

    List<SellerArrearsAuditResponse>findBySellerIdAndStatus(@Param("sellerId") Long sellerId, @Param("list") List<ArrearsAuditStatus> arrearsAuditStatusList);
    /**
     * 查询欠款单
     * @param userID    用户id
     * @return  欠款列表
     */
    List<ArrearageListResponse> findArrearsListByUserId(@Param("userID") Long userID);

    OrderArrearsAuditDO findById(Long id);

    void updateStatusById(OrderArrearsAuditDO orderArrearsAuditDO);

    /**
     * 根据导购id和订单号查询欠款
     * @param userID    用户id
     * @param orderNumber   订单号
     * @return  返回欠款信息
     */
    OrderArrearsAuditDO findArrearsByUserIdAndOrderNumber(@Param("userID") Long userID,@Param("orderNumber") String orderNumber);

    /**
     * 导购欠款还款后修改欠款审核表
     * @param repaymentTime 还款时间
     * @param orderNumber   订单号
     */
    void updateStatusAndrRepaymentTimeByOrderNumber(@Param("repaymentTime")Date repaymentTime, @Param("orderNumber")String orderNumber);

    /**
     * 获取还款记录
     * @param userID    用户id
     * @return  返回还款记录列表
     */
    List<OrderBillingPaymentDetails> getRepaymentMondyList(@Param("userID") Long userID);
}
