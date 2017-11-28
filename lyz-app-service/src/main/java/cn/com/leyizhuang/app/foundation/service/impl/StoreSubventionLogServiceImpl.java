package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.StoreSubventionLogDAO;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreSubventionLogResponse;
import cn.com.leyizhuang.app.foundation.service.StoreSubventionLogService;
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
public class StoreSubventionLogServiceImpl implements StoreSubventionLogService {

    @Autowired
    private StoreSubventionLogDAO storeSubventionLogDAO;

    @Override
    public List<StoreSubventionLogResponse> findByUserId(Long userId) {
        return this.storeSubventionLogDAO.findByUserId(userId);
    }
}
