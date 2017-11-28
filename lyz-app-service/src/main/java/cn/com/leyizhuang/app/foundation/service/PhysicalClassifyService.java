package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.PhysicalClassify;

/**
 * Created by caiyu on 2017/11/27.
 */
public interface PhysicalClassifyService {
    /**
     * 同步添加商品分类信息
     *
     * @param physicalClassify 商品分类
     */
    void saveSynchronize(PhysicalClassify physicalClassify);

    /**
     * 同步修改商品分类信息
     *
     * @param physicalClassify 商品分类
     */
    void modifySynchronize(PhysicalClassify physicalClassify);

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
    PhysicalClassify findByHqId(Long hqId);
}
