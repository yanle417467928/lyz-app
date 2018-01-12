package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.dto.CusPreDepositLogDTO;
import cn.com.leyizhuang.app.foundation.pojo.CusPreDepositLogDO;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CusPreDepositLogVO;
import com.github.pagehelper.PageInfo;

/**
 * @author GenerationRoad
 * @date 2018/1/10
 */
public interface MaCusPreDepositLogService {

    CusPreDepositLogDO save(CusPreDepositLogDTO cusPreDepositLogDTO);

    PageInfo<CusPreDepositLogVO> findAllCusPredepositLog(Integer page, Integer size, Long cusId, Long cityId, Long storeId, String keywords);

    CusPreDepositLogVO findCusPredepositLogById(Long id);
}
