package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.foundation.pojo.request.BrowseHistoryRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.BrowseHistoryResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2017/11/2
 */
@Repository
public interface BrowseHistoryDAO {

    void save(BrowseHistoryRequest browseHistory);

    void delete(@Param("userId") Long userId, @Param("identityType") AppIdentityType identityType);

   List<Long> existBrowseHistory(BrowseHistoryRequest browseHistory);

   void deleteByIds(@Param("list") List<Long> ids);

   List<BrowseHistoryResponse> findBrowseHistoryByUserIdAndIdentityType(@Param("userId") Long userId, @Param("identityType") AppIdentityType identityType);
}
