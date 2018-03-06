package cn.com.leyizhuang.app.foundation.pojo.management.order;

import cn.com.leyizhuang.app.core.constant.StoreType;
import lombok.*;

/**
 * 后台买券选择导购返回类
 * Created by caiyu on 2018/1/25.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaEmployeeResponse {
    /**
     * 员工id
     */
    private Long empId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 门店类型
     */
    private StoreType storeType;

    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 门店类型
     */
    private String storeCode;

    /**
     * 门店预存款余额
     */
    private Double balance;

    /**
     * 员工登录名
     */
    private String loginName;
}
