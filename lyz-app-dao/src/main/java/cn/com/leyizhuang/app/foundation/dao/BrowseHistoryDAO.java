package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.request.BrowseHistoryRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author GenerationRoad
 * @date 2017/11/2
 */
@Repository
public interface BrowseHistoryDAO {

    void save(BrowseHistoryRequest browseHistory);

    void delete(@Param("userId") Long userId, @Param("identityType") AppIdentityType identityType);

   Boolean existBrowseHistory(BrowseHistoryRequest browseHistory);
}
