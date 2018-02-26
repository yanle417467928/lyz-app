package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * create 2018-02-23 9:28
 * desc: 仓库调拨
 **/

@Getter
@Setter
@ToString
public class WtaWarehouseAllocationHeader {

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
     * 调拨编号
     */
    private String allocationNo;
    /**
     * 调拨类型
     */
    private String allocationType;
    /**
     * 调拨状态
     */
    private String allocationStatus;
    /**
     * 出货仓库编号
     */
    private String shippingWarehouseNo;
    /**
     * 申请调拨编号
     */
    private String poNo;
    /**
     * 申请调拨类型
     */
    private String poType;
    /**
     * 备注
     */
    private String note;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 调入"in"或调出"out"
     */
    private String uploadStatus;
    /**
     * 城市编码
     */
    private String companyId;
}
