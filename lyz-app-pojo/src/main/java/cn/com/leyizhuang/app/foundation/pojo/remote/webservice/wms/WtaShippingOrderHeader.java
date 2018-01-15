package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes:WMS~APP订单出货单头接口
 * Created with IntelliJ IDEA.
 * Date: 2017/12/21.
 * Time: 11:21.
 */
@Getter
@Setter
@ToString
public class WtaShippingOrderHeader {

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
     * 封车时间
     */
    private Date endDt;
    /**
     * 仓库编号
     */
    private String whNo;
    /**
     * 委托业主
     */
    private String ownerNo;
    /**
     * 封车单号
     */
    private String taskNo;
    /**
     * 出货类型
     */
    private String cTaskType;
    /**
     * 车辆类型
     */
    private String carNo;
    /**
     * 封车状态
     */
    private String opStatus;
    /**
     * 配送员编码
     */
    private String driver;
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

    public WtaShippingOrderHeader() {
        this.createTime = new Date();
    }
}
