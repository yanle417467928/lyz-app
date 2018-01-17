package cn.com.leyizhuang.app.foundation.pojo.inventory.allocation;

import cn.com.leyizhuang.app.core.constant.AllocationType;
import cn.com.leyizhuang.app.core.constant.AllocationTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Jerry.Ren
 * Notes:门店调拨轨迹实体类
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 10:32.
 */
@Getter
@Setter
@ToString
public class AllocationTrail {
    private Long id;
    /**
     * 调拨单ID
     */
    private Long allocationId;
    /**
     * 操作
     */
    private AllocationTypeEnum operation;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作时间
     */
    private Date operateTime;
}
