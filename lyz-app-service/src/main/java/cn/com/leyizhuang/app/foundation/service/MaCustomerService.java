package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.dto.CusPreDepositLogDTO;
import cn.com.leyizhuang.app.foundation.pojo.management.customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerDetailVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MaCustomerService {

    PageInfo<CustomerDO> queryPageVO(Integer page, Integer size);

    CustomerDO queryCustomerVOById(Long cusId);

    PageInfo<CustomerDO> queryCustomerVOByCityId(Integer page, Integer size, Long cityId);

    PageInfo<CustomerDO> queryCustomerVOByStoreId(Integer page, Integer size, Long storeId);

    PageInfo<CustomerDO> queryCustomerVOByGuideId(Integer page, Integer size, Long guideId);

    PageInfo<CustomerDO> queryCustomerVOByPhone(Integer page, Integer size, Long queryCusInfo);

    PageInfo<CustomerDO> queryCustomerVOByName(Integer page, Integer size, String queryCusInfo);

    void saveCustomer(CustomerDetailVO customer);

    Boolean isExistPhoneNumber(Long moblie);

    PageInfo<CustomerPreDepositVO> findAllCusPredeposit(Integer page, Integer size, Long cityId, Long storeId, String keywords);

    CustomerPreDepositVO queryCusPredepositByCusId(Long cusId);

    void  changeCusPredepositByCusId(CusPreDepositLogDTO cusPreDepositLogDTO);

}
