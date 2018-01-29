package cn.com.leyizhuang.app.core.constant;

import java.util.Objects;

/**
 * 取消订单处理状态
 * Created by caiyu on 2018/1/29.
 */
public enum CancelProcessingStatus {
    SEND_WMS("SEND_WMS","通知WMS"),PROCESSED("PROCESSED","已处理");

    private String value;

    private String desccription;

    CancelProcessingStatus(String value, String desccription) {
        this.value = value;
        this.desccription = desccription;
    }

    public String getValue() {
        return value;
    }

    public String getDesccription() {
        return desccription;
    }

    public static CancelProcessingStatus getAppSellerTypeByValue(String value){
        for(CancelProcessingStatus sellerType : CancelProcessingStatus.values()){
            if(Objects.equals(value, sellerType.getValue())){
                return sellerType;
            }
        }
        return null;
    }

}
