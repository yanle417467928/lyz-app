package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.ProductCouponGetType;
import lombok.*;

import java.util.Date;

/**
 * 门店产品券
 *
 * @author Richard
 * Created on 2017-10-19 16:12
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductCoupon {

    private Long id;

    private Long storeId;

    private Long gid;

    private Integer quantity;

    private Date createTime;

    private ProductCouponGetType getType;

    private String getOrdNo;

    private Double buyPrice;

    private Date effectiveStartTime;

    private Date effectiveEndTime;

    private Boolean isUsed;

    private Date useTime;

    private String useOrdNo;
}
