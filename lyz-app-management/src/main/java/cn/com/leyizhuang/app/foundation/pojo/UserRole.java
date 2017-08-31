package cn.com.leyizhuang.app.foundation.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户角色对应表
 *
 * @author Richard
 * Created on 2017-07-28 13:44
 **/
@Getter
@Setter
@ToString
public class UserRole implements Serializable{
    private static final long serialVersionUID = -1847997444228233323L;

    private Long id;
    private Long userId;
    private Long roleId;
}
