package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.MaMaterialListDAO;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.service.MaMaterialListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/31
 */
@Service
@Transactional
public class MaMaterialListServiceImpl implements MaMaterialListService {

    @Autowired
    private MaMaterialListDAO maMaterialListDAO;

    @Override
    public MaterialListDO findByUserIdAndIdentityTypeAndGoodsId(Long userId, AppIdentityType identityType, Long goodsId) {
        if (null != userId && null != identityType && null != goodsId) {
            return maMaterialListDAO.findByUserIdAndIdentityTypeAndGoodsId(userId, identityType, goodsId);
        }
        return null;
    }

    @Override
    public void saveAndUpdateMaterialList(List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate) {
        if (null != materialListSave && materialListSave.size() > 0) {
            maMaterialListDAO.batchSave(materialListSave);
        }
        if (null != materialListUpdate && materialListUpdate.size() > 0) {
            for (MaterialListDO materialListDO : materialListUpdate) {
                maMaterialListDAO.modifyQty(materialListDO.getId(), materialListDO.getQty(),materialListDO.getDeliveryId());
            }
        }
    }

    @Override
    public void updateRemarkAndDeliveryId(String remark, Long deliveryId, Long userId, AppIdentityType identityType) {
        maMaterialListDAO.updateRemarkAndDeliveryId(remark, deliveryId, userId, identityType);
    }
}
