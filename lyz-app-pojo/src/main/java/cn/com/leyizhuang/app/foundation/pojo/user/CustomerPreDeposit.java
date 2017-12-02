package cn.com.leyizhuang.app.foundation.pojo.user;

import lombok.*;

import java.util.Date;

/**
 * 顾客预存款账户
 *
 * @author Richard
 * Created on 2017-10-19 16:01
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPreDeposit {

    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 上次更新时间
     */
    private Date lastUpdateTime;
    /**
     * 顾客id
     */
    private Long cusId;
    /**
     * 余额
     */
    private Double balance;
}
