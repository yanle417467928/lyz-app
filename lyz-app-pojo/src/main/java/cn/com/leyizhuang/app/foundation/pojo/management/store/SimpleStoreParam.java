package cn.com.leyizhuang.app.foundation.pojo.management.store;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * Created with IntelliJ IDEA.
 * 后台简单城门店信息
 *
 * @author liuh
 * Date: 2017/12/24.
 * Time: 10:55.
 */
@ToString
@Getter
@Setter
public class SimpleStoreParam {

    private Long storeId;
   //门店名称
    private String storeName;

    // 是否被选中
    private Boolean isSelected;
}
