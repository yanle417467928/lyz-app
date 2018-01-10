package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.PhotoOrderDAO;
import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderDO;
import cn.com.leyizhuang.app.foundation.pojo.response.PhotoOrderDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.PhotoOrderListResponse;
import cn.com.leyizhuang.app.foundation.service.PhotoOrderService;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/24
 */
@Service
@Transactional
public class PhotoOrderServiceImpl implements PhotoOrderService {

    @Autowired
    private PhotoOrderDAO photoOrderDAO;

    @Override
    public PhotoOrderDO save(PhotoOrderDO photoOrderDO) {
        if (null != photoOrderDO) {
            this.photoOrderDAO.save(photoOrderDO);
        }
        return photoOrderDO;
    }

    @Override
    public PageInfo<PhotoOrderListResponse> findByUserIdAndIdentityTypeAndStatus(Long userId, AppIdentityType identityType, List<PhotoOrderStatus> photoOrderStatuses, Integer page, Integer size) {
       if(photoOrderStatuses.contains(PhotoOrderStatus.PENDING)){
           List<PhotoOrderListResponse> photoOrderListResponseList = this.photoOrderDAO.findByUserIdAndIdentityTypeAndStatus(userId, identityType, photoOrderStatuses);
           return new PageInfo<>(photoOrderListResponseList);
       }else{
           PageHelper.startPage(page, size);
           List<PhotoOrderListResponse> photoOrderListResponseList = this.photoOrderDAO.findByUserIdAndIdentityTypeAndStatus(userId, identityType, photoOrderStatuses);
           return new PageInfo<>(photoOrderListResponseList);
       }
    }

    @Override
    public PhotoOrderDetailsResponse findById(Long id) {
        return this.photoOrderDAO.findById(id);
    }

    @Override
    public void updateStatus(Long id, PhotoOrderStatus status) {
        this.photoOrderDAO.updateStatus(id, status);
    }
}
