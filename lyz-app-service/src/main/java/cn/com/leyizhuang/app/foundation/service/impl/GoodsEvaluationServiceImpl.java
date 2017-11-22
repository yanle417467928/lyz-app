package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.GoodsEvaluationDAO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import cn.com.leyizhuang.app.foundation.service.GoodsEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caiyu on 2017/10/18.
 */
@Service
public class GoodsEvaluationServiceImpl implements GoodsEvaluationService {

    @Autowired
    private GoodsEvaluationDAO goodsEvaluationDAO;

    @Override
    public List<GoodsEvaluation> queryEvaluationListByGid(Long gid) {
        return goodsEvaluationDAO.queryEvaluationListByGid(gid);
    }
    @Override
    public Integer getEvaluationQuantityByGid(Long gid) {
        return goodsEvaluationDAO.getEvaluationQuantityByGid(gid);
    }
}
