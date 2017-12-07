package cn.com.leyizhuang.app.foundation.pojo.activity;


import lombok.*;

import java.util.List;

/**
 * 计算促销后返回的结果
 * Created by panjie on 2017/12/5.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActResultInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 促销id
     */
    private Long actId;

    /**
     * 立减金额
     */
    private Double discount;

    /**
     * 赠品明细
     */
    List<ActGiftDetailsDO> giftDetails;
}
