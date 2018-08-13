package cn.com.leyizhuang.app.core.constant;

/**
 * 接口重传发送类型
 *
 * @author Richard
 * Created on 2017/12/01.
 */
public enum InterfaceResendChangeType {

    EBS_ORER_RESEND("EBS_ORER_RESEND", "订单传输"),
    EBS_RETURN_ORER_RESEND("EBS_RETURN_ORER_RESEND","退单传输"),
    EBS_ORER_GENERATE("EBS_ORER_GENERATE", "订单接口信息生成"),
    EBS_RETURN_ORER_GENERATE("EBS_RETURN_ORER_GENERATE","退单接口信息生成"),
    EBS_ALLOCATIONINBOUND_RESEND("EBS_ALLOCATIONINBOUND_RESEND","调拨入库单传输"),
    EBS_ALLOCATIONOUTBOUND_RESEND("EBS_ALLOCATIONOUTBOUND_RESEND","调拨出库单传输"),
    EBS_SELFTAKE_ORER_GENERATE("EBS_SELFTAKEORER_GENERATE", "自提单发货接口信息生成"),
    EBS_SELFTAKE_ORER_RESEND("EBS_SELFTAKEORER_RESEND", "自提单发货接口传输"),
    EBS_RETURNSTORE_ORER_GENERATE("EBS_RETURNSTOREORER_GENERATE", "到店退货接口信息生成"),
    EBS_RETURNSTORE_ORER_RESEND("EBS_RETURNSTOREORER_RESEND", "到店退货接口传输");

    private final String value;
    private final String description;

    InterfaceResendChangeType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static InterfaceResendChangeType getInterfaceResendChangeTypeByValue(String value) {
        for (InterfaceResendChangeType changeType : InterfaceResendChangeType.values()) {
            if (value.equals(changeType.getValue())) {
                return changeType;
            }
        }
        return null;
    }


    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }


}
