package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.util.Date;

/**
 * Created by caiyu on 2017/11/16.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluation {
    //自增id
    private Long id;
    //订单评价id
    private Long orderEvaluationId;
    //商品id
    private Long gid;
    //评价内容
    private String commentContent;
    //评价图片
    private String evaluationPictures;
    //评价时间
    private Date evaluationTime;
    //评价人
    private String evaluationName;

}
