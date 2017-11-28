package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.PhysicalClassify;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by caiyu on 2017/11/27.
 */
@Repository
public interface PhysicalClassifyDAO {
    /**
     * 同步添加商品分类信息
     * @param physicalClassify  商品分类
     */
    void saveSynchronize(PhysicalClassify physicalClassify);

    /**
     * 同步修改商品分类信息
     * @param physicalClassify 商品分类
     */
    void modifySynchronize(PhysicalClassify physicalClassify);

    /**
     * 同步删除商品分类信息
     * @param hqId  Hq分类id
     */
    void deleteSynchronize(@Param("hqId") Long hqId);

    /**
     * 根据hq分类id查找分类信息
     * @param hqId  Hq分类id
     * @return 分类信息
     */
    PhysicalClassify findByHqId(@Param("hqId") Long hqId);


}
