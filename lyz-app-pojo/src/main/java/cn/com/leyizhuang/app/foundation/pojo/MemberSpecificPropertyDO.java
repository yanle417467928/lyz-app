package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 * <section>会员特定属性表</section>
 */
@Getter
@Setter
@ToString
public class MemberSpecificPropertyDO extends BaseDO {

    private static final long serialVersionUID = -6264820157386364557L;
    //用户主键
    private Long memberId;
    //用户当前是否已登录
    private Boolean isLogin;
    //上一次操作时间
    private Date lastVisitTime;
    //登录session
    private String loginSession;
    //是否允许货到付款
    private Boolean isCashOnDelivery;


}
