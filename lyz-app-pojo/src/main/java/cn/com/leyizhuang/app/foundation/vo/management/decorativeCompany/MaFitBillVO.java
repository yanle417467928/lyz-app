package cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.FitCompayType;
import cn.com.leyizhuang.app.core.constant.StoreCreditMoneyChangeType;
import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuh
 * @date 2018/1/12
 */
@Getter
@Setter
@ToString
public class MaFitBillVO {

    private Long id;
    /**
     * 账单名称
     */
    private String billName;

    /**
     * 装饰公司信用金账单单号
     */
    private String billNo;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 账单开始日期
     */
    private String billStartDate;

    /**
     * 账单结束日期
     */
    private String billEndDate;
    /**
     * 账单还款截止日
     */
    private String repaymentDeadlineDate;

    /**
     * 账单总额
     */
    private Double billTotalAmount;
    /**
     * 已还金额
     */
    private Double currentPaidAmount;
    /**
     * 未还金额
     */
    private Double currentUnpaidAmount;

    private String status;
    /**
     * 已还上期滞纳金
     */
    private Double priorPaidInterestAmount;
    /**
     * 已还上期账单金额
     */
    private Double priorPaidBillAmount;
    /**
     * 已还总金额
     */
    private Double payAmount;

    public static final MaFitBillVO transfrom(MaFitBillVO maFitBillVO){
        if(null !=maFitBillVO){
            maFitBillVO.setPayAmount(0d);
            if(null !=maFitBillVO.getCurrentPaidAmount()){
                maFitBillVO.setPayAmount(maFitBillVO.getCurrentPaidAmount());
            }
            if(null != maFitBillVO.getPriorPaidBillAmount()){
                maFitBillVO.setPayAmount( maFitBillVO.getPayAmount()+maFitBillVO.getPriorPaidBillAmount());
            }
            if(null != maFitBillVO.getPriorPaidInterestAmount()){
                maFitBillVO.setPayAmount(maFitBillVO.getPayAmount() +maFitBillVO.getPriorPaidInterestAmount());
            }
            return maFitBillVO;
        }else{
            return null;
        }
    }

    public static final List<MaFitBillVO> transform(List<MaFitBillVO> maFitBillVOList) {
        List<MaFitBillVO> maFitBillVOLists;
        if (null != maFitBillVOList && maFitBillVOList.size() > 0) {
            maFitBillVOLists = new ArrayList<>(maFitBillVOList.size());
            maFitBillVOList.forEach(maFitBillVO -> maFitBillVOLists.add(transfrom(maFitBillVO)));
        } else {
            maFitBillVOLists = new ArrayList<>(0);
        }
        return maFitBillVOLists;
    }

}
