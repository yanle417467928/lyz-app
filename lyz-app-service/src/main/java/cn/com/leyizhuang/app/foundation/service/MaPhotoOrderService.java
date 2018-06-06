package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.MaterialListDO;
import cn.com.leyizhuang.app.foundation.vo.management.order.PhotoOrderVO;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import com.github.pagehelper.PageInfo;

import java.util.List;


/**
 * @author GenerationRoad
 * @date 2018/1/19
 */
public interface MaPhotoOrderService {
    PageInfo<PhotoOrderVO> findAllByCityIdAndStoreId(Integer page, Integer size, Long cityId, Long storeId, String keywords, String status, List<Long> storeIds);

    PhotoOrderVO findById(Long id);

    PhotoOrderVO findByIdAndStatus(Long id, List<PhotoOrderStatus> status);

    int updateStatus(Long id, PhotoOrderStatus status);

    int batchUpdateStatus(List<Long> ids, PhotoOrderStatus status);

    void updateStatusAndsaveAndUpdateMaterialList(Long photoId, PhotoOrderStatus status, List<MaterialListDO> materialListSave, List<MaterialListDO> materialListUpdate);

    int batchDelete(Long[] ids);

    List<String> findPhotosById(Long[] ids);

    void updateRemarkAndDeliveryId(String remark,Long deliveryId,Long userId,AppIdentityType identityType);

}
