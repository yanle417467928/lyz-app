package cn.com.leyizhuang.app.foundation.vo;

import cn.com.leyizhuang.app.core.constant.SexType;
import cn.com.leyizhuang.app.foundation.pojo.management.Role;
import cn.com.leyizhuang.app.foundation.pojo.management.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
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

    @NotNull(message = "登录名不允许为空")
    @Length(max = 20, min = 2, message = "登录名长度必须在2-20之间")
    private String loginName;

    @NotNull(message = "姓名不允许为空")
    @Length(max = 20, min = 2, message = "姓名长度必须在2-20之间")
    private String name;

    private String password;

    private SexType sex;

    private Integer age;

    private Integer userType;

    private Boolean status;

    private Date createTime;

    private String phone;

    private List<Role> rolesList;

    private Long roleIds[];

    private Date createdateStart;
    private Date createdateEnd;

    /**
     * 比较vo和数据库中的用户是否同一个user，采用id比较
     *
     * @param user 用户
     * @return 是否同一个人
     */
    public boolean equalsUser(User user) {
        if (user == null) {
            return false;
        }
        Long userId = user.getUid();
        if (id == null || userId == null) {
            return false;
        }
        return id.equals(userId);
    }

    public User convert2User() {
        User user = new User();
        user.setPassword(password);
        user.setLoginName(loginName);
        user.setName(name);
        user.setUid(id);
        user.setUserType(userType);
        user.setStatus(status);
        user.setAge(age);
        user.setMobile(phone);
        user.setCreateTime(createTime);
        return user;
    }
}
