package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 物流详情放回类
 * Created by caiyu on 2017/11/20.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsDetailResponse {
    /**
     * 提示语
     */
    private String describe;
    /**
     * 物流状态
     */
    private String logisticsType;
    /**
     * 执行时间
     */
    private String createTime;

    //图片
    private List<String> pictures;

}
