package cn.com.leyizhuang.app.foundation.pojo.request;

import lombok.*;

/**
 * @author Jerry.Ren
 * Notes: 顾客基础信息
 * Created with IntelliJ IDEA.
 * Date: 2017/12/14.
 * Time: 15:05.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSimpleInfo {

    /**
     * 顾客id
     */
    private Long customerId;
    /**
     * 顾客姓名
     */
    private String customerName;
}
