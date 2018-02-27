package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.EmployeeCreditMoneyLogDAO;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeCreditMoneyLogResponse;
import cn.com.leyizhuang.app.foundation.service.EmployeeCreditMoneyLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2017/12/18.
 * Time: 17:50.
 */
@Service
public class EmployeeCreditMoneyLogServiceImpl implements EmployeeCreditMoneyLogService {

    @Autowired
    private EmployeeCreditMoneyLogDAO employeeCreditMoneyLogDAO;

    @Override
    public PageInfo<EmployeeCreditMoneyLogResponse> findAllEmployeeCreditMoneyLogByUserId(Long userId, Integer page, Integer size) {
        if (userId != null) {
            PageHelper.startPage(page, size);
            List<EmployeeCreditMoneyLogResponse> logResponses = new ArrayList<>();
            List<EmployeeCreditMoneyLogResponse> empAvailableCredit = employeeCreditMoneyLogDAO.findEmpAvailableCreditByUserId(userId);
            List<EmployeeCreditMoneyLogResponse> empFixedCredit = employeeCreditMoneyLogDAO.findEmpFixedCreditByUserId(userId);
            List<EmployeeCreditMoneyLogResponse> empTempCredit = employeeCreditMoneyLogDAO.findEmpTempCreditByUserId(userId);
            logResponses.addAll(empAvailableCredit);
            logResponses.addAll(empFixedCredit);
            logResponses.addAll(empTempCredit);
            for (EmployeeCreditMoneyLogResponse response : logResponses) {
                response.attributeKindSetByChangeType(response, response.getChangeType());
            }
            //按时间排序
            logResponses.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
            return new PageInfo<>(logResponses);
        }
        return null;
    }
}
