package cn.com.leyizhuang.app.foundation.pojo.message;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.AppSystemType;
import lombok.*;

import java.util.Date;

/**
 * app用户手机信息表
 *
 * @author Richard
 * Created on 2017-12-07 10:59
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDevice {

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户身份类型
     */
    private AppIdentityType identityType;

    /**
     * 手机系统类型
     */
    private AppSystemType systemType;

    /**
     * 客户端编号
     */
    private String clientId;

    /**
     * 设备编号
     */
    private String deviceId;

    /**
     * 创建时间
     */
    private Date createTime;
}
