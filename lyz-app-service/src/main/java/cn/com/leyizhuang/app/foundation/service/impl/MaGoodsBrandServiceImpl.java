package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaGoodsBrandDAO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsBrand;
import cn.com.leyizhuang.app.foundation.service.MaGoodsBrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaGoodsBrandServiceImpl implements MaGoodsBrandService {

    @Autowired
    private MaGoodsBrandDAO maGoodsBrandDAO;

    @Override
    public PageInfo<GoodsBrand> queryPageVO(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<GoodsBrand> pageGoodsBrandList = this.maGoodsBrandDAO.findAllGoodsBrand();
        return new PageInfo<>(pageGoodsBrandList);
    }



    @Override
    public PageInfo<GoodsBrand> findGoodsBrandById(Long id,Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<GoodsBrand> pageGoodsBrandList = this.maGoodsBrandDAO.findGoodsBrandById(id);
        return new PageInfo<>(pageGoodsBrandList);
    }

    @Override
    public PageInfo<GoodsBrand> findGoodsBrandByName(String queryStoreInfo,Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<GoodsBrand> pageGoodsBrandList = this.maGoodsBrandDAO.findGoodsBrandByName(queryStoreInfo);
        return new PageInfo<>(pageGoodsBrandList);
    }

    @Override
    public void save(GoodsBrand goodsBrand){
        if (null!=goodsBrand){
            this.maGoodsBrandDAO.save(goodsBrand);
        }
    }

    @Override
    public Boolean isExistBrandName(String brandName){
        return  maGoodsBrandDAO.isExistBrandName(brandName);
    }

    @Override
    public GoodsBrand queryGoodsBrandVOById(Long goodsBrandId){
        GoodsBrand goodsBrand =  maGoodsBrandDAO.queryGoodsBrandVOById(goodsBrandId);
        return goodsBrand;
    }

    @Override
    public void update(GoodsBrand goodsBrand){
        if (null!=goodsBrand){
            this.maGoodsBrandDAO.update(goodsBrand);
        }
    }

    @Override
    public List<GoodsBrand> queryGoodsBrandList() {
        List<GoodsBrand> pageGoodsBrandList = this.maGoodsBrandDAO.findAllGoodsBrand();
        return pageGoodsBrandList;
    }
}
