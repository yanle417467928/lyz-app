package cn.com.leyizhuang.app.core.constant;

/**
 * Created by caiyu on 2017/11/20.
 */
public enum OnlinePayType {

    ALIPAY(0,"支付宝"), WE_CHAT(1,"微信"),UNION_PAY(2,"银联"),NO(3,"无");

    private final int value;
    private final String description;

    OnlinePayType(int value, String description) {
        this.value = value;
        this.description = description;
    }
    public int getValue() {
        return value;
    }

    public String getDescription(){
        return description;
    }

    public static AppIdentityType getAppIdentityTypeByValue(Integer value){
        for(AppIdentityType appIdentityType : AppIdentityType.values()){
            if(value == appIdentityType.getValue()){
                return appIdentityType;
            }
        }
        return null;
    }


}
