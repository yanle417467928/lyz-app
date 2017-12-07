package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
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

    ActBaseDO queryById();

    /**
     *
     * @param skus 商品sku字符串 sku1,sku2,...
     * @param now 当前时间
     * @param cityId 城市id
     * @param actTarget 促销目标
     * @param storeId 门店id
     * @return
     */
    List<ActBaseDO> queryListBySkus(@PathParam("skus") String skus,@PathParam("now") LocalDateTime now,
                                    @PathParam("cityId") Long cityId,@PathParam("actTarget") String actTarget,
                                    @PathParam("storeId") Long storeId);

    /**
     * 批量插入
     * @param trainRecordList
     */
    int insertBatch(List<ActBaseDO> trainRecordList);

}
