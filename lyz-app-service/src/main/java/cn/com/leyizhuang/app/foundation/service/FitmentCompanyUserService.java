package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.dto.FitmentCompanyUserDTO;
import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyUserDO;
import cn.com.leyizhuang.common.foundation.service.BaseService;
import com.github.pagehelper.PageInfo;

/**
 * @author GenerationRoad
 * @date 2017/9/20
 */
public interface FitmentCompanyUserService extends BaseService<FitmentCompanyUserDO> {
    PageInfo<FitmentCompanyUserDO> queryPage(Integer page, Integer size);

    FitmentCompanyUserDO managerModifyCompanyUser(FitmentCompanyUserDTO fitmentCompanyUserDTO);

    FitmentCompanyUserDO managerSaveCompanyUser(FitmentCompanyUserDTO fitmentCompanyUserDTO);
}
