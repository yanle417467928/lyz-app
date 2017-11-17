package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.ArrearsAuditDAO;
import cn.com.leyizhuang.app.foundation.pojo.response.ArrearsAuditResponse;
import cn.com.leyizhuang.app.foundation.service.ArrearsAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/15
 */
@Service
@Transactional
public class ArrearsAuditServiceImpl implements ArrearsAuditService {

    @Autowired
    private ArrearsAuditDAO arrearsAuditDAO;

    @Override
    public List<ArrearsAuditResponse> findByUserId(Long userId) {
        return this.arrearsAuditDAO.findByUserId(userId);
    }
}
