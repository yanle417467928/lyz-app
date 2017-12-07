package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.activity.ActLadderDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * table： act_ladder
 * Created by panjie on 2017/11/22.
 */
@Repository
public interface ActLadderDAO {

    void save(ActLadderDO actBaseDO);

    void update(ActLadderDO actBaseDO);

    List<ActLadderDO> queryList();

    ActLadderDO queryById();
}
