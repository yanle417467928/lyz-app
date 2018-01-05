package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.MaDecorativeCompanyCreditDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanyCredit;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorativeCompanySubvention;
import cn.com.leyizhuang.app.foundation.service.MaDecorativeCompanyCreditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
@Service
public class MaDecorativeCompanyCreditServiceImpl implements MaDecorativeCompanyCreditService {
    @Resource
    private MaDecorativeCompanyCreditDAO maDecorativeCompanyCreditDAO;

    @Override
    public void  updateDecorativeCompanyCredit(DecorativeCompanyCredit decorativeCompanyCredit){
        maDecorativeCompanyCreditDAO.updateDecorativeCompanyCredit(decorativeCompanyCredit);
    }

    @Override
    public void  updateDecorativeCompanySubvention(DecorativeCompanySubvention decorativeCompanySubvention){
        maDecorativeCompanyCreditDAO.updateDecorativeCompanySubvention(decorativeCompanySubvention);
    }
}
