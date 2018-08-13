package cn.com.leyizhuang.app.foundation.pojo.interfaceResend;

import cn.com.leyizhuang.app.core.constant.AllocationTypeEnum;
import cn.com.leyizhuang.app.core.constant.AppOrderStatus;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * ebs未发送订单
 *
 * @author LH
 * Created on 2017-10-10 10:48
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EbsAllocationResendInfo implements Serializable {

    private Long id;

    /**
     * 调拨单号
     */
    private String number;

    /**
     * 调拨状态
     */
    private String status;

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

    public void setStatus(AllocationTypeEnum status) {
        this.status = status.getName();
    }
}

