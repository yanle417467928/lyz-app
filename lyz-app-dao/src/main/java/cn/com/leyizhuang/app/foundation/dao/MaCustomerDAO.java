package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.PayhelperInfo;
import cn.com.leyizhuang.app.foundation.pojo.RankStore;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.MaCustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.response.MaCreateOrderPeopleResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ManageUpdateCustomerTypeResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.CusRankDO;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerLeBi;
import cn.com.leyizhuang.app.foundation.pojo.user.CustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.user.RankClassification;
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
     * @param storeIds   门店id
     * @return  顾客列表
     */
    List<CustomerDO> findCustomerByCityIdAndStoreId(@Param("list") List<Long> storeIds);

    /**
     * 后台购买产品券条件查询顾客
     *
     * @return
     */
    List<CustomerDO> findCustomerByCityIdAndStoreIdAndCustomerNameAndCustomerPhone(@Param(value = "customerQueryConditions") String customerQueryConditions,
                                                                                   @Param(value = "list") List<Long> storeIds);

    /**
     *  后台购买产品券电话号码查询顾客
     * @param customerQueryConditions
     * @return
     */
    List<CustomerDO> findCustomerByCustomerPhone(@Param(value = "customerQueryConditions") String customerQueryConditions);

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

    ManageUpdateCustomerTypeResponse findCustomerById(@Param("id")Long id);

    /**
     * 查看所有专供类型
     */
    List<RankClassification> findRankAll();

    /**
     * 查询专供门店
     */
    RankStore findRankStoreByStoreId(@Param("storeId")Long cusId);

    /**
     * 修改会员专供类型
     */
    void updateMemberTypeByRankIdAndCusId(@Param("rankId")Long rankId,@Param("cusId")Long cusId);

    /**
     * 新增专供门店信息
     */
    void saveRankStore(RankStore rankStore);

    /**
     * 新增专供会员
     */
    void saveCusRank(CusRankDO cusRankDO);

    /**
     * 查找专供会员
     */
    CusRankDO findCusRankByCusId(@Param("cusId")Long cusId);

    RankClassification findRankClassificationByRankCode(@Param("rankCode")String rankCode);

    void deleteCusRankByCusId(@Param("cusId")Long cusId);

    /**
     * 后台查询下单人
     * @param keywords 关键字
     * @return
     */
    List<MaCreateOrderPeopleResponse> maFindCreatePeople(@Param("keywords")String keywords,@Param("peopleType")String peopleType);

    List<MaCreateOrderPeopleResponse> maFindCreatePeopleByStoreId(@Param("storeId")Long storeId);

    PayhelperInfo findPayhelperInfoByCusId(@Param("cusId")Long cusId);

    void updatePayhelperInfoByCusId(PayhelperInfo payhelperInfo);

    void addPayhelperInfo(PayhelperInfo payhelperInfo);
}
