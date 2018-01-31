package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.DecorativeCompanyContractDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyContract;
import cn.com.leyizhuang.app.foundation.service.DecorativeCompanyContractService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by caiyu on 2018/1/30.
 */
@Service
public class DecorativeCompanyContractServiceImpl implements DecorativeCompanyContractService {
    @Resource
    private DecorativeCompanyContractDAO decorativeCompanyContractDAO;

    @Override
    public DecorativeCompanyContract findCompanyContractByCompanyId(Long companyId) {
        return decorativeCompanyContractDAO.findCompanyContractByCompanyId(companyId);
    }
}
