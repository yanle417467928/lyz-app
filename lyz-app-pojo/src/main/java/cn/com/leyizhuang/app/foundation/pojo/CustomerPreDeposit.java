package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * 顾客预存款账户
 *
 * @author Richard
 * Created on 2017-10-19 16:01
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPreDeposit {

    private Long id;

    private Long cusId;

    private Double balance;
}
