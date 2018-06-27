package cn.com.leyizhuang.app.foundation.service;


import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.MaFitBillVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author liuh
 * @date 2018/3/15
 */
public interface MaFitBillService {

    PageInfo<MaFitBillVO> getFitNotOutBill(Integer page, Integer size,  List<Long> storeIds, String keywords);

    PageInfo<MaFitBillVO> getHistoryFitBill(Integer page, Integer size,  List<Long> storeIds, String keywords);

    PageInfo<MaFitBillVO> getNoPayOrderBillByBillNo(Integer page, Integer size,  List<Long> storeIds, String billNo,String startTime,String endTime,String orderNo);

    PageInfo<MaFitBillVO> getPayOrderBillByBillNo(Integer page, Integer size,  List<Long> storeIds, String billNo,String startTime,String endTime,String orderNo);

    BillInfoDO getFitBillByBillNo(String billNo);

}
