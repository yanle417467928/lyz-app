package cn.com.leyizhuang.app.foundation.pojo.inventory.invoicing;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * Notes: 进销存日志视图实体
 * Created with IntelliJ IDEA.
 * Date: 2018/1/15.
 * Time: 11:51.
 */
@Getter
@Setter
@ToString
public class InvoicingVO {

    private Long id;

    /**
     * 变更类型
     */
    private String changType;

    /**
     * 变更门店或城市
     */
    private String changeTarget;

    /**
     * 商品sku
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsTitle;

    /**
     * 变更值
     */
    private Integer changeValue;

    /**
     * 变更时间
     */
    private String changeDate;

    /**
     * 关联单号
     */
    private String referenceOrder;
}
