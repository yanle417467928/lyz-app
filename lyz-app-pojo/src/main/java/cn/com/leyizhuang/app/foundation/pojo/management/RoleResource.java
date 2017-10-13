package cn.com.leyizhuang.app.foundation.pojo.management;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 角色资源对应关系中间表
 *
 * @author Richard
 * Created on 2017-07-28 13:42
 **/
@Getter
@Setter
@ToString
public class RoleResource implements Serializable {
    private static final long serialVersionUID = 6133025178729047168L;

    private Long id;
    private Long roleId;
    private Long resourceId;
}
