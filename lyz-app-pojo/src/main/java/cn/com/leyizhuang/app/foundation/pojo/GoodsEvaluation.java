package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.util.Date;

/**
 * @author caiyu
 * @date 2017/11/16
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluation {
    /**
     * 自增id
     */
    private Long id;
    /**
     * 商品id
     */
    private Long gid;
    /**
     * 评价内容
     */
    private String commentContent;
    /**
     * 评价图片
     */
    private String evaluationPictures;
    /**
     * 评价时间
     */
    private Date evaluationTime;
    /**
     * 评价人
     */
    private String evaluationName;
    /**
     * 评价人头像
     */
    private String  picUrl;
    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 是否显示
     */
    private Boolean isShow;
}
