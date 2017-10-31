package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.GoodsPriceDAO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsPrice;
import cn.com.leyizhuang.app.foundation.service.GoodsPriceService;
import cn.com.leyizhuang.app.foundation.vo.GoodsPriceVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    }

    @Override
    public void modify(GoodsPrice goodsPrice) {
        this.goodsPriceDAO.modify(goodsPrice);
    }

    @Override
    public void delete(GoodsPrice goodsPrice) {
        this.goodsPriceDAO.delete(goodsPrice.getPriceLineId());
    }

    @Override
    public GoodsPrice findGoodsPrice(Long priceLineId) {
        return this.goodsPriceDAO.findByPriceLineId(priceLineId);
    }

    @Override
    public PageInfo<GoodsPriceVO> queryPage(Integer page, Integer size, Long storeId, String keywords) {
        PageHelper.startPage(page, size);
        List<GoodsPriceVO> priceList = this.goodsPriceDAO.findByStoreId(storeId, keywords);
        return new PageInfo<>(priceList);
    }
}
