package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyContract;

/**
 * Created by caiyu on 2018/1/30.
 */
public interface DecorativeCompanyContractService {
    /**
     * 根据装饰公司id查找公司合同信息
     * @param companyId
     * @return
     */
    DecorativeCompanyContract findCompanyContractByCompanyId(Long companyId);
}
