package cn.com.leyizhuang.app.foundation.pojo.management;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 管理员角色
 *
 * @author Richard
 * Created on 2017-07-28 11:55
 **/
@Getter
@Setter
@ToString
public class Role implements Serializable {

    private static final long serialVersionUID = -7833522507545491525L;

    private Long id;

    // 角色名
    private String name;
    //排序号
    private Integer sortId;
    //简介
    private String description;
    //状态
    private Boolean status;

}
