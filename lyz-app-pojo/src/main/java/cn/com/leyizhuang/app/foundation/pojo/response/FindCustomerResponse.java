package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * 代下单时查找顾客返回类
 * Created by caiyu on 2018/3/9.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FindCustomerResponse {
    /**
     * 顾客id
     */
    private Long cusId;
    /**
     * 顾客电话
     */
    private String cusPhone;
    /**
     * 顾客姓名
     */
    private String cusName;
}
