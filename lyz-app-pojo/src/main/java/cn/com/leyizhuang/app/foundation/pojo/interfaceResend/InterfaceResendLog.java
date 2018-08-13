package cn.com.leyizhuang.app.foundation.pojo.interfaceResend;

import cn.com.leyizhuang.app.core.constant.AppIdentityType;
import cn.com.leyizhuang.app.core.constant.InterfaceResendChangeType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InterfaceResendLog {

    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 发送系统
     */
    private String sendSystem;

    /**
     * 相关单号
     */
    private String referenceNumber;
    /**
     * 变更类型
     */
    private InterfaceResendChangeType changeType;

    /**
     * 接口返回信息
     */
    private String message;
    /**
     * 变更类型描述
     */
    private String changeTypeDesc;

    /**
     * 是否操作成功
     */
    private Boolean isSuccess ;
    /**
     * 操作人员id
     */
    private Long operatorId;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作人员ip
     */
    private String operatorIp;
    /**
     * 备注
     */
    private String remark;
}
