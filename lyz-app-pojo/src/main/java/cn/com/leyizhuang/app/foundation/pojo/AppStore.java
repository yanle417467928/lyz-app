package cn.com.leyizhuang.app.foundation.pojo;


import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
@Getter
@Setter
@ToString
public class AppStore{

    private Long storeId;

    private String creatorType;

    private Date createTime;

    private String modifierType;

    private Date modifyTime;

    private StoreType storeType;

    //门店名称
    private String storeName;

    //门店编码
    private String storeCode;

    //是否默认门店
    private Boolean isDefault;

    //城市id
    private Long cityId;

    //城市code
    private String cityCode;



}
