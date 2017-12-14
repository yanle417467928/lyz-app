package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.Customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.management.Customer;
import cn.com.leyizhuang.app.foundation.vo.CustomerVO;
import com.github.pagehelper.PageInfo;

public interface MaCustomerService {

    PageInfo<CustomerDO> queryPageVO(Integer page, Integer size);

    CustomerDO queryCustomerVOById(Long cusId);

    PageInfo<CustomerDO> queryCustomerVOByCityId(Integer page, Integer size, Long cityId);

    PageInfo<CustomerDO> queryCustomerVOByStoreId(Integer page, Integer size, Long storeId);

    PageInfo<CustomerDO> queryCustomerVOByGuideId(Integer page, Integer size, Long guideId);

    PageInfo<CustomerDO> queryCustomerVOByPhone(Integer page, Integer size, Long queryCusInfo);

    PageInfo<CustomerDO> queryCustomerVOByName(Integer page, Integer size, String queryCusInfo);

    void saveCustomer(CustomerVO customer);

    Boolean isExistPhoneNumber(Long moblie);
}
