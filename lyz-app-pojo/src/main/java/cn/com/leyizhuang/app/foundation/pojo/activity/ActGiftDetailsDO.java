package cn.com.leyizhuang.app.foundation.pojo.activity;

import lombok.*;

import java.io.Serializable;

/**
 * 促销赠品明细实体
 * Created by panjie on 2017/11/22.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActGiftDetailsDO implements Serializable{
    private static final long serialVersionUID;

    static {
        serialVersionUID = 1L;
    }

    private Long id;

    // 促销ID
    private Long actId;

    // 促销代号
    private String actCode;

    // 赠品id
    private Long giftId;

    // 赠品sku
    private String giftSku;

    // 赠品标题
    private String giftTitle;

    // 赠品价格
    private Double giftPrice;

    // 赠品固定数量
    private Integer giftFixedQty = 0;

    // 产品券id
    private Long proCouponId;

    // 产品券数量
    private Integer proCouponQty = 0;

    // 现金券id
    private Long cashCouponId;

    // 现金券数量
    private Long cashCouponQty = 0L;


}
