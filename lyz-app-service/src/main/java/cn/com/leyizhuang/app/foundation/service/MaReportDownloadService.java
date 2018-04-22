package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/22
 */
public interface MaReportDownloadService {

    PageInfo<ReceiptsReportDO> findReceiptsReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, String payType,
                                                       String keywords, List<Long> storeIds, Integer page, Integer size);

    List<ReceiptsReportDO> downloadReceipts(Long cityId, Long storeId, String storeType, String startTime, String endTime, String payType,
                                                       String keywords, List<Long> storeIds);

    PageInfo<NotPickGoodsReportDO> findNotPickGoodsReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, String pickType, List<Long> storeIds, Integer page, Integer size);

    List<NotPickGoodsReportDO> notPickGoodsDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, String pickType, List<Long> storeIds);

    PageInfo<StorePredepositReportDO> findStorePredepositReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds, Integer page, Integer size);

    List<StorePredepositReportDO> storePredepositDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds);

    PageInfo<AccountGoodsItemsDO> findAccountGoodsItemsDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime,
                                                             String keywords, List<Long> storeIds, Integer page, Integer size);

    List<AccountGoodsItemsDO> downloadAccountGoodsItems(Long cityId, Long storeId, String storeType, String startTime, String endTime,
                                            String keywords, List<Long> storeIds);

    PageInfo<BillingItemsDO> findBillingItemsDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime,
                                                   String keywords, List<Long> storeIds, Integer page, Integer size);

    List<BillingItemsDO> downloadBillingItems(Long cityId, Long storeId, String storeType, String startTime, String endTime,
                                                        String keywords, List<Long> storeIds);

    PageInfo<AgencyFundDO> findAgencyFundDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime,
                                                   String keywords, List<Long> storeIds, Integer page, Integer size);

    PageInfo<ShipmentAndReturnGoods> findGoodsShipmentAndReturnOrder(Long cityId, Long storeId, String storeType, String startTime, String endTime,
                                                           String keywords, List<Long> storeIds, Integer page, Integer size);

    List<AgencyFundDO> downloadAgencyFund(Long cityId, Long storeId, String storeType, String startTime, String endTime,
                                              String keywords, List<Long> storeIds);

    List<StoreInventory> downloadStoreInventorys(Long storeId, List<Long> storeIds);
}
