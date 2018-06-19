package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.dto.StorePreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.SimpleDecorativeCompany;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorativeCompanyDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.FitCreditMoneyChangeLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreVO;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

public interface MaStoreService {

    PageInfo<StoreVO> queryPageVO(Integer page, Integer size,List<Long> storeIds);

    List<SimpleStoreParam> findStoreList(List<Long> storeIds);

    List<SimpleStoreParam> findAllStorelist(List<Long> storeIds);

    List<SimpleStoreParam> findStoresListByCityId(Long cityId,List<Long> storeIds);

    List<SimpleStoreParam> findStoresListByCityIdExcludeStoreId(Long storeId);

    List<SimpleStoreParam> findAllStoresListByCityId(Long cityId);

    StoreDetailVO queryStoreVOById(Long storeId);

    PageInfo<StoreVO> queryStoreListByCityId(Integer page, Integer size, Long cityId,List<Long> storeIds);

    PageInfo<StoreVO> findStoresListByCondition(Integer page, Integer size, String enabled, Long cityId,List<Long> storeIds);

    PageInfo<StoreVO> findStoresListByStoreInfo(Integer page, Integer size,String queryStoreInfo,List<Long> storeIds);

    void update(Long storeId,Boolean  isSelfDelivery );

    PageInfo<StoreDO> queryDecorativeCompanyPageVO(Integer page, Integer size);

    PageInfo<StoreDO> findDecorativeByCondition(Integer page, Integer size, String enabled, Long cityId);

    PageInfo<StoreDO> findDecorativeByInfo(Integer page, Integer size,String queryDecorativeInfo);

    DecorativeCompanyDetailVO queryDecorativeCompanyById(Long decorativeCompanyId);

    List<SimpleDecorativeCompany> findDecorativeCompanyVOList();

    PageInfo<DecorativeCompanyInfo> queryDecorativeCreditPageVO(Integer page, Integer size);

    PageInfo<FitCreditMoneyChangeLogVO> queryDecorativeCreditChangePage(Integer page, Integer size, String keywords, String changeType, Long storeId);

    DecorativeCompanyInfo  queryDecorativeCompanyCreditById(Long decorativeCompanyId);

    PageInfo<DecorativeCompanyInfo>  findDecorativeCreditByInfo(Integer page, Integer size,String queryDecorativeCreditInfo);

    PageInfo<DecorativeCompanyInfo> findDecorativeCreditByCondition(Integer page, Integer size, String enabled, Long cityId);

    /**
     * 获取装饰公司门店列表
     * @return  门店列表
     */
    List<StoreVO> findDecorativeCompanyList(List<Long> storeIds);

    /**
     * 根据城市获取装饰公司门店列表
     * @param cityId    城市id
     * @return  门店列表
     */
    List<StoreVO> findCompanyStoresListByCityId(Long cityId,List<Long> storeIds);

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

    PageInfo<StorePreDepositVO> findAllStorePredeposit(Integer page, Integer size, Long cityId, String keywords, String storeType, List<Long> storeIds);

    StorePreDepositVO queryStorePredepositByStoreId(Long storeId);

    void changeStorePredepositByStoreId(StorePreDepositDTO storePreDepositDTO) throws Exception;

    MaStoreInfo findStoreByStoreCode(String code);

    /**
     * 更新门店预存款
     * @param money
     * @param userId
     * @param version
     * @return
     */
    Integer updateStPreDepositByUserIdAndVersion(Double money,Long userId, Date version);

    /**
     * 生成门店预存款日志
     * @param stPreDepositLogDO
     * @return
     */
    void saveStorePreDepositLog(StPreDepositLogDO stPreDepositLogDO);

    PageInfo<StoreVO> queryPageVO(Integer page, Integer size, Long cityId, String keywords, String storeType);

    List<SimpleStoreParam> findStoresListByCityIdAndStoreId(Long cityId, List<Long> storeIds);

    List<SimpleStoreParam> findStoresListByStoreId(List<Long> storeIds);

    AppStore findAppStoreByStoreId(Long storeId);

    StorePreDeposit findByStoreId(Long storeId);

    List<SimpleStoreParam> findStoresListByStoreIdAndStoreType(List<Long> storeIds, List<StoreType> storeTypes);

    Long findCityIdByStoreId(Long storeId);

    List<SimpleStoreParam> findStoresListByCityIdAndStoreType(Long cityId, String storeType, List<Long> storeIds);

    List<SimpleStoreParam> findStoresListByCityIdAndStoreType(Long cityId, String storeType);

    List<Long> findStoresIdByStructureCode(String structureCode);

    List<Long> findStoresIdByStructureCodeAndStoreType(String structureCode,String storeType);

    List<Long> findFitCompanyIdByStoreId(List<Long> storeIds);

    Boolean exsitStoreInCompany(Long storeId,String companyCode,String storeType);

    List<Long> findFitCompanyIdBySellerId(Long id);

    List<SimpleStoreParam> findStoresListByCompanyCodeAndStoreType(String companyCode,String storeType,List<Long> storeIds);
}
