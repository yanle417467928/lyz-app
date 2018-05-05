package cn.com.leyizhuang.app.foundation.pojo.user;

import lombok.*;

import java.util.Date;

/**
 * 会员等级分类表
 * @author GenerationRoad
 * @date 2018/3/6
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RankClassification {
    private Long rankId;
    //等级名称
    private String rankName;
    //等级编码
    private String rankCode;
    //城市id
    private Long cityId;
    //专供图片
    private String rankUrl;
    //创建时间
    private Date createTime;
}
