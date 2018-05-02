package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.*;
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
                                                   @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("payType") String payType,
                                                   @Param("keywords") String keywords, @Param("list") List<Long> storeIds);


    List<NotPickGoodsReportDO> findNotPickGoodsReportDOAll(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                                           @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("pickType") String pickType,
                                                           @Param("list") List<Long> storeIds);

    List<StorePredepositReportDO> findStorePredepositReportDOAll(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                                                 @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("list") List<Long> storeIds);

    List<AccountGoodsItemsDO> findAccountGoodsItemsDOAll(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                                         @Param("startTime") String startTime, @Param("endTime") String endTime,
                                                         @Param("keywords") String keywords, @Param("list") List<Long> storeIds);

    List<BillingItemsDO> findBillingItemsDOAll(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                               @Param("startTime") String startTime, @Param("endTime") String endTime,
                                               @Param("keywords") String keywords, @Param("list") List<Long> storeIds);

    List<AgencyFundDO> findAgencyFundDOAll(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                           @Param("startTime") String startTime, @Param("endTime") String endTime,
                                           @Param("keywords") String keywords, @Param("list") List<Long> storeIds);


    List<ShipmentAndReturnGoods> queryGoodsShipmentAndReturnOrder(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                                                  @Param("startTime") String startTime, @Param("endTime") String endTime,
                                                                  @Param("keywords") String keywords, @Param("list") List<Long> storeIds);


    List<StoreInventory> findStoreInventorys(@Param("storeId") Long storeId, @Param("list") List<Long> storeIds);

    AccountGoodsItemsDO getJxPriceByOrderNoAndSku(@Param("orderNumber") String orderNumber, @Param("sku") String sku);


    List<SalesReportDO> findNoProductSalesList(@Param("companyCode") String companyCode, @Param("storeType") String storeType, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("list") List<Long> storeIds);

    List<SalesReportDO> findProductSalesList(@Param("companyCode") String companyCode, @Param("storeType") String storeType, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("list") List<Long> storeIds);

    List<AccountGoodsItemsDO> findAccountZGGoodsItemsDOAll(@Param("cityId") Long cityId, @Param("storeId") Long storeId,
                                                           @Param("startTime") String startTime, @Param("endTime") String endTime,
                                                           @Param("keywords") String keywords, @Param("list") List<Long> storeIds);


    List<SalesReportDO> findProductArrearsList(@Param("companyCode") String companyCode, @Param("storeType") String storeType, @Param("list") List<Long> storeIds);

    List<SalesReportDO> findNoProductArrearsList(@Param("companyCode") String companyCode, @Param("storeType") String storeType, @Param("list") List<Long> storeIds);

}
