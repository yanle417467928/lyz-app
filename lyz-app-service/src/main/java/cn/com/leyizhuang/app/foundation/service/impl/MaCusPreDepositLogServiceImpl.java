package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.foundation.dao.MaCusPreDepositLogDAO;
import cn.com.leyizhuang.app.foundation.dto.CusPreDepositLogDTO;
import cn.com.leyizhuang.app.foundation.pojo.CusPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.service.MaCusPreDepositLogService;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.service.MaEmployeeService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import cn.com.leyizhuang.common.util.CountUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author GenerationRoad
 * @date 2018/1/10
 */
@Service
@Transactional
public class MaCusPreDepositLogServiceImpl implements MaCusPreDepositLogService {

    @Autowired
    private MaCusPreDepositLogDAO maCusPreDepositLogDAO;

    @Autowired
    private MaEmployeeService maEmployeeService;

    @Autowired
    private MaCustomerService maCustomerService;

    @Override
    public CusPreDepositLogDO save(CusPreDepositLogDTO cusPreDepositLogDTO) {
        CusPreDepositLogDO cusPreDepositLogDO = this.transform(cusPreDepositLogDTO);
        CustomerPreDepositVO customerPreDepositVO = this.maCustomerService.queryCusPredepositByCusId(cusPreDepositLogDO.getCusId());
        cusPreDepositLogDO.setBalance(CountUtil.add(Double.parseDouble(customerPreDepositVO.getBalance()), cusPreDepositLogDO.getChangeMoney()));

        this.maCusPreDepositLogDAO.save(cusPreDepositLogDO);
        return cusPreDepositLogDO;
    }

    public CusPreDepositLogDO transform(CusPreDepositLogDTO cusPreDepositLogDTO) {
        if (null != cusPreDepositLogDTO){
            CusPreDepositLogDO cusPreDepositLogDO = new CusPreDepositLogDO();
            cusPreDepositLogDO.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), cusPreDepositLogDTO.getChangeMoney(), cusPreDepositLogDTO.getChangeType());
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            EmployeeDO employeeDO = this.maEmployeeService.findEmployeeDOByEmpId(shiroUser.getId());
            cusPreDepositLogDO.setUserIdAndOperatorinfo(cusPreDepositLogDTO.getCusId(), employeeDO.getEmpId(), employeeDO.getIdentityType(), "");
            cusPreDepositLogDO.setMerchantOrderNumber(cusPreDepositLogDTO.getMerchantOrderNumber());
            cusPreDepositLogDO.setRemarks(cusPreDepositLogDTO.getRemarks());
            return cusPreDepositLogDO;
        } else {
            return null;
        }
    }
}
