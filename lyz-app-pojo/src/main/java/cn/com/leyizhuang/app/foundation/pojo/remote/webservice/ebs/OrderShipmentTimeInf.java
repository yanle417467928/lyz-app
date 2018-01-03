package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppDeliveryType;
import cn.com.leyizhuang.app.core.constant.ProductType;
import lombok.*;

import java.util.Date;

/**
 * 门店自提订单出货时间
 *
 * @author Richard
 * Created on 2018-01-02 13:39
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderShipmentTimeInf {

    private Long id;

    /**
     * 订单（主单）号
     */
    private String mainOrderNumber;

    /**
     * 分公司 id
     */
    private String sobId;

    /**
     * 发货日期
     */
    private Date shipmentTime;

}
