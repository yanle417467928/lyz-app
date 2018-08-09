package cn.com.leyizhuang.app.foundation.pojo.goods;

import lombok.*;

import java.time.LocalDateTime;

/**
 * ebs商品价格表
 *
 * @author
 * Created on 2017-07-12 17:26
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EbsGoodsPrice {
    /**
     * 门店编码
     */
    String storeCode ;
    /**
     * 商品编码
     */
    String sku  ;
    /**
     * 会员价
     */
    String VIPPrice;
    /**
     * 零售价
     */
    String retailPrice ;
    /**
     * 经销价
     */
    String wholesalePrice ;
    /**
     * 生效时间
     */
    String startTime;
    /**
     * 失效时间
     */
    String endTime ;
    /**
     * 价目表类型
     */
    String priceType;
    /**
     * 行id
     */
    String lineId ;
    /**
     * 价目表名称
     */
    String priceListName;
}
