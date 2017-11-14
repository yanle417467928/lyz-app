package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.AppAdminStoreInventoryDAO;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.vo.AppAdminStoreInventoryVO;
import cn.com.leyizhuang.app.foundation.service.AppAdminStoreInventoryService;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 门店库存服务实现类
 *
 * @author Richard
 *         Created on 2017-07-12 15:06
 **/
@Service
public class AppAdminStoreInventoryServiceImpl extends BaseServiceImpl<StoreInventory> implements AppAdminStoreInventoryService {

    public AppAdminStoreInventoryServiceImpl(BaseDAO<StoreInventory> baseDAO) {
        super(baseDAO);
    }

    @Autowired
    private AppAdminStoreInventoryDAO storeInventoryDAO;

    @Override
    public PageInfo<AppAdminStoreInventoryVO> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<AppAdminStoreInventoryVO> storeInventoryList = storeInventoryDAO.queryListVO();
        return new PageInfo<>(storeInventoryList);
    }


}
