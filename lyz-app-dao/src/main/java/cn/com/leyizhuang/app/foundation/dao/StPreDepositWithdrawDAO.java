package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.PreDepositWithdrawStatus;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositWithdraw;
import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by panjie on 2018/2/5.
 */
@Repository
public interface StPreDepositWithdrawDAO {

    int save(StPreDepositWithdraw DO);

    int update(StPreDepositWithdraw DO);

    StPreDepositWithdraw findById(@Param("id") Long id);

    StPreDepositWithdraw findByApplyNo(@Param("applyNo") String applyNo);

    int updateStatus(@Param("applyNo") String applyNo,@Param("status") PreDepositWithdrawStatus status);

    List<StPreDepositWithdraw> findByKeywords(@Param("keywords") String keywords,@Param("status") String status
                                            ,@Param("startDateTime") String startDateTime,@Param("endDateTime") String endDateTime);

    List<StPreDepositWithdraw> findByStId(@Param("stId") Long stId,@Param("status") PreDepositWithdrawStatus status);
}
