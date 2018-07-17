package cn.com.leyizhuang.app.foundation.pojo.management.store;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author liuh
 * Notes:ebs~app 直营要货退货
 */
@Getter
@Setter
@ToString
public class StoreReturnAndRequireGoodsInf {

    /**
     * 事务类型
     */
    private String transType;
    /**
     *门店事务编号
     */
    private String transNumber;
    /**
     * 门店编号
     */
    private String StoreName;
    /**
     * 事务时间
     */
    private Date shipDate;

    /**
     * 物料编号
     */
    private String itemCode;

    /**
     * 物料名称
     */
    private String skuName;

    /**
     * 数量 "正数"入库，"负数"出库
     */
    private Long quantity;

}
