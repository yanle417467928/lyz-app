package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 商品价格表
 *
 * @author Richard
 * Created on 2017-07-12 17:26
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsPrice {

    private Long gpid;

    //门店id
    private Long storeId;

    //商品id
    private Long gid;

    //商品编码
    private String sku;

    //价目表行id，来自hq系统
    private Long priceLineId;

    //会员价
    private Double VIPPrice;

    //零售价
    private Double retailPrice;

    //经销价
    private Double wholesalePrice;

    // 生效起始时间
    private LocalDateTime startTime;

    // 生效结束时间
    private LocalDateTime endTime;

    //商品价目类型（COMMON<一般>，A<专供A>，B<专供B>）
    private String priceType;

}
