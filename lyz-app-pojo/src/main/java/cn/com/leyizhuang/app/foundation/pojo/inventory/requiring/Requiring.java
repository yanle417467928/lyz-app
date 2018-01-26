package cn.com.leyizhuang.app.foundation.pojo.inventory.requiring;

import cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms.AtwRequisitionOrderGoods;
import cn.com.leyizhuang.app.foundation.pojo.response.StoreResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes:
 * Created with IntelliJ IDEA.
 * Date: 2018/1/17.
 * Time: 8:57.
 */
@Getter
@Setter
@ToString
public class Requiring {

    private Long id;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 商品列表
     */
    private List<AtwRequisitionOrderGoods> goodsList;

    /**
     * 门店信息
     */
    private StoreResponse detailAddress;

    /**
     * 订单备注
     */
    private String remarkInfo;
    /**
     * 管理员备注信息
     */
    private String managerRemarkInfo;
}
