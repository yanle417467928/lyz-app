package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AdminUserStoreDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.AdminUserStoreDO;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.AdminUserStoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/2/27
 */
@Service
@Transactional
public class AdminUserStoreServiceImpl implements AdminUserStoreService {

    @Autowired
    private AdminUserStoreDAO adminUserStoreDAO;

    @Override
    public int batchSave(List<AdminUserStoreDO> adminUserStoreDOList) {
        if (null != adminUserStoreDOList && adminUserStoreDOList.size() > 0) {
            return this.adminUserStoreDAO.batchSave(adminUserStoreDOList);
        }
        return 0;
    }

    @Override
    public List<AdminUserStoreVO> findByUid(Long uid) {
        return this.adminUserStoreDAO.findByUid(uid);
    }
}
