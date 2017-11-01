package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 确认订单页面简单商品信息请求
 * @author Jerry.Ren
 * Date: 2017/11/1.
 * Time: 10:45.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderGoodsSimpleRequest implements Serializable {

    /**
     * 商品Id
     */
    private Long id;
    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 是否赠品
     */
    private Boolean isGift = Boolean.FALSE;
}
