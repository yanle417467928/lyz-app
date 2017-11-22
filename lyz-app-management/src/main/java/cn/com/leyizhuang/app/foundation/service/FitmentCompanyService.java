package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.dto.FitmentCompanyDTO;
import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyDO;
import cn.com.leyizhuang.common.foundation.service.BaseService;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/19
 */
public interface FitmentCompanyService extends BaseService<FitmentCompanyDO> {

    PageInfo<FitmentCompanyDO> queryPage(Integer page, Integer size);

    FitmentCompanyDO managerModifyCompany(FitmentCompanyDTO fitmentCompanyDTO);

    FitmentCompanyDO managerSaveCompany(FitmentCompanyDTO fitmentCompanyDTO);

    List<FitmentCompanyDO> queryListByFrozen(Boolean frozen);
}
