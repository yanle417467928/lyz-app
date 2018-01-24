package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.vo.management.order.PhotoOrderVO;
import cn.com.leyizhuang.common.core.constant.PhotoOrderStatus;
import com.github.pagehelper.PageInfo;

import java.util.List;


/**
 * @author GenerationRoad
 * @date 2018/1/19
 */
public interface MaPhotoOrderService {
    PageInfo<PhotoOrderVO> findAll(Integer page, Integer size, Long cityId, Long storeId, String keywords);

    PhotoOrderVO findById(Long id);

    int updateStatus(Long id, PhotoOrderStatus status);

    int batchUpdateStatus(List<Long> ids, PhotoOrderStatus status);

}
