package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.config.shiro.ShiroUser;
import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.MaCusLebiLogDAO;
import cn.com.leyizhuang.app.foundation.dto.CusLebiDTO;
import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBiVariationLog;
import cn.com.leyizhuang.app.foundation.service.MaCusLebiLogService;
import cn.com.leyizhuang.app.foundation.service.MaCustomerService;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CusLebiLogVO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CustomerLebiVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/13
 */
@Service
@Transactional
public class MaCusLebiLogServiceImpl implements MaCusLebiLogService {

    @Autowired
    private MaCusLebiLogDAO maCusLebiLogDAO;

    @Autowired
    private MaCustomerService maCustomerService;

    @Override
    public PageInfo<CusLebiLogVO> findAllCusLebiLog(Integer page, Integer size, Long cusId, Long cityId, Long storeId, String keywords, List<Long> storeIds) {
        PageHelper.startPage(page, size);
        List<CusLebiLogVO> list = this.maCusLebiLogDAO.findAllCusLebiLog(cusId, cityId, storeId, keywords,storeIds);
        return new PageInfo<>(list);
    }

    @Override
    public CusLebiLogVO findCusLebiLogById(Long id) {
        return this.maCusLebiLogDAO.findCusLebiLogById(id);
    }

    @Override
    public void save(CusLebiDTO cusLebiDTO) {
        CustomerLeBiVariationLog log = this.transform(cusLebiDTO);
        this.maCusLebiLogDAO.save(log);
    }

    public CustomerLeBiVariationLog transform(CusLebiDTO cusLebiDTO) {
        if (null != cusLebiDTO) {
            CustomerLeBiVariationLog log = new CustomerLeBiVariationLog();
            log.setCusId(cusLebiDTO.getCusId());
            log.setVariationTime(new Date());
            log.setLeBiVariationType(cusLebiDTO.getChangeType());
            log.setVariationTypeDesc(cusLebiDTO.getChangeType().getDescription());
            log.setVariationQuantity(cusLebiDTO.getChangeNum());
            CustomerLebiVO customerLebiVO = maCustomerService.queryCusLebiByCusId(log.getCusId());
            log.setAfterVariationQuantity(customerLebiVO.getQuantity());
            log.setRemarks(cusLebiDTO.getRemarks());
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            log.setOperatorId(shiroUser.getId());
            log.setOperatorIp("");
            log.setOperatorType(AppIdentityType.ADMINISTRATOR);
            return log;
        } else {
            return null;
        }
    }
}
