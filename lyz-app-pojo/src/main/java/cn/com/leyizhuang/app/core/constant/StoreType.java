package cn.com.leyizhuang.app.core.constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 门店类型枚举
 *
 * @author Richard
 * Created on 2017-10-20 18:09
 **/
public enum StoreType {

    ZY("ZY", "直营门店"), JM("JM", "加盟门店"), FX("FX", "分销公司"), ZS("ZS", "装饰公司"), FXCK("FXCK", "分销仓库");

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

    public static List<StoreType> getNotZsType(){
        List<StoreType> storeTypes = new ArrayList<>();
        storeTypes.add(StoreType.ZY);
        storeTypes.add(StoreType.JM);
        storeTypes.add(StoreType.FX);
        storeTypes.add(StoreType.FXCK);
        return storeTypes;
    }

    public static List<StoreType> getStoreTypeList() {
        List<StoreType> storeTypes = getNotZsType();
        storeTypes.add(StoreType.ZS);
        return storeTypes;
    }
}
