package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Created by panjie on 2018/4/3.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SellDetailsSingleDO {

    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 年份
      */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 导购id
     */
    private Long sellerId;

    /**
     * 姓名
     */
    private String sellerName;

    /**
     * 目标数
     */
    private Integer targetQty = 0;

    /**
     * 完成数
     */
    private Integer finishQty;

    /**
     * 完成率
     */
    private Double finishChance;

    /**
     * 组织id
     */
    private Long structureId;

    /**
     * 组织id
     */
    private String structureCode;

    /**
     * 组织名
     */
    private String structureName;

    /**
     * 标志:‘TS’ 桶数；‘HYS’:活跃数 ; 'XKF' ： 新开发会员数 ；"XL" ： 销量
     */
    private String flag;

    /**
     * hr 目标销量
     */
    private Double targetSales;

    /**
     * hr 完成销量
     */
    private Double finishSales;

    /**
     * lyz 目标销量
     */
    private Double lyzTargetSales;

    /**
     * lyz 完成销量
     */
    private Double lyzFinishSales;


}
