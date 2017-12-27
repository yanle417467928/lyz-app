package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.activity.ActAddSaleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by panjie on 2017/12/26.
 */
public interface ActAddSaleDAO {

    void save(ActAddSaleDO DO);

    void update(ActAddSaleDO DO);

    List<ActAddSaleDO> queryList();

    List<ActAddSaleDO> queryByActId(@Param("actBaseId") Long actBaseId);

    ActAddSaleDO queryById();

    void deleteByActBaseId(@Param("actBaseId") Long actBaseId);
}
