package cn.com.leyizhuang.app.foundation.pojo;

import lombok.*;

import java.io.Serializable;

/**
 * 现金券 -- 品牌 中间表
 * Created by panjie on 2018/1/4.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CashCouponBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    private  Long id;

    private  Long ccid;

    private  Long brandId;

    private  String brandName;
}
