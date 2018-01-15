package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes:WMS~APP出货单接口商品明细
 * Created with IntelliJ IDEA.
 * Date: 2017/12/21.
 * Time: 11:15.
 */
@Getter
@Setter
@ToString
public class WtaShippingOrderGoods {

    private Long id;
    /**
     * 创建时间（本条数据创建时间）
     */
    private Date createTime;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 封车类型
     */
    private String taskType;
    /**
     * 封车单号
     */
    private String taskNo;
    /**
     * 商品编码
     */
    private String gCode;
    /**
     * 出货数量
     */
    private Integer dAckQty;
    /**
     * 封车状态
     */
    private String opStatus;
    /**
     * 出货单号
     */
    private String sourceNo;
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

    public WtaShippingOrderGoods() {
        this.createTime = new Date();
    }
}
