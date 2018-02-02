package cn.com.leyizhuang.app.foundation.pojo.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 顾客身份类型
 *
 * @author Richard
 * Created on 2018-02-02 9:34
 **/
@Getter
@Setter
@ToString
public class CustomerProfession {
    private Long id;

    private String title;

    private String description;

    private Date createTime;

    private String status;
}
