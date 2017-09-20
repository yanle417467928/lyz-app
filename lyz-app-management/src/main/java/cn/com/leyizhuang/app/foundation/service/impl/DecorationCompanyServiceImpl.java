package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.DecorationCompanyDAO;
import cn.com.leyizhuang.app.foundation.pojo.DecorationCompanyDO;
import cn.com.leyizhuang.app.foundation.service.DecorationCompanyService;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import cn.com.leyizhuang.common.foundation.service.impl.BaseServiceImpl;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return null;
    }
}
