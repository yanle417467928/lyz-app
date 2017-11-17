package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.OrderEvaluation;
import org.springframework.stereotype.Repository;

/**
 * Created by caiyu on 2017/10/17.
 */
@Repository
public interface OrderEvaluationDAO {
    void addOrderEvaluation(OrderEvaluation orderEvaluation);

    void addOrderGoodsEvaluation(GoodsEvaluation goodsEvaluation);
}
