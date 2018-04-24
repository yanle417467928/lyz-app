package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.MaCusPreDepositLogDAO;
import cn.com.leyizhuang.app.foundation.dto.CusPreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.CusPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.service.MaCusPreDepositLogService;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CusPreDepositLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerPreDepositVO;
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
    private MaCustomerService maCustomerService;

    @Override
    public CusPreDepositLogDO save(CusPreDepositDTO cusPreDepositDTO) {
        CusPreDepositLogDO cusPreDepositLogDO = this.transform(cusPreDepositDTO);
        this.maCusPreDepositLogDAO.save(cusPreDepositLogDO);
        return cusPreDepositLogDO;
    }

    @Override
    public PageInfo<CusPreDepositLogVO> findAllCusPredepositLog(Integer page, Integer size, Long cusId, Long cityId, Long storeId, String keywords, List<Long> storeIds, String changeType) {
        PageHelper.startPage(page, size);
        List<CusPreDepositLogVO> list = this.maCusPreDepositLogDAO.findAllCusPredepositLog(cusId, cityId, storeId, keywords, storeIds, changeType);
        return new PageInfo<>(list);
    }

    @Override
    public CusPreDepositLogVO findCusPredepositLogById(Long id) {
        return this.maCusPreDepositLogDAO.findCusPredepositLogById(id);
    }

    public CusPreDepositLogDO transform(CusPreDepositDTO cusPreDepositDTO) {
        if (null != cusPreDepositDTO){
            CusPreDepositLogDO cusPreDepositLogDO = new CusPreDepositLogDO();
            cusPreDepositLogDO.setCreateTimeAndChangeMoneyAndType(LocalDateTime.now(), cusPreDepositDTO.getChangeMoney(), cusPreDepositDTO.getChangeType());
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            cusPreDepositLogDO.setUserIdAndOperatorinfo(cusPreDepositDTO.getCusId(), shiroUser.getId(), AppIdentityType.ADMINISTRATOR, "");
            cusPreDepositLogDO.setMerchantOrderNumber(cusPreDepositDTO.getMerchantOrderNumber());
            cusPreDepositLogDO.setRemarks(cusPreDepositDTO.getRemarks());
            if (null != cusPreDepositDTO.getTransferTime() && !("".equals(cusPreDepositDTO.getTransferTime()))) {
                cusPreDepositLogDO.setTransferTime(LocalDateTime.parse(cusPreDepositDTO.getTransferTime() + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                cusPreDepositLogDO.setTransferTime(LocalDateTime.now());
            }
            cusPreDepositLogDO.setChangeTypeDesc(cusPreDepositDTO.getChangeType().getDescription());
            CustomerPreDepositVO customerPreDepositVO = this.maCustomerService.queryCusPredepositByCusId(cusPreDepositLogDO.getCusId());
            cusPreDepositLogDO.setBalance(Double.parseDouble(customerPreDepositVO.getBalance()));
            return cusPreDepositLogDO;
        } else {
            return null;
        }
    }
}
