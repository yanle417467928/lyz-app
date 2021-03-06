package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.GoodsEvaluation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by caiyu on 2017/10/17.
 */
@Repository
public interface GoodsEvaluationDAO {
    List<GoodsEvaluation> queryEvaluationListByGid(@Param("gid") Long gid);

    Integer getEvaluationQuantityByGid(@Param("gid") Long gid);
}
