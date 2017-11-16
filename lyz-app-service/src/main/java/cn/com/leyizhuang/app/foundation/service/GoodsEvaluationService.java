package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditGoodsInfo;

import java.util.List;

/**
 * Created by caiyu on 2017/10/18.
 */
public interface GoodsEvaluationService {
    List<GoodsEvaluation> queryEvaluationListByGid(Long gid);

    void addGoodsEvaluation(GoodsEvaluation goodsEvaluation);

    Integer getEvaluationQuantityByGid(Long gid);
}
