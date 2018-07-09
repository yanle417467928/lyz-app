package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.response.BillInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.BillRepaymentGoodsInfoResponse;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.BillRepaymentGoodsDetailsVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.BillRepaymentInfoVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.MaFitBillVO;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liuh
 * @date 2018/3/15
 */
public interface MaFitBillService {

    PageInfo<MaFitBillVO> getFitNotOutBill(Integer page, Integer size,  List<Long> storeIds, String keywords);

    PageInfo<MaFitBillVO> getHistoryFitBill(Integer page, Integer size,  List<Long> storeIds, String keywords);

    PageInfo<BillRepaymentGoodsInfoResponse> getNoPayOrderBillByBillNo(Integer page, Integer size,   Long storeId,String startTime,String endTime,String orderNo);

    PageInfo<BillRepaymentInfoVO> getbillRepaymentInfoByBillNo(Integer page, Integer size, String billNo, String startTime, String endTime, String repaymentNo);

    PageInfo<BillRepaymentGoodsDetailsVO> getbillRepaymentOrderInfoByBillNo(Integer page, Integer size  , String repaymentNo);

    BillInfoDO getFitBillByBillNo(String billNo);

    void saveBillRepaymentInfo(BillRepaymentInfoDO billRepaymentInfoDO);

    void saveBillRepaymentGoodsDetails(BillRepaymentGoodsDetailsDO billRepaymentGoodsDetailsDO);

    BillInfoResponse lookBill(LocalDateTime starTime, LocalDateTime endTime, Long storeid , Integer page, Integer size) throws Exception;

    List<BillRepaymentGoodsInfoResponse> computeInterestAmount2(Long storeId, List<BillRepaymentGoodsInfoResponse> goodsDetailsDOList);

    BillInfoDO createBillInfo(Long storeId);
}
