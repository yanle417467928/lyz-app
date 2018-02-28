package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.CusPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CusPreDepositLogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/10
 */
@Repository
public interface MaCusPreDepositLogDAO {

    void save(CusPreDepositLogDO cusPreDepositLogDO);

    List<CusPreDepositLogVO> findAllCusPredepositLog(@Param("cusId") Long cusId, @Param("cityId")Long cityId, @Param("storeId")Long storeId, @Param("keywords")String keywords);

    CusPreDepositLogVO findCusPredepositLogById(Long id);

}
