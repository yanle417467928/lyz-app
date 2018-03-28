package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsGrade;



/**
 * @author liuh
 * @date 2018/03/27
 */
public interface GoodsGradeService {
    GoodsGrade queryBySkuAndSobId(String sku , String sobId);

    void addGoodsGrade(GoodsGrade goodsGrade);

    void updateGoodsGrade(GoodsGrade goodsGrade);

}
