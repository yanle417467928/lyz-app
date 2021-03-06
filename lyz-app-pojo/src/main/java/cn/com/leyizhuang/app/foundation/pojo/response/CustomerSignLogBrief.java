package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 顾客签到日志简要
 *
 * @author Richard
 * Created on 2017-12-14 17:32
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSignLogBrief implements Serializable{


    private static final long serialVersionUID = -6631708612837160074L;

    /**
     * 签到日期
     */
    private Date signTime;

    /**
     * 签到奖励
     */
    private Integer award;

    /**
     * 说明
     */
    private String description;


}
