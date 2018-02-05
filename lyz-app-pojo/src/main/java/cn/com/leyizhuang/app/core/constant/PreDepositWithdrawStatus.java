package cn.com.leyizhuang.app.core.constant;

/**
 * 预存款提现申请单 状态
 * Created by panjie on 2018/2/5.
 */
public enum PreDepositWithdrawStatus {

    CHECKING(0,"待审核"),CHECKPASS(1,"申请通过"),CHECKRETURN(2,"申请退回"),REMITED(3,"已打款"),CANCEL(4,"已取消");

    private Integer value;

    private String description;

    PreDepositWithdrawStatus(Integer value, String description){
        this.value = value;
        this.description = description;
    }

    public static PreDepositWithdrawStatus getPreDepositWithdrawStatusByValue(Integer value) {
        for (PreDepositWithdrawStatus type : PreDepositWithdrawStatus.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
