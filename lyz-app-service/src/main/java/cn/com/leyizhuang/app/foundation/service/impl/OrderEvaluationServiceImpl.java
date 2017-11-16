package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.OrderEvaluationDAO;
import cn.com.leyizhuang.app.foundation.pojo.OrderEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderGoodsEvaluationRequest;
import cn.com.leyizhuang.app.foundation.service.OrderEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by caiyu on 2017/11/16.
 */
@Service
public class OrderEvaluationServiceImpl implements OrderEvaluationService {
    @Autowired
    private OrderEvaluationDAO orderEvaluationDAO;
    @Override
    public void addOrderEvaluation(OrderGoodsEvaluationRequest orderGoodsEvaluationRequest) {
        OrderEvaluation orderEvaluation =  new OrderEvaluation();
        orderEvaluationDAO.addOrderEvaluation(orderEvaluation);
    }
}
