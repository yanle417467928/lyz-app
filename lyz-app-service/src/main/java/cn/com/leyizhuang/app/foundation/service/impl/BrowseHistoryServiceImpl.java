package cn.com.leyizhuang.app.foundation.service.impl;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.dao.BrowseHistoryDAO;
import cn.com.leyizhuang.app.foundation.pojo.request.BrowseHistoryRequest;
import cn.com.leyizhuang.app.foundation.service.BrowseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/2
 */
@Service
public class BrowseHistoryServiceImpl implements BrowseHistoryService {

    @Autowired
    private BrowseHistoryDAO browseHistoryDAO;

    @Override
    public BrowseHistoryRequest save(BrowseHistoryRequest browseHistory) {
        this.browseHistoryDAO.save(browseHistory);
        return browseHistory;
    }

    @Override
    public void delete(Long userId, AppIdentityType identityType) {
        this.browseHistoryDAO.delete(userId, identityType);
    }

    @Override
    public List<Long> existBrowseHistory(BrowseHistoryRequest browseHistory) {
        return this.browseHistoryDAO.existBrowseHistory(browseHistory);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        this.browseHistoryDAO.deleteByIds(ids);
    }
}
