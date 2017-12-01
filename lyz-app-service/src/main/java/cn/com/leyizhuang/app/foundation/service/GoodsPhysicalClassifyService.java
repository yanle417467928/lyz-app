package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.GoodsPhysicalClassify;

/**
 * Created by caiyu on 2017/11/27.
 */
public interface GoodsPhysicalClassifyService {
    /**
     * 同步添加商品分类信息
     *
     * @param goodsPhysicalClassify 商品分类
     */
    void saveSynchronize(GoodsPhysicalClassify goodsPhysicalClassify);

    /**
     * 同步修改商品分类信息
     *
     * @param goodsPhysicalClassify 商品分类
     */
    void modifySynchronize(GoodsPhysicalClassify goodsPhysicalClassify);

    /**
     * 同步删除商品分类信息
     *
     * @param hqId Hq分类id
     */
    void deleteSynchronize(Long hqId);

    /**
     * 根据hq分类id查找分类信息
     *
     * @param hqId Hq分类id
     * @return 分类信息
     */
    GoodsPhysicalClassify findByHqId(Long hqId);
}
