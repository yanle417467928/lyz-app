package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.reportDownload.NotPickGoodsReportDO;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.ReceiptsReportDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/22
 */
@Repository
public interface MaReportDownloadDAO {

    List<ReceiptsReportDO> findReceiptsReportDOAll(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                                   @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("payType")String payType,
                                                   @Param("keywords")String keywords, @Param("list") List<Long> storeIds);


    List<NotPickGoodsReportDO> findNotPickGoodsReportDOAll(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                                           @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("pickType") String pickType,
                                                           @Param("list") List<Long> storeIds);
}
