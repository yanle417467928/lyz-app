package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.OrderEvaluation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by caiyu on 2017/10/17.
 */
@Repository
public interface OrderEvaluationDAO {
    /**
     * 保存订单评价
     * @param orderEvaluation   订单评价类
     */
    void addOrderEvaluation(OrderEvaluation orderEvaluation);

    /**
     * 保存商品评价
     * @param goodsEvaluation   商品评价类
     */
    void addOrderGoodsEvaluation(GoodsEvaluation goodsEvaluation);

    /**
     * 修改商品评价状态
     * @param orderNumber   订单号
     * @param goodsId   商品id
     */
    void updeteGoodsEvaluationStatus(@Param("orderNumber") String orderNumber,@Param("goodsId") Long goodsId);

    /**
     * 获取订单评价
     * @param orderNumber   订单号
     * @return  订单评价
     */
    OrderEvaluation queryOrderEvaluationListByOrderNumber(@Param("orderNumber") String orderNumber);
}
