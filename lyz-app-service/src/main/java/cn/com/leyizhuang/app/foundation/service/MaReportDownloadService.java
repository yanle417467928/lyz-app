package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.reportDownload.NotPickGoodsReportDO;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.ReceiptsReportDO;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.StorePredepositReportDO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/22
 */
public interface MaReportDownloadService {

    PageInfo<ReceiptsReportDO> findReceiptsReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, String payType,
                                                       String keywords, List<Long> storeIds, Integer page, Integer size);

    List<ReceiptsReportDO> receiptsDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, String payType,
                                                       String keywords, List<Long> storeIds);

    PageInfo<NotPickGoodsReportDO> findNotPickGoodsReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, String pickType, List<Long> storeIds, Integer page, Integer size);

    List<NotPickGoodsReportDO> notPickGoodsDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, String pickType, List<Long> storeIds);

    PageInfo<StorePredepositReportDO> findStorePredepositReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds, Integer page, Integer size);

    List<StorePredepositReportDO> storePredepositDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds);

}
