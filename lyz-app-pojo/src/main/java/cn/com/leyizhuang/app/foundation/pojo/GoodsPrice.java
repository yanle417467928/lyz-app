package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * 商品价格表
 *
 * @author Richard
 *         Created on 2017-07-12 17:26
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsPrice {

    private Long id;

    //门店id
    private Long storeId;

    //商品id
    private Long goodsId;

    //商品编码
    private String goodsCode;

    //价目表行id，来自hq系统
    private Long priceLineId;

    //会员价
    private Double memberPrice;

    //零售价
    private Double retailPrice;

    //经销价
    private Double distributionPrice;


}
