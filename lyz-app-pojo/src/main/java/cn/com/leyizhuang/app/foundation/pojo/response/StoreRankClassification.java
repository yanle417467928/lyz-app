package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/5/2
 */
@ToString
@Setter
@Getter
public class StoreRankClassification {
    //等级名称
    private String rankName;
    //等级编码
    private String rankCode;
    //等级图片
    private String rankUrl;
}
