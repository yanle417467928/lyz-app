package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.FitmentCompanyDAO;
import cn.com.leyizhuang.app.foundation.dto.FitmentCompanyDTO;
import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyDO;
import cn.com.leyizhuang.app.foundation.service.FitmentCompanyService;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/19
 */
@Service
@Transactional
public class FitmentCompanyServiceImpl extends BaseServiceImpl<FitmentCompanyDO> implements FitmentCompanyService {

    private FitmentCompanyDAO fitmentCompanyDAO;

    public FitmentCompanyServiceImpl(FitmentCompanyDAO fitmentCompanyDAO) {
        super(fitmentCompanyDAO);
        this.fitmentCompanyDAO = fitmentCompanyDAO;
    }

    @Override
    public PageInfo<FitmentCompanyDO> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<FitmentCompanyDO> fitmentCompanyDOList = this.fitmentCompanyDAO.queryList();
        return new PageInfo<>(fitmentCompanyDOList);
    }

    @Override
    public FitmentCompanyDO managerModifyCompany(FitmentCompanyDTO fitmentCompanyDTO) {
        FitmentCompanyDO fitmentCompanyDO = transform(fitmentCompanyDTO);
        fitmentCompanyDO.setModifierInfoByManager(0L);
        this.fitmentCompanyDAO.modify(fitmentCompanyDO);
        return fitmentCompanyDO;
    }

    @Override
    public FitmentCompanyDO managerSaveCompany(FitmentCompanyDTO fitmentCompanyDTO) {
        FitmentCompanyDO fitmentCompanyDO = transform(fitmentCompanyDTO);
        fitmentCompanyDO.setCreatorInfoByManager(0L);
        this.fitmentCompanyDAO.save(fitmentCompanyDO);
        return fitmentCompanyDO;
    }

    @Override
    public List<FitmentCompanyDO> queryListByFrozen(Boolean frozen) {
        if (null == frozen) {
            frozen = true;
        }
        return this.fitmentCompanyDAO.queryListByFrozen(frozen);
    }

    private FitmentCompanyDO transform(FitmentCompanyDTO fitmentCompanyDTO) {
        FitmentCompanyDO fitmentCompanyDO = null;
        if (null != fitmentCompanyDTO) {
            fitmentCompanyDO = new FitmentCompanyDO();
            if (null != fitmentCompanyDTO && fitmentCompanyDTO.getId() != 0) {
                fitmentCompanyDO.setId(fitmentCompanyDTO.getId());
            }
            fitmentCompanyDO.setName(fitmentCompanyDTO.getName());
            fitmentCompanyDO.setCode(fitmentCompanyDTO.getCode());
            fitmentCompanyDO.setAddress(fitmentCompanyDTO.getAddress());
            fitmentCompanyDO.setPhone(fitmentCompanyDTO.getPhone());
            fitmentCompanyDO.setCredit(fitmentCompanyDTO.getCredit());
            fitmentCompanyDO.setPromotionMoney(fitmentCompanyDTO.getPromotionMoney());
            fitmentCompanyDO.setWalletMoney(fitmentCompanyDTO.getWalletMoney());
            fitmentCompanyDO.setFrozen(fitmentCompanyDTO.getFrozen());
        }
        return fitmentCompanyDO;
    }
}
