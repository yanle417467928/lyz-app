package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.core.constant.BillStatusEnum;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import cn.com.leyizhuang.app.foundation.pojo.response.BillHistoryListResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.BillRepaymentGoodsInfoResponse;
import cn.com.leyizhuang.app.foundation.pojo.response.BillHistoryListResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Repository
public interface BillInfoDAO {

    BillRepaymentInfoDO findBillRepaymentInfoByRepaymentNo(String repaymentNo);

    void updateBillRepaymentInfo(BillRepaymentInfoDO billRepaymentInfoDO);

    List<BillRepaymentGoodsDetailsDO> findRepaymentGoodsDetailsByRepaymentNo(String repaymentNo);

    BillInfoDO findBillInfoByBillNo(String billNo);

    void updateBillInfo(BillInfoDO billInfoDO);

    BillInfoDO findBillByStatus(@Param("status") BillStatusEnum status,@Param("storeId") Long storeId);

    List<BillRepaymentGoodsInfoResponse> getCurrentOrderDetails(@Param("startTime") LocalDateTime startTime,
                                                          @Param("endTime") LocalDateTime endTime,
                                                          @Param("isPaid") Boolean isPaid, @Param("storeId") Long storeId);

    List<BillHistoryListResponse> findBillHistoryListByEmpId(Long empId);

    List<BillRepaymentInfoDO> findBillRepaymentInfoByBillNo(String billNo);

    List<BillRepaymentGoodsDetailsDO> findRepaymentGoodsDetailsByBillNo(String billNo);

    void saveBillInfo(BillInfoDO billInfo);

    void updateBillStatus(@Param("storeId") Long storeId, @Param("beforeStatus")BillStatusEnum beforeStatus, @Param("afterStatus")BillStatusEnum afterStatus);

    BillInfoDO findBillInfoByBillEndDateAndStoreIdAndStatus(@Param("storeId") Long storeId, @Param("billEndDate")String billEndDate, @Param("status")BillStatusEnum status);

}
