package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerLebiVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MaCustomerDAO {
    List<CustomerDO> findAllVO();

    CustomerDO queryCustomerVOById(Long cusId);

    List<CustomerDO> queryCustomerVOByCityId(Long cityId);

    List<CustomerDO> queryCustomerVOByStoreId(Long storeId);

    List<CustomerDO> queryCustomerVOByGuideId(Long guideId);

    List<CustomerDO> queryCustomerVOByPhone(Long queryCusInfo);

    List<CustomerDO> queryCustomerVOByName(String queryCusInfo);

    void save(CustomerDO customer);

    Boolean isExistPhoneNumber(Long moblie);

    List<CustomerPreDepositVO> findAllCusPredeposit(@Param("cityId") Long cityId, @Param("storeId")Long storeId, @Param("keywords")String keywords);

    CustomerPreDepositVO queryCusPredepositByCusId(Long cusId);

    CustomerPreDeposit findPredepositByCusId(Long cusId);

    void savePreDeposit(CustomerPreDeposit preDeposit);

    int updateDepositByUserId(@Param("userId") Long userId, @Param("deposit") Double customerDeposit, @Param("lastUpdateTime")Timestamp lastUpdateTime, @Param("oldLastUpdateTime")Timestamp oldLastUpdateTime);

    List<CustomerLebiVO> findAllCusLebi(@Param("cityId") Long cityId, @Param("storeId")Long storeId, @Param("keywords")String keywords);

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

}
