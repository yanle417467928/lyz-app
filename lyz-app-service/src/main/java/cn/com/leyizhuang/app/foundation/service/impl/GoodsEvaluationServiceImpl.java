package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.GoodsEvaluationDAO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import cn.com.leyizhuang.app.foundation.service.GoodsEvaluationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public PageInfo<GoodsEvaluation> queryEvaluationListByGid(Long gid, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<GoodsEvaluation> goodsEvaluationList = goodsEvaluationDAO.queryEvaluationListByGid(gid);
        return new PageInfo<>(goodsEvaluationList);
    }

    @Override
    public Integer getEvaluationQuantityByGid(Long gid) {
        return goodsEvaluationDAO.getEvaluationQuantityByGid(gid);
    }
}
