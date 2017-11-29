package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderDO;
import cn.com.leyizhuang.app.foundation.pojo.response.PhotoOrderDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.PhotoOrderListResponse;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/24
 */
public interface PhotoOrderService {
    PhotoOrderDO save(PhotoOrderDO photoOrderDO);

    List<PhotoOrderListResponse> findByUserIdAndIdentityTypeAndStatus(Long userId, AppIdentityType identityType, List<PhotoOrderStatus> photoOrderStatuses);

    PhotoOrderDetailsResponse findById(Long id);

    void updateStatus(Long id, PhotoOrderStatus status);

}
