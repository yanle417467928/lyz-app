package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * create 2018-02-23 9:58
 * desc:仓库调拨明细
 **/

@Getter
@Setter
@ToString
public class WtaWarehouseAllocationGoods {

    private Long id;

    /**
     * 出货数量
     */
    private Integer ackQty;

    /**
     * 验收时间
     */
    private Date checkTime;

    /**
     * 验收数量
     */
    private Integer checkQty;

    /**
     * 商品编码
     */
    private String sku;

    /**
     * 备注
     */
    private String note;

    /**
     * 调拨行ID
     */
    private Long allocationId;

    /**
     * 调拨单号
     */
    private String allocationNo;

    /*
    * 接收时间
    * */
    private Date receiveTime;
}
