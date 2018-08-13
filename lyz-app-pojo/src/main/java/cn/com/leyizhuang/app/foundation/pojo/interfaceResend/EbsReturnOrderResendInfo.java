package cn.com.leyizhuang.app.foundation.pojo.interfaceResend;

import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import cn.com.leyizhuang.app.core.constant.AppReturnOrderStatus;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * ebs未发送退单
 *
 * @author LH
 * Created on 2017-10-10 10:48
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EbsReturnOrderResendInfo implements Serializable {

    private Long id;

    /**
     * 订单号
     */
    private String returnNo;

    /**
     * 订单头状态
     */
    private String returnStatus;

    /**
     * 是否生成
     */
    private Boolean isGenerate;

    /**
     * 是否发送成功
     */
    private Boolean isSend;

    /**
     * 错误信息
     */
    private String errorMsg;


    /**
     * 接口数据生成时间
     */
    private Date createTime;

    public void setReturnStatus(AppReturnOrderStatus status) {
        this.returnStatus = status.getDescription();
    }
}

