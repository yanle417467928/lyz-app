package cn.com.leyizhuang.app.foundation.pojo.user;

import lombok.*;

import java.util.Date;

/**
 * 顾客乐币账户
 *
 * @author Richard
 * Created on 2017-10-19 15:58
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLeBi {

    /**
     * 自增主键
     */
    private Long id;
    /**
     * 顾客id
     */
    private Long cusId;
    /**
     * 剩余数量
     */
    private Integer quantity;
    /**
     * (废弃字段)订单可以使用乐币抵扣金额(注:这个字段不作持久化)
     */
    @Deprecated
    private Double rebate;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 上次更新时间
     */
    private Date lastUpdateTime;

}
