package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.List;

/**
 * table: act_base
 * Created by panjie on 2017/11/22.
 */
@Repository
public interface ActBaseDAO{

    void save(ActBaseDO actBaseDO);

    void update(ActBaseDO actBaseDO);

    List<ActBaseDO> queryList();

    ActBaseDO queryById(@Param("id") Long id);

    List<ActBaseDO> queryByIdList(@Param("ids") List<Long> ids);

    /**
     * 根据关键字返回结果
     * @param keywords
     * @return
     */
    List<ActBaseDO> queryByKeywords(@Param("keywords") String keywords,@Param("status") String status);

    /**
     *
     * @param skus 商品sku字符串 sku1,sku2,...
     * @param now 当前时间
     * @param cityId 城市id
     * @param actTarget 促销目标
     * @param storeId 门店id
     * @return
     */
    List<ActBaseDO> queryListBySkus(@Param("skus") List<String> skus, @Param("now") LocalDateTime now,
                                    @Param("cityId") Long cityId, @Param("actTarget") String actTarget,
                                    @Param("storeId") Long storeId);

    /**
     * 返回id集合中过期的促销
     * @param actIds
     * @param now
     * @return
     */
    List<ActBaseDO> queryListByActIdsAndEndTime(@Param("actIds") List<Long> actIds,@Param("now") LocalDateTime now);

    /**
     * 批量插入
     * @param trainRecordList
     */
    int insertBatch(List<ActBaseDO> trainRecordList);

    void insertActLjGoodsMapping();

}
