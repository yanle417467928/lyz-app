package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * create 2018-01-27 16:30
 * desc:
 **/
@Getter
@Setter
@ToString
public class WtaReturnOrderDeliveryClerk {

    private Long id;

    /**
     * 创建时间(wms提供)
     */
    private Date createTime;

    /**
     * 退单号
     */
    private String returnNo;
    /**
     * 仓库编号
     */
    private String warehouseNo;
    /**
     * 配送员编码
     */
    private String driver;
}
