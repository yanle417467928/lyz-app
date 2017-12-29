package cn.com.leyizhuang.app.foundation.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 电商App后台门店库存视图模型
 *
 * @author Richard
 * Created on 2017/5/6.
 */
@Getter
@Setter
@ToString
public class AppAdminStoreInventoryVO implements Serializable {

    private static final long serialVersionUID = 7599537144994727142L;

    /**
     * 行记录主键
     */
    private Long id;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 门店编码
     */
    private String storeCode;
    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 真实库存量
     */
    private Long realInventory;

    /**
     * 可售库存量
     */
    private Long soldInventory;

}
