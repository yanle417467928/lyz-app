package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.reportDownload.ReceiptsReportDO;
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

}
