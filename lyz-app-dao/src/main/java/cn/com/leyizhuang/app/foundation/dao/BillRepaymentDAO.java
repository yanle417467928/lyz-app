package cn.com.leyizhuang.app.foundation.dao;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentInfoDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 12421 on 2018/7/4.
 */
@Repository
public interface BillRepaymentDAO {

    int saveBillRepayment(BillRepaymentInfoDO repaymentInfoDO);

    int uodateBillRepayment(BillRepaymentInfoDO repaymentInfoDO);

    BillRepaymentInfoDO findById(Long id);

    BillRepaymentInfoDO findByRepaymentNo(String repaymentNo);

    List<BillRepaymentInfoDO> findByBillNo(String billNo);

    int saveBillRepaymentGoodsDetails(BillRepaymentGoodsDetailsDO goodsDetailsDO);

    int updateBillRepaymentGoodsDetails(BillRepaymentGoodsDetailsDO goodsDetailsDO);

    BillRepaymentGoodsDetailsDO findByDetailsId(Long detailsId);

    List<BillRepaymentGoodsDetailsDO> findDetailsByRepaymentNo(String repaymentNo);

}
