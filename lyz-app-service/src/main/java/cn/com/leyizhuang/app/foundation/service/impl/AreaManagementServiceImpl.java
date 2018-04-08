package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AreaManagementDAO;
import cn.com.leyizhuang.app.foundation.service.AreaManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/4/8
 */
@Service
public class AreaManagementServiceImpl implements AreaManagementService {

    @Autowired
    private AreaManagementDAO areaManagementDAO;

    @Override
    public List<String> findAreaManagementByParentCodeAndLevelIsFive(String parentCode) {
        return this.areaManagementDAO.findAreaManagementByParentCodeAndLevelIsFive(parentCode);
    }
}
