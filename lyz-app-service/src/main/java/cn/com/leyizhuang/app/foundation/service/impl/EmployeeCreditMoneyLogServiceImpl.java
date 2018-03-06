package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.EmployeeCreditMoneyLogDAO;
import cn.com.leyizhuang.app.foundation.pojo.response.EmployeeCreditMoneyLogResponse;
import cn.com.leyizhuang.app.foundation.service.EmployeeCreditMoneyLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            List<EmployeeCreditMoneyLogResponse> empAvailableCredit = employeeCreditMoneyLogDAO.findEmpAvailableCreditByUserId(userId);
            for (EmployeeCreditMoneyLogResponse response : empAvailableCredit) {
                response.attributeKindSetByChangeType(response);
            }
            return new PageInfo<>(empAvailableCredit);
        }
        return null;
    }
}
