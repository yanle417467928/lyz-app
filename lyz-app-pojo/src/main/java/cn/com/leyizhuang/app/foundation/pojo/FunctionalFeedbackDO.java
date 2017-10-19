package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.FunctionalFeedbackStatusEnum;
import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author GenerationRoad
 * @date 2017/10/10
 */
@Setter
@Getter
@ToString
public class FunctionalFeedbackDO extends BaseDO {
    //类型
    private String type;
    //内容
    private String content;
    //图片信息
    private String pictureUrl;
    //联系电话
    private String phone;
    //状态
    private FunctionalFeedbackStatusEnum status;
    //用户id
    private Long userId;
    //用户类型
    private AppIdentityType identityType;
    //创建时间
    private LocalDateTime createTime;
}
