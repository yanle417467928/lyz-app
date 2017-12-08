package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.activity.ActSubAmountDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * table: act_sub_amount
 * Created by panjie on 2017/11/22.
 */
@Repository
public interface ActSubAmountDAO {
    void save(ActSubAmountDO DO);

    void update(ActSubAmountDO DO);

    List<ActSubAmountDO> queryList();

    ActSubAmountDO queryById();

    ActSubAmountDO queryByActId(@Param("actId") Long actId);
}
