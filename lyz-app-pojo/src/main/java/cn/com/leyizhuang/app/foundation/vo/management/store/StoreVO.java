package cn.com.leyizhuang.app.foundation.vo.management.store;

import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 门店VO
 * @author liuh
 * @date 2017/11/4
 */
@ToString
@Getter
@Setter
public class StoreVO {

    private Long storeId;

    //门店所在城市编码
    private SimpleCityParam cityCode;

    //门店名称
    private String storeName;

    //门店类型
    private String storeType;

    //门店编码
    private String storeCode;

    // 是否生效
    private Boolean enable;

    // 门店组织id
    private Long storeOrgId;

    public void setStoreType(StoreType storeType){
        this.storeType = storeType.getDescription();
    }

}
