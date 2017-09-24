package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyDO;
import cn.com.leyizhuang.common.foundation.dao.BaseDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/9/19
 */
@Repository
public interface FitmentCompanyDAO extends BaseDAO<FitmentCompanyDO>{

    List<FitmentCompanyDO> queryListByFrozen(Boolean frozen);

}
