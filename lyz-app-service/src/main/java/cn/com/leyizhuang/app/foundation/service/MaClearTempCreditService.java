package cn.com.leyizhuang.app.foundation.service;

public interface MaClearTempCreditService {
    String getCron();

    Boolean update(String cronTime, String jobName);

}
