package cn.com.leyizhuang.app.foundation.pojo.request.settlement;

import lombok.*;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * 生成订单时产品券信息封装类
 *
 * @author Richard
 * Date: 2017/11/13.
 * Time: 10:55.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductCouponSimpleInfo implements Serializable {

    /**
     * 产品券商品id
     */
    private Long id;

    /**
     * 产品券商品数量
     */
    private Integer qty;
}
