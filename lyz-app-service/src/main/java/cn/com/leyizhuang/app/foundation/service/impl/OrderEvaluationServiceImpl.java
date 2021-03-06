package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.OrderEvaluationDAO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.OrderEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderEvaluationRequest;
import cn.com.leyizhuang.app.foundation.service.OrderEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Calendar;

/**
 * @author caiyu
 * @date 2017/11/16
 */
@Service
public class OrderEvaluationServiceImpl implements OrderEvaluationService {

    @Autowired
    private OrderEvaluationDAO orderEvaluationDAO;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrderEvaluation(OrderEvaluationRequest orderEvaluationRequest) throws IOException {

        OrderEvaluation orderEvaluation = new OrderEvaluation();
        //订单评价设值
        orderEvaluation.setLogisticsStar(orderEvaluationRequest.getLogisticsStar());
        orderEvaluation.setProductStar(orderEvaluationRequest.getProductStar());
        orderEvaluation.setServiceStars(orderEvaluationRequest.getServiceStars());
        orderEvaluation.setOrderNumber(orderEvaluationRequest.getOrderNumber());
        orderEvaluation.setEvaluationTime(Calendar.getInstance().getTime());
        //保存订单评价
        orderEvaluationDAO.addOrderEvaluation(orderEvaluation);
        //修改订单评价为已评价
        orderEvaluationDAO.updateOrderEvaluationStatus(orderEvaluationRequest.getOrderNumber());
    }

    @Override
    public void addOrderGoodsEvaluation(GoodsEvaluation goodsEvaluation) {
        if (null != goodsEvaluation) {
            orderEvaluationDAO.addOrderGoodsEvaluation(goodsEvaluation);
        }
    }

    @Override
    public void updeteGoodsEvaluationStatus(String orderNumber, Long goodsId) {
        orderEvaluationDAO.updeteGoodsEvaluationStatus(orderNumber, goodsId);
    }

    @Override
    public OrderEvaluation queryOrderEvaluationListByOrderNumber(String orderNumber) {
        return orderEvaluationDAO.queryOrderEvaluationListByOrderNumber(orderNumber);
    }
}
