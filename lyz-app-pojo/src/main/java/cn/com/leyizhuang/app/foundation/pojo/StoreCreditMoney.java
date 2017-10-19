package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * 门店信用金
 *
 * @author Richard
 * Created on 2017-10-19 16:06
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StoreCreditMoney {

    private Long id;

    private Long storeId;

    private Double creditLimit;


}
