package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.ActBaseDAO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.app.foundation.service.AppActService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by panjie on 2017/11/22.
 */
@Service
public class AppActServiceImpl implements AppActService{

    @Autowired
    private ActBaseDAO actBaseDAO;

    @Override
    public List<ActBaseDO> queryList() {
        return actBaseDAO.queryList();
    }
}
