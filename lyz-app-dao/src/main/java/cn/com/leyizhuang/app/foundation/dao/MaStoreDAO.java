package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.AppStore;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.store.MaStoreInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.SimpleDecorativeCompany;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface MaStoreDAO {

    List<StoreVO> findAllVO();

    List<SimpleStoreParam> findStoresList();

    List<SimpleStoreParam> findAllStorelist();

    List<SimpleStoreParam> findStoresListByCityId(Long cityId);

    List<SimpleStoreParam> findAllStoresListByCityId(Long cityId);

    List<SimpleDecorativeCompany> queryDecorativeCompanyVOList();

    StoreDetailVO findStoresVOById(Long storeId);

    List<StoreVO> queryStoreListByCityId(Long cityId);

    List<StoreVO> findStoresListByCondition(@Param("enabled") String enabled, @Param("cityId") Long cityId);

    List<StoreVO> findStoresListByStoreInfo(String queryStoreInfo);

    void update( @Param("storeId") Long storeId,@Param("isSelfDelivery") Boolean isSelfDelivery);

    List<StoreDO> queryDecorativeCompanyPageVO();

    List<StoreDO> findDecorativeByCondition(@Param("enabled") String enabled, @Param("cityId") Long cityId);

    List<StoreDO> findDecorativeByInfo(String queryDecorativeInfo);

    StoreDO findDecorativeById(Long decorativeCompanyId);

    List<DecorativeCompanyInfo> findDecorativeCreditList();

    DecorativeCompanyInfo findDecorativeCreditById(Long decorativeCompanyId);

    List<DecorativeCompanyInfo> findDecorativeCreditByInfo(String queryDecorativeCreditInfo);

    List<DecorativeCompanyInfo> findDecorativeCreditByCondition(@Param("enabled") String enabled, @Param("cityId") Long cityId);

    /**
     * 获取装饰公司门店列表
     * @return  门店列表
     */
    List<StoreVO> findDecorativeCompanyList();

    /**
     * 根据城市获取装饰公司门店列表
     * @param cityId    城市id
     * @return  门店列表
     */
    List<StoreVO> findCompanyStoresListByCityId(@Param("cityId") Long cityId);

    /**
     * 根据城市获取支持门店自提门店列表
     * @param cityId    城市id
     * @return  门店列表
     */
    List<StoreVO> findSelfDeliveryStoresListByCityId(@Param("cityId") Long cityId);

    /**
     * 获取支持门店自提门店列表
     * @return  门店列表
     */
    List<StoreVO> findSelfDeliveryStoresList();

    List<StorePreDepositVO> findAllStorePredeposit(@Param("cityId") Long cityId, @Param("keywords")String keywords,
                                                   @Param("storeType") String storeType, @Param("list") List<Long> storeIds);

    StorePreDepositVO queryStorePredepositByStoreId(Long storeId);

    StorePreDeposit findByStoreId(Long storeId);

    void savePreDeposit(StorePreDeposit storePreDeposit);

    int updateDepositByStoreId(@Param("storeId")Long storeId, @Param("money")Double money, @Param("lastUpdateTime")Timestamp lastUpdateTime, @Param("oldUpdateTime")Timestamp oldUpdateTime);

    MaStoreInfo findStoreByStoreCode(String code);

    Integer updateStPreDepositByUserIdAndVersion(@Param("money") Double money,@Param("userId")Long userId, @Param("version")Date version);

    void saveStorePreDepositLog(StPreDepositLogDO stPreDepositLogDO);

    List<StoreVO> findAllStoreVO(@Param("cityId") Long cityId, @Param("keywords")String keywords, @Param("storeType") String storeType);

    List<SimpleStoreParam> findStoresListByCityIdAndStoreId(@Param("cityId") Long cityId, @Param("list") List<Long> storeIds);

    List<SimpleStoreParam> findStoresListByStoreId(@Param("list") List<Long> storeIds);

    AppStore findAppStoreByStoreId(Long storeId);

}
