package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.GoodsDAO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.dto.GoodsDTO;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    /**
     * @title   保存编辑后的商品信息
     * @descripe
     * @param goodsDTO
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/9/9
     */
    @Override
    public GoodsDO managerSaveGoods(GoodsDTO goodsDTO) {
        GoodsDO goodsDO = transform(goodsDTO);
        goodsDO.setModifierInfoByManager(0L);
        goodsDAO.modify(goodsDO);
        return goodsDO;
    }

    /**
     * @title   GoodsDTO转GoodsDO
     * @descripe
     * @param goodsDTO
     * @return
     * @throws
     * @author GenerationRoad
     * @date 2017/9/9
     */
    private GoodsDO transform(GoodsDTO goodsDTO){
        GoodsDO goodsDO = this.goodsDAO.queryById(goodsDTO.getId());
        goodsDO.setGoodsName(goodsDTO.getGoodsName());
        goodsDO.setGoodsCode(goodsDTO.getGoodsCode());
        goodsDO.setBrandId(goodsDTO.getBrandId());
        goodsDO.setCategoryId(goodsDTO.getCategoryId());
        goodsDO.setCoverImageUri(goodsDTO.getCoverImageUri());
        goodsDO.setIsColorful(goodsDTO.getIsColorful());
        goodsDO.setIsColorPackage(goodsDTO.getIsColorPackage());
        goodsDO.setIsGift(goodsDTO.getIsGift());
        goodsDO.setIsOnSale(goodsDTO.getIsOnSale());
        goodsDO.setLeftNumber(Long.parseLong(goodsDTO.getLeftNumber()));
        String onSaleTime = goodsDTO.getOnSaleTime();
        if (null != onSaleTime && !"".equals(onSaleTime)) {
            goodsDO.setOnSaleTime(LocalDateTime.parse(onSaleTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        goodsDO.setShowPictures(goodsDTO.getShowPictures());
        goodsDO.setTitle(goodsDTO.getTitle());
        goodsDO.setSubTitle(goodsDTO.getSubTitle());
        return goodsDO;
    }


}
