package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.vo.AppAdminStoreInventoryVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * App后台管理门店库存服务
 *
 * @author Richard
 * Created on 2017-07-12 15:01
 **/
public interface AppAdminStoreInventoryService{

    /**
     * 门店库存分页查询
     *
     * @param page
     * @param size
     * @param keywords
     * @return
     */
    PageInfo<AppAdminStoreInventoryVO> queryPage(Integer page, Integer size, String keywords,Long cityId,Long storeId,List<Long> storeIds);

    PageInfo<AppAdminStoreInventoryVO> queryPageByStoreId(Integer page, Integer size, String keywords,Long storeId);

    PageInfo<AppAdminStoreInventoryVO> queryStoreInventoryByInfo(Integer page, Integer size, String keywords,Long cityId,Long storeId,String info);

    StoreInventory queryStoreInventoryById(Long storeId);

}
