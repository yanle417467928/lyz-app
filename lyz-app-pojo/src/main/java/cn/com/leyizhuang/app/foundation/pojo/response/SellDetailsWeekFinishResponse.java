package cn.com.leyizhuang.app.foundation.pojo.response;

import java.time.LocalDateTime;

/**
 * Created by panjie on 2018/3/27.
 */
public class SellDetailsWeekFinishResponse {

    private Long id;

    /**
     * 导购Id
     */
    private Long sellerId;

    /**
     * 周数
     */
    private Integer week;

    /**
     * 开始时间
     */
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    private LocalDateTime endDate;

    /**
     * 完成数量
     */
    private Integer finishQty;
}
