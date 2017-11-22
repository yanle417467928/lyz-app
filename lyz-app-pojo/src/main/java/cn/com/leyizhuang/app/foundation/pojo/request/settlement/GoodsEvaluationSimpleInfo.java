package cn.com.leyizhuang.app.foundation.pojo.request.settlement;

import lombok.*;

/**
 * 商品评价信息，用于前后台商品信息交互
 *
 * @author caiyu
 * @date 2017/11/16
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluationSimpleInfo {
    /**
     * 商品Id
     */
    private Long gid;
    /**
     * 商品评价内容
     */
    private String commentContent;

    /**
     * 商品评价图片
     */
    private String evaluationPictures;
}
