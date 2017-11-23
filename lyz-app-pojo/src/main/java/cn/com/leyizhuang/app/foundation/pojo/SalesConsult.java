package cn.com.leyizhuang.app.foundation.pojo;


import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Richard
 * Created on 2017/3/24.
 */
@Getter
@Setter
@ToString
public class SalesConsult extends BaseDO {

    private static final long serialVersionUID = -2016854268132811202L;

    private String consultName;
    private String consultMobilePhone;
    private Long ascriptionStoreId;

    public SalesConsult() {
        super();
    }

    public SalesConsult(String consultName, String consultMobilePhone, Long ascriptionStoreId) {
        this.consultName = consultName;
        this.consultMobilePhone = consultMobilePhone;
        this.ascriptionStoreId = ascriptionStoreId;
    }
}
