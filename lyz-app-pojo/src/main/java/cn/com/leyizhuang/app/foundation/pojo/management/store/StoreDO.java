package cn.com.leyizhuang.app.foundation.pojo.management.store;

import cn.com.leyizhuang.app.foundation.pojo.management.city.SimpleCityParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
/**
 * @author liuh
 * @date 2017/11/4
 */
@ToString
@Getter
@Setter
public class StoreDO {

    private Long storeId;

    // 创建类型
    private String creatorType;

    // 创建时间
    private Date createTime;

    // 上一次修改类型
    private String modifierType;

    // 上一次修改时间
    private Date modifyTime;

    //门店所在城市编码
    private SimpleCityParam cityCode;

    //门店名称
    private String storeName;

    //门店编码
    private String storeCode;

    //是否默认
    private Boolean isDefault;

    //门店类型
    private String storeType;

    //门店电话
    private String phone;

    //省
    private String province;

    //市
    private String city;

    //区
    private String area;

    //详细地址
    private String detailedAddress;

    // 是否生效
    private Boolean enable;

    // 是否支持门店自提
    private Boolean isSelfDelivery;

    // 销售经理
    private String salesManager;

    //装饰公司类型
    private  String company;

    //销售员
    private  String seller;



}
