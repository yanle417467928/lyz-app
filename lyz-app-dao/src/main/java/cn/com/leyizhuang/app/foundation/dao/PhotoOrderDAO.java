package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderDO;
import cn.com.leyizhuang.app.foundation.pojo.response.PhotoOrderDetailsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.PhotoOrderListResponse;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/24
 */
@Repository
public interface PhotoOrderDAO {

    void save(PhotoOrderDO photoOrderDO);

    List<PhotoOrderListResponse> findByUserIdAndIdentityTypeAndStatus(@Param("userId") Long userId, @Param("identityType")AppIdentityType identityType, @Param("list") List<PhotoOrderStatus> photoOrderStatuses);

    PhotoOrderDetailsResponse findById(Long id);

    void updateStatus(@Param("id") Long id, @Param("status") PhotoOrderStatus status);
}
