package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.*;

import java.util.List;

/**
 * @Author Richard
 * @Date 2018/6/26 13:32
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaPhotoOrderDetailResponse {

    /**
     * 订单金额信息
     */
    private MaOrderCalulatedAmountResponse maOrderCalulatedAmountResponse;

    /**
     *订单商品信息
     */
    private List<MaPhotoOrderGoodsDetailResponse> maPhotoOrderGoodsDetailResponse;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 详细收货地址
     */
    private String detailedAddress;

    /**
     * 收货城市id
     */
    private String cityName;

    /**
     * 下单人类型
     */
    private Integer identityType;

}
