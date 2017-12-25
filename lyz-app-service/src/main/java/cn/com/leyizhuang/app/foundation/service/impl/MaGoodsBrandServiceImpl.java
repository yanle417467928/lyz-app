package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaGoodsBrandDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsBrand;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.SimpaleGoodsBrandParam;
import cn.com.leyizhuang.app.foundation.service.MaGoodsBrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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
    public Boolean editIsExistBrandName(String brandName,Long id){
        return  maGoodsBrandDAO.editIsExistBrandName(brandName,id);
    }


    @Override
    public Boolean isExistSort(Long sortId){
        return  maGoodsBrandDAO.isExistSort(sortId);
    }


    @Override
    public Boolean editIsExistSort(Long sortId,Long id){
        return  maGoodsBrandDAO.editIsExistSort(sortId,id);
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
    public List<SimpaleGoodsBrandParam> queryGoodsBrandList() {
        List<SimpaleGoodsBrandParam> pageGoodsBrandList = this.maGoodsBrandDAO.queryGoodsBrandList();
        return pageGoodsBrandList;
    }

    @Override
    public void delete(Long id) {
        if (null != id) {
            maGoodsBrandDAO.delete(id);
        }
    }
}
