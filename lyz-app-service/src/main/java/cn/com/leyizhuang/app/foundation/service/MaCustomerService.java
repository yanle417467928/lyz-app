package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.Customer;
import cn.com.leyizhuang.app.foundation.vo.CustomerVO;
import com.github.pagehelper.PageInfo;

public interface MaCustomerService {

    PageInfo<CustomerVO> queryPageVO(Integer page, Integer size);

    CustomerVO queryCustomerVOById(Long cusId);

    PageInfo<CustomerVO> queryCustomerVOByCityId(Integer page, Integer size, Long cityId);

    PageInfo<CustomerVO> queryCustomerVOByStoreId(Integer page, Integer size, Long storeId);

    PageInfo<CustomerVO> queryCustomerVOByGuideId(Integer page, Integer size, Long guideId);

    PageInfo<CustomerVO> queryCustomerVOByPhone(Integer page, Integer size, Long queryCusInfo);

    PageInfo<CustomerVO> queryCustomerVOByName(Integer page, Integer size, String queryCusInfo);

    void saveCustomer(Customer customer);

    Boolean isExistPhoneNumber(Long moblie);
}
