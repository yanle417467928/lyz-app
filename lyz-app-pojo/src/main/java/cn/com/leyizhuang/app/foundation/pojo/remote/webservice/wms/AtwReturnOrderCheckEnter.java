package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import cn.com.leyizhuang.app.core.constant.AppReturnOrderStatus;
import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import lombok.*;

import java.util.Date;

/**
 * @author Jerry.Ren
 * create 2018-01-27 15:10
 * desc: 确认收货
 **/
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AtwReturnOrderCheckEnter {

    private Long id;
    /**
     * 创建时间（本条数据创建时间）
     */
    private Date createTime;
    /**
     * 退单号
     */
    private String returnNo;
    /**
     * 退货单状态
     */
    private AppReturnOrderStatus returnStatus;
    /**
     * 确认收货时间
     */
    private Date checkGoodsTime;
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

    public static AtwReturnOrderCheckEnter transform(ReturnOrderBaseInfo returnOrderBaseInfo) {
        AtwReturnOrderCheckEnter checkEnter = new AtwReturnOrderCheckEnter();
        checkEnter.setReturnNo(returnOrderBaseInfo.getReturnNo());
        checkEnter.setCreateTime(new Date());
        checkEnter.setCheckGoodsTime(new Date());
        checkEnter.setReturnStatus(AppReturnOrderStatus.PENDING_REFUND);

        return checkEnter;
    }
}
