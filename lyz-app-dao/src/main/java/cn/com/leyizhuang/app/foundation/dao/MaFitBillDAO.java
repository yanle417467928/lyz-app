package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorationCompanyCreditBillingDO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.AccountGoodsItemsDO;
import cn.com.leyizhuang.app.foundation.pojo.response.BillRepaymentGoodsInfoResponse;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.MaFitBillVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liuh
 * @date 2018/3/15
 */
@Repository
public interface MaFitBillDAO {

    List<MaFitBillVO> getFitNotOutBill(@Param("list") List<Long> storeIds,@Param("keywords") String keywords);

    List<MaFitBillVO> getHistoryFitBill(@Param("list") List<Long> storeIds,@Param("keywords") String keywords);

    List<BillRepaymentInfoDO> getbillRepaymentInfoByBillNo( @Param("billNo") String billNo, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("repaymentNo") String repaymentNo);

    List<BillRepaymentGoodsDetailsDO> getbillRepaymentOrderInfoByBillNo(@Param("repaymentNo") String repaymentNo);

    BillInfoDO getFitBillByBillNo(@Param("billNo") String billNo);

    void saveBillRepaymentInfo(BillRepaymentInfoDO billRepaymentInfoDO);

    void saveBillRepaymentGoodsDetails(BillRepaymentGoodsDetailsDO billRepaymentGoodsDetailsDO);

    List<BillRepaymentGoodsInfoResponse> getCurrentOrderDetails(@Param("startTime") LocalDateTime startTime,
                                                                @Param("endTime") LocalDateTime endTime,
                                                                @Param("isPaid") Boolean isPaid, @Param("storeId") Long storeId,@Param("orderNo") String orderNo);
}
