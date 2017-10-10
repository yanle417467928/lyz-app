package cn.com.leyizhuang.app.core.constant;

import org.apache.commons.codec.binary.StringUtils;

/**
 * @author GenerationRoad
 * @date 2017/10/10
 */
public enum FunctionalFeedbackStatusEnum {
    NOT_CHECKED("未查看"), VIEW_ALREADY("已查看"), RESOLVED("已解决"), RETURN_VISIT("已回访");

    private final String value;
    FunctionalFeedbackStatusEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public static FunctionalFeedbackStatusEnum getAppUserTypeByValue(String value){
        for(FunctionalFeedbackStatusEnum functionalFeedbackStatusEnum : FunctionalFeedbackStatusEnum.values()){
            if(StringUtils.equals(value, functionalFeedbackStatusEnum.getValue())){
                return functionalFeedbackStatusEnum;
            }
        }
        return null;
    }
}
