package cn.com.leyizhuang.app.foundation.pojo.user;

import lombok.*;

/**
 * 顾客乐币账户
 *
 * @author Richard
 * Created on 2017-10-19 15:58
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLeBi {


    private Long id;
    private Long cusId;
    private Integer quantity;
    private Double rebate;

    public CustomerLeBi(Integer quantity, Double rebate) {
        this.quantity = quantity;
        this.rebate = rebate;
    }
}
