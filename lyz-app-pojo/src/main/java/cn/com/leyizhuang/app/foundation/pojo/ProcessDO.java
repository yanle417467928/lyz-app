package cn.com.leyizhuang.app.foundation.pojo;

import cn.com.leyizhuang.common.foundation.pojo.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 工序包类
 * Created by caiyu on 2017/9/18.
 */
@Getter
@Setter
@ToString
public class ProcessDO extends BaseDO {

    private static final long serialVersionUID = 1713125788030092982L;
    //工序包名称
    private String ProcessName;

}
