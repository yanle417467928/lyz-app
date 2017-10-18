package cn.com.leyizhuang.app.foundation.service;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/10/17
 */
public interface MaterialListService {
    void batchSave(Long userId, Integer identityType, String[] param);

    void modifyQty(Long id, Integer qty);

    void deleteMaterialList(List<Long> ids);

}
