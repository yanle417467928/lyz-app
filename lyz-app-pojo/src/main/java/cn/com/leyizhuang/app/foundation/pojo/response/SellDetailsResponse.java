package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

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
     * 人员id
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

}