package cn.com.leyizhuang.app.foundation.pojo.order;

import lombok.*;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * 简单商品信息，用于前后台商品信息交互
 * @author Jerry.Ren
 * Date: 2017/11/2.
 * Time: 10:55.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSimpleInfo implements Serializable {
    /**
     * 商品Id
     */
    private Long id;
    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 是否赠品
     */
    private Boolean isGift = Boolean.FALSE;
}