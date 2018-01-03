package cn.com.leyizhuang.app.core.constant;

/**
 * APP单据产品类型
 *
 * @author Richard
 * Created on 2017-10-25 10:12
 **/
public enum ProductType {

    HR("HR", "华润"), LYZ("LYZ", "乐易装"), YR("YR", "莹润"), YF("YF", "运费"),
    XQ("XQ", "喜鹊");

    private String value;

    private String description;

    ProductType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static ProductType getProductTypeByValue(String value) {
        for (ProductType productType : ProductType.values()) {
            if (productType.getValue().equalsIgnoreCase(value)) {
                return productType;
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
