package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * create 2018-02-23 11:53
 * desc:仓库报损报溢
 **/
@Getter
@Setter
@ToString
public class WtaWarehouseReportDamageAndOverflow {

    private Long id;
    /**
     * 仓库编号
     */
    private String warehouseNo;
    /**
     * 委托业主
     */
    private String ownerNo;
    /**
     * 报损单号
     */
    private String wasteNo;
    /**
     * 报损行ID
     */
    private Long wasteId;
    /**
     * 报损类型
     */
    private String wasteType;
    /**
     * 商品编码
     */
    private String sku;
    /**
     * 商品数量
     */
    private Integer qty;
    /**
     * 报损状态
     */
    private String wasteStatus;
    /**
     * 城市编码
     */
    private String companyId;
}
