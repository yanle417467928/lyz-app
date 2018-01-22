package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.vo.management.order.PhotoOrderVO;
import com.github.pagehelper.PageInfo;


/**
 * @author GenerationRoad
 * @date 2018/1/19
 */
public interface MaPhotoOrderService {
    PageInfo<PhotoOrderVO> findAll(Integer page, Integer size, Long cityId, Long storeId, String keywords);

    PhotoOrderVO findById(Long id);
}
