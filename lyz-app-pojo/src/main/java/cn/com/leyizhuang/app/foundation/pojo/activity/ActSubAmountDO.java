package cn.com.leyizhuang.app.foundation.pojo.activity;

import lombok.*;

import java.io.Serializable;

/**
 * 立减促销明细实体
 * Created by panjie on 2017/11/22.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActSubAmountDO implements Serializable{

    private static final long serialVersionUID;

    static {
        serialVersionUID = 1L;
    }

    private Long id;

    // 促销ID
    private Long actId;

    // 促销代号
    private String actCode;

    // 立减金额
    private Double subAmount;

    // 固定金额
    private Double fixedAmount;

    // 折扣比例(9.9\8.8\7.7)
    private Double discount;
}
