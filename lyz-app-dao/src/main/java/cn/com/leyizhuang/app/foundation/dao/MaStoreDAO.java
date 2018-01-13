package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.StorePreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.management.store.SimpleStoreParam;
import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.SimpleDecorativeCompany;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StoreVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
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

    List<StorePreDepositVO> findAllStorePredeposit(@Param("cityId") Long cityId, @Param("keywords")String keywords, @Param("storeType") String storeType);

    StorePreDepositVO queryStorePredepositByStoreId(Long storeId);

    StorePreDeposit findByStoreId(Long storeId);

    void savePreDeposit(StorePreDeposit storePreDeposit);

    int updateDepositByStoreId(@Param("storeId")Long storeId, @Param("money")Double money, @Param("lastUpdateTime")Timestamp lastUpdateTime, @Param("oldUpdateTime")Timestamp oldUpdateTime);
}
