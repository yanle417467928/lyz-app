package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2018/4/19
 */
@Getter
@Setter
@ToString
public class WtaOrderLogistics {
    private Long id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 操作后物流状态
     */
    private String logisticStatus;
    /**
     * 操作（创建）时间
     */
    private Date createTime;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 仓库编号
     */
    private String warehouseNo;
    /**
     * 物流操作单号
     */
    private String taskNo;
    //接收时间
    private Date receiveTime;
    //处理标记
    private String handleFlag;
    //错误信息
    private String errMessage;
    //处理时间
    private Date handleTime;
}
