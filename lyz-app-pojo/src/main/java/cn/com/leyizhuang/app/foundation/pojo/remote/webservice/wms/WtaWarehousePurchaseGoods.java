package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * create 2018-02-23 11:06
 * desc:仓库采购明细
 **/
@Getter
@Setter
@ToString
public class WtaWarehousePurchaseGoods {

    private Long id;
    /**
     * 验收行id
     */
    private Long recId;
    /**
     * 验收单号
     */
    private String recNo;
    /**
     * 验收数量
     */
    private Integer recQty;
    /**
     * 商品编码
     */
    private String sku;
}
