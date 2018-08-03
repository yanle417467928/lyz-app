package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.MaMaterialListDAO;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.management.MaterialChangeDetailLog;
import cn.com.leyizhuang.app.foundation.pojo.management.MaterialChangeHeadLog;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.MaUpdateMaterialResponse;
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
                maMaterialListDAO.modifyQty(materialListDO.getId(), materialListDO.getQty(),materialListDO.getDeliveryId(),materialListDO.getIsGenerateOrder(),materialListDO.getUserId(),materialListDO.getIdentityType());
            }
        }
    }

    @Override
    public void updateRemarkAndDeliveryId(String remark, Long deliveryId, Long userId, AppIdentityType identityType) {
        maMaterialListDAO.updateRemarkAndDeliveryId(remark, deliveryId, userId, identityType);
    }

    @Override
    public List<MaterialListDO> findMaPhotoOrderMaterialListByPhotoNumber(String photoNo) {
        return maMaterialListDAO.findMaPhotoOrderMaterialListByPhotoNumber(photoNo);
    }

    @Override
    public List<MaUpdateMaterialResponse> findMaAllMaterialListByPhotoNumber(String photoNo, String identityType,String rankCode) {
        return maMaterialListDAO.findMaAllMaterialListByPhotoNumber(photoNo, identityType, rankCode);
    }

    @Override
    public void deleteMaterialListByUserIdAndIdentityTypeAndGoodsSku(Long userId, String identityType, String sku) {
        maMaterialListDAO.deleteMaterialListByUserIdAndIdentityTypeAndGoodsSku(userId, identityType, sku);
    }

    @Override
    public void saveMaterialChangeHeadLogAndDetailLog(MaterialChangeHeadLog materialChangeHeadLog, List<MaterialChangeDetailLog> materialChangeDetailLogList) {

        if (null != materialChangeHeadLog) {
            maMaterialListDAO.saveMaterialChangeHeadLog(materialChangeHeadLog);
            Long updateHeadId = materialChangeHeadLog.getId();
            if (null != materialChangeDetailLogList && materialChangeDetailLogList.size() > 0){
                for (MaterialChangeDetailLog materialChangeDetailLog : materialChangeDetailLogList){
                    materialChangeDetailLog.setUpdateHeadId(updateHeadId);
                    maMaterialListDAO.saveMaterialChangeDetailLog(materialChangeDetailLog);
                }
            }
        }
    }

    @Override
    public List<MaUpdateMaterialResponse> findProxyMaterialListByPhotoNumber(Long userid, AppIdentityType identityType) {
        if (null != userid && null != identityType){
            return maMaterialListDAO.findProxyMaterialListByPhotoNumber(userid, identityType);
        }
        return null;
    }
}
