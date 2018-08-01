package cn.com.leyizhuang.app.foundation.vo.management.decorativeCompany;

import cn.com.leyizhuang.app.core.constant.*;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author liuh
 * @date 2018/1/12
 */
@Getter
@Setter
@ToString
public class FitCreditMoneyChangeLogVO {

    private Long id;
    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 门店编码
     */
    private String storeCode;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 门店类型
     */
    private String storeType;
    /**
     * 生成时间
     */
    private String createTime;
    /**
     * 变更金额
     */
    private Double changeAmount;
    /**
     * 变更后金额
     */
    private Double creditLimitAvailableAfterChange;
    /**
     * 相关单号
     */
    private String referenceNumber;
    /**
     * 变更类型
     */
    private String changeType;
    /**
     * 变更类型描述
     */
    private String changeTypeDesc;

    /**
     * 操作人id
     */
    private String operatorId;
    /**
     * 操作人名称
     */
    private String operatorName;
    /**
     * 操作人员类型
     */
    private String operatorType;
    /**
     * 操作人员ip
     */
    private String operatorIp;
    private String remark;

    public void setChangeType(StoreCreditMoneyChangeType changeType){
        this.changeType = changeType.getDescription();
    }
    public void setCreateTime(LocalDateTime createTime){
        this.createTime = TimeTransformUtils.df.format(createTime);
    }
    public void setStoreType(FitCompayType changeType){
        this.storeType = changeType.getDescription();
    }

    public void setOperatorType(AppIdentityType appIdentityType){
        this.operatorType = appIdentityType.getDescription();
    }
}
