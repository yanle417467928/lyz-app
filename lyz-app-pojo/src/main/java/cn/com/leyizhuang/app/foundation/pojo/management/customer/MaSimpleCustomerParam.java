package cn.com.leyizhuang.app.foundation.pojo.management.customer;

import lombok.*;

/**
 * @Author caiyu
 * @Date 2018/8/15 11:42
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MaSimpleCustomerParam {

    /**
     * 顾客id
     */
    private Long cusId;

    /**
     * 顾客姓名
     */
    private String cusName;
}
