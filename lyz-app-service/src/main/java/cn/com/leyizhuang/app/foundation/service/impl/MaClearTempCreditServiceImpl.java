package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaClearTempCreditDAO;
import cn.com.leyizhuang.app.foundation.service.MaClearTempCreditService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MaClearTempCreditServiceImpl implements MaClearTempCreditService {

    @Resource
    private MaClearTempCreditDAO clearTempCreditDAO;

     @Override
    public String getCron(){
        return  clearTempCreditDAO.getCron();
    }

    @Override
    public Boolean update(String cronTime,String jobName){
        return  clearTempCreditDAO.update(cronTime,jobName);
    }

}
