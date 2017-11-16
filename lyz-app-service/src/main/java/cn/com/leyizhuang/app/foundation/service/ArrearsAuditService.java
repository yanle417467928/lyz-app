package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
public interface ArrearsAuditService {

    List<ArrearsAuditResponse> findByUserId(Long userId);
}
