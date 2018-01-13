package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * 门店类型枚举
 *
 * @author Richard
 * Created on 2017-10-20 18:09
 **/
public enum StoreType {

    ZY("ZY","直营门店"),JM("JM","加盟门店"),FX("FX","分销公司"),ZS("ZS","装饰公司");

    private final String value;
    private final String description;


    StoreType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription(){
        return description;
    }

    public static StoreType getStoreTypeByValue(String value){
        for(StoreType storeType : StoreType.values()){
            if(Objects.equals(value, storeType.getValue())){
                return storeType;
            }
        }
        return null;
    }
}
