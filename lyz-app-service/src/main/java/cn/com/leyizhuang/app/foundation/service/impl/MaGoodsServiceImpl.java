package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaGoodsDAO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.service.MaGoodsService;
import cn.com.leyizhuang.app.foundation.vo.management.goods.GoodsResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/30
 */
@Service
@Transactional
public class MaGoodsServiceImpl implements MaGoodsService {

    @Autowired
    private MaGoodsDAO maGoodsDAO;

    @Override
    public List<GoodsResponseVO> findGoodsByCidAndCusId(Long cusId, List<Long> cids) {
        return this.maGoodsDAO.findGoodsByCidAndCusId(cusId, cids);
    }

    @Override
    public List<GoodsResponseVO> findGoodsByCidAndEmpId(Long empId, List<Long> cids) {
        return this.maGoodsDAO.findGoodsByCidAndEmpId(empId, cids);
    }

    @Override
    public GoodsDO findGoodsById(Long id) {
        if (null != id) {
            return maGoodsDAO.findGoodsById(id);
        }
        return null;
    }

    @Override
    public List<GoodsResponseVO> findGoodsByCidAndCusIdAndUserRank(Long cusId) {
        return this.maGoodsDAO.findGoodsByCidAndCusIdAndUserRank(cusId);
    }
}
