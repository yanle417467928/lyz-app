package cn.com.leyizhuang.app.foundation.vo.management.customer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 顾客乐币类
 * @author GenerationRoad
 * @date 2018/1/13
 */
@Getter
@Setter
@ToString
public class CustomerLebiVO {
    private  Long id;
    //顾客ID
    private Long cusId;
    //门店名称
    private String storeName;
    //真实姓名
    private String name;
    //手机号码
    private String mobile;
    //数量
    private Integer quantity = 0;

}
