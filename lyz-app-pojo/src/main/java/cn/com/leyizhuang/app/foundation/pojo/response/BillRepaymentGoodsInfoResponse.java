package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.bill.BillRepaymentGoodsDetailsDO;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 账单明细响应类
 * Created by 12421 on 2018/6/28.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BillRepaymentGoodsInfoResponse {

    private Long bill_id;
    //订单号
    private String orderNo;
    //退单号
    private String returnNo;
    //信用金使用金额
    private Double orderCreditMoney;
    //订单创建时间
    private Date orderTime;
    //出货/反配时间
    private Date shipmentTime;
    //订单类型（order、return）
    private String orderType;
    //滞纳金
    private Double interestAmount;

    public static BillRepaymentGoodsInfoResponse transform(BillRepaymentGoodsDetailsDO goodsDetailsDO){
        if (null != goodsDetailsDO) {
            BillRepaymentGoodsInfoResponse goodsInfoResponse = new BillRepaymentGoodsInfoResponse();
            goodsInfoResponse.setOrderNo(goodsDetailsDO.getOrderNo());
            goodsInfoResponse.setReturnNo(goodsDetailsDO.getReturnNo());
            goodsInfoResponse.setShipmentTime(goodsDetailsDO.getShipmentTime());
            goodsInfoResponse.setOrderCreditMoney(goodsDetailsDO.getOrderCreditMoney());
            goodsInfoResponse.setInterestAmount(goodsDetailsDO.getInterestAmount());

        }
        return null;
    }

    public static BillRepaymentGoodsDetailsDO transfer(BillRepaymentGoodsInfoResponse response){
        BillRepaymentGoodsDetailsDO detailsDO = new BillRepaymentGoodsDetailsDO();

        detailsDO.setId(response.getBill_id());
        if (response.getNumber().contains("T")){
            detailsDO.setReturnNo(response.getNumber());
        }else {
            detailsDO.setOrderNo(response.getNumber());
        }
        detailsDO.setOrderCreditMoney(response.getOrderCreditMoney());
        detailsDO.setOrderTime(response.getOrderTime());
        detailsDO.setShipmentTime(response.getShipmentTime());
        detailsDO.setOrderType(response.getOrderType());
        detailsDO.setInterestAmount(response.getInterestAmount());

        return detailsDO;
    }

    public static List<BillRepaymentGoodsDetailsDO> transfer(List<BillRepaymentGoodsInfoResponse> list){
        List<BillRepaymentGoodsDetailsDO> goodsDetailsDOS = new ArrayList<>();

        for (BillRepaymentGoodsInfoResponse response : list){
            BillRepaymentGoodsDetailsDO detailsDO = BillRepaymentGoodsInfoResponse.transfer(response);
            goodsDetailsDOS.add(detailsDO);
        }
        return  goodsDetailsDOS;
    }
}
