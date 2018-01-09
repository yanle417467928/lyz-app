package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorativeCompanyDetailVO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.SimpleDecorativeCompany;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MaStoreService {

    PageInfo<StoreVO> queryPageVO(Integer page, Integer size);

    List<SimpleStoreParam> findStoreList();

    List<SimpleStoreParam> findAllStorelist();

    List<SimpleStoreParam> findStoresListByCityId(Long cityId);

    List<SimpleStoreParam> findAllStoresListByCityId(Long cityId);

    StoreDetailVO queryStoreVOById(Long storeId);

    PageInfo<StoreVO> queryStoreListByCityId(Integer page, Integer size, Long cityId);

    PageInfo<StoreVO> findStoresListByCondition(Integer page, Integer size, String enabled, Long cityId);

    PageInfo<StoreVO> findStoresListByStoreInfo(Integer page, Integer size,String queryStoreInfo);

     void update(Long storeId,Boolean  isSelfDelivery );

     PageInfo<StoreDO> queryDecorativeCompanyPageVO(Integer page, Integer size);

    PageInfo<StoreDO> findDecorativeByCondition(Integer page, Integer size, String enabled, Long cityId);

    PageInfo<StoreDO> findDecorativeByInfo(Integer page, Integer size,String queryDecorativeInfo);

    DecorativeCompanyDetailVO queryDecorativeCompanyById(Long decorativeCompanyId);

    List<SimpleDecorativeCompany> findDecorativeCompanyVOList();

    PageInfo<DecorativeCompanyInfo> queryDecorativeCreditPageVO(Integer page, Integer size);

    DecorativeCompanyInfo  queryDecorativeCompanyCreditById(Long decorativeCompanyId);

    PageInfo<DecorativeCompanyInfo>  findDecorativeCreditByInfo(Integer page, Integer size,String queryDecorativeCreditInfo);

    PageInfo<DecorativeCompanyInfo> findDecorativeCreditByCondition(Integer page, Integer size, String enabled, Long cityId);

    /**
     * 获取装饰公司门店列表
     * @return  门店列表
     */
    List<StoreVO> findCompanyStoresList();

    /**
     * 根据城市获取装饰公司门店列表
     * @param cityId    城市id
     * @return  门店列表
     */
    List<StoreVO> findCompanyStoresListByCityId(Long cityId);

    /**
     * 根据城市获取支持门店自提门店列表
     * @param cityId    城市id
     * @return  门店列表
     */
    List<StoreVO> findSelfDeliveryStoresListByCityId(Long cityId);

    /**
     * 获取支持门店自提门店列表
     * @return  门店列表
     */
    List<StoreVO> findSelfDeliveryStoresList();
}
