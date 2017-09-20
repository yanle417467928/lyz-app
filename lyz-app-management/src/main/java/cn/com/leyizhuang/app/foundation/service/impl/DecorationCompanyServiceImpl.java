package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.DecorationCompanyDAO;
import cn.com.leyizhuang.app.foundation.pojo.DecorationCompanyDO;
import cn.com.leyizhuang.app.foundation.pojo.dto.DecorationCompanyDTO;
import cn.com.leyizhuang.app.foundation.service.DecorationCompanyService;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
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
public class DecorationCompanyServiceImpl extends BaseServiceImpl<DecorationCompanyDO> implements DecorationCompanyService {

    private DecorationCompanyDAO decorationCompanyDAO;

    public DecorationCompanyServiceImpl(DecorationCompanyDAO decorationCompanyDAO) {
        super(decorationCompanyDAO);
        this.decorationCompanyDAO = decorationCompanyDAO;
    }

    @Override
    public PageInfo<DecorationCompanyDO> queryPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<DecorationCompanyDO> decorationCompanyDOList = this.decorationCompanyDAO.queryList();
        return new PageInfo<>(decorationCompanyDOList);
    }

    @Override
    public DecorationCompanyDO managerModifyCompany(DecorationCompanyDTO decorationCompanyDTO) {
        DecorationCompanyDO decorationCompanyDO = transform(decorationCompanyDTO);
        decorationCompanyDO.setModifierInfoByManager(0L);
        this.decorationCompanyDAO.modify(decorationCompanyDO);
        return decorationCompanyDO;
    }

    @Override
    public DecorationCompanyDO managerSaveCompany(DecorationCompanyDTO decorationCompanyDTO) {
        DecorationCompanyDO decorationCompanyDO = transform(decorationCompanyDTO);
        decorationCompanyDO.setCreatorInfoByManager(0L);
        this.decorationCompanyDAO.save(decorationCompanyDO);
        return decorationCompanyDO;
    }

    private DecorationCompanyDO transform(DecorationCompanyDTO decorationCompanyDTO){
        DecorationCompanyDO decorationCompanyDO = null;
        if (null != decorationCompanyDTO) {
            decorationCompanyDO = new DecorationCompanyDO();
            if (null != decorationCompanyDTO && decorationCompanyDTO.getId() != 0){
                decorationCompanyDO.setId(decorationCompanyDTO.getId());
            }
            decorationCompanyDO.setName(decorationCompanyDTO.getName());
            decorationCompanyDO.setCode(decorationCompanyDTO.getCode());
            decorationCompanyDO.setAddress(decorationCompanyDTO.getAddress());
            decorationCompanyDO.setPhone(decorationCompanyDTO.getPhone());
            decorationCompanyDO.setCredit(decorationCompanyDTO.getCredit());
            decorationCompanyDO.setPromotionMoney(decorationCompanyDTO.getPromotionMoney());
            decorationCompanyDO.setWalletMoney(decorationCompanyDTO.getWalletMoney());
            decorationCompanyDO.setFrozen(decorationCompanyDTO.getFrozen());
        }
        return decorationCompanyDO;
    }
}
