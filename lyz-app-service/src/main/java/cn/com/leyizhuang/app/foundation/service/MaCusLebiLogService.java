package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.dto.CusLebiDTO;
import cn.com.leyizhuang.app.foundation.pojo.CustomerLeBiVariationLog;
import cn.com.leyizhuang.app.foundation.vo.management.customer.CusLebiLogVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/1/13
 */
public interface MaCusLebiLogService {

    PageInfo<CusLebiLogVO> findAllCusLebiLog(Integer page, Integer size, Long cusId, Long cityId, Long storeId, String keywords, List<Long> storeIds);

    CusLebiLogVO findCusLebiLogById(Long id);

    void save(CusLebiDTO cusLebiDTO);
}
