package cn.com.leyizhuang.app.foundation.pojo.response.materialList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by caiyu on 2017/11/2.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialWorkerAuditResponse {
    /**
     * 物料单编号
     */
    private String auditNo;
    /**
     * 提交人姓名
     */
    private String worker;

    /**
     * 料单备注
     */
    private String remark;

    /**
     * 收货人姓名
     */
    private String receiver;

    /**
     * 收货人电话号码
     */
    private String receiverPhone;
    /**
     * /收货市
     */
    private String deliveryCity;
    /**
     * 收货区
     */
    private String deliveryCounty;
    /**
     * 收货街道
     */
    private String deliveryStreet;
    /**
     * 收货小区
     */
    private String residenceName;
    /**
     * 详细地址
     */
    private String detailedAddress;
    /**
     * 物料单商品list
     */
    private List<NormalMaterialListResponse> goodsList;


}
