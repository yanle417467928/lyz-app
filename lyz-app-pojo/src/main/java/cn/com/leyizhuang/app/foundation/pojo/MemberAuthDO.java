package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
@Getter
@Setter
@ToString
public class MemberAuthDO extends BaseDO {

    private static final long serialVersionUID = 7068812928546792515L;

    public MemberAuthDO() {
    }

    public MemberAuthDO(Long memberId, String username, String password,
                        String mobile, String email, String qqToken,
                        String wechatToken, String alipayToken,
                        String sinaToken, Boolean usable, Date unusableEndTime) {
        this.memberId = memberId;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
        this.qqToken = qqToken;
        this.wechatToken = wechatToken;
        this.alipayToken = alipayToken;
        this.sinaToken = sinaToken;
        this.usable = usable;
        this.unusableEndTime = unusableEndTime;
    }

    private Long memberId;
    private String username;
    private String password;
    private String mobile;
    private String email;
    private String qqToken;
    private String wechatToken;
    private String alipayToken;
    private String sinaToken;
    private Boolean usable;
    private Date unusableEndTime;

    public static Boolean validateMobile(String origin) {
        return origin.matches("^1([0-9]{10})$");
    }

    public static Boolean validateEmail(String origin) {
        return origin.matches("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
    }


}
