package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.BankDAO;
import cn.com.leyizhuang.app.foundation.pojo.management.BankDO;
import cn.com.leyizhuang.app.foundation.service.BankService;
import cn.com.leyizhuang.app.foundation.vo.management.BankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/21
 */
@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankDAO bankDAO;

    @Override
    public List<BankVO> findBankByIsEnable() {
        return this.bankDAO.findBankByIsEnable();
    }

    @Override
    public BankDO findBankById(Long id) {
        return this.bankDAO.findBankById(id);
    }
}
