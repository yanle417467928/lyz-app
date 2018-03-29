package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.MaCustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerLebiVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface MaCustomerDAO {
    List<CustomerDO> findAllVO(@Param("list") List<Long> storeIds);

    CustomerDO queryCustomerVOById(Long cusId);

    List<CustomerDO> queryCustomerVOByCityId(@Param("cityId") Long cityId,@Param("list") List<Long> storeIds);

    List<CustomerDO> queryCustomerVOByStoreId(Long storeId);

    List<CustomerDO> queryCustomerVOByGuideId(Long guideId);

    List<CustomerDO> queryCustomerVOByPhone( @Param("queryCusInfo") Long queryCusInfo,@Param("list") List<Long> storeIds);

    List<CustomerDO> queryCustomerVOByName(@Param("queryCusInfo") String queryCusInfo,@Param("list") List<Long> storeIds);

    void save(CustomerDO customer);

    void updateCustomer(CustomerDO customer);

    Boolean isExistPhoneNumber(Long moblie);

    Boolean isExistPhoneNumberByCusId(@Param(value = "mobile") Long mobile,@Param(value = "cusId") Long cusId);

    List<CustomerPreDepositVO> findAllCusPredeposit(@Param("cityId") Long cityId, @Param("storeId")Long storeId, @Param("keywords")String keywords, @Param("list") List<Long> storeIds);

    CustomerPreDepositVO queryCusPredepositByCusId(Long cusId);

    CustomerPreDeposit findPreDepositByCusId(Long cusId);

    void savePreDeposit(CustomerPreDeposit preDeposit);

    int updateDepositByUserId(@Param("userId") Long userId, @Param("deposit") Double customerDeposit, @Param("lastUpdateTime")Timestamp lastUpdateTime, @Param("oldLastUpdateTime")Timestamp oldLastUpdateTime);

    List<CustomerLebiVO> findAllCusLebi(@Param("cityId") Long cityId, @Param("storeId")Long storeId, @Param("keywords")String keywords, @Param("list") List<Long> storeIds);

    CustomerLebiVO queryCusLebiByCusId(Long cusId);

    void saveLebi(CustomerLeBi customerLeBi);

    int updateLebiByUserId(@Param("userId") Long userId, @Param("quantity") Integer quantity, @Param("lastUpdateTime")Timestamp lastUpdateTime, @Param("oldLastUpdateTime")Timestamp oldLastUpdateTime);

    CustomerLeBi findLebiByCusId(Long cusId);

    /**
     * 后台购买产品券选择顾客
     * @param cityId    城市id
     * @param storeId   门店id
     * @return  顾客列表
     */
    List<CustomerDO> findCustomerByCityIdAndStoreId(@Param("cityId") Long cityId, @Param("storeId")Long storeId);

    /**
     * 后台购买产品券条件查询顾客
     *
     * @return
     */
    List<CustomerDO> findCustomerByCityIdAndStoreIdAndCustomerNameAndCustomerPhone(@Param(value = "customerQueryConditions") String customerQueryConditions,
                                                                               @Param(value = "cityId") Long cityId, @Param(value = "storeId") Long storeId);
    /**
     * 根据灯号返回顾客信息
     * @param light
     * @return
     */
    List<CustomerDO> findCustomerByLightAndStatusTrue(@Param("light") String light);

    /**
     * 更新灯号
     * @param cusIdList
     * @param light
     */
    void updateLight(@Param("cusIdList") List<Long> cusIdList , @Param("light") String light);

    Integer updateDepositByUserIdAndVersion(@Param("userId") Long userId, @Param("deposit") Double customerDeposit,@Param("version")Date version);

    void saveCusPreDepositLog(MaCustomerPreDeposit customerPreDeposit);

    Long findCityIdByCusId(Long cusId);

}
