package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.StPreDepositWithdraw;
import org.apache.ibatis.annotations.Param;

/**
 * Created by panjie on 2018/2/5.
 */
public interface StPreDepositWithdrawDAO {

    int save(StPreDepositWithdraw DO);

    int update(StPreDepositWithdraw DO);

    StPreDepositWithdraw findById(@Param("id") Long id);


}
