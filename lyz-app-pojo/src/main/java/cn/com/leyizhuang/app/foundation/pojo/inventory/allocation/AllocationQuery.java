package cn.com.leyizhuang.app.foundation.pojo.inventory.allocation;

import cn.com.leyizhuang.app.core.constant.AllocationTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * Notes:门店调拨查询参数类
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 10:48.
 */
@Getter
@Setter
@ToString
public class AllocationQuery {

    /**
     * 城市ID
     */
    private Long city;
    /**
     * 门店调出ID
     */
    private Long formName;
    /**
     * 门店调入ID
     */
    private Long toName;
    /**
     * 状态枚举格式
     */
    private AllocationTypeEnum allocationTypeEnum;
    /**
     * 开始时间date格式
     */
    private String startDateTime;
    /**
     * 结束时间date格式
     */
    private String endDateTime;

    // 当前登录门店ID
    private Long storeId;
}