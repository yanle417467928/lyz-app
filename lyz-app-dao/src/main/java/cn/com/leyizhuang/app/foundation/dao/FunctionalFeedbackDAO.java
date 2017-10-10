package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.FunctionalFeedbackDO;
import org.springframework.stereotype.Repository;

/**
 * @author GenerationRoad
 * @date 2017/10/10
 */
@Repository
public interface FunctionalFeedbackDAO {

    void save(FunctionalFeedbackDO functionalFeedbackDO);
}
