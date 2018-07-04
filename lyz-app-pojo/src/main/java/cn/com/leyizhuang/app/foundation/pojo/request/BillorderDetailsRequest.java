package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

/**
 * Created by 12421 on 2018/7/4.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BillorderDetailsRequest {
    /**
     * 订单或者退单id
     */
    private Long id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 退单号
     */
    private String returnNo;
    /**
     * 类型
     */
    private String orderType;

}
