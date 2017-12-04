package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.util.Date;

/**
 * 门店赞助金
 *
 * @author Richard
 * Created on 2017-10-19 16:10
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StoreSubvention {

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
     * 门店id
     */
    private Long storeId;
    /**
     * 余额
     */
    private Double balance;


}
