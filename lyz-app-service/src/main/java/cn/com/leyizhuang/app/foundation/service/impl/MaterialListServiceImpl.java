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
    public void batchSave(List<MaterialListDO> materialListDOList) {
        if (null != materialListDOList && materialListDOList.size()>0){
            materialListDAO.batchSave(materialListDOList);
        }

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
        return this.materialListDAO.findByUserIdAndIdentityType(userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
    }

    @Override
    public MaterialListDO findByUserIdAndIdentityTypeAndGoodsId(Long userId, AppIdentityType identityType, Long goodsId) {
        if (null != userId && null != identityType && null != goodsId){
            return materialListDAO.findByUserIdAndIdentityTypeAndGoodsId(userId,identityType,goodsId);
        }
        return null;
    }
}
