package cn.com.leyizhuang.app.foundation.pojo.vo;

import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.app.foundation.pojo.Role;
import cn.com.leyizhuang.app.foundation.pojo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 管理员视图对象
 *
 * @author Richard
 * Created on 2017-07-28 14:41
 **/
@Getter
@Setter
@ToString
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1122196596335613641L;
    private Long id;

    private String loginName;

    private String name;

    private String password;

    private SexType sex;

    private Integer age;

    private Integer userType;

    private Integer status;

    private Date createTime;

    private String phone;

    private List<Role> rolesList;

    private String roleIds;

    private Date createdateStart;
    private Date createdateEnd;

    /**
     * 比较vo和数据库中的用户是否同一个user，采用id比较
     * @param user 用户
     * @return 是否同一个人
     */
    public boolean equalsUser(User user) {
        if (user == null) {
            return false;
        }
        Long userId = user.getId();
        if (id == null || userId == null) {
            return false;
        }
        return id.equals(userId);
    }
}
