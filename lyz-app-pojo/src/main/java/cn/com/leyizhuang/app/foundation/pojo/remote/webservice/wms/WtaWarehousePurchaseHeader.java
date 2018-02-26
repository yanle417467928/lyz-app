package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * create 2018-02-23 11:05
 * desc: 仓库采购头档
 **/

@Getter
@Setter
@ToString
public class WtaWarehousePurchaseHeader {

    private Long id;
    /**
     * 仓库号
     */
    private String warehouseNo;
    /**
     * 验收单号
     */
    private String recNo;
    /**
     * 验收汇总单
     */
    private String gatherRecNo;
    /**
     * 进货汇总单
     */
    private String gatherNo;
    /**
     * 进货单号
     */
    private String inNo;
    /**
     * 备注
     */
    private String note;
    /**
     * 采购单号
     */
    private String purchaseNo;
    /**
     * 城市编码
     */
    private String companyId;
}
