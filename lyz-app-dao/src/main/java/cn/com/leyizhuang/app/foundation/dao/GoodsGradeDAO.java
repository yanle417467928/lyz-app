package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.GoodsGrade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsGradeDAO {
    GoodsGrade queryBySkuAndSobId(@Param(value = "sku") String sku ,@Param(value = "sobId") String sobId);

    void addGoodsGrade(GoodsGrade goodsGrade);

    void updateGoodsGrade(GoodsGrade goodsGrade);
}
