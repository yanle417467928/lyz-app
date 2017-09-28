package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.GoodsCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 门店服务Dao层
 *
 * @author Richard
 * Created on 2017-07-24 9:12
 **/
@Repository
public interface AppProductCategoryDAO{

    List<GoodsCategory> findSecondCategoryByFirstCategoryCode(String categoryCode);

    List<GoodsCategory> findSecondCategoryByFirstCategoryCodeAndStoreId(@Param(value = "categoryCode") String categoryCode,
                                                                        @Param(value = "storeId") Long storeId);
}
