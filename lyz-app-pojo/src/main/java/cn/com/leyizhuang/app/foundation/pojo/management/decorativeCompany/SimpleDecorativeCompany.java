package cn.com.leyizhuang.app.foundation.pojo.management.decorativeCompany;

import cn.com.leyizhuang.app.foundation.pojo.management.store.StoreDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 后台简化装饰公司信息
 *
 * @author liuh
 * Created on 2017-11-03 14:14
 **/

@ToString
@Getter
@Setter
public class SimpleDecorativeCompany {
    private Long id;
    //门店名称
    private String storeName;

}

