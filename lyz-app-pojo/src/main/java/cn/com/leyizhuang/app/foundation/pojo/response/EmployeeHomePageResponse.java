package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Created by Jerry.Ren
 * Date: 2017/10/19.
 * Time: 15:44.
 */
@Setter
@Getter
@ToString
public class EmployeeHomePageResponse implements Serializable {

    //头像地址
    private String picUrl;
    //姓名
    private String name;
    //员工编码
    private String number;
    //账户余额
    private Double balance;
    //员工电话
    private String mobile;
    //配送数量（配送员字段）
    private Integer sendQty;
    //城市ID
    private Long cityId;
}
