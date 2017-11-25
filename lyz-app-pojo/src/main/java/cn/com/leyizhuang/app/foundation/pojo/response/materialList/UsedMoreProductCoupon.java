package cn.com.leyizhuang.app.foundation.pojo.response.materialList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Jerry.Ren
 * Notes:使用多张产品券时的传输对象
 * Created with IntelliJ IDEA.
 * Date: 2017/11/25.
 * Time: 10:34.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class UsedMoreProductCoupon implements Serializable {

    /**
     * 产品券商品ID
     */
    private Long gid;
    /**
     * 使用数量
     */
    private Integer qty;
    /**
     * 剩余总数量
     */
    private Integer totalQty;
}
