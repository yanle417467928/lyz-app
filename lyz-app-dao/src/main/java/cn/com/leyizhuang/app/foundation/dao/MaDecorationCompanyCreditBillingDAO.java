package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorationCompanyCreditBillingDO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.AccountGoodsItemsDO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/15
 */
@Repository
public interface MaDecorationCompanyCreditBillingDAO {

    List<DecorationCompanyCreditBillingDetailsVO> getDecorationCompanyCreditOrder(@Param("storeId") Long storeId,
                                                                                  @Param("startTime")String startTime, @Param("endTime")String endTime,
                                                                                  @Param("keywords")String keywords);

    Double getBillAllCreditMoney(@Param("storeId") Long storeId, @Param("list")List<String> orderNumbers);

    int saveCreditBillingDO(DecorationCompanyCreditBillingDO decorationCompanyCreditBillingDO);

    int saveCreditBillingDetailsDO(DecorationCompanyCreditBillingDetailsDO decorationCompanyCreditBillingDetailsDO);

    int batchSaveCreditBillingDetailsDO(@Param("list") List<DecorationCompanyCreditBillingDetailsDO> creditBillingDetailsDOS);

    List<DecorationCompanyCreditBillingDetailsDO> getCreditBillingDetailsByOrderNumber(@Param("list") List<String> orderNumbers,
                                                                                       @Param("storeId") Long storeId);

    List<DecorationCompanyCreditBillingVO> getDecorationCompanyCreditBilling(@Param("storeId") Long storeId, @Param("startTime")String startTime,
                                                                             @Param("endTime")String endTime, @Param("keywords")String keywords,
                                                                             @Param("isPayOff") Boolean isPayOff);

    DecorationCompanyCreditBillingVO getDecorationCompanyCreditBillingById(Long id);

    List<DecorationCompanyCreditBillingDetailsVO> getDecorationCompanyCreditBillingDetailsByCreditBillingNo(String creditBillingNo);

    int updateCreditBillingDetails(DecorationCompanyCreditBillingDO decorationCompanyCreditBillingDO);

    DecorationCompanyCreditBillingDO getCreditBillingById(Long id);

    DecorationCompanyCreditBillingDO getCreditBillingByCreditBillingNo(String creditBillingNo);

    List<AccountGoodsItemsDO> findGoodsItemsDOAll(String creditBillingNo);

}
