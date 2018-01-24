package cn.com.leyizhuang.app.foundation.service;

public interface MaClearTempCreditService {
    String getCron(Long id);

    Boolean update(String cronTime, String jobName);

}
