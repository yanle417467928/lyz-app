package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.OperationReasonsDAO;
import cn.com.leyizhuang.app.foundation.pojo.response.OperationReasonsResponse;
import cn.com.leyizhuang.app.foundation.service.OperationReasonsService;
import cn.com.leyizhuang.common.core.constant.OperationReasonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/13
 */
@Service
@Transactional
public class OperationReasonsServiceImpl implements OperationReasonsService {

    @Autowired
    private OperationReasonsDAO cancelReasonsDAO;


    @Override
    public List<OperationReasonsResponse> findAllByType(OperationReasonType type) {
        return this.cancelReasonsDAO.findAllByType(type);
    }
}
