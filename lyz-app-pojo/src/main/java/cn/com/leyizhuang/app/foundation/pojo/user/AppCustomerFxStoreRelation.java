package cn.com.leyizhuang.app.foundation.pojo.user;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * App 顾客-分销门店关系表
 *
 * @author Richard
 * Created on 2017-09-19 11:00
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppCustomerFxStoreRelation implements Serializable {

    private static final long serialVersionUID = -5749739135096612483L;

    private Long id;

    private Long cusId;

    private String fxStoreCode;

    private Date createTime;


}
