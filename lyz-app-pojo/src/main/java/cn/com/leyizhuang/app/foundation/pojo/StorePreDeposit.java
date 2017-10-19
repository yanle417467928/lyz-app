package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * 门店预存款
 *
 * @author Richard
 * Created on 2017-10-19 16:09
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StorePreDeposit {

    private Long id;

    private Long storeId;

    private Double balance;


}
