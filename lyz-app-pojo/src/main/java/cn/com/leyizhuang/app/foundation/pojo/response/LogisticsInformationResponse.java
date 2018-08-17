package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.core.constant.LogisticStatus;
import lombok.*;

import java.util.List;

/**
 * 物流详情返类
 * Created by caiyu on 2017/11/20.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsInformationResponse {
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 配送员姓名
     */
    private String deliveryName;
    /**
     * 配送员电话
     */
    private String deliveryPhone;
    /**
     * 配送仓库
     */
    private String warehouseName;
    /**
     * 配送员头像
     */
    private String pictuerUrl;
    /**
     * 物流信息
     */
    private List<LogisticsDetailResponse> logisticsDetail;

    public static final LogisticsInformationResponse transform(LogisticsInformationResponse response){
        String orderNumber = response.getOrderNumber();
        List<LogisticsDetailResponse> logisticsDetailResponseList = response.getLogisticsDetail();

        if (orderNumber.contains("FW")){
            for (LogisticsDetailResponse rep : logisticsDetailResponseList){
                if (rep.getLogisticsType().equals(LogisticStatus.INITIAL.getDescription())){
                    // 等待物流接受
                    rep.setDescribe("等待服务中心接收");
                    rep.setLogisticsType("等待接收");
                }else if (rep.getLogisticsType().equals(LogisticStatus.RECEIVED.getDescription())){
                    // 已经接受
                    rep.setDescribe("服务中心已接收");
                    rep.setLogisticsType("已接收");
                }else if (rep.getLogisticsType().equals(LogisticStatus.ALREADY_POSITIONED.getDescription())){
                    // 已经定位
                    rep.setDescribe("已领料");
                    rep.setLogisticsType("已领料");
                }else if (rep.getLogisticsType().equals(LogisticStatus.PICKING_GOODS.getDescription())){
                    // 已拣货
                    rep.setDescribe("已派工");
                    rep.setLogisticsType("已派工");
                }else if (rep.getLogisticsType().equals(LogisticStatus.LOADING.getDescription())){
                    // 已装车
                    rep.setDescribe("正在施工");
                    rep.setLogisticsType("正在施工");
                }else if (rep.getLogisticsType().equals(LogisticStatus.SEALED_CAR.getDescription())){
                    // 已封车
                    rep.setDescribe("服务已经结案");
                    rep.setLogisticsType("已结案");
                }
            }
        }

        return response;
    }
}
