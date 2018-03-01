package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeCreditMoneyLogResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes: 导购信用金变动记录持久层
 * Created with IntelliJ IDEA.
 * Date: 2017/12/18.
 * Time: 17:56.
 */
@Repository
public interface EmployeeCreditMoneyLogDAO {

    /**
     * 查询导购所有信用额度记录
     *
     * @param userId
     * @return
     */
    @Deprecated
    List<EmployeeCreditMoneyLogResponse> findEmployeeCreditMoneyByUserId(Long userId);
    /**
     * 查询导购临时额度变动记录
     *
     * @param userId
     * @return
     */
    List<EmployeeCreditMoneyLogResponse> findEmpTempCreditByUserId(Long userId);

    /**
     * 查询导购可用额度变动记录
     *
     * @param userId
     * @return
     */
    List<EmployeeCreditMoneyLogResponse> findEmpAvailableCreditByUserId(Long userId);

    /**
     * 查询导购固定额度变动记录
     *
     * @param userId
     * @return
     */
    List<EmployeeCreditMoneyLogResponse> findEmpFixedCreditByUserId(Long userId);

}
