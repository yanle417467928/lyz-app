package cn.com.leyizhuang.app.foundation.pojo.remote.webservice.wms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * create 2018-02-06 17:16
 * desc: 仓库整转零
 **/

@Getter
@Setter
@ToString
public class WtaWarehouseWholeSplitToUnit {

    private Long id;
    /**
     * 仓库编号
     */
    private String warehouseNo;
    /**
     * 主家收货
     */
    private String ownerNo;
    /**
     * 整转零单号
     */
    private String directNo;
    /**
     * 商品编码
     */
    private String sku;
    /**
     * 零商品编码
     */
    private String dSku;
    /**
     * 数量
     */
    private Integer qty;
    /**
     * 零数量
     */
    private Integer dQty;
    /**
     * 状态
     */
    private String status;
    /**
     * 制单人编号
     */
    private String creatorNo;
    /**
     * 制单创建时间
     */
    private Date createTime;
    /**
     * 城市编码
     */
    private String companyId;

    //接收时间
    private Date receiveTime;

    //处理标记
    private String handleFlag;

    //错误信息
    private String errMessage;

    //处理时间
    private Date handleTime;
}
