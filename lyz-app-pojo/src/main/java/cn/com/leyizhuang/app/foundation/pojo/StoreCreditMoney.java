package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 门店信用金
 *
 * @author Richard
 * Created on 2017-10-19 16:06
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StoreCreditMoney {

    private Long id;

    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 固定信用额度
     */
    private Double creditLimit;
    /**
     * 信用额度余额
     */
    private Double creditLimitAvailable;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 上次更新时间
     */
    private Timestamp lastUpdateTime;


}
