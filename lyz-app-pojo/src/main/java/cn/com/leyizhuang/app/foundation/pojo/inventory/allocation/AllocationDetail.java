package cn.com.leyizhuang.app.foundation.pojo.inventory.allocation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class AllocationDetail {

    private Long id;
    /**
     * 调拨单ID
     */
    private Long allocationId;
    /**
     * 商品id
     */
    private Long gId;
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
    private Long qty;
    /**
     * 真实数量
     */
    private Long realQty;
}
