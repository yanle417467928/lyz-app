package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.OrderEvaluationDAO;
import cn.com.leyizhuang.app.foundation.pojo.OrderEvaluation;
import cn.com.leyizhuang.app.foundation.service.OrderEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by caiyu on 2017/11/16.
 */
public class OrderEvaluationServiceImpl implements OrderEvaluationService {
    @Autowired
    private OrderEvaluationDAO orderEvaluationDAO;
    @Override
    public void addOrderEvaluation(OrderEvaluation orderEvaluation) {
        orderEvaluationDAO.addOrderEvaluation(orderEvaluation);
    }
}
