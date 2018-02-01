package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaOrderPhotoDAO;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.service.MaMaterialListService;
import cn.com.leyizhuang.app.foundation.service.MaPhotoOrderService;
import cn.com.leyizhuang.app.foundation.vo.management.order.PhotoOrderVO;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public PageInfo<PhotoOrderVO> findAll(Integer page, Integer size, Long cityId, Long storeId, String keywords) {
        PageHelper.startPage(page, size);
        List<PhotoOrderVO> photoOrderVOS = maOrderPhotoDAO.findAll(cityId, storeId, keywords);
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
        return this.maOrderPhotoDAO.updateStatus(id, status);
    }

    @Override
    public int batchUpdateStatus(List<Long> ids, PhotoOrderStatus status) {
        return this.maOrderPhotoDAO.batchUpdateStatus(ids, status);
    }

    @Override
    public void updateStatusAndsaveAndUpdateMaterialList(Long photoId, PhotoOrderStatus status, List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate) {
        //修改拍照下单状态
        this.maOrderPhotoDAO.updateStatus(photoId,status);
        //加入下料清单
        this.maMaterialListService.saveAndUpdateMaterialList(materialListSave, materialListUpdate);
    }

    @Override
    public int batchDelete(Long[] ids) {
        return this.maOrderPhotoDAO.batchDelete(ids);
    }


}
