package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyContract;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by caiyu on 2018/1/30.
 */
@Repository
public interface DecorativeCompanyContractDAO {
    /**
     * 根据装饰公司id查找公司合同信息
     * @param companyId
     * @return
     */
    DecorativeCompanyContract findCompanyContractByCompanyId(@Param("companyId") Long companyId);
}
