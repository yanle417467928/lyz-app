package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.foundation.dao.ProcessDAO;
import cn.com.leyizhuang.app.foundation.pojo.ProcessDO;
import cn.com.leyizhuang.app.foundation.pojo.response.ProcessGoodsResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.ProcessResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.UserGoodsResponse;
import cn.com.leyizhuang.app.foundation.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caiyu on 2017/9/20.
 */
@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    private ProcessDAO processDAO;

    @Override
    public List<ProcessDO> queryList() {
        return processDAO.queryList();
    }

    @Override
    public ProcessDO selectById(Long id) {
        if (id != null) {
            return processDAO.selectById(id);
        }
        return null;
    }

    @Override
    public ProcessDO selectByProcessName(String processName) {
        if (processName == null && "".equals(processName)) {
            return null;
        }
        return processDAO.selectByProcessName(processName);
    }

    @Override
    public void update(ProcessDO processDO) {
        if (processDO != null) {
            processDAO.update(processDO);
        }
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            processDAO.delete(id);
        }
    }

    @Override
    public void save(ProcessDO processDO) {
        if (processDO != null) {
            processDAO.save(processDO);
        }
    }

    @Override
    public List<ProcessDO> findAllProcessAndGoods() {
        return processDAO.findAllProcessAndGoods();
    }

    @Override
    public void saveProcessAndGoods(Long pID, Long gID) {
        processDAO.saveProcessAndGoods(pID,gID);
    }

    @Override
    public List<ProcessResponse> queryAllList() {
        return this.processDAO.queryAllList();
    }

    @Override
    public List<UserGoodsResponse> queryByProcessIdAndUserId(Long userId, Long processId, Integer identityType) {
        if (identityType == 6) {
            return processDAO.queryByProcessIdAndEmployeeId(userId, processId);
        } else {
            return processDAO.queryByProcessIdAndCustomerId(userId, processId);
        }
    }
}
