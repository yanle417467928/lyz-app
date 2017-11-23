package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.vo.AppAdminStoreInventoryVO;
import cn.com.leyizhuang.common.foundation.service.BaseService;
import com.github.pagehelper.PageInfo;

/**
 * App后台管理门店库存服务
 *
 * @author Richard
 * Created on 2017-07-12 15:01
 **/
public interface AppAdminStoreInventoryService extends BaseService<StoreInventory> {
    PageInfo<AppAdminStoreInventoryVO> queryPage(Integer page, Integer size);


}
