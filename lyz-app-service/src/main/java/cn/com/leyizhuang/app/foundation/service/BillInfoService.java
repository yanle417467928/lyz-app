package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.BillStatusEnum;
import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.request.BillorderDetailsRequest;
import cn.com.leyizhuang.app.foundation.pojo.response.BillHistoryListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.BillInfoResponse;
import com.github.pagehelper.PageInfo;
import cn.com.leyizhuang.app.foundation.pojo.response.BillRepaymentGoodsInfoResponse;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
public interface BillInfoService {

    List<BillRepaymentGoodsDetailsDO> computeInterestAmount(Long storeId, List<BillRepaymentGoodsDetailsDO> goodsDetailsDOList);

    List<BillRepaymentGoodsInfoResponse> computeInterestAmount2(Long storeId, List<BillRepaymentGoodsInfoResponse> goodsDetailsDOList);

    BillRepaymentInfoDO findBillRepaymentInfoByRepaymentNo(String repaymentNo);

    void handleBillRepaymentAfterOnlinePayUp(String repaymentNo, OnlinePayType onlinePayType, Integer identityType);

    void updateBillRepaymentInfo(BillRepaymentInfoDO billRepaymentInfoDO);

    List<BillRepaymentGoodsDetailsDO> findRepaymentGoodsDetailsByRepaymentNo(String repaymentNo);

    BillInfoDO findBillInfoByBillNo(String billNo);

    void updateBillInfo(BillInfoDO billInfoDO);

    BillInfoResponse lookBill(LocalDateTime starTime, LocalDateTime endTime, Long storeid ,Integer page,Integer size) throws Exception;

    PageInfo<BillHistoryListResponse> findBillHistoryListByEmpId(Long empId, Integer identityType, Integer page, Integer size);

    BillInfoResponse findBillHistoryDetail(String billNo);

    List<BillRepaymentInfoDO> findBillRepaymentInfoByBillNo(String billNo);

    List<BillRepaymentGoodsDetailsDO> findRepaymentGoodsDetailsByBillNo(String billNo);

    BillInfoDO createBillInfo(Long storeId);

    void saveBillInfo(BillInfoDO billInfo);

    void updateBillStatus(Long storeId, BillStatusEnum beforeStatus, BillStatusEnum afterStatus);

    void handleBillInfoInBillDate(Long storeId);

    BillInfoDO findBillInfoByBillStartDateAndStoreIdAndStatus(Long storeId, String billStratDate, BillStatusEnum status);

    Double calculatePayAmount(Long storeId,List<BillorderDetailsRequest> orderDetails);

    BillRepaymentInfoDO createRepayMentInfo(Long storeId,Long userId,String repaymentSystem,List<BillorderDetailsRequest> billorderDetailsRequests,
                             Double stPreDeposit,Double cash,Double pos,Double totalRepaymentAmount,
                             String posNumber,Double other,
                             String billNo) throws Exception;

    List<BillRepaymentGoodsInfoResponse> findPaidOrderDetailsByOids(List<Long> list,Long storeId);

    List<BillRepaymentGoodsInfoResponse> findPaidReturnOrderDetailsByOids(List<Long> list,Long storeId);
}
