package cn.com.leyizhuang.app.foundation.pojo.management;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author GenerationRoad
 * @date 2018/3/21
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BankDO implements Serializable {
    private Long id;
    //银行名称
    private String bankName;
    //开户行
    private String openingBank;
    //银行账号
    private String bankAccount;
    //编码
    private String code;
    //是否有效
    private Boolean isEnable;
    //创建时间
    private Date createTime;
    //变更时间
    private Date updateTime;
}
