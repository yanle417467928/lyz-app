package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * 门店赞助金
 *
 * @author Richard
 * Created on 2017-10-19 16:10
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StoreSubvention {

    private Long id;

    private Long storeId;

    private Double balance;


}
