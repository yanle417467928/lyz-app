package cn.com.leyizhuang.app.foundation.vo.management.store;

import cn.com.leyizhuang.app.core.constant.CustomerPreDepositChangeType;
import cn.com.leyizhuang.app.core.constant.StoreType;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author GenerationRoad
 * @date 2018/1/12
 */
@Getter
@Setter
@ToString
public class StorePreDepositLogVO {

    private Long id;
    /**
     * 生成时间
     */
    private String createTime;
    /**
     * 变更金额
     */
    private String changeMoney;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 使用订单号
     */
    private String orderNumber;
    /**
     * 变更类型
     */
    private String changeType;
    /**
     * 变更后预存款
     */
    private String balance;
    /**
     * 到账时间
     */
    private String transferTime;
    /**
     * 商户订单号
     */
    private String merchantOrderNumber;
    /*门店名称*/
    private String storeName;
    /*门店编码*/
    private String storeCode;
    /*操作人id*/
    private String operatorName;
    /**
     * 操作人员类型
     */
    private String operatorType;
    /**
     * 操作人员ip
     */
    private String operatorIp;
    /**
     * 修改原因
     */
    private String detailReason;
    /*市*/
    private String city;
    /**
     * 门店类型
     */
    private String storeType;

    public void setChangeType(CustomerPreDepositChangeType changeType){
        this.changeType = changeType.getDescription();
    }
    public void setCreateTime(LocalDateTime createTime){
        this.createTime = TimeTransformUtils.df.format(createTime);
    }

    public void setTransferTime(LocalDateTime transferTime){
        this.transferTime = TimeTransformUtils.df1.format(transferTime);
    }

    public void setStoreType(StoreType storeType){
        this.storeType = storeType.getDescription();
    }
}
