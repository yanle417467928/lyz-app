package cn.com.leyizhuang.app.foundation.pojo.management.store;

import cn.com.leyizhuang.app.foundation.pojo.FitmentCompanyDO;
import cn.com.leyizhuang.app.foundation.vo.FitmentCompanyVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liuh
 * Notes: 门店进销存类
 */
@Getter
@Setter
@ToString
public class StoreInvoicingInf {

    /**
     * 门店编码
     */
    private String storeCode;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * SKU
     */
    private String sku;
    /**
     * 商品名称
     */
    private String skuName;
    /**
     * 发货数量
     */
    private Integer orderDeliveryQty;
    /**
     * 退货数量
     */
    private Integer selfTakeOrderReturnQty;

    /**
     * 调入数量
     */
    private Integer storeAllocateInboundQty;

    /**
     * 调出数量
     */
    private Integer storeAllocateOutboundQty;

    /**
     * 要货数量
     */
    private Integer storeImportGoodsQty;
    /**
     * 出货数量
     */
    private Integer storeExportGoodsQty;
    /**
     * 盘点入库
     */
    private Integer storeInputGoodsQty;
    /**
     * 盘点出库
     */
    private Integer storeOutputGoodsQty;
    /**
     * 变更时间
     */
    private String changeTime;

    /**
     * 门店剩余库存
     */
    private Integer surplusInventory;

    /**
     * 门店初始库存
     */
    private Integer initialIty;

    /**
     * 门店真实库存
     */
    private Integer realIty;


    public static final List<StoreInvoicingInf> transform(List<StoreInvoicingInf> storeInvoicingInfList) {
        List<StoreInvoicingInf> storeInvoicingInfListTrans;
        if (null != storeInvoicingInfList && storeInvoicingInfList.size() > 0) {
            storeInvoicingInfListTrans = new ArrayList<>(storeInvoicingInfList.size());
            for (StoreInvoicingInf storeInvoicingInf : storeInvoicingInfList) {
                if (null != storeInvoicingInf.getOrderDeliveryQty()) {
                    storeInvoicingInf.setOrderDeliveryQty(-storeInvoicingInf.getOrderDeliveryQty());
                }
                if (null == storeInvoicingInf.getOrderDeliveryQty()) {
                    storeInvoicingInf.setOrderDeliveryQty(0);
                }
                if (null == storeInvoicingInf.getSelfTakeOrderReturnQty()) {
                    storeInvoicingInf.setSelfTakeOrderReturnQty(0);
                }
                if (null == storeInvoicingInf.getStoreAllocateInboundQty()) {
                    storeInvoicingInf.setStoreAllocateInboundQty(0);
                }
                if (null == storeInvoicingInf.getStoreAllocateOutboundQty()) {
                    storeInvoicingInf.setStoreAllocateOutboundQty(0);
                }
                if (null == storeInvoicingInf.getStoreExportGoodsQty()) {
                    storeInvoicingInf.setStoreExportGoodsQty(0);
                }
                if (null == storeInvoicingInf.getStoreImportGoodsQty()) {
                    storeInvoicingInf.setStoreImportGoodsQty(0);
                }
                if (null == storeInvoicingInf.getStoreInputGoodsQty()) {
                    storeInvoicingInf.setStoreInputGoodsQty(0);
                }
                if (null == storeInvoicingInf.getStoreOutputGoodsQty()) {
                    storeInvoicingInf.setStoreOutputGoodsQty(0);
                }
                storeInvoicingInfListTrans.add(storeInvoicingInf);
            }
        } else {
            storeInvoicingInfListTrans = new ArrayList<>(0);
        }
        return storeInvoicingInfListTrans;
    }

}
