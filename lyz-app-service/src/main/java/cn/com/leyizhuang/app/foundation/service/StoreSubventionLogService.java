package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.response.StoreSubventionLogResponse;
import com.github.pagehelper.PageInfo;

/**
 * @author GenerationRoad
 * @date 2017/11/27
 */
public interface StoreSubventionLogService {
    PageInfo<StoreSubventionLogResponse> findByUserId(Long userId, Integer page, Integer size);
}
