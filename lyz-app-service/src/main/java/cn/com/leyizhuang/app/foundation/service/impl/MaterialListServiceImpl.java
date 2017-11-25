package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.MaterialListDAO;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.CouponMaterialListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.materialList.NormalMaterialListResponse;
import cn.com.leyizhuang.app.foundation.service.GoodsService;
import cn.com.leyizhuang.app.foundation.service.MaterialListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author GenerationRoad
 * @date 2017/10/17
 */
@Service
public class MaterialListServiceImpl implements MaterialListService {

    @Autowired
    private GoodsService goodsService;

    private MaterialListDAO materialListDAO;

    public MaterialListServiceImpl(MaterialListDAO materialListDAO) {
        this.materialListDAO = materialListDAO;
    }

    @Override
    @Transactional
    public void batchSave(List<MaterialListDO> materialListDOList) {
        if (null != materialListDOList && materialListDOList.size() > 0) {
            materialListDAO.batchSave(materialListDOList);
        }

    }

    @Override
    @Transactional
    public void modifyQty(Long id, Integer qty) {
        this.materialListDAO.modifyQty(id, qty);
    }

    @Override
    @Transactional
    public void deleteMaterialList(List<Long> ids) {
        this.materialListDAO.batchDelete(ids);
    }

    @Override
    public List<NormalMaterialListResponse> findByUserIdAndIdentityType(Long userId, Integer identityType) {
        return this.materialListDAO.findByUserIdAndIdentityType(userId, AppIdentityType.getAppIdentityTypeByValue(identityType));
    }

    @Override
    public MaterialListDO findByUserIdAndIdentityTypeAndGoodsId(Long userId, AppIdentityType identityType, Long goodsId) {
        if (null != userId && null != identityType && null != goodsId) {
            return materialListDAO.findByUserIdAndIdentityTypeAndGoodsId(userId, identityType, goodsId);
        }
        return null;
    }

    @Override
    public List<MaterialListDO> findMaterialListByUserIdAndIdentityType(Long userId, AppIdentityType identityType) {
        if (null != userId && null != identityType) {
            return materialListDAO.findMaterialListByUserIdAndIdentityType(userId, identityType);
        }
        return null;
    }

    @Override
    public List<Long> findMaterialListGoodsIdsByUserIdAndIdentityType(Long userId, AppIdentityType identityType) {
        if (null != userId && null != identityType) {
            return materialListDAO.findMaterialListGoodsIdsByUserIdAndIdentityType(userId, identityType);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteMaterialListByUserIdAndIdentityTypeAndGoodsId(Long empId, AppIdentityType identityType, List<Long> deleteGoodsIds) {
        if (null != empId && null != identityType && null != deleteGoodsIds && deleteGoodsIds.size() > 0) {
            materialListDAO.deleteMaterialListByUserIdAndIdentityTypeAndGoodsId(empId,
                    identityType, deleteGoodsIds);
        }
    }

    @Override
    public Map<Long, Integer> findGoodsQtyByUserIdAndIdentityTypeAndGoodsId(Long userId, AppIdentityType identityType, Long goodsId) {
        if (null != userId && null != identityType && null != goodsId) {
            return materialListDAO.findGoodsQtyByUserIdAndIdentityTypeAndGoodsId(userId,
                    identityType, goodsId);
        }
        return null;
    }

    @Override
    public Boolean existOtherMaterialListByUserIdAndIdentityType(Long userID, Integer identityType) {
        if (null != userID && null != identityType) {
            return materialListDAO.existOtherMaterialListByUserIdAndIdentityType(userID, AppIdentityType.getAppIdentityTypeByValue(identityType));
        }
        return null;
    }

    @Override
    public List<NormalMaterialListResponse> findMaterialListByUserIdAndTypeAndAuditIsNotNull(Long userId, AppIdentityType identityType) {
        if (null != userId && null != identityType) {
            return materialListDAO.findMaterialListByUserIdAndTypeAndAuditIsNotNull(userId, identityType);
        }
        return null;
    }

    @Override
    public List<NormalMaterialListResponse> findMaterialListByUserIdAndTypeAndIsCouponId(Long userId, AppIdentityType identityType) {
        if (null != userId && null != identityType) {
            return materialListDAO.findMaterialListByUserIdAndTypeAndIsCouponId(userId, identityType);
        }
        return null;
    }

    @Override
    public MaterialListDO findCouponTransformByUserIdAndIdentityTypeAndGoodsId(Long userId, AppIdentityType identityType, Long goodsId) {
        if (null != userId && null != identityType && null != goodsId) {
            return materialListDAO.findCouponTransformByUserIdAndIdentityTypeAndGoodsId(userId, identityType,goodsId);
        }
        return null;
    }

    @Override
    public MaterialListDO findCouponTransformByUserIdAndCusIdAndIdentityTypeAndGoodsId(Long userId, Long cusId, AppIdentityType identityType, Long goodsId) {
        if (null != userId && null != cusId && null != identityType && null != goodsId) {
            return materialListDAO.findCouponTransformByUserIdAndCusIdAndIdentityTypeAndGoodsId(userId,cusId, identityType,goodsId);
        }
        return null;
    }

    @Override
    public List<CouponMaterialListResponse> findGuideMaterialListByUserIdAndCusIdAndIdentityType(Long userId, Long cusId, AppIdentityType identityType) {
        if (null != userId && null != identityType && null != cusId) {
            return materialListDAO.findGuideMaterialListByUserIdAndCusIdAndIdentityType(userId,cusId, identityType);
        }
        return null;
    }

    @Override
    public List<CouponMaterialListResponse> findCoutomerMaterialListByUserIdAndIdentityType(Long userId, AppIdentityType identityType) {
        if (null != userId && null != identityType) {
            return materialListDAO.findCoutomerMaterialListByUserIdAndIdentityType(userId, identityType);
        }
        return null;
    }
}
