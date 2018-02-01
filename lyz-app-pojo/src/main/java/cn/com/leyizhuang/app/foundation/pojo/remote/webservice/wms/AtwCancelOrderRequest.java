package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes:APP~WMS订单取消请求接口
 * Created with IntelliJ IDEA.
 * Date: 2017/12/21.
 * Time: 10:31.
 */
@Getter
@Setter
@ToString
public class AtwCancelOrderRequest {

    private Long id;
    /**
     * 创建时间（本条数据创建时间）
     */
    private Date createTime;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 订单状态
     */
    private AppOrderStatus orderStatus;
    /**
     * 取消时间
     */
    private Date cancelTime;
    /**
     * 取消原因
     */
    private String cancelReason;
    /**
     * 接口传输标识
     */
    private Boolean sendFlag;
    /**
     * 接口错误信息
     */
    private String errorMessage;
    /**
     * wms收到信息时间
     */
    private Date sendTime;

    public static AtwCancelOrderRequest transform(String orderNo, String cancelReason, AppOrderStatus appOrderStatus) {
        AtwCancelOrderRequest atwCancelOrderRequest = new AtwCancelOrderRequest();
        atwCancelOrderRequest.setOrderNo(orderNo);
        atwCancelOrderRequest.setCancelReason(cancelReason);
        atwCancelOrderRequest.setCancelTime(new Date());
        atwCancelOrderRequest.setCreateTime(new Date());
        atwCancelOrderRequest.setOrderStatus(appOrderStatus);

        return atwCancelOrderRequest;
    }

    public void setErrorStatus(String errorMessage) {
        if (errorMessage != null) {
            this.setSendFlag(false);
            this.setErrorMessage(errorMessage);
        } else {
            this.setSendTime(new Date());
            this.setSendFlag(true);
        }
    }
}
