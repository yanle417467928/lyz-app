package cn.com.leyizhuang.app.foundation.pojo.user;

import lombok.*;

import java.util.Date;

/**
 * 顾客--等级 中间类
 * @author GenerationRoad
 * @date 2018/3/6
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CusRankDO {
    private Long id;
    //顾客id
    private Long cusId;
    //等级id
    private Long rankId;
    //编号
    private String number;
    //创建时间
    private Date createTime;
}
