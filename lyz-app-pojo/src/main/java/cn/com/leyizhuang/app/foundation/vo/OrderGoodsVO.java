package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.foundation.pojo.goods.GoodsDO;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *  订单商品信息
 * @author GenerationRoad
 * @date 2017/9/6
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderGoodsVO {
    private static final Logger LOG = LoggerFactory.getLogger(OrderGoodsVO.class);

    /**
     * 商品ID
     */
    private Long gid;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     *  商品编码
     */
    private String sku;
    /**
     * 零售价
     */
    private Double retailPrice;
    /**
     * 会员价
     */
    private Double vipPrice;
    /**
     *  经销价
     */
    private Double wholesalePrice;
    /**
     * 公司标识
     */
    private String companyFlag;
    /**
     *  价目表行id
     */
    private Long priceItemId;
    /**
     * 数量
     */
    private Integer qty;




}
