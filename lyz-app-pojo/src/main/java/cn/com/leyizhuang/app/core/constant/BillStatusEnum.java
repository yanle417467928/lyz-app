package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * @author GenerationRoad
 * @date 2018/6/26
 */
public enum BillStatusEnum {
    NOT_OUT("NOT_OUT","未出账单"),ALREADY_OUT("ALREADY_OUT","已出账单"),HISTORY("HISTORY","历史账单");

    private String value;

    private String description;

    BillStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDesccription() {
        return description;
    }

    public static BillStatusEnum getBillStatusEnumByValue(String value){
        for(BillStatusEnum billStatusEnum : BillStatusEnum.values()){
            if(Objects.equals(value, billStatusEnum.getValue())){
                return billStatusEnum;
            }
        }
        return null;
    }
}
