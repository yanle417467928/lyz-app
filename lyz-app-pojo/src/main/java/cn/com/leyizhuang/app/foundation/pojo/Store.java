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
public class Store extends BaseDO{


    private static final long serialVersionUID = 8159082443377417556L;

    public Store() {
        super();
    }

    public Store( String storeName, String storeCode) {
        super();

        this.storeName = storeName;
        this.storeCode = storeCode;
    }

    private String storeName;
    private String storeCode;

}
