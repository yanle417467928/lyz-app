package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

/**
 * 物料审核单列表返回值
 * Created by caiyu on 2017/10/19.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaterialAuditSheetResponse {
    //料单头id
    private Long auditHeaderID;
    //料单编号
    private String auditNo;
    //收货市
    private String deliveryCity;
    //收货区
    private String deliveryCounty;
    //收货街道
    private String deliveryStreet;
    //收货小区
    private String residenceName;
    //收货详细地址
    private String detailedAddress;
    //料单状态
    private int status;
    //商品总数量
    private int totalQty;
    //商品总价格（零售）
    private Double totalPrice;
    //审核是否通过
    private Boolean isAudited;
    //商品图片
    private List<String> pictureList;
    //工人姓名
    private String worker;
}
