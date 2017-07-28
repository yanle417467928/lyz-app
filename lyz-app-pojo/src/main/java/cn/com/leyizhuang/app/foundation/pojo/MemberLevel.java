package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 * <section>会员等级信息表</section>
 */
@Getter
@Setter
@ToString
public class MemberLevel extends BaseDO {

    private static final long serialVersionUID = -5423603901188207188L;

    public MemberLevel() {}

    public MemberLevel(String title, String iconUri, Integer rank) {
        this.title = title;
        this.iconUri = iconUri;
        this.rank = rank;
    }
    //会员等级名称
    private String title;
    //会员图标地址路径
    private String iconUri;
    //会员级别（表示会员等级高低）
    private Integer rank;


}
