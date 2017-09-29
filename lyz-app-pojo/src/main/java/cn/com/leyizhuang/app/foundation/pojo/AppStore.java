package cn.com.leyizhuang.app.foundation.pojo;


import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
@Getter
@Setter
@ToString
public class AppStore extends BaseDO{


    private static final long serialVersionUID = 8159082443377417556L;

    public AppStore() {
        super();
    }


    //门店名称
    private String storeName;

    //门店编码
    private String storeCode;

    //是否默认门店
    private Boolean isDefault;

    //城市id
    private Long cityId;




}