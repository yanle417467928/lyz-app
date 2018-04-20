package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.dto.DecorationCompanyCreditBillingDTO;
import cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany.DecorationCompanyCreditBillingDO;
import cn.com.leyizhuang.app.foundation.pojo.reportDownload.AccountGoodsItemsDO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingDetailsVO;
import cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany.DecorationCompanyCreditBillingVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/3/15
 */
public interface MaDecorationCompanyCreditBillingService {

    PageInfo<DecorationCompanyCreditBillingDetailsVO> getDecorationCompanyCreditOrder(Integer page, Integer size, Long storeId,
                                                                                      String startTime, String endTime, String keywords);


    Double getBillAllCreditMoney(Long storeId, List<String> orderNumbers);

    void createCreditBilling(String[] orderNumbers, DecorationCompanyCreditBillingDTO creditBillingDTO);

    PageInfo<DecorationCompanyCreditBillingVO> getDecorationCompanyCreditBilling(Integer page, Integer size, Long storeId,
                                                                                 String startTime, String endTime, String keywords, Boolean isPayOff);

    DecorationCompanyCreditBillingVO getDecorationCompanyCreditBillingById(Long id);

    List<DecorationCompanyCreditBillingDetailsVO> getDecorationCompanyCreditBillingDetailsByCreditBillingNo(String creditBillingNo);

    Boolean repaymentCreditBilling(Long id, Double amount, String paymentType);

    DecorationCompanyCreditBillingDO getCreditBillingById(Long id);

    DecorationCompanyCreditBillingDO getCreditBillingByCreditBillingNo(String creditBillingNo);

    List<AccountGoodsItemsDO> findGoodsItemsDOAll(String creditBillingNo);

}
