package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.foundation.dao.MaCusPreDepositLogDAO;
import cn.com.leyizhuang.app.foundation.dto.CusPreDepositLogDTO;
import cn.com.leyizhuang.app.foundation.pojo.CusPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.pojo.management.employee.EmployeeDO;
import cn.com.leyizhuang.app.foundation.service.MaCusPreDepositLogService;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.service.MaEmployeeService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CusPreDepositLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
import cn.com.leyizhuang.common.util.CountUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        cusPreDepositLogDO.setBalance(Double.parseDouble(customerPreDepositVO.getBalance()));

        this.maCusPreDepositLogDAO.save(cusPreDepositLogDO);
        return cusPreDepositLogDO;
    }

    @Override
    public PageInfo<CusPreDepositLogVO> findAllCusPredepositLog(Integer page, Integer size, Long cusId, Long cityId, Long storeId, String keywords) {
        PageHelper.startPage(page, size);
        List<CusPreDepositLogVO> list = this.maCusPreDepositLogDAO.findAllCusPredepositLog(cusId, cityId, storeId, keywords);
        return new PageInfo<>(list);
    }

    @Override
    public CusPreDepositLogVO findCusPredepositLogById(Long id) {
        return this.maCusPreDepositLogDAO.findCusPredepositLogById(id);
    }

    public CusPreDepositLogDO transform(CusPreDepositLogDTO cusPreDepositLogDTO) {
        if (null != cusPreDepositLogDTO){
            CusPreDepositLogDO cusPreDepositLogDO = new CusPreDepositLogDO();
            cusPreDepositLogDO.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), cusPreDepositLogDTO.getChangeMoney(), cusPreDepositLogDTO.getChangeType());
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
//            EmployeeDO employeeDO = this.maEmployeeService.findEmployeeDOByEmpId(shiroUser.getId());
            cusPreDepositLogDO.setUserIdAndOperatorinfo(cusPreDepositLogDTO.getCusId(), shiroUser.getId(), null, "");
            cusPreDepositLogDO.setMerchantOrderNumber(cusPreDepositLogDTO.getMerchantOrderNumber());
            cusPreDepositLogDO.setRemarks(cusPreDepositLogDTO.getRemarks());
            cusPreDepositLogDO.setTransferTime(LocalDateTime.parse(cusPreDepositLogDTO.getTransferTime() + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            cusPreDepositLogDO.setChangeTypeDesc(cusPreDepositLogDTO.getChangeType().getDescription());
            return cusPreDepositLogDO;
        } else {
            return null;
        }
    }
}
