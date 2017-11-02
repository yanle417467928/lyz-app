package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.request.BrowseHistoryRequest;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/2
 */
public interface BrowseHistoryService {

    BrowseHistoryRequest save(BrowseHistoryRequest browseHistory);

    void delete(Long userId, AppIdentityType identityType);

    List<Long> existBrowseHistory(BrowseHistoryRequest browseHistory);

    void deleteByIds(List<Long> ids);
}
