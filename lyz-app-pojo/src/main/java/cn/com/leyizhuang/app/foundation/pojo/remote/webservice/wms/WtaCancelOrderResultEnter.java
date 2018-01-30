package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * create 2018-01-27 16:19
 * desc: 取消订单结果确认
 **/
@Getter
@Setter
@ToString
public class WtaCancelOrderResultEnter {

    private Long id;

    /**
     * 创建时间(wms提供)
     */
    private Date createTime;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 是否已退
     */
    private String isCancel;

    /**
     * 接口错误信息
     */
    private String errorMessage;
}
