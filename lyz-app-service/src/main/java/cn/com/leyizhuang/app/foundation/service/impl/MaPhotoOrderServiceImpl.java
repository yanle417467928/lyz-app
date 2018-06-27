package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.MaOrderPhotoDAO;
import cn.com.leyizhuang.app.foundation.dao.MaterialAuditSheetDAO;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditGoodsInfo;
import cn.com.leyizhuang.app.foundation.pojo.order.MaterialAuditSheet;
import cn.com.leyizhuang.app.foundation.service.MaMaterialListService;
import cn.com.leyizhuang.app.foundation.service.MaPhotoOrderService;
import cn.com.leyizhuang.app.foundation.service.MaterialAuditGoodsInfoService;
import cn.com.leyizhuang.app.foundation.vo.management.order.PhotoOrderVO;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/19
 */
@Service
@Transactional
public class MaPhotoOrderServiceImpl implements MaPhotoOrderService {

    @Autowired
    private MaOrderPhotoDAO maOrderPhotoDAO;

    @Autowired
    private MaMaterialListService maMaterialListService;

    @Resource
    private MaterialAuditGoodsInfoService materialAuditGoodsInfoService;

    @Autowired
    private MaterialAuditSheetDAO materialAuditSheetDAO;

    @Override
    public PageInfo<PhotoOrderVO> findAllByCityIdAndStoreId(Integer page, Integer size, Long cityId, Long storeId, String keywords, String status, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<PhotoOrderVO> photoOrderVOS = maOrderPhotoDAO.findAllByCityIdAndStoreId(cityId, storeId, keywords, status, storeIds);
        return new PageInfo<>(photoOrderVOS);
    }

    @Override
    public PhotoOrderVO findById(Long id) {
        if (null != id){
            return this.maOrderPhotoDAO.findById(id);
        }
        return null;
    }

    @Override
    public PhotoOrderVO findByIdAndStatus(Long id, List<PhotoOrderStatus> status) {
        if (null != id){
            return this.maOrderPhotoDAO.findByIdAndStatus(id, status);
        }
        return null;
    }

    @Override
    public int updateStatus(Long id, PhotoOrderStatus status) {
        //修改处理人ID
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Long userId = null;
        if (null != shiroUser) {
            userId = shiroUser.getId();
        }
        return this.maOrderPhotoDAO.updateStatus(id, status, userId);
    }

    @Override
    public int batchUpdateStatus(List<Long> ids, PhotoOrderStatus status) {
        return this.maOrderPhotoDAO.batchUpdateStatus(ids, status);
    }

    @Override
    public void updateStatusAndsaveAndUpdateMaterialList(Long photoId, PhotoOrderStatus status, List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate) {
        //修改处理人ID
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Long userId = null;
        if (null != shiroUser) {
            userId = shiroUser.getId();
        }
        //修改拍照下单状态
        this.maOrderPhotoDAO.updateStatus(photoId,status,userId);
        //加入下料清单
        this.maMaterialListService.saveAndUpdateMaterialList(materialListSave, materialListUpdate);

//        if (null != shiroUser) {
//            this.maOrderPhotoDAO.updateOperationUserId(shiroUser.getId(), photoId);
//        }
    }

    @Override
    public void updateStatusAndsaveAndUpdateMaterialList(List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate) {
        //加入下料清单
        this.maMaterialListService.saveAndUpdateMaterialList(materialListSave, materialListUpdate);
    }

    @Override
    public int batchDelete(Long[] ids) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Long userId = null;
        if (null != shiroUser) {
            userId = shiroUser.getId();
        }
        return this.maOrderPhotoDAO.batchDelete(ids, userId);
    }

    @Override
    public List<String> findPhotosById(Long[] ids) {
        if (null != ids || ids.length > 0) {
            return this.maOrderPhotoDAO.findPhotosById(ids);
        }else{
            return null;
        }
    }

    @Override
    public void updateRemarkAndDeliveryId(String remark, Long deliveryId, Long userId, AppIdentityType identityType) {
        maMaterialListService.updateRemarkAndDeliveryId(remark, deliveryId, userId, identityType);
    }

    @Override
    public PhotoOrderVO findByPhotoOrderNo(String photoOrderNo) {
        if (null != photoOrderNo){
            return this.maOrderPhotoDAO.findByPhotoOrderNo(photoOrderNo);
        }
        return null;
    }

    @Override
    public void updatePhotoOrderProxyId(Long proxyId, String photoOrderNo) {
        this.maOrderPhotoDAO.updatePhotoOrderProxyId(proxyId, photoOrderNo);
    }

    @Override
    @Transactional
    public Boolean saveMaterialAuditSheet(MaterialAuditSheet materialAuditSheet, List<MaterialAuditGoodsInfo> materialAuditGoodsInfoList) {
        if (null != materialAuditSheet && null != materialAuditGoodsInfoList && materialAuditGoodsInfoList.size() > 0){
            //保存物料审核单头
            materialAuditSheetDAO.addMaterialAuditSheet(materialAuditSheet);
            //获取物料审核单id
            Long auditHeaderID = materialAuditSheet.getAuditHeaderID();

            for (MaterialAuditGoodsInfo materialAuditGoodsInfo : materialAuditGoodsInfoList) {
                //对物料审核单商品详情请进行赋值
                materialAuditGoodsInfo.setAuditHeaderID(auditHeaderID);
                materialAuditGoodsInfoService.addMaterialAuditGoodsInfo(materialAuditGoodsInfo);
            }
            return true;
        }
        return false;
    }

    @Override
    public void updateStatusAndsaveAndUpdateMaterialStatus(Long photoId, PhotoOrderStatus status) {
        //修改处理人ID
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        Long userId = null;
        if (null != shiroUser) {
            userId = shiroUser.getId();
        }
        //修改拍照下单状态
        this.maOrderPhotoDAO.updateStatus(photoId,status,userId);
    }

}
