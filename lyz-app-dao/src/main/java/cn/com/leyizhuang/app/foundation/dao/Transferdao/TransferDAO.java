package cn.com.leyizhuang.app.foundation.dao.Transferdao;


import cn.com.leyizhuang.app.foundation.datatransferPojo.TdOrderGoods;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by panjie on 2018/3/24.
 */
@Repository
public interface TransferDAO {

    List<TdOrderGoods> getTdOrderGoods();

}
