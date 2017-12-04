package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.util.Date;

/**
 * 门店预存款
 *
 * @author Richard
 * Created on 2017-10-19 16:09
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StorePreDeposit {

    private Long id;

    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 余额
     */
    private Double balance;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上次更新时间
     */
    private Date lastUpdateTime;
}
