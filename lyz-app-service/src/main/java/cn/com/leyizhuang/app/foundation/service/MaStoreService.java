package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.store.StoreDO;
import cn.com.leyizhuang.app.foundation.vo.DecorativeCompanyVO;
import cn.com.leyizhuang.app.foundation.vo.StoreVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MaStoreService {

    PageInfo<StoreVO> queryPageVO(Integer page, Integer size);

    List<StoreVO> findStoreList();

    List<StoreVO> findStoresListByCityId(Long cityId);

    StoreVO queryStoreVOById(Long storeId);

    PageInfo<StoreVO> queryStoreListByCityId(Integer page, Integer size, Long cityId);

    PageInfo<StoreVO> findStoresListByCondition(Integer page, Integer size, String enabled, Long cityId);

    PageInfo<StoreVO> findStoresListByStoreInfo(Integer page, Integer size,String queryStoreInfo);

     void update(Long storeId,Boolean  isSelfDelivery );

     PageInfo<StoreDO> queryDecorativeCompanyPageVO(Integer page, Integer size);

    PageInfo<StoreDO> findDecorativeByCondition(Integer page, Integer size, String enabled, Long cityId);

    PageInfo<StoreDO> findDecorativeByInfo(Integer page, Integer size,String queryDecorativeInfo);

    DecorativeCompanyVO  queryDecorativeCompanyById(Long decorativeCompanyId);

    List<DecorativeCompanyVO> findDecorativeCompanyVOList();

    PageInfo<DecorativeCompanyInfo> queryDecorativeCreditPageVO(Integer page, Integer size);

    DecorativeCompanyInfo  queryDecorativeCompanyCreditById(Long decorativeCompanyId);

    PageInfo<DecorativeCompanyInfo>  findDecorativeCreditByInfo(Integer page, Integer size,String queryDecorativeCreditInfo);

    PageInfo<DecorativeCompanyInfo> findDecorativeCreditByCondition(Integer page, Integer size, String enabled, Long cityId);

}
