package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.TimingTaskErrorMessageDO;
import org.springframework.stereotype.Repository;

/**
 * Created by caiyu on 2018/3/1.
 */
@Repository
public interface TimingTaskErrorMessageDAO {
    void saveTimingTaskErrorMessage(TimingTaskErrorMessageDO timingTaskErrorMessageDO);
}
