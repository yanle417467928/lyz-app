package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.management.BankDO;
import cn.com.leyizhuang.app.foundation.vo.management.BankVO;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/21
 */
public interface BankService {

    List<BankVO> findBankByIsEnable();

    BankDO findBankById(Long id);

}
