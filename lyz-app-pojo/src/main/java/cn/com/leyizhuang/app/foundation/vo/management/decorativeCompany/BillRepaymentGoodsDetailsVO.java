package cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 账单还款商品明细表
 * @author GenerationRoad
 * @date 2018/6/26
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BillRepaymentGoodsDetailsVO {
    private Long id;
    //订单号
    private String orderNo;
    //信用金使用金额
    private Double orderCreditMoney;
    //订单创建时间
    private Date orderTime;
    //出货/反配时间
    private Date shipmentTime;
    //滞纳金
    private Double interestAmount;
    //总金额
    private Double totalAmount;

    public static final BillRepaymentGoodsDetailsVO transfrom(BillRepaymentGoodsDetailsDO billRepaymentGoodsDetailsDO){
        if(null !=billRepaymentGoodsDetailsDO){
            BillRepaymentGoodsDetailsVO billRepaymentGoodsDetailsVO = new BillRepaymentGoodsDetailsVO();
            billRepaymentGoodsDetailsVO.setId(billRepaymentGoodsDetailsDO.getId());
            billRepaymentGoodsDetailsVO.setOrderCreditMoney(billRepaymentGoodsDetailsDO.getOrderCreditMoney());
            billRepaymentGoodsDetailsVO.setOrderNo(billRepaymentGoodsDetailsDO.getOrderNo());
            billRepaymentGoodsDetailsVO.setOrderTime(billRepaymentGoodsDetailsDO.getOrderTime());
            billRepaymentGoodsDetailsVO.setShipmentTime(billRepaymentGoodsDetailsDO.getShipmentTime());
            billRepaymentGoodsDetailsVO.setInterestAmount(billRepaymentGoodsDetailsDO.getInterestAmount());
            if(null !=billRepaymentGoodsDetailsDO.getInterestAmount() && null != billRepaymentGoodsDetailsDO.getOrderCreditMoney()){
                billRepaymentGoodsDetailsVO.setTotalAmount(billRepaymentGoodsDetailsDO.getInterestAmount()+billRepaymentGoodsDetailsDO.getOrderCreditMoney());
            }else if(null !=billRepaymentGoodsDetailsDO.getInterestAmount() && null == billRepaymentGoodsDetailsDO.getOrderCreditMoney()){
                billRepaymentGoodsDetailsVO.setTotalAmount(billRepaymentGoodsDetailsDO.getInterestAmount());
            }else if(null ==billRepaymentGoodsDetailsDO.getInterestAmount() && null != billRepaymentGoodsDetailsDO.getOrderCreditMoney()){
                billRepaymentGoodsDetailsVO.setTotalAmount(billRepaymentGoodsDetailsDO.getOrderCreditMoney());
            }else{
                billRepaymentGoodsDetailsVO.setTotalAmount(0d);
            }
            return billRepaymentGoodsDetailsVO;
        }else{
            return null;
        }
    }

    public static final List<BillRepaymentGoodsDetailsVO> transform(List<BillRepaymentGoodsDetailsDO> billRepaymentGoodsDetailsDOList) {
        List<BillRepaymentGoodsDetailsVO> billRepaymentGoodsDetailsVOList;
        if (null != billRepaymentGoodsDetailsDOList && billRepaymentGoodsDetailsDOList.size() > 0) {
            billRepaymentGoodsDetailsVOList = new ArrayList<>(billRepaymentGoodsDetailsDOList.size());
            billRepaymentGoodsDetailsDOList.forEach(billRepaymentGoodsDetailsDO -> billRepaymentGoodsDetailsVOList.add(transfrom(billRepaymentGoodsDetailsDO)));
        } else {
            billRepaymentGoodsDetailsVOList = new ArrayList<>(0);
        }
        return billRepaymentGoodsDetailsVOList;
    }
}
