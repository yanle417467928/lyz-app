package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import cn.com.leyizhuang.app.core.constant.AppFreightOrderType;
import cn.com.leyizhuang.app.core.constant.AppWhetherFlag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 订单运费
 *
 * @author Richard
 * Created on 2018-02-23 11:50
 **/
@Getter
@Setter
@ToString
public class OrderKeyInf {

    private Long id;

    /**
     * 主单号
     */
    private String mainOrderNumber;

    /**
     * 订单类型 order:订单 return_order:退单
     */
    private AppFreightOrderType type;

    /**
     * 订单商品零售价总额
     */
    private Double orderAmt;

    /**
     * 订单商品结算价总额
     */
    private Double goodsAmt;

    /**
     * 运费金额
     */
    private Double freight;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否传输成功
     */
    private AppWhetherFlag sendFlag;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 传输成功时间
     */
    private Date sendTime;

    /**
     * 装饰公司信用额度
     */
    private Double decCreditMoney;
    /**
     * 装饰公司预存款
     */
    private Double decPreDeposit;
    /**
     * 装饰公司赞助金
     */
    private Double decSubvention;

    /**
     * 导购信用额度
     */
    private Double empCreditMoney;

    /**
     * 欠款
     */
    private Double arrearage;


    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;


}
