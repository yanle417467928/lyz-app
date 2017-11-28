package cn.com.leyizhuang.app.foundation.pojo.request.settlement;

import cn.com.leyizhuang.app.foundation.pojo.request.GoodsIdQtyParam;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 生成订单时 促销相关信息封装类
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
public class PromotionSimpleInfo implements Serializable {


    private static final long serialVersionUID = 2056782746751737227L;

    /**
     * 促销id
     */
    private Long promotionId;

    /**
     * 立减金额
     */
    private Double discount;

    /**
     * 赠品明细
     */
    List<GoodsIdQtyParam> presentInfo;

}
