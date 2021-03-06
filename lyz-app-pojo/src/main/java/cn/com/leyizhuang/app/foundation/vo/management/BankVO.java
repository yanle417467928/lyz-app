package cn.com.leyizhuang.app.foundation.vo.management;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author GenerationRoad
 * @date 2018/3/21
 */
@Setter
@Getter
@ToString
public class BankVO {
    private Long id;
    //银行名称
    private String bankName;
    //银行开户人+账号
    private String bankAccount;
    //开户行
    private String openingBank;
    //编码
    private String code;
}
