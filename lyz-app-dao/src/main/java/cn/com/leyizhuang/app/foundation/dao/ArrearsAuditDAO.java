package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
@Repository
public interface ArrearsAuditDAO {

    List<ArrearsAuditResponse> findByUserId(Long userId);

}
