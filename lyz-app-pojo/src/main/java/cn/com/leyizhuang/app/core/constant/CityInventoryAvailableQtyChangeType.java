package cn.com.leyizhuang.app.core.constant;

/**
 * 城市可用库存变更类型
 *
 * @author Richard
 * Created on 2017/11/30.
 */
public enum CityInventoryAvailableQtyChangeType {

    CITY_WASTAGE("CITY_WASTAGE", "城市报损"),
    CITY_OVERFLOW("CITY_OVERFLOW", "城市报溢"),
    CITY_PURCHASE_INBOUND("CITY_PURCHASE_INBOUND", "城市采购入库"),
    CITY_PURCHASE_OUTBOUND("CITY_PURCHASE_OUTBOUND", "城市采购退货"),
    ALLOCATE_INBOUND("ALLOCATE_INBOUND", "调拨入库"),
    ALLOCATE_OUTBOUND("ALLOCATE_OUTBOUND", "调拨出库"),
    WHOLE_TO_SCRAPPY("WHOLE_TO_SCRAPPY", "整转零"),
    HOUSE_DELIVERY_ORDER("HOUSE_DELIVERY_ORDER", "配送下单"),
    HOUSE_DELIVERY_ORDER_RETURN("HOUSE_DELIVERY_ORDER_RETURN", "配送单退货"),
    HOUSE_DELIVERY_ORDER_CANCEL("HOUSE_DELIVERY_ORDER_CANCEL", "配送单取消"),
    PICKING_ORDER("PICKING_ORDER", "领用单"),
    PICKING_ORDER_OUTBOUND("PICKING_ORDER_OUTBOUND", "领用单出库"),
    SELF_TAKE_ORDER("SELF_TAKE_ORDER", "自提单下单");

    private final String value;
    private final String description;

    CityInventoryAvailableQtyChangeType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static CityInventoryAvailableQtyChangeType getCityInventoryAvailableQtyChangeTypeByValue(String value) {
        for (CityInventoryAvailableQtyChangeType changeType : CityInventoryAvailableQtyChangeType.values()) {
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
