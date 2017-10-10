package cn.com.leyizhuang.app.core.constant;

import org.apache.commons.codec.binary.StringUtils;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
public enum AppUserType {

    SELLER(0,"导购"), DELIVERY_CLERK(1,"配送员"),DECORATE_MANAGER(2,"装饰公司经理"),DECORATE_EMPLOYEE(3,"装饰公司员工"),
    SUPERVISOR(4,"店长"),MANAGER(5,"店经理"),CUSTOMER(6,"顾客");

    private final int value;
    private final String description;

    AppUserType(int value, String description) {
        this.value = value;
        this.description = description;
    }
    public int getValue() {
        return value;
    }

    public String getDescription(){
        return description;
    }

    public static AppUserType getAppUserTypeByValue(Integer value){
        for(AppUserType appUserType : AppUserType.values()){
            if(value == appUserType.getValue()){
                return appUserType;
            }
        }
        return null;
    }


}
