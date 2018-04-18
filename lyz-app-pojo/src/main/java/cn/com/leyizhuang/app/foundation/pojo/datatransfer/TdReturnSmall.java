package cn.com.leyizhuang.app.foundation.pojo.datatransfer;

import lombok.*;

import java.util.Date;

/**
 * 退货单
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TdReturnSmall {
    private Long id;

    /**
     * 退货单编号
     */
    private String returnNumber;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 退货单状态 1:待通知物流 2:待取货 3: 待确认收货 4 待退款（物流确认） 5 已完成 6 退货取消
     */
    private Long statusId;

    /**
     * 状态名称 zp
     */
    private String statusName;

    /**
     * 门店id
     */
    private Long diySiteId;

    /**
     * 门店名称
     */
    private String diySiteTitle;

    /**
     * 门店地址
     */
    private String diySiteAddress;

    /**
     * 门店电话
     */
    private String diySiteTel;

    /**
     * 客户备注
     */
    private String remarkInfo;

    /**
     * 后台备注
     */
    private String managerRemarkInfo;

    /**
     * 申请用户
     */
    private String username;

    /**
     * 退货单下单时间
     */
    private Date orderTime;

    /**
     * 取消时间
     */
    private Date cancelTime;


    /**
     * 退货方式 1 到店退货， 2 物流取货
     */
    private Long turnType;

    private String turnTypeName;

    /**
     * 原订单配送方式
     */
    private String deliverTypeTitle;

    /**
     * 原订单配送地址
     */
    private String shoppingAddress;

    /**
     * 销售顾问名字
     */
    private String sellerRealName;

    private String sellerUsername;

    private Date returnTime;
    /**
     * 退货金额
     */
    private Double turnPrice;

    /**
     * 快递员
     */
    private String driver;

    /**
     * 门店编码
     */
    private String diyCode;

    /**
     * 退款明细
     */
    private String returnDetail;

    /**
     * 是否是退券单
     */
    private Boolean isCoupon;

    /**
     * 取消退货申请的时间
     */
    private Date cancelReturnTime;

    private Double jxReturn;

    /**
     * 退还装饰公司的信用额度
     */
    private Double credit = 0d;

    /**
     * 退还装饰公司的赞助金
     */
    private Double promotionMoneyPayed = 0d;

    /**
     * 装饰公司支付宝退款金额
     */
    private Double alipayMoney = 0d;

    /**
     * 装饰公司钱包退款金额
     */
    private Double walletMoney = 0d;

    public String getStatusName() {
        // 1:待通知物流 2:待取货 3: 待确认收货 4 待退款（物流确认） 5 已完成   6退货取消
        if (this.statusId == 1L) {
            return "待通知物流";
        } else if (this.statusId == 2L) {
            return "待取货";
        } else if (this.statusId == 3L) {
            return "待退款";
        } else if (this.statusId == 4L) {
            return "待退款";
        } else if (this.statusId == 5L) {
            return "已完成";
        } else if (this.statusId == 6L) {
            return "退货取消";
        }

        return "";
    }

    public String getTurnTypeName() {
        // 1 到店退货， 2 物流取货
        if (this.turnType == 1L) {
            return "到店退货";
        } else if (this.turnType == 2L) {
            return "物流取货";
        }
        return "";
    }
}
