package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.CancelProcessingStatus;
import lombok.*;

/**
 * 取消订单参数保存类
 * Created by caiyu on 2018/1/29.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CancelOrderParametersDO {
    /**
     * 自增主键
     */
   private Long id;
    /**
     * 订单号
     */
   private String orderNumber;
    /**
     * 用户id
     */
   private Long userId;
    /**
     * 用户类型
     */
   private Integer identityType;
    /**
     *取消原因
     */
   private String reasonInfo;
    /**
     * 备注
     */
   private String remarksInfo;

    /**
     * 取消处理状态
     */
    private CancelProcessingStatus cancelStatus;
}
