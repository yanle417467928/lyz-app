package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderEvaluationRequest;

import java.io.IOException;

/**
 * Created by caiyu on 2017/10/18.
 */
public interface OrderEvaluationService {

    void addOrderEvaluation(OrderEvaluationRequest orderEvaluationRequest) throws IOException;

    void addOrderGoodsEvaluation(GoodsEvaluation goodsEvaluation);
}
