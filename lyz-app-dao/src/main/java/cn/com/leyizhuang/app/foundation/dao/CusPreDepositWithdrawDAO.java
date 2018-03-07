package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.PreDepositWithdrawStatus;
import cn.com.leyizhuang.app.foundation.pojo.user.CusPreDepositWithdraw;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Created by panjie on 2018/2/5.
 */
@Repository
public interface CusPreDepositWithdrawDAO {

    int save(CusPreDepositWithdraw DO);

    int update(CusPreDepositWithdraw DO);

    CusPreDepositWithdraw findById(@Param("id") Long id);

    CusPreDepositWithdraw findByApplyNo(@Param("applyNo") String applyNo);

    int updateStatus(@Param("applyNo") String applyNo,@Param("status") PreDepositWithdrawStatus status);

    List<CusPreDepositWithdraw> findByKeywords(@Param("keywords") String keywords,@Param("status") String status
                                              ,@Param("startDateTime") String startDateTime,@Param("endDateTime") String endDateTime);

    List<CusPreDepositWithdraw> findByCusId(@Param("cusId") Long cusId,@Param("status") PreDepositWithdrawStatus status);
}
