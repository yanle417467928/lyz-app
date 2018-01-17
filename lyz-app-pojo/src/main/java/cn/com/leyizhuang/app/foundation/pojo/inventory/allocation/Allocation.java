package cn.com.leyizhuang.app.foundation.pojo.inventory.allocation;

import cn.com.leyizhuang.app.core.constant.AllocationTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

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
public class Allocation {

    private Long id;
    /**
     * 调拨单号
     */
    private String number;
    /**
     * 城市ID
     */
    private Long cityId;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 调出门店ID
     */
    private Long allocationFrom;
    /**
     * 调出名称
     */
    private String allocationFromName;
    /**
     * 调入门店ID
     */
    private Long allocationTo;
    /**
     * 调出名称
     */
    private String allocationToName;
    /**
     * 状态
     */
    private AllocationTypeEnum status;
    /**
     * 注释
     */
    private String comment;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改者
     */
    private String modifier;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 调拨明细
     */
    private List<AllocationDetail> details;
    /**
     * 调拨轨迹
     */
    private List<AllocationTrail> trails;
}
