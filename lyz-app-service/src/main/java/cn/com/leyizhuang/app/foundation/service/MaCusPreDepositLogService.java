package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.dto.CusPreDepositDTO;
import cn.com.leyizhuang.app.foundation.pojo.CusPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CusPreDepositLogVO;
import com.github.pagehelper.PageInfo;

/**
 * @author GenerationRoad
 * @date 2018/1/10
 */
public interface MaCusPreDepositLogService {

    CusPreDepositLogDO save(CusPreDepositDTO cusPreDepositDTO);

    PageInfo<CusPreDepositLogVO> findAllCusPredepositLog(Integer page, Integer size, Long cusId, Long cityId, Long storeId, String keywords);

    CusPreDepositLogVO findCusPredepositLogById(Long id);
}
