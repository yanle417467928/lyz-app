package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.io.Serializable;

/**
 * 导购获取产品券客户接口返回对象
 *
 * @author Richard
 * Created on 2017-11-24 17:18
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductCouponCustomer implements Serializable {

    private static final long serialVersionUID = -8738342779970663140L;

    private Long cusId;

    private String name;

    private String mobile;
}
