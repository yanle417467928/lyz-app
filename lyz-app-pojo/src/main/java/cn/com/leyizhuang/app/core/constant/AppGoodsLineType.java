package cn.com.leyizhuang.app.core.constant;

/**
 * 产品行类型
 *
 * @author Richard
 * Created on 2017-12-11 10:39
 **/
public enum AppGoodsLineType {


    GOODS("GOODS", "本品"), PRESENT("PRESENT", "赠品"), PRODUCT_COUPON("PRODUCT_COUPON", "产品券");

    private String value;

    private String description;

    AppGoodsLineType(String value, String desccription) {
        this.value = value;
        this.description = desccription;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static AppGoodsLineType getAppGoodsLineTypeByValue(String value) {
        for (AppGoodsLineType lineType : AppGoodsLineType.values()) {
            if (lineType.getValue().equals(value)) {
                return lineType;
            }
        }
        return null;
    }

}
