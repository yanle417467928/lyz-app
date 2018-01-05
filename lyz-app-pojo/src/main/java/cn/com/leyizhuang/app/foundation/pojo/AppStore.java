package cn.com.leyizhuang.app.foundation.pojo;


import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author CrazyApeDX
 * Created on 2017/3/24.
 */
@Getter
@Setter
@ToString
public class AppStore {

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 所在城市id
     */
    private Long cityId;
    /**
     * 创建类型
     */
    private String creatorType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改类型
     */
    private String modifierType;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 门店类型
     */
    private StoreType storeType;
    /**
     *  门店名称
     */
    private String storeName;
    /**
     * 门店编码
     */
    private String storeCode;
    /**
     * 是否默认门店
     */
    private Boolean isDefault;
    /**
     * 城市code
     */
    private String cityCode;
    /**
     *  门店座机号码
     */
    private String phone;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String area;
    /**
     *  门店详细地址
     */
    private String detailedAddress;
    /**
     * 是否生效
      */
    private Boolean enable;
    /**
     * 是否支持门店自提
     */
    private Boolean isSelfDelivery;

    /**
     * 门店组织全编码
     */
    private String storeStructureCode;
    /**
     * 门店组织id
     */
    private String storeOrgId;
    /**
     * 分公司id
     */
    private Long sobId;


}
