package cn.com.leyizhuang.app.foundation.pojo.activity;

import lombok.*;

import java.util.Date;

/**
 * @Author Richard
 * @Date 2018/7/11 16:42
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ActMemberConference {
    //自增id
    private Long id;
    //会员id
    private Long cusId;
    //促销id
    private Long actId;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
}
