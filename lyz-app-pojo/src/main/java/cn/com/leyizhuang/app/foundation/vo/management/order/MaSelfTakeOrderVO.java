package cn.com.leyizhuang.app.foundation.vo.management.order;

import cn.com.leyizhuang.app.foundation.vo.management.store.StoreVO;
import lombok.*;

import java.util.Date;

/**
 * 自提单返回类
 * Created by liuh on 2017/12/29.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MaSelfTakeOrderVO {
    /**
     * 门店
     */
    private String storeName;
    /**
     * 运费号
     */
    private String orderNumber;
    /**
     * 下单人姓名
     */
    private String creatorName;
    /**
     *  顾客姓名
     */
    private String customerName;
    /**
     * 订单创建时间
     */
    private Date createTime;
}
