package cn.com.leyizhuang.app.foundation.pojo;


import cn.com.leyizhuang.app.core.constant.Sex;
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
public class AppMemberInfoDO extends BaseDO {

    private static final long serialVersionUID = 7249486778759150902L;

    public AppMemberInfoDO() {
    }

    public AppMemberInfoDO(Long memberId, String name, String nickname,
                           String headImageUri, Date birthday, Sex sex) {
        this.memberId = memberId;
        this.name = name;
        this.nickname = nickname;
        this.headImageUri = headImageUri;
        this.birthday = birthday;
        this.sex = sex;
    }

    private Long memberId;
    private String name;
    private String nickname;
    private String headImageUri;
    private Date birthday;
    private Sex sex;


}
