package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearageListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.SellerArrearsAuditResponse;
import cn.com.leyizhuang.common.core.constant.ArrearsAuditStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
}
