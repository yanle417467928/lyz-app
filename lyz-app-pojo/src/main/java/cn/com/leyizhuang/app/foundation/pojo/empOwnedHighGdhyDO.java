package cn.com.leyizhuang.app.foundation.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 导购在2018年4月1号之前拥有的高端会员
 * Created by panjie on 2018/4/10.
 */
@Setter
@Getter
@ToString
public class empOwnedHighGdhyDO {

    private Long id;

    /**
     * 导购工号
     */
    private String empNumber;

    /**
     * 顾客电话
     */
    private String cusPhone;
}
