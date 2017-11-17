package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.OrderEvaluationDAO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.OrderEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderEvaluationRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.OrderGoodsEvaluationRequest;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsEvaluationSimpleInfo;
import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import cn.com.leyizhuang.app.foundation.service.OrderEvaluationService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
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

        OrderEvaluation orderEvaluation =  new OrderEvaluation();
        //订单评价设值
        orderEvaluation.setLogisticsStar(orderEvaluationRequest.getLogisticsStar());
        orderEvaluation.setProductStar(orderEvaluationRequest.getProductStar());
        orderEvaluation.setServiceStars(orderEvaluationRequest.getServiceStars());
        orderEvaluation.setOrderNumber(orderEvaluationRequest.getOrderNumber());
        orderEvaluation.setEvaluationTime(Calendar.getInstance().getTime());
        //保存订单评价
        orderEvaluationDAO.addOrderEvaluation(orderEvaluation);

      /*  //获取订单评价id
        Long orderEvaluationId = orderEvaluation.getId();
        //商品评价设值
        GoodsEvaluation goodsEvaluation = new GoodsEvaluation();
        goodsEvaluation.setOrderEvaluationId(orderEvaluationId);
        goodsEvaluation.setEvaluationTime(orderEvaluation.getEvaluationTime());
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType1 = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, GoodsEvaluationSimpleInfo.class);
        List<GoodsEvaluationSimpleInfo> goodsEvaluationList = objectMapper.readValue(orderGoodsEvaluationRequest.getGoodsList(), javaType1);*/

    }

    @Override
    public void addOrderGoodsEvaluation(GoodsEvaluation goodsEvaluation) {
        if (null != goodsEvaluation){
            orderEvaluationDAO.addOrderGoodsEvaluation(goodsEvaluation);
        }
    }
}
