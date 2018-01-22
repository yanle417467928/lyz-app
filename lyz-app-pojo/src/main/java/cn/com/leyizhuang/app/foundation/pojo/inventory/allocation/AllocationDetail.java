package cn.com.leyizhuang.app.foundation.pojo.inventory.allocation;

import lombok.*;

/**
 * @author Jerry.Ren
 * Notes: 门店调拨商品明细实体类
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 10:32.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllocationDetail {

    private Long id;
    /**
     * 调拨单ID
     */
    private Long allocationId;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 商品编码
     */
    private String sku;
    /**
     * 数量
     */
    private Integer qty;
    /**
     * 真实数量
     */
    private Integer realQty;
}
