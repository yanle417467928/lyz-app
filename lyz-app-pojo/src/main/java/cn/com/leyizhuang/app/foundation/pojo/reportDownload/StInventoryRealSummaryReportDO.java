package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import lombok.*;

/**
 * @Date 2018/7/27 15:40
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StInventoryRealSummaryReportDO {

    /**
     * 城市
     */
    public String city;
    /**
     *门店
     */
    public String store;
    /**
     *商品编码
     */
    public String sku;
    /**
     *商品名称
     */
    public String skuName;
    /**
     *变更类型
     */
    public String changeType;
    /**
     *变更类型描述
     */
    public String changeTypeDesc;
    /**
     *自提单退货
     */
    public Integer storeOrderQty;
    /**
     *自提单发货
     */
    public Integer storeReturnQty;
    /**
     *门店要货
     */
    public Integer storeImportGoodsQty;
    /**
     *门店要货退货
     */
    public Integer storeExportGoodsQty;
    /**
     *门店调拨入库
     */
    public Integer storeAllocateInBoundQty;
    /**
     *门店调拨出库
     */
    public Integer storeAllocateOutBoundQty;
    /**
     *盘点入库
     */
    public Integer storeInventoryInboundQty;
    /**
     *盘点出库
     */
    public Integer storeInventoryOutboundQty;
    /**
     *期初库存数
     */
    public Integer interInventoryQty;
    /**
     *期末库存数
     */
    public Integer afterInventoryQty;
}
