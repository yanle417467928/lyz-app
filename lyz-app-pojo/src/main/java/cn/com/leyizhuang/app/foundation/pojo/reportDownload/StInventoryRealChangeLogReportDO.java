package cn.com.leyizhuang.app.foundation.pojo.reportDownload;

import lombok.*;

/**
 * @Author Richard
 * @Date 2018/7/20 10:52
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StInventoryRealChangeLogReportDO {

    /**
     * 商品名称
     */
    public String skuName;

    /**
     * 商品编码
     */
    public String sku;

    /**
     * 门店
     */
    public String store;

    /**
     *变更数量
     */
    public Integer changeQty;

    /**
     * 变更后数量
     */
    public Integer afterChangeQty;

    /**
     * 变更类型
     */
    public String changeTypeDesc;

    /**
     * 相关订单号
     */
    public String referenceNumber;

    /**
     * 退单单号
     */
    public String returnNumber;

    /**
     * 变更时间
     */
    public String changeTime;

    /**
     * 初始数量
     */
    public Integer initialQty;
}
