package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.StoreSubventionLogDAO;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreSubventionLogResponse;
import cn.com.leyizhuang.app.foundation.service.StoreSubventionLogService;
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
public class StoreSubventionLogServiceImpl implements StoreSubventionLogService {

    @Autowired
    private StoreSubventionLogDAO storeSubventionLogDAO;

    @Override
    public PageInfo<StoreSubventionLogResponse> findByUserId(Long userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<StoreSubventionLogResponse> storeSubventionLogResponseList =this.storeSubventionLogDAO.findByUserId(userId);
        return new PageInfo<>(storeSubventionLogResponseList);
    }
}
