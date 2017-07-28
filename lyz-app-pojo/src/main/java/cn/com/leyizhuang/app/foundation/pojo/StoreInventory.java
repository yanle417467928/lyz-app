package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 门店库存业务实体
 *
 * @author Richard
 *         Created on 2017-07-12 13:52
 **/
@Getter
@Setter
@ToString
public class StoreInventory extends BaseDO{

    //门店ID
    private Long storeId;

    //商品ID
    private Long goodsId;

    //真实库存量
    private Long realInventory;

    //可售库存量
    private Long soldInventory;

}
