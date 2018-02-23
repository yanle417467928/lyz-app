package cn.com.leyizhuang.app.foundation.pojo.management.returnOrder;

import lombok.*;
/**
 * @author LIUH
 * Notes: 退单物流状态
 * Created with IntelliJ IDEA.
 * Date: 2017/12/8.
 * Time: 15:10.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaReturnOrderProductCouponInfo {

    private Long id;
    //券id
    private Long pcid;
    //商品编码
    private String sku;
    //退券数量
    private Integer returnQty;
    //是否已退
    private Boolean isReturn;
}
