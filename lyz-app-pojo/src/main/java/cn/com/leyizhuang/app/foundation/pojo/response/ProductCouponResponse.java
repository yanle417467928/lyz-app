package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 顾客产品券接口返回对象
 *
 * @author Richard
 * Created on 2017-09-19 13:57
 **/
@Getter
@Setter
@ToString
public class ProductCouponResponse implements Serializable {


    private static final long serialVersionUID = -210446196520449249L;

    private Long  goodsId;

    private String goodsName;

    private String goodsCode;

    private String goodsSpecification;

    private String goodsUnit;

    private Integer leftNumber;

    private String coverImageUri;

    /**
     *  生效开始时间
     */
    private Date effectiveStartTime;

    /**
     * 生效结束时间
     */
    private Date effectiveEndTime;

    /**
     * 是否为专供
     */
    private Boolean isZG = false;
}
