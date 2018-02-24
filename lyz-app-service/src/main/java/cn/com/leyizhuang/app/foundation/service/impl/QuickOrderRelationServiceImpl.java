package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.QuickOrderRelationDAO;
import cn.com.leyizhuang.app.foundation.pojo.QuickOrderRelationDO;
import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import cn.com.leyizhuang.app.foundation.service.QuickOrderRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author GenerationRoad
 * @date 2017/11/21
 */
@Service
public class QuickOrderRelationServiceImpl implements QuickOrderRelationService {

    @Autowired
    private QuickOrderRelationDAO quickOrderRelationDAO;

    @Override
    public GoodsDO findByNumber(Long userId, AppIdentityType identityType, String number) {
        return this.quickOrderRelationDAO.findByNumber(userId, identityType, number);
    }

    @Override
    public QuickOrderRelationDO findQuickOrderRelationDOByNumber(String number) {
        return this.quickOrderRelationDAO.findQuickOrderRelationDOByNumber(number);
    }
}
