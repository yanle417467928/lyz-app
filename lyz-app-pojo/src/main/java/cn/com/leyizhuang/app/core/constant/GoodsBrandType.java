package cn.com.leyizhuang.app.core.constant;

/**
 * 商品品牌类型
 *
 * @author liuh
 * Created on 2017-12-11 10:39
 **/
public enum GoodsBrandType {

    JINNIU(1, "金牛"), MLMT(2, "摩利美涂 摩利美涂"), OM(3, "OEM"), HR(4, "华润漆"),
    TAPAI(5, "塔牌"), LYZ(6, "乐易装"), YR(7, "莹润");

    private final int value;

    private final String description;

    GoodsBrandType(int value, String desccription) {
        this.value = value;
        this.description = desccription;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static GoodsBrandType getGoodsBrandTypeByValue(int value) {
        for (GoodsBrandType goodsBrandType : GoodsBrandType.values()) {
            if (goodsBrandType.getValue()==value) {
                return goodsBrandType;
            }
        }
        return null;
    }

}
