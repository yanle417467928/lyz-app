package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * 导购类型
 *
 * @author Richard
 * Created on 2017-10-27 10:39
 **/
public enum AppSellerType {


    SELLER("SELLER","普通导购"),SUPERVISOR("SUPERVISOR","店长"),MANAGER("MANAGER","店经理");

    private String value;

    private String desccription;

    AppSellerType(String value, String desccription) {
        this.value = value;
        this.desccription = desccription;
    }

    public String getValue() {
        return value;
    }

    public String getDesccription() {
        return desccription;
    }

    public static AppSellerType getAppSellerTypeByValue(String value){
        for(AppSellerType sellerType : AppSellerType.values()){
            if(Objects.equals(value, sellerType.getValue())){
                return sellerType;
            }
        }
        return null;
    }

}
