package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaGoodsCategoryDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.GoodsCategoryDO;
import cn.com.leyizhuang.app.foundation.pojo.management.goods.SimpleGoodsCategoryParam;
import cn.com.leyizhuang.app.foundation.service.MaGoodsCategoryService;
import cn.com.leyizhuang.app.foundation.vo.management.goodscategory.GoodsCategoryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaGoodsCategoryServiceImpl implements MaGoodsCategoryService {

    @Autowired
    private MaGoodsCategoryDAO maGoodsCategoryDAO;

    @Override
    public PageInfo<GoodsCategoryDO> queryPageVO(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<GoodsCategoryDO> pageGoodsCategoryList = this.maGoodsCategoryDAO.findAllGoodsCategory();
        return new PageInfo<>(pageGoodsCategoryList);
    }

    @Override
    public List<SimpleGoodsCategoryParam> findGoodsCategorySelection() {
        List<SimpleGoodsCategoryParam> GoodsCategoryList = this.maGoodsCategoryDAO.findGoodsCategorySelection();
        return GoodsCategoryList;
    }

    @Override
    public PageInfo<GoodsCategoryDO> findGoodsCategoryByPid(Long pid,Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<GoodsCategoryDO> pageGoodsCategoryList = this.maGoodsCategoryDAO.findGoodsCategoryByPid(pid);
        return new PageInfo<>(pageGoodsCategoryList);
    }

    @Override
    public PageInfo<GoodsCategoryDO> findGoodsCategoryByPcode(String queryCategoryInfo,Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<GoodsCategoryDO> pageGoodsCategoryList = this.maGoodsCategoryDAO.findGoodsCategoryByPcode(queryCategoryInfo);
        return new PageInfo<>(pageGoodsCategoryList);
    }

    @Override
    public void save(GoodsCategoryVO goodsCategoryVO){
        if (null!=goodsCategoryVO){
            GoodsCategoryDO goodsCategoryDO = GoodsCategoryDO.transform(goodsCategoryVO);
            this.maGoodsCategoryDAO.save(goodsCategoryDO);
        }
    }

    @Override
    public Boolean isExistCategoryName(String categoryName){
        return  maGoodsCategoryDAO.isExistCategoryName(categoryName);
    }


    @Override
    public Boolean editIsExistCategoryName(String categoryName,Long id){
        return  maGoodsCategoryDAO.editIsExistCategoryName(categoryName,id);
    }

    @Override
    public GoodsCategoryVO queryGoodsCategoryVOById(Long goodsCategoryId){
        GoodsCategoryDO goodsCategoryDO =  maGoodsCategoryDAO.queryGoodsCategoryVOById(goodsCategoryId);
        GoodsCategoryVO goodsCategoryVO = GoodsCategoryVO.transform(goodsCategoryDO);
        return goodsCategoryVO;
    }

    @Override
    public void update(GoodsCategoryVO goodsCategoryVO){
        if (null!=goodsCategoryVO){
            GoodsCategoryDO goodsCategoryDO = GoodsCategoryDO.transform(goodsCategoryVO);
            this.maGoodsCategoryDAO.update(goodsCategoryDO);
        }
    }


    @Override
    public List<GoodsCategoryVO> findEditGoodsCategory() {
        List<GoodsCategoryDO> GoodsCategoryList = this.maGoodsCategoryDAO.findEditGoodsCategory();
        List<GoodsCategoryVO> GoodsCategoryVOList = GoodsCategoryVO.transform(GoodsCategoryList);
        return GoodsCategoryVOList;
    }

    @Override
    public void delete(Long id) {
        if (null != id) {
            maGoodsCategoryDAO.delete(id);
        }
    }


    @Override
    public Boolean isExistSortId(Long sortId){
        return  maGoodsCategoryDAO.isExistSortId(sortId);
    }

    @Override
    public Boolean editIsExistSortId(Long sortId,Long id){
        return  maGoodsCategoryDAO.editIsExistSortId(sortId,id);
    }

    @Override
    public List<GoodsCategoryDO> findGoodsCategoryByPCategoryCode(String categoryCode) {
        return this.maGoodsCategoryDAO.findGoodsCategoryByPCategoryCode(categoryCode);
    }
}
