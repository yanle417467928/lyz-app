package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.DecorationCompanyDO;
import cn.com.leyizhuang.common.foundation.service.BaseService;
import com.github.pagehelper.PageInfo;

/**
 * @author GenerationRoad
 * @date 2017/9/19
 */
public interface DecorationCompanyService extends BaseService<DecorationCompanyDO> {

    PageInfo<DecorationCompanyDO> queryPage(Integer page, Integer size);
}
