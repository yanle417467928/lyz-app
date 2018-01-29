package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import cn.com.leyizhuang.app.core.constant.ReturnOrderType;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes:APP~WMS退单取消请求接口
 * Created with IntelliJ IDEA.
 * Date: 2017/12/21.
 * Time: 10:31.
 */
@Getter
@Setter
@ToString
public class AtwCancelReturnOrderRequest {

    private Long id;
    /**
     * 创建时间（本条数据创建时间）
     */
    private Date createTime;
    /**
     * 门店编码
     */
    private String storeCode;
    /**
     * 退单号
     */
    private String returnNo;
    /**
     * 退单类型
     */
    private ReturnOrderType returnType;
    /**
     * 退货时间
     */
    private Date returnTime;
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

    public static AtwCancelReturnOrderRequest transform(ReturnOrderBaseInfo returnOrderBaseInfo) {
        AtwCancelReturnOrderRequest atwCancelOrderRequest = new AtwCancelReturnOrderRequest();
        atwCancelOrderRequest.setReturnNo(returnOrderBaseInfo.getReturnNo());
        atwCancelOrderRequest.setReturnType(returnOrderBaseInfo.getReturnType());
        atwCancelOrderRequest.setReturnTime(returnOrderBaseInfo.getReturnTime());
        atwCancelOrderRequest.setCreateTime(new Date());
        atwCancelOrderRequest.setStoreCode(returnOrderBaseInfo.getStoreCode());

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
