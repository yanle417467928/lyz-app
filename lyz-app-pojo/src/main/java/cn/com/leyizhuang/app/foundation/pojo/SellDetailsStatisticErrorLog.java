package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.util.Date;

/**
 * 统计导购业绩失败后记录的日子  高端桶数，活跃会员数，新开发高端会员......
 * Created by panjie on 2018/4/12.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellDetailsStatisticErrorLog {

    private Long id;

    private Date createTime;

    private Long sellerId;

    private String errorMsg;

    // 是否处理
    private Boolean isDispose = false;

    private String flag;
}
