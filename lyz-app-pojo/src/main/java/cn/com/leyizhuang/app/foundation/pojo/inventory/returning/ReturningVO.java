package cn.com.leyizhuang.app.foundation.pojo.inventory.returning;

import cn.com.leyizhuang.app.foundation.pojo.returnorder.ReturnOrderBaseInfo;
import cn.com.leyizhuang.common.util.TimeTransformUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry.Ren
 * Notes: app后台退货单管理实体类
 * Created with IntelliJ IDEA.
 * Date: 2018/1/19.
 * Time: 13:44.
 */
@Getter
@Setter
@ToString
public class ReturningVO {

    private Long id;

    /**
     * 退单号
     */
    private String returnNumber;
    /**
     * 订单状态
     */
    private String orderStatus;
    /**
     * 申请用户
     */
    private String creatorPhone;
    /**
     * 顾客姓名
     */
    private String customerName;
    /**
     * 退单类型（CANCEL_RETURN(1, "取消退货"), REFUSED_RETURN(2, "拒签退货"), NORMAL_RETURN(3, "正常退货")）
     */
    private String returnType;
    /**
     * 退货原因
     */
    private String reasonInfo;
    /**
     * 退货时间
     */
    private String returnTime;


    public static ReturningVO transform(ReturnOrderBaseInfo baseInfo) {
        ReturningVO vo = new ReturningVO();
        vo.setCreatorPhone(baseInfo.getCreatorPhone());
        vo.setCustomerName(baseInfo.getCustomerName());
        vo.setOrderStatus(baseInfo.getReturnStatus().getDescription());
        vo.setReasonInfo(baseInfo.getReasonInfo());
        vo.setReturnNumber(baseInfo.getReturnNo());
        vo.setReturnTime(TimeTransformUtils.getString(baseInfo.getReturnTime(), "yyyy-MM-dd HH:mm:ss"));
        vo.setReturnType(baseInfo.getReturnType().getDescription());
        return vo;
    }

    public static List<ReturningVO> transform(List<ReturnOrderBaseInfo> baseInfoList) {
        List<ReturningVO> voList;
        if (null != baseInfoList && baseInfoList.size() > 0) {
            voList = new ArrayList<>(baseInfoList.size());
            baseInfoList.forEach(returnOrderBaseInfo -> voList.add(transform(returnOrderBaseInfo)));
        } else {
            voList = new ArrayList<>(0);
        }
        return voList;
    }
}
