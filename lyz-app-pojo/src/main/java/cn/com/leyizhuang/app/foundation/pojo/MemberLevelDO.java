package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author CrazyApeDX
 *         Created on 2017/3/24.
 */
@Getter
@Setter
@ToString
public class MemberLevelDO extends BaseDO {

    private static final long serialVersionUID = -5423603901188207188L;

    public MemberLevelDO() {}

    public MemberLevelDO(String title, String iconUri, Integer rank, Boolean isDefault) {
        this.title = title;
        this.iconUri = iconUri;
        this.rank = rank;
        this.isDefault = isDefault;
    }

    private String title;
    private String iconUri;
    private Integer rank;
    private Boolean isDefault = Boolean.FALSE;


}
