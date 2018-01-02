package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.StoreCreditMoneyLogDAO;
import cn.com.leyizhuang.app.foundation.pojo.StoreCreditMoney;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreCreditMoneyLogResponse;
import cn.com.leyizhuang.app.foundation.service.StoreCreditMoneyLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/27
 */
@Service
@Transactional
public class StoreCreditMoneyLogServiceImpl implements StoreCreditMoneyLogService {

    @Autowired
    private StoreCreditMoneyLogDAO storeCreditMoneyLogDAO;

    @Override
    public PageInfo<StoreCreditMoneyLogResponse> findByUserId(Long userId, Integer page, Integer size) {
        if (userId != null) {
            PageHelper.startPage(page, size);
            List<StoreCreditMoneyLogResponse> logResponseList = this.storeCreditMoneyLogDAO.findByUserId(userId);
            return new PageInfo<>(logResponseList);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StoreCreditMoney findStoreCreditMoneyByUserId(Long userId) {
        return this.storeCreditMoneyLogDAO.findStoreCreditMoneyByUserId(userId);
    }
}
