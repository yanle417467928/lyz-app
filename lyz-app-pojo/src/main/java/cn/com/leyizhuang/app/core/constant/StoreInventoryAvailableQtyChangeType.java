package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * 门店可用库存变更类型
 *
 * @author Richard
 * Created on 2017/11/29.
 */
public enum StoreInventoryAvailableQtyChangeType {

    SELF_TAKE_ORDER("SELF_TAKE_ORDER", "自提单下单"),
    SELF_TAKE_ORDER_CANCEL("SELF_TAKE_ORDER_CANCEL", "自提单取消"),
    SELF_TAKE_ORDER_CANCEL_AUTO("SELF_TAKE_ORDER_CANCEL_AUTO", "自提单超时自动取消"),
    SELF_TAKE_ORDER_RETURN("SELF_TAKE_ORDER_RETURN","自提单退单"),
    STORE_IMPORT_GOODS("STORE_IMPORT_GOODS","门店要货"),
    STORE_EXPORT_GOODS("STORE_EXPORT_GOODS","门店退货"),
    STORE_ALLOCATE_INBOUND("STORE_ALLOCATE_INBOUND","门店调拨入库"),
    STORE_ALLOCATE_OUTBOUND("STORE_ALLOCATE_OUTBOUND","门店调拨出库"),
    STORE_INVENTORY_INBOUND("STORE_INVENTORY_INBOUND","盘点入库"),
    STORE_INVENTORY_OUTBOUND("STORE_INVENTORY_OUTBOUND","盘点出库");
    private final String value;
    private final String description;

    StoreInventoryAvailableQtyChangeType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static StoreInventoryAvailableQtyChangeType getStoreInventoryAvailableQtyChangeTypeByValue(String value) {
        for (StoreInventoryAvailableQtyChangeType changeType : StoreInventoryAvailableQtyChangeType.values()) {
            if (value.equals(changeType.getValue())) {
                return changeType;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }


}
