package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

/**
 * Created by caiyu on 2018/4/28.
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManageUpdateCustomerTypeResponse {
    /**
     * 顾客id
     */
    private Long cusId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 电话
     */
    private String phone;
    /**
     * 性别
     */
    private String sex;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 会员类型
     */
    private String memberType;
}
