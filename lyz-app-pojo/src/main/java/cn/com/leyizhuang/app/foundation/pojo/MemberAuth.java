package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 * <section>会员鉴权信息表</section>
 */
@Getter
@Setter
@ToString
public class MemberAuth extends BaseDO {

    private static final long serialVersionUID = 7068812928546792515L;

    public MemberAuth() {
    }

    public MemberAuth(Long memberId, String username, String password,
                        String mobile, String email, Boolean status, Date disableEndTime) {
        this.memberId = memberId;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
        this.status = status;
        this.disableEndTime = disableEndTime;
    }
    //会员主键
    private Long memberId;
    //会员用户名
    private String username;
    //会员密码
    private String password;
    //会员手机号码
    private String mobile;
    //会员电子邮箱
    private String email;
    //会员账号是否启用
    private Boolean status;
    //会员账号禁用结束时间
    private Date disableEndTime;

    public static Boolean validateMobile(String origin) {
        return origin.matches("^1([0-9]{10})$");
    }

    public static Boolean validateEmail(String origin) {
        return origin.matches("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
    }


}