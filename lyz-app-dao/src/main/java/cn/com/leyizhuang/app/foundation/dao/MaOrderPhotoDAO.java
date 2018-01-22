package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.vo.management.order.PhotoOrderVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/19
 */
@Repository
public interface MaOrderPhotoDAO {
    List<PhotoOrderVO> findAll(@Param("cityId")Long cityId, @Param("storeId")Long storeId, @Param("keywords")String keywords);

    PhotoOrderVO findById(Long id);

}
