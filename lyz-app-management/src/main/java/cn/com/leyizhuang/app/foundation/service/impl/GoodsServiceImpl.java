package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.GoodsDAO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/6
 */
@Service
@Transactional
public class GoodsServiceImpl extends BaseServiceImpl<GoodsDO> implements GoodsService {

    private GoodsDAO goodsDAO;
    public GoodsServiceImpl(GoodsDAO goodsDAO) {
        super(goodsDAO);
        this.goodsDAO = goodsDAO;
    }

    /**
     * @title 商品分页查询
     * @descripe
     * @param page
     * @param size
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/9/6
     */
    @Override
    public PageInfo<GoodsDO> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<GoodsDO> goodsDOList = goodsDAO.queryList();
        return new PageInfo<>(goodsDOList);
    }

}
