package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.OrderEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderEvaluationRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderGoodsEvaluationRequest;

import java.io.IOException;
import java.util.List;

/**
 * Created by caiyu on 2017/10/18.
 */
public interface OrderEvaluationService {

    void addOrderEvaluation(OrderEvaluationRequest orderEvaluationRequest) throws IOException;

    void addOrderGoodsEvaluation(GoodsEvaluation goodsEvaluation);
}
