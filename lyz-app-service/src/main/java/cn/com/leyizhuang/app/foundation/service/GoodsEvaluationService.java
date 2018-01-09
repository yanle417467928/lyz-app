package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by caiyu on 2017/10/18.
 */
public interface GoodsEvaluationService {
    PageInfo<GoodsEvaluation> queryEvaluationListByGid(Long gid, Integer page, Integer size);

    Integer getEvaluationQuantityByGid(Long gid);
}
