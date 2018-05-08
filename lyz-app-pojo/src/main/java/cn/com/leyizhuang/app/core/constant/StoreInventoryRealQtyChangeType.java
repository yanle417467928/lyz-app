package cn.com.leyizhuang.app.core.constant;

/**
 * 门店可用库存变更类型
 *
 * @author Richard
 * Created on 2017/11/29.
 */
public enum StoreInventoryRealQtyChangeType {

    ORDER_DELIVERY("ORDER_DELIVERY", "订单发货"),
    ORDER_RETURN("ORDER_RETURN", "订单退货"),
    STORE_IMPORT_GOODS("STORE_IMPORT_GOODS","门店要货"),
    STORE_EXPORT_GOODS("STORE_EXPORT_GOODS","门店退货"),
    STORE_ALLOCATE_INBOUND("STORE_ALLOCATE_INBOUND","门店调拨入库"),
    STORE_ALLOCATE_OUTBOUND("STORE_ALLOCATE_OUTBOUND","门店调拨出库"),
    STORE_INVENTORY_INBOUND("STORE_INVENTORY_INBOUND","盘点入库"),
    STORE_INVENTORY_OUTBOUND("STORE_INVENTORY_OUTBOUND","盘点出库");
    private final String value;
    private final String description;

    StoreInventoryRealQtyChangeType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static StoreInventoryRealQtyChangeType getStoreInventoryRealQtyChangeTypeByValue(String value) {
        for (StoreInventoryRealQtyChangeType changeType : StoreInventoryRealQtyChangeType.values()) {
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
