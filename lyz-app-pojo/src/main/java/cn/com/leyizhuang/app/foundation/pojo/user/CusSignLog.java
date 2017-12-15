package cn.com.leyizhuang.app.foundation.pojo.user;

import lombok.*;

import java.util.Date;

/**
 * 顾客签到日志
 *
 * @author Richard
 * Created on 2017-12-14 17:17
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CusSignLog {

    private Long id;

    /**
     * 顾客id
     */
    private Long cusId;

    /**
     * 顾客姓名
     */
    private String cusName;

    /**
     * 签到时间
     */
    private Date signTime;

    /**
     * 奖励乐币数量
     */
    private Integer awardLebiQty;

    /**
     * 签到奖励说明
     */
    private String description;

}
