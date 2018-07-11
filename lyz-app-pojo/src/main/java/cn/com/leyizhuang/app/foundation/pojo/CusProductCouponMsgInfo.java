package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.app.core.constant.CouponGetType;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品券信息类
 *
 * @author liuh
 * Created on 2017-09-29 10:00
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CusProductCouponMsgInfo implements Serializable {

    /**
     * 电话
     */
    private String mobile;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 失效时间
     */
    private Date effectiveEndTime;
}
