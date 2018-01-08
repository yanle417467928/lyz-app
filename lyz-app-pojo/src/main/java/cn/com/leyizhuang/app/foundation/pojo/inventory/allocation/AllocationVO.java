package cn.com.leyizhuang.app.foundation.pojo.inventory.allocation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jerry.Ren
 * Notes: 门店调拨实体类
 * Created with IntelliJ IDEA.
 * Date: 2018/1/3.
 * Time: 10:31.
 */
@Getter
@Setter
@ToString
public class AllocationVO {

    private Long id;
    /**
     * 调拨单号
     */
    private String number;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 调出名称
     */
    private String allocationFromName;
    /**
     * 调出名称
     */
    private String allocationToName;
    /**
     * 状态
     */
    private String status;
    /**
     * 修改时间
     */
    private String modifyTime;
}
