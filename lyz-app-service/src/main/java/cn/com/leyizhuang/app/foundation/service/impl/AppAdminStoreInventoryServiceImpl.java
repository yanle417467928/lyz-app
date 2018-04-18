package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppAdminStoreInventoryDAO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.service.AppAdminStoreInventoryService;
import cn.com.leyizhuang.app.foundation.vo.AppAdminStoreInventoryVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 门店库存服务实现类
 *
 * @author Richard
 * Created on 2017-07-12 15:06
 **/
@Service
public class AppAdminStoreInventoryServiceImpl implements AppAdminStoreInventoryService {

    @Autowired
    private AppAdminStoreInventoryDAO storeInventoryDAO;


    @Override
    public PageInfo<AppAdminStoreInventoryVO> queryPage(Integer page, Integer size, String keywords, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<AppAdminStoreInventoryVO> storeInventoryList = storeInventoryDAO.queryListVO(keywords, storeIds);
        return new PageInfo<>(storeInventoryList);
    }


    @Override
    public PageInfo<AppAdminStoreInventoryVO> queryPageByStoreId(Integer page, Integer size, String keywords, Long storeId) {
        PageHelper.startPage(page, size);
        if (null != storeId) {
            List<AppAdminStoreInventoryVO> storeInventoryList = storeInventoryDAO.queryListByStoreId(storeId);
            return new PageInfo<>(storeInventoryList);
        }
        return null;
    }

    @Override
    public PageInfo<AppAdminStoreInventoryVO> queryStoreInventoryByInfo(Integer page, Integer size, String keywords, Long storeId, String info) {
        PageHelper.startPage(page, size);
        if (-1 == storeId) {
            storeId = null;
        }
        List<AppAdminStoreInventoryVO> storeInventoryList = storeInventoryDAO.queryStoreInventoryByInfo(storeId,info);
        return new PageInfo<>(storeInventoryList);
    }

    @Override
    public StoreInventory queryStoreInventoryById(Long storeId) {
        if (storeId != null) {
            return storeInventoryDAO.queryByStoreId(storeId);
        }
        return null;
    }
}
