package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.order.OrderArrearsAuditDO;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
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

    List<ArrearsAuditResponse> findByUserId(@Param("userId") Long userId, @Param("list") List<ArrearsAuditStatus> arrearsAuditStatusList);

    List<ArrearsAuditResponse> findByUserIdAndOrderNo(@Param("userId")Long userId, @Param("orderNo")String orderNo, @Param("list")List<ArrearsAuditStatus> arrearsAuditStatusList);

    void save(OrderArrearsAuditDO orderArrearsAuditDO);

}
