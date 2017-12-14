package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.Customer.CustomerDO;
import cn.com.leyizhuang.app.foundation.pojo.management.Customer;
import cn.com.leyizhuang.app.foundation.vo.CustomerVO;
import org.springframework.stereotype.Repository;

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
}
