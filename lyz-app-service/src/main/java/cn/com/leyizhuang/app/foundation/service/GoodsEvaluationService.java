package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;

import java.util.List;

/**
 * Created by caiyu on 2017/10/18.
 */
public interface GoodsEvaluationService {
    List<GoodsEvaluation> queryEvaluationListByGid(Long gid);

    Integer getEvaluationQuantityByGid(Long gid);
}
