package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.Customer;
import cn.com.leyizhuang.app.foundation.vo.CustomerVO;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MaCustomerDAO {
    List<CustomerVO> findAllVO();

    CustomerVO queryCustomerVOById(Long cusId);

    List<CustomerVO> queryCustomerVOByCityId(Long cityId);

    List<CustomerVO> queryCustomerVOByStoreId(Long storeId);

    List<CustomerVO> queryCustomerVOByGuideId(Long guideId);

    List<CustomerVO> queryCustomerVOByPhone(Long queryCusInfo);

    List<CustomerVO> queryCustomerVOByName(String queryCusInfo);

    void save(Customer customer);

    Boolean isExistPhoneNumber(Long moblie);
}
