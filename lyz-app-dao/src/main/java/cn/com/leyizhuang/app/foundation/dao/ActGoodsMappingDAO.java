package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.activity.ActGoodsMappingDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * table: act_goods_mapping
 * Created by panjie on 2017/11/22.
 */
@Repository
public interface ActGoodsMappingDAO {

    void save(ActGoodsMappingDO actBaseDO);

    void update(ActGoodsMappingDO actBaseDO);

    List<ActGoodsMappingDO> queryList();

    ActGoodsMappingDO queryById();

    List<ActGoodsMappingDO> queryListByActId(@Param("actId") Long actId);

    List<String> querySkusByActId(@Param("actId") Long actId);

    void deleteByActBaseId(@Param("actBaseId") Long actBaseId);
    /**
     * 批量插入
     * @param trainRecordList
     */
    int insertBatch(List<ActGoodsMappingDO> trainRecordList);
}
