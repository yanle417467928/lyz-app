package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.ebs;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author liuh
 * Notes:ebs~app 直营要货退货
 */
@Getter
@Setter
@ToString
public class EtaReturnAndRequireGoodsInf {

    private Long id;
    /**
     * 分公司ID
     */
    private Long sobId;
    /**
     * 事务唯一ID
     */
    private String transId;
    /**
     * 事务类型
     */
    private String transType;
    /**
     *门店事务编号
     */
    private String transNumber;
    /**
     * 门店客户ID
     */
    private Long customerId;
    /**
     * 门店客户编号
     */
    private String customerNumber;
    /**
     * 门店编号
     */
    private String diySiteCode;
    /**
     * 事务时间
     */
    private Date shipDate;

    /**
     * 物料编号
     */
    private String itemCode;
    /**
     * 数量 "正数"入库，"负数"出库
     */
    private Long quantity;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String attribute4;

    private String attribute5;

}
