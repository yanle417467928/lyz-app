package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorationCompanyCreditBillingDO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.AccountGoodsItemsDO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.MaFitBillVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuh
 * @date 2018/3/15
 */
@Repository
public interface MaFitBillDAO {

    List<MaFitBillVO> getFitNotOutBill(@Param("list") List<Long> storeIds,@Param("keywords") String keywords);

    List<MaFitBillVO> getHistoryFitBill(@Param("list") List<Long> storeIds,@Param("keywords") String keywords);

    BillInfoDO getFitBillByBillNo(@Param("billNo") String billNo);
}
