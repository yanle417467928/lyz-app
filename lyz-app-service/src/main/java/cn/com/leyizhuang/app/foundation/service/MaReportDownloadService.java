package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.CusExpiringSoonProductCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.*;
import com.github.pagehelper.PageInfo;

import java.util.Date;
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

    AccountGoodsItemsDO getJxPriceByOrderNoAndSku(String orderNumber, String sku,String goodsLineType);

    List<ShipmentAndReturnGoods> downShipmentAndReturnOrder(Long cityId, Long storeId, String storeType, String startTime, String endTime,

                                                                     String keywords, List<Long> storeIds);
    PageInfo<SalesReportDO> findSalesList(String companyCode, String storeType,
                                      String startTime, String endTime, Boolean isProductCoupon, List<Long> storeIds,String productType ,Long storeId, Integer page, Integer size);


    PageInfo<ArrearsReportDO> findArrearsList(String companyCode, String storeType, List<Long> storeIds,Long storeId, Integer page, Integer size, String endTime);


    List<ArrearsReportDO> downArrearsList(String companyCode, String storeType, List<Long> storeIds,Long storeId, String endTime);


    List<SalesReportDO> downSalesReport(String companyCode, String storeType,
                                            String startTime, String endTime, Boolean isProductCoupon, List<Long> storeIds,String productType,Long storeId);

    PageInfo<AccountGoodsItemsDO> findAccountZGGoodsItemsDOAll(Long cityId, Long storeId, String startTime, String endTime,
                                                             String keywords, List<Long> storeIds, Integer page, Integer size);

    List<AccountGoodsItemsDO> downloadAccountZGGoodsItems(Long cityId, Long storeId, String startTime, String endTime,
                                                        String keywords, List<Long> storeIds);


    PageInfo<EmpCreditMoneyChangeReportDO> findEmployeeCreditMoneyReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds, Integer page, Integer size);

    PageInfo<CompanyCreditMoneyChangeReportDO> findCompanyCreditMoneyReportDOAll(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds, Integer page, Integer size);

    List<EmpCreditMoneyChangeReportDO> employeeCreditMoneyDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds);

    List<CompanyCreditMoneyChangeReportDO> companyCreditMoneyDownload(Long cityId, Long storeId, String storeType, String startTime, String endTime, List<Long> storeIds);

    PageInfo<DistributionDO> findDistributionDOAll(Long cityId, String wareHouseNo, String deliveryClerkNo, String startTime, String endTime,
                                                             String keywords, Integer page, Integer size);

    List<DistributionDO> downloadDistributionDO(Long cityId, String wareHouseNo, String deliveryClerkNo, String startTime, String endTime,
                                                        String keywords);

    PageInfo<AccountGoodsItemsDO> findAccountGoodsItemsDOHR(Long cityId, Long storeId, String storeType, String startTime, String endTime,
                                                             String keywords, List<Long> storeIds, Integer page, Integer size);

    List<AccountGoodsItemsDO> downloadAccountGoodsItemsHR(Long cityId, Long storeId, String storeType, String startTime, String endTime,
                                                        String keywords, List<Long> storeIds);


    PageInfo<PhotoOrderCheckDO> findPhotoOrderCheckDOAll(Long cityId, String startTime, String endTime, String keywords, Integer page, Integer size);

    List<PhotoOrderCheckDO> downloadPhotoOrderCheckDO(Long cityId, String startTime, String endTime, String keywords);

    PageInfo<CusExpiringSoonProductCouponInfo> findExpiringSoonProductAll(Long cityId, Long storeId, String storeType, String cusName, Integer page, Integer size, List<Long> storeIds);

    List<CusExpiringSoonProductCouponInfo> downloadExpiringSoonProduct(Long cityId, Long storeId, String storeType, String cusName, List<Long> storeIds);

    PageInfo<EmpCreditDO> empCreditMoneySituationPage(Long cityId,String storeType,List<Long> storeIds,Integer page, Integer size,String keywords);

    PageInfo<StCreditDO> stCreditMoneySituationPage(Long cityId, List<Long> storeIds, Integer page, Integer size,String keywords);

    List<EmpCreditDO> empCreditMoneySituation(Long cityId,String storeType,List<Long> storeIds,String keywords);

    List<StCreditDO> stCreditMoneySituation(Long cityId,List<Long> storeIds,String keywords);

    PageInfo<CusProductCouponSummary> findCusProductCouponSummaryPage(Long cityId, Long storeId, List<Long> storeIds, Integer page, Integer size,String keywords, String endTime, String productType);

    List<CusProductCouponSummary> findCusProductCouponSummaryList(Long cityId, Long storeId, List<Long> storeIds, String keywords, String endTime,String productType);

    PageInfo<CusProductCouponChangeLogReportDO> findCusProductCouponChangeLogPage(Long cityId, Long storeId, List<Long> storeIds, Integer page, Integer size,String keywords,String endTime,String startTime, String productType);

    List<CusProductCouponChangeLogReportDO> findCusProductCouponChangeLogList(Long cityId, Long storeId, List<Long> storeIds,String keywords,String endTime,String startTime, String productType);

    PageInfo<StInventoryRealChangeLogReportDO> findStInventoryRealChangeLogPage(Long cityId, Long storeId, List<Long> storeIds, Integer page, Integer size,String endTime, String startTime);

    List<StInventoryRealChangeLogReportDO> findStInventoryRealChangeLogList(Long cityId, Long storeId, List<Long> storeIds,String endTime, String startTime);
}
