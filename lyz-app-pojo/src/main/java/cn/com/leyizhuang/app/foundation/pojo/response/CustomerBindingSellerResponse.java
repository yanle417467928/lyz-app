package cn.com.leyizhuang.app.foundation.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 员工登录接口返回对象
 *
 * @author Richard
 * Created on 2017-09-19 13:57
 **/
@Getter
@Setter
@ToString
@AllArgsConstructor
public class CustomerBindingSellerResponse implements Serializable {

    private static final long serialVersionUID = -2021841116529083413L;
    //导购电话是否存在
    private Boolean isExist;
    //用户id
    private String guideName;

    private String guideStore;
}
