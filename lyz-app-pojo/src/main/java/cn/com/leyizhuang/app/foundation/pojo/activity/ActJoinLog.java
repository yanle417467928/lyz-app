package cn.com.leyizhuang.app.foundation.pojo.activity;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 促销参与记录
 * Created by 12421 on 2018/7/20.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActJoinLog {

    private Long id;

    private Long actId;

    private LocalDateTime createTime;

    private Long userId;

    private AppIdentityType identityType;
}
