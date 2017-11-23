package cn.com.leyizhuang.app.core.constant;

/**
 * Created by caiyu on 2017/11/22.
 */
public enum LogisticStatus {
    RECEIVED(0,"已接收"),ALREADY_POSITIONED(1,"已定位"),PICKING_GOODS(2,"已拣货"),LOADING(3,"已装车"),SEALED_CAR(4,"已封车"),CONFIRM_ARRIVAL(5,"确认到货");

    private final int value;
    private final String description;

    LogisticStatus(int value, String description) {
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
