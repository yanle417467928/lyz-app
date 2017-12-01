package cn.com.leyizhuang.app.foundation.pojo.activity;

import lombok.*;

import java.io.Serializable;

/**
 * 阶梯促销实体
 * Created by panjie on 2017/11/22.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActLadderDO implements Serializable{

    private static final long serialVersionUID;

    static {
        serialVersionUID = 1L;
    }

    private Long id;

    // 促销ID
    private Long actId;

    // 促销代号
    private String actCode;

    // 最小数量
    private Integer minQty;

    // 最大数量
    private Integer maxQty;

    // 最小金额
    private Double minAmount;

    // 最大金额
    private Double maxAmount;

    // 立减金额
    private Double subAmount;

    // 固定金额
    private Double fixedAmount;

    // 折扣比例(9.9\8.8\7.7)
    private Double discount;
}
