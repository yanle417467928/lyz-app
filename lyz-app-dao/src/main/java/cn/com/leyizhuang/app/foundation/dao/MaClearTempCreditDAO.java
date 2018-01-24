package cn.com.leyizhuang.app.foundation.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaClearTempCreditDAO {
    String getCron(Long id);

    Boolean update(@Param(value = "cronTime") String cronTime, @Param(value = "jobName") String jobName);


}
