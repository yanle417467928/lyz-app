package cn.com.leyizhuang.app.foundation.pojo.management.store;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuh
 * Notes: 门店进销存类
 */
@Getter
@Setter
@ToString
public class StoreInvoicingInfVO {

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
     * 要退货数量
     */
    private Integer storeGoodsQty;

    /**
     * 盘点数量
     */
    private Integer storePutGoodsQty;

    /**
     * 变更时间
     */
    private String changeTime;

    /**
     * 门店剩余库存
     */
    private Integer surplusInventory;

    /**
     * 门店真实库存
     */
    private Integer realIty;
    /**
     * 门店初始库存
     */
    private Integer initialIty;

    public static final List<StoreInvoicingInfVO> transform(List<StoreInvoicingInf> storeInvoicingInfList) {
        List<StoreInvoicingInfVO> storeInvoicingInfListTrans;
        if (null != storeInvoicingInfList && storeInvoicingInfList.size() > 0) {
            storeInvoicingInfListTrans = new ArrayList<>(storeInvoicingInfList.size());
            for (StoreInvoicingInf storeInvoicingInf : storeInvoicingInfList) {
                StoreInvoicingInfVO storeInvoicingInfVO = new StoreInvoicingInfVO();
                if (null != storeInvoicingInf.getOrderDeliveryQty()) {
                    storeInvoicingInf.setOrderDeliveryQty(storeInvoicingInf.getOrderDeliveryQty());
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
                storeInvoicingInfVO.setOrderDeliveryQty(storeInvoicingInf.getOrderDeliveryQty());
                storeInvoicingInfVO.setChangeTime(storeInvoicingInf.getChangeTime());
                storeInvoicingInfVO.setRealIty(storeInvoicingInf.getRealIty());
                storeInvoicingInfVO.setSelfTakeOrderReturnQty(storeInvoicingInf.getSelfTakeOrderReturnQty());
                storeInvoicingInfVO.setSku(storeInvoicingInf.getSku());
                storeInvoicingInfVO.setSkuName(storeInvoicingInf.getSkuName());
                storeInvoicingInfVO.setStoreAllocateInboundQty(storeInvoicingInf.getStoreAllocateInboundQty());
                storeInvoicingInfVO.setStoreAllocateOutboundQty(storeInvoicingInf.getStoreAllocateOutboundQty());
                storeInvoicingInfVO.setStoreCode(storeInvoicingInf.getStoreCode());
                storeInvoicingInfVO.setStoreGoodsQty(storeInvoicingInf.getStoreExportGoodsQty()+storeInvoicingInf.getStoreImportGoodsQty());
                storeInvoicingInfVO.setStoreName(storeInvoicingInf.getStoreName());
                storeInvoicingInfVO.setStorePutGoodsQty(storeInvoicingInf.getStoreInputGoodsQty()+storeInvoicingInf.getStoreOutputGoodsQty());
                storeInvoicingInfVO.setSurplusInventory(storeInvoicingInf.getSurplusInventory());
                storeInvoicingInfVO.setInitialIty(storeInvoicingInf.getInitialIty());
                storeInvoicingInfListTrans.add(storeInvoicingInfVO);
            }
        } else {
            storeInvoicingInfListTrans = new ArrayList<>(0);
        }
        return storeInvoicingInfListTrans;
    }

}
