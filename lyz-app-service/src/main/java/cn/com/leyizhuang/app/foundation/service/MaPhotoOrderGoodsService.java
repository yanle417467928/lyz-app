package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderGoodsDO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/2/23
 */
public interface MaPhotoOrderGoodsService {

    int batchSave(List<PhotoOrderGoodsDO> photoOrderGoodsDOList);

    PageInfo<PhotoOrderGoodsDO> findPhotoOrderGoodsByPhotoOrderNo(Integer page, Integer size, String photoOrderNo);

}
