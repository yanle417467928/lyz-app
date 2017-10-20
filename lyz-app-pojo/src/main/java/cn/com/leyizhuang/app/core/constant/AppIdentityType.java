package cn.com.leyizhuang.app.core.constant;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
public enum AppIdentityType {

    SELLER(0,"导购"), DELIVERY_CLERK(1,"配送员"),DECORATE_MANAGER(2,"装饰公司经理"),DECORATE_EMPLOYEE(3,"装饰公司员工"),
    SUPERVISOR(4,"店长"),MANAGER(5,"店经理"),CUSTOMER(6,"顾客");

    private final int value;
    private final String description;

    AppIdentityType(int value, String description) {
        this.value = value;
        this.description = description;
    }
    public int getValue() {
        return value;
    }

    public String getDescription(){
        return description;
    }

    public static AppIdentityType getAppUserTypeByValue(Integer value){
        for(AppIdentityType appIdentityType : AppIdentityType.values()){
            if(value == appIdentityType.getValue()){
                return appIdentityType;
            }
        }
        return null;
    }


}