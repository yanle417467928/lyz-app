package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * 拍照下单商品类
 * @author GenerationRoad
 * @date 2018/2/22
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PhotoOrderGoodsDO {

    private Long id;
    //商品ID
    private Long gid;
    //商品名称
    private String skuName;
    //商品名称
    private Integer goodsQty;
    //拍照下单单号
    private String photoOrderNo;
    //是否生成订单
    private String isGenerateOrder;
}
