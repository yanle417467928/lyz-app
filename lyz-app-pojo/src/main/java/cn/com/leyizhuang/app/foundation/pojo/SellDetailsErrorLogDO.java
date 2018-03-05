package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.util.Date;

/**
 * 销量记录失败 日志类
 * Created by panjie on 2018/3/5.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellDetailsErrorLogDO {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 记录时间
     */
    private Date recordTime;

    /**
     * 状态
     */
    private Boolean status;
}
