package cn.com.leyizhuang.app.foundation.pojo.datatransfer;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TdOrderLogistics {
    /**
     * 订单id
     */
    private  Long oid;
    /**
     * 订单号
     */
    private String mainOrderNumber;
    /**
     * 配送方式
     */
    private String deliverTypeTitle;
    /**
     * 门店自提单预约门店编码
     */
    private String diySiteCode;
    /**
     * 门店自提单预约门店名称
     */
    private String diySiteName;
    /**
     * 门店自提单预约门店地址
     */
    private String detailedAddress;
    /**
     * 配送单预约配送时间
     */
    private String deliveryDate;
    /**
     * 配送员编码
     */
    private String driver;
    /**
     * 配送单配送员id
     */
    private Long empId;
    /**
     * 配送单配送员姓名
     */
    private String name;
    /**
     * 配送员电话
     */
    private String mobile;
    /**
     * 仓库名称
     */
    private String whNo;
    /**
     * 收货人姓名
     */
    private String shippingName;
    /**
     * 收货人电话
     */
    private String shippingPhone;
    /**
     * 收货地址全称
     */
    private String shippingAddress;
    /**
     * 城市
     */
    private String city;
    /**
     * 县/区
     */
    private String disctrict;
    /**
     * 街道
     */
    private String subdistrict;
    /**
     * 详细地址
     */
    private String detailAddress;

}
