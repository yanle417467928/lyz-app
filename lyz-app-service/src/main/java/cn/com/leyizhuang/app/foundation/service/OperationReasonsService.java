package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.response.CancelReasonsResponse;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/13
 */
public interface OperationReasonsService {

    List<CancelReasonsResponse> findAll();
}
