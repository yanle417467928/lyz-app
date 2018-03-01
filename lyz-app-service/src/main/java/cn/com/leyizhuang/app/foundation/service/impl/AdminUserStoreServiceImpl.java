package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.foundation.dao.AdminUserStoreDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.AdminUserStoreDO;
import cn.com.leyizhuang.app.foundation.service.AdminUserStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.AdminUserStoreVO;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Override
    public void batchUpdateAndSave(List<AdminUserStoreDO> adminUserStoreDOList, Long uid) {
        List<AdminUserStoreDO> list = this.adminUserStoreDAO.findAdminUserStoreDOByUid(uid);
        List<AdminUserStoreDO> addList = new ArrayList<>();
        List<AdminUserStoreDO> deleteList = new ArrayList<>();

        deleteList = this.statisticsAdminUserStore(list, adminUserStoreDOList);
        addList = this.statisticsAdminUserStore(adminUserStoreDOList, list);
        this.batchSave(addList);
        this.batchDelete(deleteList);

    }

    @Override
    public int batchDelete(List<AdminUserStoreDO> adminUserStoreDOList) {
        if (null != adminUserStoreDOList && adminUserStoreDOList.size() > 0) {
            return this.adminUserStoreDAO.batchDelete(adminUserStoreDOList);
        }
        return 0;
    }

    @Override
    public List<Long> findStoreIdByUid(Long uid) {

        return this.adminUserStoreDAO.findStoreIdByUid(uid);
    }

    @Override
    public List<Long> findStoreIdList() {
        //获取登录用户ID
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        List<Long> storeIds = new ArrayList<>();
        if (null != shiroUser) {
            //查询登录用户门店权限的门店ID
            storeIds = this.adminUserStoreDAO.findStoreIdByUid(shiroUser.getId());
        }
        return storeIds;
    }

    private List<AdminUserStoreDO> statisticsAdminUserStore(List<AdminUserStoreDO> list1, List<AdminUserStoreDO> list2){
        List<AdminUserStoreDO> list = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            Long storeId = list1.get(i).getStoreId();
            Long cityId = list1.get(i).getCityId();
            Boolean flag = Boolean.FALSE;
            for (int j = 0; j < list2.size(); j++) {
                Long storeIdNew = list2.get(j).getStoreId();
                Long cityIdNew = list2.get(j).getCityId();
                if (null != cityId && null != cityIdNew && cityIdNew.equals(cityId)){
                    flag = Boolean.TRUE;
                }
                if (null != storeId && null != storeIdNew && storeIdNew.equals(storeId)){
                    flag = Boolean.TRUE;
                }
            }
            if (!flag){
                list.add(list1.get(i));
            }
        }
        return list;
    }
}
