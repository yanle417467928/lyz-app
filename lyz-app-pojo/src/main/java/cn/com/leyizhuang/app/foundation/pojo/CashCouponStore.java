package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

/**
 * 现金券 -- 门店
 * Created by panjie on 2018/1/4.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CashCouponStore {

    private static final long serialVersionUID = 1L;

    private  Long id;

    private  Long ccid;

    private  Long storeId;

    private  String storeName;
}
