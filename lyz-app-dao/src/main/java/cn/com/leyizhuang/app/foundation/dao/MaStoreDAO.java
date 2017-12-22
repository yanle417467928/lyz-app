package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.decorativeCompany.DecorativeCompanyInfo;
import cn.com.leyizhuang.app.foundation.pojo.store.StoreDO;
import cn.com.leyizhuang.app.foundation.vo.DecorativeCompanyVO;
import cn.com.leyizhuang.app.foundation.vo.StoreVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaStoreDAO {

    List<StoreVO> findAllVO();

    List<StoreVO> findStoresList();

    List<StoreVO> findStoresListByCityId(Long cityId);

    StoreVO findStoresVOById(Long storeId);

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
    List<StoreVO> findCompanyStoresList();

    /**
     * 根据城市获取装饰公司门店列表
     * @param cityId    城市id
     * @return  门店列表
     */
    List<StoreVO> findCompanyStoresListByCityId(@Param("cityId") Long cityId);
}
