package cn.com.leyizhuang.app.foundation.pojo.request.settlement;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *  生成订单时产品券信息封装类
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

    private String sku;

    private Integer qty;
}
