package cn.com.leyizhuang.app.foundation.pojo.request;

import cn.com.leyizhuang.app.foundation.pojo.request.settlement.GoodsSimpleInfo;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 确认订单页面简单商品信息请求
 * @author Jerry.Ren
 * Date: 2017/11/1.
 * Time: 10:45.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderGoodsSimpleRequest implements Serializable {

    private static final long serialVersionUID = 6037339634254711067L;
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 用户身份类型
     */
    private Integer identityType;
    /**
     * 代下单客户id
     */
    private Long customerId;
    /**
     * 传输的商品集合
     */
    private List<GoodsSimpleInfo> goodsList;
}
