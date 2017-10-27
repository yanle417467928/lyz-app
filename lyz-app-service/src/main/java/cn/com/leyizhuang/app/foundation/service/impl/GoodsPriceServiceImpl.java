package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.GoodsPriceDAO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.service.GoodsPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author GenerationRoad
 * @date 2017/10/26
 */
@Service
@Transactional
public class GoodsPriceServiceImpl implements GoodsPriceService {

    @Autowired
    private GoodsPriceDAO goodsPriceDAO;

    @Override
    public void save(GoodsPrice goodsPrice) {
        this.goodsPriceDAO.save(goodsPrice);
        //日志
    }

    @Override
    public void modify(GoodsPrice goodsPrice) {
        this.goodsPriceDAO.modify(goodsPrice);
        //日志
    }

    @Override
    public void delete(GoodsPrice goodsPrice) {
        this.goodsPriceDAO.delete(goodsPrice.getPriceLineId());
        //日志

    }

    @Override
    public GoodsPrice findGoodsPrice(Long priceLineId) {
        return this.goodsPriceDAO.findByPriceLineId(priceLineId);
    }
}
