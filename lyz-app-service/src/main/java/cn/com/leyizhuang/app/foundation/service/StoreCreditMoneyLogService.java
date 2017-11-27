package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.response.StoreCreditMoneyLogResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/27
 */
public interface StoreCreditMoneyLogService {
    List<StoreCreditMoneyLogResponse> findByUserId(Long userId);
}
