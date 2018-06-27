package cn.com.leyizhuang.app.foundation.service;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;

import java.util.List;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
public interface BillInfoService {


    List<BillRepaymentGoodsDetailsDO> computeInterestAmount(Long storeId, List<BillRepaymentGoodsDetailsDO> goodsDetailsDOList);

    BillRepaymentInfoDO findBillRepaymentInfoByRepaymentNo(String repaymentNo);

}
