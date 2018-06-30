package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.core.constant.OnlinePayType;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.response.BillHistoryListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.BillInfoResponse;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
public interface BillInfoService {


    List<BillRepaymentGoodsDetailsDO> computeInterestAmount(Long storeId, List<BillRepaymentGoodsDetailsDO> goodsDetailsDOList);

    BillRepaymentInfoDO findBillRepaymentInfoByRepaymentNo(String repaymentNo);

    void handleBillRepaymentAfterOnlinePayUp(String repaymentNo, OnlinePayType onlinePayType);

    void updateBillRepaymentInfo(BillRepaymentInfoDO billRepaymentInfoDO);

    List<BillRepaymentGoodsDetailsDO> findRepaymentGoodsDetailsByRepaymentNo(String repaymentNo);

    BillInfoDO findBillInfoByBillNo(String billNo);

    void updateBillInfo(BillInfoDO billInfoDO);

    BillInfoResponse lookBill(String starTime, String endTime, Long storeid ,Integer page,Integer size) throws Exception;

    PageInfo<BillHistoryListResponse> findBillHistoryListByEmpId(Long empId, Integer identityType, Integer page, Integer size);

    BillInfoResponse findBillHistoryDetail(String billNo);

    List<BillRepaymentInfoDO> findBillRepaymentInfoByBillNo(String billNo);

    List<BillRepaymentGoodsDetailsDO> findRepaymentGoodsDetailsByBillNo(String billNo);

}
