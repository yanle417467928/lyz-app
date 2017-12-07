package cn.com.leyizhuang.app.foundation.dao;


import cn.com.leyizhuang.app.foundation.pojo.activity.ActBaseDO;
import cn.com.leyizhuang.app.foundation.pojo.activity.ActGiftDetailsDO;
import org.springframework.stereotype.Repository;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * table: act_gift_details
 * Created by panjie on 2017/11/22.
 */
@Repository
public interface ActGiftDetailsDAO {

    void save(ActGiftDetailsDO DO);

    void update(ActGiftDetailsDO DO);

    List<ActGiftDetailsDO> queryList();

    ActGiftDetailsDO queryById(@PathParam("id") Long id);

    List<ActGiftDetailsDO> queryByActId(@PathParam("ActId") Long ActId);
}
