package cn.com.leyizhuang.app.core.constant;

/**
 * 促销枚举类型
 *
 * SUB: 立减、 FIX: 固定金额、 DIS: 打折、 GOO: 送商品、 PRO: 送产品券、 CAS: 送优惠券、 ADD：加价购
 *
 * Created by panjie on 2017/11/22.
 */
public enum ActPromotionType {

    SUB("SUB","立减金额"),
    FIX("FIX","固定总价"),
    DIS("DIS","打折"),
    GOO("GOO","送商品"),
    PRO("PRO","送产品券"),
    CAS("CAS","送优惠券"),
    ADD("ADD","加价购");

    private final String value;
    private final String description;

    ActPromotionType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ActPromotionType getActPromotionTypeByValue(String value) {
        for (ActPromotionType type : ActPromotionType.values()) {
            if (value == type.getValue()) {
                return type;
            }
        }
        return null;
    }

}
