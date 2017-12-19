package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.OrderEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderEvaluationRequest;

import java.io.IOException;

/**
 * Created by caiyu on 2017/10/18.
 */
public interface OrderEvaluationService {
    /**
     * 保存订单评价
     *
     * @param orderEvaluationRequest 订单评价请求参数类
     */
    void addOrderEvaluation(OrderEvaluationRequest orderEvaluationRequest) throws IOException;

    /**
     * 保存商品评价
     *
     * @param goodsEvaluation 商品评价类
     */
    void addOrderGoodsEvaluation(GoodsEvaluation goodsEvaluation);

    /**
     * 修改商品评价状态
     *
     * @param orderNumber 订单号
     * @param goodsId     商品id
     */
    void updeteGoodsEvaluationStatus(String orderNumber, Long goodsId);

    /**
     * 获取订单评价
     *
     * @param orderNumber 订单号
     * @return 订单评价
     */
    OrderEvaluation queryOrderEvaluationListByOrderNumber(String orderNumber);
}
