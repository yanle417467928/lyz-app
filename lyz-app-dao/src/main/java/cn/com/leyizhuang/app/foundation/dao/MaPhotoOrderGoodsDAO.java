package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.PhotoOrderGoodsDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/2/23
 */
@Repository
public interface MaPhotoOrderGoodsDAO {

    int batchSave(@Param("list") List<PhotoOrderGoodsDO> photoOrderGoodsDOList);

    List<PhotoOrderGoodsDO> findPhotoOrderGoodsByPhotoOrderNo(@Param("photoOrderNo")String photoOrderNo);

}
