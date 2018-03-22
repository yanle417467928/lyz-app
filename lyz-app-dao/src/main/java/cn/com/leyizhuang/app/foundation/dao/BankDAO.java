package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.BankDO;
import cn.com.leyizhuang.app.foundation.vo.management.BankVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/21
 */
@Repository
public interface BankDAO {

    List<BankVO> findBankByIsEnable();

    BankDO findBankById(Long id);

}
