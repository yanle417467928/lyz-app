package cn.com.leyizhuang.app.foundation.pojo.response;

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
}
