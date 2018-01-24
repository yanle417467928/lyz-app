package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaOrderPhotoDAO;
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
    public int updateStatus(Long id, PhotoOrderStatus status) {
        return this.maOrderPhotoDAO.updateStatus(id, status);
    }

    @Override
    public int batchUpdateStatus(List<Long> ids, PhotoOrderStatus status) {
        return this.maOrderPhotoDAO.batchUpdateStatus(ids, status);
    }
}
