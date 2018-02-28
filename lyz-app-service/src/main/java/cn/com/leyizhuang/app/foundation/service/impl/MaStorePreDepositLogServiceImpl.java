package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.MaStorePreDepositLogDAO;
import cn.com.leyizhuang.app.foundation.dto.StorePreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.StPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.service.MaStorePreDepositLogService;
import cn.com.leyizhuang.app.foundation.service.MaStoreService;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.store.StorePreDepositVO;
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
 * @date 2018/1/12
 */
@Service
@Transactional
public class MaStorePreDepositLogServiceImpl implements MaStorePreDepositLogService {

    @Autowired
    private MaStoreService maStoreServiceImpl;

    @Autowired
    private MaStorePreDepositLogDAO maStorePreDepositLogDAO;

    @Override
    public StPreDepositLogDO save(StorePreDepositDTO storePreDepositDTO) {
        StPreDepositLogDO stPreDepositLogDO = this.transform(storePreDepositDTO);
        StorePreDepositVO storePreDepositVO = this.maStoreServiceImpl.queryStorePredepositByStoreId(stPreDepositLogDO.getStoreId());
        stPreDepositLogDO.setBalance(Double.parseDouble(storePreDepositVO.getBalance()));

        this.maStorePreDepositLogDAO.save(stPreDepositLogDO);
        return stPreDepositLogDO;
    }

    @Override
    public PageInfo<StorePreDepositLogVO> findAllStorePredepositLog(Integer page, Integer size, Long storeId, Long cityId, String storeType, String keywords, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<StorePreDepositLogVO> list = this.maStorePreDepositLogDAO.findAllStorePredepositLog(storeId, cityId, storeType, keywords, storeIds);
        return new PageInfo<>(list);
    }

    @Override
    public StorePreDepositLogVO findStorePredepositLogById(Long id) {
        return this.maStorePreDepositLogDAO.findStorePredepositLogById(id);
    }


    public StPreDepositLogDO transform(StorePreDepositDTO storePreDepositDTO) {
        if (null != storePreDepositDTO){
            StPreDepositLogDO stPreDepositLogDO = new StPreDepositLogDO();
            stPreDepositLogDO.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), storePreDepositDTO.getChangeMoney(), storePreDepositDTO.getChangeType());
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
//            EmployeeDO employeeDO = this.maEmployeeService.findEmployeeDOByEmpId(shiroUser.getId());
            stPreDepositLogDO.setUserIdAndOperatorinfo(storePreDepositDTO.getStoreId(), shiroUser.getId(), AppIdentityType.ADMINISTRATOR, "");
            stPreDepositLogDO.setMerchantOrderNumber(storePreDepositDTO.getMerchantOrderNumber());
            stPreDepositLogDO.setRemarks(storePreDepositDTO.getRemarks());
            if (null != storePreDepositDTO.getTransferTime() && !("".equals(storePreDepositDTO.getTransferTime()))) {
                stPreDepositLogDO.setTransferTime(LocalDateTime.parse(storePreDepositDTO.getTransferTime() + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                stPreDepositLogDO.setTransferTime(LocalDateTime.now());
            }
            stPreDepositLogDO.setChangeTypeDesc(storePreDepositDTO.getChangeType().getDescription());
            return stPreDepositLogDO;
        } else {
            return null;
        }
    }
}
