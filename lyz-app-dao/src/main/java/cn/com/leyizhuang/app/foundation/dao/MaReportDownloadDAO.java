package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.CusExpiringSoonProductCouponInfo;
import cn.com.leyizhuang.app.foundation.pojo.inventory.StoreInventory;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    List<NotPickGoodsReportDO> findNotPickGoodsReportDOAllNEW(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                                           @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("pickType") String pickType,
                                                           @Param("list") List<Long> storeIds);

    List<StorePredepositReportDO> findStorePredepositReportDOAll(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                                                 @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("list") List<Long> storeIds);

    List<StorePredepositReportDO> findStorePredepositReportDOAllNEW(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
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

    AccountGoodsItemsDO getJxPriceByOrderNoAndSku(@Param("orderNumber") String orderNumber,
                                                  @Param("sku") String sku,
                                                  @Param("goodsLineType") String goodsLineType);



    List<SalesReportDO> findNoProductSalesList(@Param("companyCode") String companyCode, @Param("storeType") String storeType, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("list") List<Long> storeIds);


    List<SalesReportDO> findProductSalesList(@Param("companyCode") String companyCode,
                                             @Param("storeType") String storeType,
                                             @Param("startTime") String startTime,
                                             @Param("endTime") String endTime,
                                             @Param("list") List<Long> storeIds,
                                             @Param("productType") String productType,
                                             @Param("storeId") Long storeId);

    List<AccountGoodsItemsDO> findAccountZGGoodsItemsDOAll(@Param("cityId") Long cityId, @Param("storeId") Long storeId,
                                                           @Param("startTime") String startTime, @Param("endTime") String endTime,
                                                           @Param("keywords") String keywords, @Param("list") List<Long> storeIds);



    List<ArrearsReportDO> findArrearsList(@Param("companyCode") String companyCode, @Param("storeType") String storeType,
                                          @Param("list") List<Long> storeIds, @Param("storeId") Long storeId, @Param("endTime")String endTime);


    List<EmpCreditMoneyChangeReportDO> findEmployeeCreditMoneyReportDOAll(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                                                          @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("list") List<Long> storeIds);

    List<DistributionDO> findDistributionDOAll(@Param("cityId") Long cityId, @Param("wareHouseNo") String wareHouseNo, @Param("deliveryClerkNo") String deliveryClerkNo,
                                                         @Param("startTime") String startTime, @Param("endTime") String endTime,
                                                         @Param("keywords") String keywords);

    List<AccountGoodsItemsDO> findAccountGoodsItemsDOHR(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                                         @Param("startTime") String startTime, @Param("endTime") String endTime,
                                                         @Param("keywords") String keywords, @Param("list") List<Long> storeIds);

    List<PhotoOrderCheckDO> findPhotoOrderCheckDOAll(@Param("cityId") Long cityId, @Param("startTime") String startTime,
                                                     @Param("endTime") String endTime, @Param("keywords") String keywords);

    List<CusExpiringSoonProductCouponInfo> findExpiringSoonProductAll(@Param("cityId") Long cityId, @Param("storeId") Long storeId, @Param("storeType") String storeType,
                                                                      @Param("cusName") String cusName, @Param("list") List<Long> storeIds);

    List<EmpCreditDO> empCreditMoneySituation(@Param("city") Long cityId,@Param("storeType") String storeType,@Param("storeIds") List<Long> storeIds,
                                              @Param("keywords") String keywords);

    List<StCreditDO> stCreditMoneySituation(@Param("city") Long cityId,@Param("storeIds") List<Long> storeIds,
                                            @Param("keywords") String keywords);
}
