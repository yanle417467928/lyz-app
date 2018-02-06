package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import org.apache.ibatis.annotations.Param;

/**
 * Created by panjie on 2018/2/5.
 */
public interface CusPreDepositWithdrawDAO {

    int save(CusPreDepositWithdraw DO);

    int update(CusPreDepositWithdraw DO);

    CusPreDepositWithdraw findById(@Param("id") Long id);


}
