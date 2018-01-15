package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes:APP~WMS订单下传接口表
 * Created with IntelliJ IDEA.
 * Date: 2017/12/21.
 * Time: 10:51.
 */
@Getter
@Setter
@ToString
public class AtwReturnOrder {

    private Long id;

    /**
     * 创建时间（本条数据创建时间）
     */
    private Date createTime;
    /**
     * 申请退货时间
     */
    private Date cancelTime;
    /**
     * 确认收货时间
     */
    private Date checkTime;
    /**
     * 门店地址
     */
    private String diySiteAddress;
    /**
     * 门店编码
     * diy_site_id字面上的意义是门店ID，但因为历史原因这个标签必须传递门店编码
     */
    private String diySiteId;
    /**
     * 门店名称
     */
    private String diySiteTitle;
    /**
     * 门店电话
     */
    private String diySiteTel;
    /**
     * 管理员后台备注信息
     */
    private String managerRemarkInfo;
    /**
     * 订单备注信息
     */
    private String remarkInfo;
    /**
     * 原单号（主单号）
     */
    private String orderNumber;
    /**
     * 支付方式值
     */
    private Integer payTypeId;
    /**
     * 支付方式描述
     */
    private String payTypeTitle;
    /**
     * 退单号
     */
    private String returnNumber;
    /**
     * 退单时间
     */
    private Date returnTime;
    /**
     * 退货单状态一期：1:待通知物流 2:待取货 3: 待确认收货 4 待退款（物流确认） 5 已完成 6 退货取消
     */
    private Integer statusId;
    /**
     * 申请人姓名
     */
    private String userName;
    /**
     * 原订单配送方式
     */
    private String deliverTypeTitle;
    /**
     * 退款金额
     */
    private Double returnPrice;
    /**
     * 退货方式
     */
    private String returnType;
    /**
     * 原订单收货地址
     */
    private String shoppingAddress;
    /**
     * 导购真实姓名
     */
    private String sellerRealName;
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 接口传输标识
     */
    private Boolean sendFlag;
    /**
     * 接口错误信息
     */
    private String errorMessage;
    /**
     * wms收到信息时间
     */
    private Date sendTime;

}
