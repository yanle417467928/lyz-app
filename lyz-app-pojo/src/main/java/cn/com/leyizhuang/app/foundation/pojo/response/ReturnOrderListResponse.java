package cn.com.leyizhuang.app.foundation.pojo.response;

import cn.com.leyizhuang.app.foundation.pojo.request.CustomerSimpleInfo;
import lombok.*;

import java.util.List;

/**
 * @author Jerry.Ren
 * Notes: 退货单列表响应实体
 * Created with IntelliJ IDEA.
 * Date: 2017/12/14.
 * Time: 14:55.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderListResponse {

    /**
     * 退单编号
     */
    private String returnNo;
    /**
     * 顾客基础信息
     */
    private CustomerSimpleInfo customer;
    /**
     * 退单状态
     */
    private String status;
    /**
     * 状态描述
     */
    private String statusDesc;
    /**
     * 商品图片
     */
    private List<String> goodsImgList;
    /**
     * 商品总数量
     */
    private Integer count;
    /**
     * 退付金额
     */
    private Double returnPrice;
    /**
     * 退单类型
     */
    private String returnType;
}
