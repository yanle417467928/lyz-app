package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.vo.management.order.PhotoOrderVO;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/19
 */
@Repository
public interface MaOrderPhotoDAO {
    List<PhotoOrderVO> findAllByCityIdAndStoreId(@Param("cityId")Long cityId, @Param("storeId")Long storeId, @Param("keywords")String keywords,
                                                 @Param("status") String status, @Param("list") List<Long> storeIds);

    PhotoOrderVO findById(Long id);

    PhotoOrderVO findByIdAndStatus(@Param("id")Long id, @Param("list")List<PhotoOrderStatus> status);

    int updateStatus(@Param("id")Long id, @Param("status")PhotoOrderStatus status);

    int batchUpdateStatus(@Param("list")List<Long> ids, @Param("status")PhotoOrderStatus status);

    int batchDelete(@Param("array")Long[] ids);

    int updateOperationUserId(@Param("userId") Long userId, @Param("id") Long id);

    List<String> findPhotosById(@Param("array")Long[] ids);

}
