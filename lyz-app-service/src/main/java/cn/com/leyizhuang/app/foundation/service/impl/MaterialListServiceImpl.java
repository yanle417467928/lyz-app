package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.MaterialListDAO;
import cn.com.leyizhuang.app.foundation.pojo.GoodsDO;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.response.MaterialListResponse;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.MaterialListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/17
 */
@Service
@Transactional
public class MaterialListServiceImpl implements MaterialListService {

    @Autowired
    private GoodsService goodsService;

    private MaterialListDAO materialListDAO;

    public MaterialListServiceImpl(MaterialListDAO materialListDAO){
        this.materialListDAO = materialListDAO;
    }

    @Override
    public void batchSave(Long userId, Integer identityType, String[] param) {
        List<MaterialListDO> materialListDOlist = new ArrayList<MaterialListDO>();
        for (int i = 0; i < param.length; i++) {
            MaterialListDO materialListDO = new MaterialListDO();
            materialListDO.setUserId(userId);
            materialListDO.setIdentityType(AppIdentityType.getAppUserTypeByValue(identityType));
            String[] arrayParam = param[i].split("-");
            if (arrayParam.length == 2) {
                GoodsDO goodsDO = this.goodsService.findGoodsById(Long.parseLong(arrayParam[0]));
                if (null != goodsDO){
                    materialListDO.setGid(goodsDO.getGid());
                    materialListDO.setSku(goodsDO.getSku());
                    materialListDO.setSkuName(goodsDO.getSkuName());
                    materialListDO.setGoodsSpecification(goodsDO.getGoodsSpecification());
                    materialListDO.setGoodsUnit(goodsDO.getGoodsUnit());
                    if (null != goodsDO.getCoverImageUri()) {
                        String uri[] = goodsDO.getCoverImageUri().split(",");
                        materialListDO.setCoverImageUri(uri[0]);
                    }
                    materialListDO.setQty(Integer.parseInt(arrayParam[1]));
                    materialListDOlist.add(materialListDO);
                }
            }
        }
        this.materialListDAO.batchSave(materialListDOlist);
    }

    @Override
    public void modifyQty(Long id, Integer qty) {
        this.materialListDAO.modifyQty(id, qty);
    }

    @Override
    public void deleteMaterialList(List<Long> ids) {
        this.materialListDAO.batchDelete(ids);
    }

    @Override
    public List<MaterialListResponse> findByUserIdAndIdentityType(Long userId, Integer identityType) {
        return this.materialListDAO.findByUserIdAndIdentityType(userId, AppIdentityType.getAppUserTypeByValue(identityType));
    }
}
