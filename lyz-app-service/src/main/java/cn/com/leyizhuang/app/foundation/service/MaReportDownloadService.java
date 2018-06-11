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

    AccountGoodsItemsDO getJxPriceByOrderNoAndSku(String orderNumber, String sku);

    List<ShipmentAndReturnGoods> downShipmentAndReturnOrder(Long cityId, Long storeId, String storeType, String startTime, String endTime,

                                                                     String keywords, List<Long> storeIds);
    PageInfo<SalesReportDO> findSalesList(String companyCode, String storeType,
                                      String startTime, String endTime, Boolean isProductCoupon, List<Long> storeIds, Integer page, Integer size,String productType);


    PageInfo<ArrearsReportDO> findArrearsList(String companyCode, String storeType, List<Long> storeIds, Integer page, Integer size);


    List<ArrearsReportDO> downArrearsList(String companyCode, String storeType, List<Long> storeIds);


    List<SalesReportDO> downSalesReport(String companyCode, String storeType,
                                            String startTime, String endTime, Boolean isProductCoupon, List<Long> storeIds,String productType);

    PageInfo<AccountGoodsItemsDO> findAccountZGGoodsItemsDOAll(Long cityId, Long storeId, String startTime, String endTime,
                                                             String keywords, List<Long> storeIds, Integer page, Integer size);

    List<AccountGoodsItemsDO> downloadAccountZGGoodsItems(Long cityId, Long storeId, String startTime, String endTime,
                                                        String keywords, List<Long> storeIds);


    PageInfo<EmpCreditMoneyChangeReportDO> findEmployeeCreditMoneyReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds, Integer page, Integer size);

    List<EmpCreditMoneyChangeReportDO> employeeCreditMoneyDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds);

    PageInfo<DistributionDO> findDistributionDOAll(Long cityId, String wareHouseNo, String deliveryClerkNo, String startTime, String endTime,
                                                             String keywords, Integer page, Integer size);

    List<DistributionDO> downloadDistributionDO(Long cityId, String wareHouseNo, String deliveryClerkNo, String startTime, String endTime,
                                                        String keywords);
}
