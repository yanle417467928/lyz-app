package cn.com.leyizhuang.app.foundation.pojo.management;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import lombok.*;

import java.util.Date;

/**
 * Created by caiyu on 2018/6/16.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaterialChangeHeadLog {
    private Long id;

    private Long userId;

    private AppIdentityType identityType;

    private Date createTime;

    private Long updatePeopleId;
}
