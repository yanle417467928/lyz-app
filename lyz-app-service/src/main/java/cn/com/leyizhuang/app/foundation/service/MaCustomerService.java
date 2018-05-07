package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.dto.CusLebiDTO;
import cn.com.leyizhuang.app.foundation.dto.CusPreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.RankStore;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.MaCustomerPreDeposit;
import cn.com.leyizhuang.app.foundation.pojo.response.ManageUpdateCustomerTypeResponse;
import cn.com.leyizhuang.app.foundation.pojo.user.CusRankDO;
import cn.com.leyizhuang.app.foundation.pojo.user.RankClassification;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerLebiVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MaCustomerService {

    PageInfo<CustomerDO> queryPageVO(Integer page, Integer size, List<Long> storeIds);

    CustomerDO queryCustomerVOById(Long cusId);

    PageInfo<CustomerDO> queryCustomerVOByCityId(Integer page, Integer size, Long cityId,List<Long> storeIds);

    PageInfo<CustomerDO> queryCustomerVOByStoreId(Integer page, Integer size, Long storeId);

    PageInfo<CustomerDO> queryCustomerVOByGuideId(Integer page, Integer size, Long guideId);

    PageInfo<CustomerDO> queryCustomerVOByPhone(Integer page, Integer size, Long queryCusInfo,List<Long> storeIds);

    PageInfo<CustomerDO> queryCustomerVOByName(Integer page, Integer size, String queryCusInfo,List<Long> storeIds);

    void saveCustomer(CustomerDetailVO customer);

    void updateCustomer(CustomerDetailVO customer);

    Boolean isExistPhoneNumber(Long moblie);

    Boolean isExistPhoneNumberByCusId(Long moblie,Long cusId);

    PageInfo<CustomerPreDepositVO> findAllCusPredeposit(Integer page, Integer size, Long cityId, Long storeId, String keywords, List<Long> storeIds);

    CustomerPreDepositVO queryCusPredepositByCusId(Long cusId);

    void  changeCusPredepositByCusId(CusPreDepositDTO cusPreDepositDTO) throws Exception;

    PageInfo<CustomerLebiVO> findAllCusLebi(Integer page, Integer size, Long cityId, Long storeId, String keywords, List<Long> storeIds);

    CustomerLebiVO queryCusLebiByCusId(Long cusId);

    void  changeCusLebiByCusId(CusLebiDTO cusLebiDTO) throws Exception;

    /**
     * 更新顾客预存款
     * @param userId
     * @param customerDeposit
     * @param version
     * @return
     */
    Integer updateDepositByUserIdAndVersion(Long userId,Double customerDeposit,Date version);

    /**
     * 后台购买产品券选择顾客
     * @param storeIds   门店id
     * @return  顾客列表
     */
    List<CustomerDO> findCustomerByCityIdAndStoreId(List<Long> storeIds);

    /**
     * 后台购买产品券条件查询顾客
     *
     * @return
     */
    List<CustomerDO> findCustomerByCityIdAndStoreIdAndCustomerNameAndCustomerPhone(String customerQueryConditions,List<Long> sotreIds);


    /**
     * 生成顾客预存款变更日志
     * @param customerPreDeposit
     */
    void saveCusPreDepositLog( MaCustomerPreDeposit customerPreDeposit);

    Long findCityIdByCusId(Long cusId);

    ManageUpdateCustomerTypeResponse queryCustomerById(Long id);

    /**
     * 查看所有专供
     */
    List<RankClassification> findRankAll();


    /**
     * 查询专供门店
     */
    Boolean findStoreByCusId(Long cusId);

    /**
     * 修改会员类型
     */
    void updateMemberType(ManageUpdateCustomerTypeResponse manageUpdateCustomerTypeResponse);


}
