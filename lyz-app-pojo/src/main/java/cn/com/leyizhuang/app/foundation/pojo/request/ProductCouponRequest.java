package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes: 使用产品券的请求实体
 * Created with IntelliJ IDEA.
 * Date: 2017/11/21.
 * Time: 15:06.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductCouponRequest implements Serializable{

    /**
     * 顾客ID
     */
    private Long userId;
    /**
     * 身份类型
     */
    private Integer identityType;
    /**
     * 使用的产品券ID和数量
     */
    private List<GoodsIdQtyParam> productCouponList;
}
