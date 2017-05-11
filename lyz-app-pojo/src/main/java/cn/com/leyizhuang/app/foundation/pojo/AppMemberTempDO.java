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
public class AppMemberTempDO extends BaseDO {

    private static final long serialVersionUID = -6264820157386364557L;

    private Long memberId;//用户主键
    private Boolean isLogin;//用户当前是否已登录
    private Date lastVisitTime;//上一次操作时间
    private String loginSession;//登录session
    private Boolean isCashOnDelivery;//是否允许货到付款


}
