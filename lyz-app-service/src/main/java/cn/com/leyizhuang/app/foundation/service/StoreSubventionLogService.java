package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.response.StoreSubventionLogResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/27
 */
public interface StoreSubventionLogService {
    List<StoreSubventionLogResponse> findByUserId(Long userId);
}
