package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

/**
 * 商品评价返回类
 * Created by caiyu on 2017/11/17.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluationListResponse {
    /**
     * 评价总条数
     */
    private Integer evaluationQuantity;
    /**
     * 评价内容
     */
    private String commentContent;
    /**
     * 评价图片
     */
    private List<String> evaluationPictures;
    /**
     * 评价人
     */
    private String evaluationName;
    /**
     * 是否显示
     */
    private Boolean isShow;
    /**
     * 评价人头像
     */
    private String picUrl;
    /**
     * 评价时间
     */
    private String evaluationTime;
}
