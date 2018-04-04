package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

/**
 * 个人销量统计响应类
 * Created by panjie on 2018/3/15.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellDetailsResponse {

    /**
     * 主键id
     */
    private Long lineId;

    /**
     * 导购id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 目标数
     */
    private Integer targetQty;

    /**
     * 完成数
     */
    private Integer finishQty;

    /**
     * 完成率
     */
    private Double finishChance;

    /**
     * 周完成详情
     */
    private List<SellDetailsWeekFinishResponse> weekFinishDetails;

    private String flag;

}
